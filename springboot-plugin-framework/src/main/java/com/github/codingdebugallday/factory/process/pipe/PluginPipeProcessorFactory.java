package com.github.codingdebugallday.factory.process.pipe;

import java.util.ArrayList;
import java.util.List;

import com.github.codingdebugallday.extension.ExtensionInitializer;
import com.github.codingdebugallday.factory.PluginRegistryInfo;
import com.github.codingdebugallday.factory.process.pipe.bean.BasicBeanProcessor;
import com.github.codingdebugallday.factory.process.pipe.bean.ConfigBeanProcessor;
import com.github.codingdebugallday.factory.process.pipe.bean.ConfigFileBeanProcessor;
import com.github.codingdebugallday.factory.process.pipe.bean.OneselfListenerStopEventProcessor;
import com.github.codingdebugallday.factory.process.pipe.classs.PluginClassProcess;
import org.springframework.context.ApplicationContext;

/**
 * <p>
 * 插件的pipe处理者工厂
 * </p>
 *
 * @author isaac 2020/6/16 14:08
 * @since 1.0
 */
public class PluginPipeProcessorFactory implements PluginPipeProcessor {

    private final ApplicationContext applicationContext;
    private final List<PluginPipeProcessor> pluginPipeProcessors = new ArrayList<>();

    public PluginPipeProcessorFactory(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void initialize() {
        // OneselfListenerStopEventProcessor 触发停止事件, 必须第一个执行
        pluginPipeProcessors.add(new OneselfListenerStopEventProcessor(applicationContext));
        pluginPipeProcessors.add(new PluginClassProcess());
        // 配置文件在所有bean中第一个初始化。
        pluginPipeProcessors.add(new ConfigFileBeanProcessor(applicationContext));
        // 接下来初始化插件中配置bean的初始化
        pluginPipeProcessors.add(new ConfigBeanProcessor(applicationContext));
        pluginPipeProcessors.add(new BasicBeanProcessor(applicationContext));
        // 添加扩展
        pluginPipeProcessors.addAll(ExtensionInitializer.getPipeProcessorExtends());

        // 进行初始化
        for (PluginPipeProcessor pluginPipeProcessor : pluginPipeProcessors) {
            pluginPipeProcessor.initialize();
        }
    }

    @Override
    public void register(PluginRegistryInfo pluginRegistryInfo) {
        for (PluginPipeProcessor pluginPipeProcessor : pluginPipeProcessors) {
            pluginPipeProcessor.register(pluginRegistryInfo);
        }
    }

    @Override
    public void unregister(PluginRegistryInfo pluginRegistryInfo) {
        for (PluginPipeProcessor pluginPipeProcessor : pluginPipeProcessors) {
            pluginPipeProcessor.unregister(pluginRegistryInfo);
        }
    }

}
