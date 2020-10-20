package com.github.thestyleofme.plugin.framework.extension.resources.resolver;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;

import com.github.thestyleofme.plugin.framework.constants.BaseConstants;
import com.github.thestyleofme.plugin.framework.extension.resources.StaticResourceConfig;
import com.github.thestyleofme.plugin.framework.loader.PluginResource;
import com.github.thestyleofme.plugin.framework.realize.BasePlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileUrlResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.resource.AbstractResourceResolver;
import org.springframework.web.servlet.resource.ResourceResolverChain;

/**
 * <p>
 * 插件资源发现者
 * </p>
 *
 * @author isaac 2020/6/19 10:33
 * @since 1.0
 */
public class PluginResourceResolver extends AbstractResourceResolver {

    private static final Logger LOG = LoggerFactory.getLogger(PluginResourceResolver.class);

    private static final String RESOLVED_RESOURCE_CACHE_KEY_PREFIX = "resolvedPluginResource:";
    private static final String CLASSPATH = "classpath";
    private static final String FILE = "file";

    private static final Map<String, PluginStaticResource> PLUGIN_RESOURCE_MAP = new ConcurrentHashMap<>();

    PluginResourceResolver() {
    }


    @Override
    protected Resource resolveResourceInternal(HttpServletRequest request,
                                               String requestPath, List<? extends Resource> locations,
                                               ResourceResolverChain chain) {
        int startOffset = (requestPath.startsWith(BaseConstants.Symbol.SLASH) ? 1 : 0);
        int endOffset = requestPath.indexOf('/', 1);
        if (endOffset != -1) {
            String pluginId = requestPath.substring(startOffset, endOffset);
            String partialPath = requestPath.substring(endOffset + 1);

            PluginStaticResource pluginResource = PLUGIN_RESOURCE_MAP.get(pluginId);

            if (pluginResource == null) {
                return null;
            }

            String key = computeKey(request, requestPath);
            // 先判断缓存中是否存在。
            Resource resource = pluginResource.getCacheResource(key);
            if (resource != null) {
                return resource;
            }

            // 从classpath 获取资源
            resource = resolveClassPath(pluginResource, partialPath);
            if (resource != null) {
                pluginResource.putCacheResource(key, resource);
                return resource;
            }

            // 从外置文件路径获取资源
            resource = resolveFilePath(pluginResource, partialPath);
            if (resource != null) {
                pluginResource.putCacheResource(key, resource);
                return resource;
            }
            return null;

        }
        return chain.resolveResource(request, requestPath, locations);
    }

    /**
     * 解决 ClassPath 的资源文件。也就是插件中定义的  classpath:/xx/xx/ 配置
     *
     * @param pluginResource 插件资源配置Bean
     * @param partialPath    部分路径
     * @return 资源。没有发现则返回null
     */
    private Resource resolveClassPath(PluginStaticResource pluginResource,
                                      String partialPath) {
        Set<String> classPaths = pluginResource.getClassPaths();
        if (classPaths == null || classPaths.isEmpty()) {
            return null;
        }

        BasePlugin basePlugin = pluginResource.getBasePlugin();
        if (basePlugin == null) {
            return null;
        }


        for (String classPath : classPaths) {
            try {
                Resource resource = new PluginResource(classPath + partialPath, basePlugin);
                if (resource.exists()) {
                    return resource;
                }
            } catch (Exception e) {
                LOG.debug("Get static resources of classpath '{}' error.", classPath, e);
            }
        }
        return null;
    }

    /**
     * 解决插件中配置的绝对文件路径的文件资源。也就是插件中定义的  file:D://xx/xx/ 配置
     *
     * @param pluginResource 插件资源配置Bean
     * @param partialPath    部分路径
     * @return 资源。没有发现则返回null
     */
    private Resource resolveFilePath(PluginStaticResource pluginResource, String partialPath) {
        Set<String> filePaths = pluginResource.getFilePaths();
        if (filePaths == null || filePaths.isEmpty()) {
            return null;
        }

        for (String filePath : filePaths) {
            Path fullPath = Paths.get(filePath + partialPath);
            if (!Files.exists(fullPath)) {
                continue;
            }
            try {
                FileUrlResource fileUrlResource = new FileUrlResource(fullPath.toString());
                if (fileUrlResource.exists()) {
                    return fileUrlResource;
                }
            } catch (Exception e) {
                LOG.debug("Get static resources of path '{}' error.", fullPath, e);
            }
        }
        return null;
    }


    @Override
    protected String resolveUrlPathInternal(String resourceUrlPath,
                                            List<? extends Resource> locations,
                                            ResourceResolverChain chain) {
        return null;
    }

    /**
     * 计算key
     *
     * @param request     request
     * @param requestPath 请求路径
     * @return 返回key
     */
    protected String computeKey(HttpServletRequest request, String requestPath) {
        StringBuilder key = new StringBuilder(RESOLVED_RESOURCE_CACHE_KEY_PREFIX);
        key.append(requestPath);
        if (request != null) {
            String codingKey = getContentCodingKey(request);
            if (StringUtils.hasText(codingKey)) {
                key.append("+encoding=").append(codingKey);
            }
        }
        return key.toString();
    }

