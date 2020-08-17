package com.basic.example.main.plugin;

import com.github.codingdebugallday.plugin.framework.integration.application.PluginApplication;
import com.github.codingdebugallday.plugin.framework.integration.refresh.AbstractPluginSpringBeanRefresh;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 输出名称工厂
 * </p>
 *
 * @author isaac 2020/6/16 17:24
 * @since 1.0
 */
@Component
public class ConsoleNameFactory extends AbstractPluginSpringBeanRefresh<ConsoleName> {

    public ConsoleNameFactory(PluginApplication pluginApplication) {
        super(pluginApplication);
        pluginApplication.addListener(this);
    }
}
