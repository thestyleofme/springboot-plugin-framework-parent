package com.github.codingdebugallday.plugin.framework.factory.process.post;

import java.util.ArrayList;
import java.util.List;

import com.github.codingdebugallday.plugin.framework.extension.ExtensionInitializer;
import com.github.codingdebugallday.plugin.framework.factory.PluginRegistryInfo;
import com.github.codingdebugallday.plugin.framework.factory.process.post.bean.PluginConfigurationPostProcessor;
import com.github.codingdebugallday.plugin.framework.factory.process.post.bean.PluginControllerPostProcessor;
import com.github.codingdebugallday.plugin.framework.factory.process.post.bean.PluginInvokePostProcessor;
import com.github.codingdebugallday.plugin.framework.factory.process.post.bean.PluginOneselfStartEventProcessor;
import org.springframework.context.ApplicationContext;

/**
 * <p>
 * 插件后置处理工厂
 * </p>
 *
 * @author isaac 2020/6/16 15:22
 * @since 1.0
 */
public class PluginPostProcessorFactory implements PluginPostProcessor {

    private final List<PluginPostProcessor> pluginPostProcessors = new ArrayList<>();
    private final ApplicationContext applicationContext;

    public PluginPostProcessorFactory(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void initialize() {
        // 添加扩展
        pluginPostProcessors.addAll(ExtensionInitializer.getPostProcessorExtends());
        // 以下顺序不能更改。
        pluginPostProcessors.add(new PluginConfigurationPostProcessor(applicationContext));
        pluginPostProcessors.add(new PluginInvokePostProcessor(applicationContext));
        pluginPostProcessors.add(new PluginControllerPostProcessor(applicationContext));
        // 主要触发启动监听事件，因此在最后一个执行。配合 OneselfListenerStopEventProcessor 该类触发启动、停止事件。
        pluginPostProcessors.add(new PluginOneselfStartEventProcessor(applicationContext));
        // 进行初始化
        for (PluginPostProcessor pluginPostProcessor : pluginPostProcessors) {
            pluginPostProcessor.initialize();
        }
    }

    @Override
    public void register(List<PluginRegistryInfo> pluginRegistryInfos) {
        for (PluginPostProcessor pluginPostProcessor : pluginPostProcessors) {
            pluginPostProcessor.register(pluginRegistryInfos);
        }
    }

    @Override
    public void unregister(List<PluginRegistryInfo> pluginRegistryInfos) {
        for (PluginPostProcessor pluginPostProcessor : pluginPostProcessors) {
            pluginPostProcessor.unregister(pluginRegistryInfos);
        }
    }

}
