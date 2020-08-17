package com.github.codingdebugallday.plugin.framework.integration.listener;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.context.support.GenericApplicationContext;

/**
 * <p>
 * 插件监听工厂
 * </p>
 *
 * @author isaac 2020/6/16 14:14
 * @since 1.0
 */
public class PluginListenerFactory implements PluginListener {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final List<PluginListener> listeners = new ArrayList<>();
    private final List<Class<?>> listenerClasses = new ArrayList<>();

    @Override
    public void register(String pluginId) {
        for (PluginListener listener : listeners) {
            try {
                listener.register(pluginId);
            } catch (Exception e) {
                log.error("listener registry error,", e);
            }
        }
    }

    @Override
    public void unregister(String pluginId) {
        for (PluginListener listener : listeners) {
            try {
                listener.unregister(pluginId);
            } catch (Exception e) {
                log.error("listener unregister error,", e);
            }
        }
    }

    @Override
    public void failure(String pluginId, Throwable throwable) {
        for (PluginListener listener : listeners) {
            try {
                listener.failure(pluginId, throwable);
            } catch (Exception e) {
                log.error("listener failure error,", e);
            }
        }
    }

    /**
     * 添加监听者
     *
     * @param pluginListener 插件监听者
     */
    public void addPluginListener(PluginListener pluginListener) {
        if (pluginListener != null) {
            listeners.add(pluginListener);
        }
    }

    /**
     * 添加监听者
     *
     * @param pluginListenerClass 插件监听者Class类
     * @param <T>                 插件监听者类。继承 PluginListener
     */
    public <T extends PluginListener> void addPluginListener(Class<T> pluginListenerClass) {
        if (pluginListenerClass != null) {
            synchronized (listenerClasses) {
                listenerClasses.add(pluginListenerClass);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends PluginListener> void buildListenerClass(GenericApplicationContext applicationContext) {
        if (applicationContext == null) {
            return;
        }
        synchronized (listenerClasses) {
            for (Class<?> listenerClass : listenerClasses) {
                // 兼容 spring 4.x
                applicationContext.registerBeanDefinition(listenerClass.getName(), BeanDefinitionBuilder.genericBeanDefinition(listenerClass).getBeanDefinition());
                T bean = (T) applicationContext.getBean(listenerClass);
                listeners.add(bean);
            }
            listenerClasses.clear();
        }
    }

    /**
     * 得到监听者
     *
     * @return 监听者集合
     */
    public List<PluginListener> getListeners() {
        return listeners;
    }

    /**
     * 得到监听者class
     *
     * @return 监听者class集合
     */
    public List<Class<?>> getListenerClasses() {
        return listenerClasses;
    }

}