    /**
     * 根据请求获取内容code key
     *
     * @param request request
     * @return key
     */
    private String getContentCodingKey(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.ACCEPT_ENCODING);
        if (!StringUtils.hasText(header)) {
            return null;
        }
        return Arrays.stream(StringUtils.tokenizeToStringArray(header, ","))
                .map(token -> {
                    int index = token.indexOf(';');
                    return (index >= 0 ? token.substring(0, index) : token).trim().toLowerCase();
                })
                .sorted()
                .collect(Collectors.joining(","));
    }


    /**
     * 每新增一个插件, 都需要调用该方法，来解析该插件的 StaticResourceConfig 配置。并将其保存到 StaticResourceConfig bean 中。
     *
     * @param basePlugin 插件信息
     */
    public static synchronized void parse(BasePlugin basePlugin) {
        if (!(basePlugin instanceof StaticResourceConfig)) {
            return;
        }
        StaticResourceConfig staticResourceConfig = (StaticResourceConfig) basePlugin;
        String pluginId = basePlugin.getWrapper().getPluginId();

        Set<String> locations = staticResourceConfig.locations();
        if (CollectionUtils.isEmpty(locations)) {
            return;
        }
        Set<String> classPaths = new HashSet<>();
        Set<String> filePaths = new HashSet<>();

        for (String location : locations) {
            if (StringUtils.isEmpty(location) || location.indexOf(':') == -1) {
                LOG.warn("This plugin '{}' location config {} cannot be resolved", pluginId, location);
                continue;
            }
            handleResource(pluginId, location, classPaths, filePaths);
        }
        PluginStaticResource pluginResource = new PluginStaticResource();
        pluginResource.setClassPaths(classPaths);
        pluginResource.setFilePaths(filePaths);
        pluginResource.setBasePlugin(basePlugin);

        if (PLUGIN_RESOURCE_MAP.containsKey(pluginId)) {
            // 如果存在该插件id的插件资源信息, 则先移除它
            remove(pluginId);
        }
        PLUGIN_RESOURCE_MAP.put(pluginId, pluginResource);
    }

    private static void handleResource(String pluginId,
                                       String location,
                                       Set<String> classPaths,
                                       Set<String> filePaths) {
        final int first = location.indexOf(':');
        String type = location.substring(0, first);
        String path = location.substring(first + 1);
        if (CLASSPATH.equalsIgnoreCase(type)) {
            if (path.startsWith(BaseConstants.Symbol.SLASH)) {
                path = path.substring(1);
            }
            if (!path.endsWith(BaseConstants.Symbol.SLASH)) {
                path = path + BaseConstants.Symbol.SLASH;
            }
            classPaths.add(path);
        } else if (FILE.equalsIgnoreCase(type)) {
            if (!path.endsWith(File.separator)) {
                path = path + File.separator;
            }
            filePaths.add(path);
        } else {
            LOG.warn("The plugin '{}' type '{}' cannot be resolved", pluginId, type);
        }
    }


    /**
     * 卸载插件时。调用该方法移除插件的资源信息
     *
     * @param pluginId 插件id
     */
    public static synchronized void remove(String pluginId) {
        PluginStaticResource pluginResource = PLUGIN_RESOURCE_MAP.get(pluginId);
        if (pluginResource == null) {
            return;
        }
        PLUGIN_RESOURCE_MAP.remove(pluginId);
    }


    /**
     * 插件资源解析后的信息
     */
    private static class PluginStaticResource {

        /**
         * basePlugin bean
         */
        private BasePlugin basePlugin;

        /**
         * 定义的classpath集合
         */
        private Set<String> classPaths;

        /**
         * 定义的文件路径集合
         */
        private Set<String> filePaths;

        /**
         * 缓存的资源。key 为资源的可以。值为资源
         */
        private final Map<String, Resource> cacheResourceMaps = new ConcurrentHashMap<>();


        BasePlugin getBasePlugin() {
            return basePlugin;
        }

        void setBasePlugin(BasePlugin basePlugin) {
            this.basePlugin = basePlugin;
        }

        Set<String> getClassPaths() {
            return classPaths;
        }

        void setClassPaths(Set<String> classPaths) {
            this.classPaths = classPaths;
        }

        Set<String> getFilePaths() {
            return filePaths;
        }

        void setFilePaths(Set<String> filePaths) {
            this.filePaths = filePaths;
        }


        Resource getCacheResource(String key) {
            return cacheResourceMaps.get(key);
        }

        void putCacheResource(String key, Resource resource) {
            if (StringUtils.isEmpty(key) || resource == null) {
                return;
            }
            cacheResourceMaps.put(key, resource);
        }

    }


}
