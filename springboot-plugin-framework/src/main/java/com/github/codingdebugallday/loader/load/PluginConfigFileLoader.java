package com.github.codingdebugallday.loader.load;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import com.github.codingdebugallday.exceptions.PluginException;
import com.github.codingdebugallday.loader.PluginResourceLoader;
import com.github.codingdebugallday.loader.ResourceWrapper;
import com.github.codingdebugallday.realize.BasePlugin;
import com.github.codingdebugallday.utils.OrderPriority;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

/**
 * <p>
 * 插件配置文件加载者
 * </p>
 *
 * @author isaac 2020/6/16 16:05
 * @since 1.0
 */
public class PluginConfigFileLoader implements PluginResourceLoader {

    private static final Logger log = LoggerFactory.getLogger(PluginConfigFileLoader.class);

    private final String configFilePath;
    private final String fileName;

    public PluginConfigFileLoader(String configFilePath,
                                  String fileName) {
        this.configFilePath = configFilePath;
        this.fileName = fileName;
    }

    @Override
    public String key() {
        return null;
    }

    @Override
    public ResourceWrapper load(BasePlugin basePlugin) {
        List<Supplier<SupplierBean>> suppliers = new ArrayList<>();
        suppliers.add(findConfigRoot());
        suppliers.add(findPluginRoot(basePlugin));
        suppliers.add(findClassPath(basePlugin));

        for (Supplier<SupplierBean> supplier : suppliers) {
            SupplierBean supplierBean = supplier.get();
            Resource resource = supplierBean.getResource();
            if (resource.exists()) {
                List<Resource> resources = new ArrayList<>();
                resources.add(resource);
                ResourceWrapper resourceWrapper = new ResourceWrapper();
                resourceWrapper.addResources(resources);
                log.info("Load the plugin '{}' config file '{}' from '{}'",
                        basePlugin.getWrapper().getPluginId(), fileName, supplierBean.getPath());
                return resourceWrapper;
            }
        }
        throw new PluginException("Not found plugin '" + basePlugin.getWrapper().getPluginId() + "' " +
                "config file : " + fileName);
    }

    @Override
    public void unload(BasePlugin basePlugin, ResourceWrapper resourceWrapper) {
        // Do nothing
    }

    @Override
    public OrderPriority order() {
        return OrderPriority.getHighPriority().down(20);
    }

    /**
     * 从插件文件的根目录查找配置文件
     *
     * @param basePlugin basePlugin
     * @return 返回resource
     */
    private Supplier<SupplierBean> findPluginRoot(BasePlugin basePlugin) {
        return () -> {
            Path pluginPath = basePlugin.getWrapper().getPluginPath();
            String rootPath = pluginPath.getParent().toString();
            String configPath = rootPath + File.separatorChar + fileName;
            Resource resource = new FileSystemResource(configPath);
            return new SupplierBean(rootPath, resource);
        };
    }


    /**
     * 从插件配置文件 pluginConfigFilePath 的路径下查找配置文件
     *
     * @return 返回resource
     */
    private Supplier<SupplierBean> findConfigRoot() {
        return () -> {
            String filePath = configFilePath + File.separatorChar + fileName;
            Resource resource = new FileSystemResource(filePath);
            return new SupplierBean(configFilePath, resource);
        };
    }

    /**
     * 从ClassPath 中查找配置文件
     *
     * @param basePlugin basePlugin
     * @return 返回resource
     */
    private Supplier<SupplierBean> findClassPath(BasePlugin basePlugin) {
        return () -> {
            Resource resource = new ClassPathResource("/" + fileName, basePlugin.getWrapper().getPluginClassLoader());
            return new SupplierBean("classPath", resource);
        };
    }

    private static class SupplierBean {
        private final String path;
        private final Resource resource;

        public SupplierBean(String path, Resource resource) {
            this.path = path;
            this.resource = resource;
        }

        public String getPath() {
            return path;
        }

        public Resource getResource() {
            return resource;
        }
    }


}
