package com.github.codingdebugallday.factory.process.pipe.bean.configuration;

import java.util.List;
import java.util.Objects;

import com.github.codingdebugallday.exceptions.PluginException;
import com.github.codingdebugallday.integration.IntegrationConfiguration;
import com.github.codingdebugallday.loader.PluginResourceLoader;
import com.github.codingdebugallday.loader.ResourceWrapper;
import com.github.codingdebugallday.loader.load.PluginConfigFileLoader;
import com.github.codingdebugallday.realize.BasePlugin;
import org.springframework.core.io.Resource;
import org.springframework.util.CollectionUtils;

/**
 * <p>
 * 抽象的插件配置文件解析者
 * </p>
 *
 * @author isaac 2020/6/16 14:03
 * @since 1.0
 */
public abstract class AbstractConfigurationParser implements ConfigurationParser {

    private final IntegrationConfiguration configuration;

    public AbstractConfigurationParser(IntegrationConfiguration configuration) {
        Objects.requireNonNull(configuration);
        this.configuration = configuration;
    }

    @Override
    public Object parse(BasePlugin basePlugin, PluginConfigDefinition pluginConfigDefinition) {
        Class<?> configClass = pluginConfigDefinition.getConfigClass();
        if (pluginConfigDefinition.getConfigClass() == null) {
            throw new IllegalArgumentException("pluginConfigDefinition : " + pluginConfigDefinition + " " +
                    "configClass can not be null");
        }
        String fileName = pluginConfigDefinition.getFileName();
        if (pluginConfigDefinition.getFileName() == null || "".equals(pluginConfigDefinition.getFileName())) {
            throw new IllegalArgumentException("pluginConfigDefinition : " + pluginConfigDefinition + " " +
                    "fileName can not be empty");
        }
        PluginResourceLoader resourceLoader = new PluginConfigFileLoader(
                configuration.pluginConfigFilePath(),
                fileName
        );
        ResourceWrapper resourceWrapper = resourceLoader.load(basePlugin);
        if (resourceWrapper == null) {
            return null;
        }
        List<Resource> resources = resourceWrapper.getResources();
        if (CollectionUtils.isEmpty(resources) || resources.size() != 1) {
            return null;
        }
        Object o = parse(resources.get(0), configClass);
        if (o == null) {
            try {
                return configClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new PluginException(e);
            }
        }
        return o;
    }


    /**
     * 子类实现解析
     *
     * @param resource          配置文件的资源信息
     * @param pluginConfigClass 配置文件class
     * @return 返回映射后的存在值得对象
     */
    protected abstract Object parse(Resource resource, Class<?> pluginConfigClass);

}
