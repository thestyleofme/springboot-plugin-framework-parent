package com.github.codingdebugallday.integration.refresh;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import com.github.codingdebugallday.integration.application.PluginApplication;
import com.github.codingdebugallday.integration.listener.PluginListener;

/**
 * <p>
 * 抽象的SpringBean刷新类监听类
 * 继承该类。在插件动态的注册卸载时, refresh方法被触发, 可以获取到当前环境所有T实现的所有beans(包括主程序中的beans)
 * </p>
 *
 * @author isaac 2020/6/16 15:58
 * @since 1.0
 */
public abstract class AbstractSpringBeanRefresh<T> implements PluginListener {

    private List<T> beans;

    protected final Class<T> typeClass;
    protected final PluginApplication pluginApplication;


    @SuppressWarnings("unchecked")
    public AbstractSpringBeanRefresh(PluginApplication pluginApplication) {
        this.pluginApplication = pluginApplication;
        pluginApplication.addListener(this);
        this.typeClass = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass())
                .getActualTypeArguments()[0];
    }

    @Override
    public void register(String pluginId) {
        this.beans = refresh();
        registerEvent(beans);
    }

    @Override
    public void unregister(String pluginId) {
        this.beans = refresh();
        unregisterEvent(beans);
    }

    @Override
    public void failure(String pluginId, Throwable throwable) {

    }

    /**
     * 注册事件
     *
     * @param beans 当前所有实现的bean
     */
    protected void registerEvent(List<T> beans) {

    }

    /**
     * 卸载事件
     *
     * @param beans 当前卸载后所有的beans
     */
    protected void unregisterEvent(List<T> beans) {

    }

    /**
     * 刷新bean
     *
     * @return 返回刷新后的Bean集合
     */
    protected List<T> refresh() {
        return pluginApplication
                .getPluginUser()
                .getBeans(typeClass);
    }


    /**
     * 得到beans
     *
     * @return beansMap
     */
    public List<T> getBeans() {
        return beans;
    }
}
