package com.github.codingdebugallday.plugin.framework.factory.process.pipe.bean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.codingdebugallday.plugin.framework.factory.PluginRegistryInfo;
import com.github.codingdebugallday.plugin.framework.factory.SpringBeanRegister;
import com.github.codingdebugallday.plugin.framework.factory.process.pipe.PluginPipeProcessor;
import com.github.codingdebugallday.plugin.framework.factory.process.pipe.classs.group.ConfigBeanGroup;
import com.github.codingdebugallday.plugin.framework.realize.ConfigBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

/**
 * <p>
 * 插件中实现 ConfigBean 接口的的处理者
 * </p>
 *
 * @author isaac 2020/6/16 14:05
 * @since 1.0
 */
public class ConfigBeanProcessor implements PluginPipeProcessor {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private static final String KEY = "ConfigBeanProcessor";

    private final SpringBeanRegister springBeanRegister;
    private final ApplicationContext applicationContext;

    public ConfigBeanProcessor(ApplicationContext applicationContext) {
        this.springBeanRegister = new SpringBeanRegister(applicationContext);
        this.applicationContext = applicationContext;
    }


    @Override
    public void initialize() {
        // ignore
    }

    @Override
    public void register(PluginRegistryInfo pluginRegistryInfo) {
        List<Class<?>> configBeans =
                pluginRegistryInfo.getGroupClasses(ConfigBeanGroup.GROUP_ID);
        if (configBeans == null || configBeans.isEmpty()) {
            return;
        }
        String pluginId = pluginRegistryInfo.getPluginWrapper().getPluginId();
        Map<String, ConfigBean> configBeanMap = new HashMap<>(8);
        for (Class<?> aClass : configBeans) {
            if (aClass == null) {
                continue;
            }
            String name = springBeanRegister.register(pluginId, aClass);
            Object bean = applicationContext.getBean(name);
            if (bean instanceof ConfigBean) {
                ConfigBean configBean = (ConfigBean) bean;
                configBean.initialize();
                configBeanMap.put(name, configBean);
            }
        }
        pluginRegistryInfo.addProcessorInfo(KEY, configBeanMap);
    }

    @Override
    public void unregister(PluginRegistryInfo pluginRegistryInfo) {
        Map<String, ConfigBean> configBeanMap = pluginRegistryInfo.getProcessorInfo(KEY);
        if (configBeanMap == null) {
            return;
        }
        String pluginId = pluginRegistryInfo.getPluginWrapper().getPluginId();
        configBeanMap.forEach((beanName, configBean) -> {
            if (configBean == null) {
                return;
            }
            try {
                configBean.destroy();
            } catch (Exception e) {
                log.error("ConfigBean '' destroy exception. {}", e.getMessage(), e);
            }
            springBeanRegister.unregister(pluginId, beanName);
        });

    }


}
