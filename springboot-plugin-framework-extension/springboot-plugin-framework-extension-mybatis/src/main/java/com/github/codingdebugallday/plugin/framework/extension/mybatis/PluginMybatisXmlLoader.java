package com.github.codingdebugallday.plugin.framework.extension.mybatis;

import java.io.IOException;
import java.util.*;

import com.github.codingdebugallday.plugin.framework.exceptions.PluginException;
import com.github.codingdebugallday.plugin.framework.extension.mybatis.configuration.SpringBootMybatisConfig;
import com.github.codingdebugallday.plugin.framework.loader.PluginResourceLoader;
import com.github.codingdebugallday.plugin.framework.loader.ResourceWrapper;
import com.github.codingdebugallday.plugin.framework.realize.BasePlugin;
import com.github.codingdebugallday.plugin.framework.utils.OrderPriority;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 定制插件 Mybatis xml 加载者
 * </p>
 *
 * @author isaac 2020/6/18 17:24
 * @since 1.0
 */
public class PluginMybatisXmlLoader implements PluginResourceLoader {

    private static final Logger LOG = LoggerFactory.getLogger(PluginMybatisXmlLoader.class);

    public static final String DEFAULT_KEY = "PluginMybatisXmlLoader";

    private static final String TYPE_FILE = "file";
    private static final String TYPE_CLASSPATH = "classpath";
    private static final String TYPE_PACKAGE = "package";

    PluginMybatisXmlLoader() {

    }


    @Override
    public String key() {
        return DEFAULT_KEY;
    }

    @Override
    public ResourceWrapper load(BasePlugin basePlugin) {
        if (!(basePlugin instanceof SpringBootMybatisConfig)) {
            LOG.warn("Plugin '{}' not implements SpringBootMybatisConfig, If you need to use mybatis in the plugin," +
                    "Please implements SpringBootMybatisConfig interface", basePlugin.getWrapper().getPluginId());
            return null;
        }
        SpringBootMybatisConfig springBootMybatisConfig = (SpringBootMybatisConfig) basePlugin;
        Set<String> mybatisMapperXmlLocationsMatch = springBootMybatisConfig.mybatisMapperXmlLocationsMatch();
        if (mybatisMapperXmlLocationsMatch == null || mybatisMapperXmlLocationsMatch.isEmpty()) {
            LOG.warn("SpringBootMybatisConfig -> mybatisMapperXmlLocationsMatch return is empty, " +
                    "Please check configuration");
            return new ResourceWrapper();
        }
        ResourcePatternResolver resourcePatternResolver =
                new PathMatchingResourcePatternResolver(basePlugin.getWrapper().getPluginClassLoader());
        List<Resource> resources = new ArrayList<>();
        for (String mybatisMapperXmlLocationMatch : mybatisMapperXmlLocationsMatch) {
            if (StringUtils.isEmpty(mybatisMapperXmlLocationMatch)) {
                continue;
            }
            List<Resource> loadResources = load(resourcePatternResolver, mybatisMapperXmlLocationMatch);
            if (loadResources != null && !loadResources.isEmpty()) {
                resources.addAll(loadResources);
            }
        }
        ResourceWrapper resourceWrapper = new ResourceWrapper();
        resourceWrapper.addResources(resources);
        return resourceWrapper;
    }

    @Override
    public void unload(BasePlugin basePlugin, ResourceWrapper resourceWrapper) {
        //
    }

    @Override
    public OrderPriority order() {
        return OrderPriority.getHighPriority();
    }

    /**
     * 加载xml资源
     *
     * @param resourcePatternResolver       资源匹配解决者
     * @param mybatisMapperXmlLocationMatch mybatis xml 文件匹配规则
     * @return List
     */
    private List<Resource> load(ResourcePatternResolver resourcePatternResolver,
                                String mybatisMapperXmlLocationMatch) {
        String[] split = mybatisMapperXmlLocationMatch.split(":");
        if (split.length != 2) {
            return Collections.emptyList();
        }
        String type = split[0];
        String location = split[1];
        String matchLocation = null;
        if (Objects.equals(type, TYPE_CLASSPATH) || Objects.equals(type, TYPE_FILE)) {
            matchLocation = location;
        } else if (Objects.equals(type, TYPE_PACKAGE)) {
            matchLocation = location.replace(".", "/");
        }
        if (matchLocation == null) {
            LOG.error("mybatisMapperXmlLocation {} illegal", mybatisMapperXmlLocationMatch);
            return Collections.emptyList();
        }
        try {
            Resource[] resources = resourcePatternResolver.getResources(matchLocation);
            return Arrays.asList(resources);
        } catch (IOException e) {
            LOG.error("mybatisMapperXmlLocation match error", e);
            throw new PluginException("mybatisMapperXmlLocation match error");
        }
    }

}
