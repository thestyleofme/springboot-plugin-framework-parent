package com.github.codingdebugallday.integration.listener;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

/**
 * <p>
 * 插件初始化监听者工厂
 * </p>
 *
 * @author isaac 2020/6/16 14:12
 * @since 1.0
 */
public class PluginInitializerListenerFactory implements PluginInitializerListener {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final List<PluginInitializerListener> pluginInitializerListeners = new ArrayList<>();

    public final ApplicationContext applicationContext;

    public PluginInitializerListenerFactory(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        // 添加默认的初始化监听者
        pluginInitializerListeners.add(new com.github.codingdebugallday.integration.listener.DefaultInitializerListener(applicationContext));
    }

    @Override
    public void before() {
        try {
            for (PluginInitializerListener pluginInitializerListener : pluginInitializerListeners) {
                pluginInitializerListener.before();
            }
        } catch (Exception e) {
            log.error("before error,", e);
        }
    }

    @Override
    public void complete() {
        try {
            for (com.github.codingdebugallday.integration.listener.PluginInitializerListener pluginInitializerListener : pluginInitializerListeners) {
                pluginInitializerListener.complete();
            }
        } catch (Exception e) {
            log.error("complete error,", e);
        }
    }

    @Override
    public void failure(Throwable throwable) {
        try {
            for (com.github.codingdebugallday.integration.listener.PluginInitializerListener pluginInitializerListener : pluginInitializerListeners) {
                pluginInitializerListener.failure(throwable);
            }
        } catch (Exception e) {
            log.error("failure error,", e);
        }
    }

    /**
     * 添加监听者
     *
     * @param pluginInitializerListener pluginInitializerListener
     */
    public void addPluginInitializerListeners(com.github.codingdebugallday.integration.listener.PluginInitializerListener pluginInitializerListener) {
        if (pluginInitializerListener != null) {
            pluginInitializerListeners.add(pluginInitializerListener);
        }
    }

}
