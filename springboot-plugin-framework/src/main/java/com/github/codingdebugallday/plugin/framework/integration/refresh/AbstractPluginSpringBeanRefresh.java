package com.github.codingdebugallday.plugin.framework.integration.refresh;

import java.util.List;

import com.github.codingdebugallday.plugin.framework.integration.application.PluginApplication;

/**
 * <p>
 * 抽象的插件SpringBean刷新类监听类
 * 继承该类。在插件动态的注册卸载时, refresh方法被触发, 可以获取到当前环境所有T实现的所有beans(不包括主程序中的beans)
 * </p>
 *
 * @author isaac 2020/6/16 15:58
 * @since 1.0
 */
public abstract class AbstractPluginSpringBeanRefresh<T> extends AbstractSpringBeanRefresh<T> {

    public AbstractPluginSpringBeanRefresh(PluginApplication pluginApplication) {
        super(pluginApplication);
    }

    /**
     * 刷新bean
     */
    @Override
    protected List<T> refresh() {
        return pluginApplication
                .getPluginUser()
                .getPluginBeans(typeClass);
    }

}
