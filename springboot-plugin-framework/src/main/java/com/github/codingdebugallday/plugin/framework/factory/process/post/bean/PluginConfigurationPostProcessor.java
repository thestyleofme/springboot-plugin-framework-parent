package com.github.codingdebugallday.plugin.framework.factory.process.post.bean;

import java.util.List;
import java.util.Objects;

import com.github.codingdebugallday.plugin.framework.factory.PluginRegistryInfo;
import com.github.codingdebugallday.plugin.framework.factory.process.post.PluginPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ConfigurationClassPostProcessor;
import org.springframework.context.support.GenericApplicationContext;

/**
 * <p>
 * 插件中Configuration处理者
 * </p>
 *
 * @author isaac 2020/6/16 13:55
 * @since 1.0
 */
public class PluginConfigurationPostProcessor implements PluginPostProcessor {

    private final GenericApplicationContext applicationContext;

    public PluginConfigurationPostProcessor(ApplicationContext applicationContext) {
        Objects.requireNonNull(applicationContext);
        this.applicationContext = (GenericApplicationContext) applicationContext;
    }

    @Override
    public void initialize() {
        // ignore
    }

    @Override
    public void register(List<PluginRegistryInfo> pluginRegistryInfos) {
        ConfigurationClassPostProcessor configurationClassPostProcessor =
                applicationContext.getBean(ConfigurationClassPostProcessor.class);
        configurationClassPostProcessor.processConfigBeanDefinitions(applicationContext);
    }

    @Override
    public void unregister(List<PluginRegistryInfo> pluginRegistryInfos) {
        // ignore
    }

}
