package com.github.thestyleofme.plugin.framework.factory.process.pipe.bean;

import com.github.thestyleofme.plugin.framework.factory.PluginRegistryInfo;

/**
 * <p>
 * 插件bean注册者
 * </p>
 *
 * @author isaac 2020/6/16 14:59
 * @since 1.0
 */
public interface PluginBeanRegister<T> {

    /**
     * 注册者的唯一标识
     *
     * @return String
     */
    String key();

    /**
     * 注册插件中的bane
     *
     * @param registerPluginInfo 插件信息
     * @return 返回注册的bean的标识。卸载时,会将该参数传入
     */
    T register(PluginRegistryInfo registerPluginInfo);

    /**
     * 卸载插件中的bean
     *
     * @param registerPluginInfo 插件信息
     * @param t                  注册时返回的参数
     */
    void unregister(PluginRegistryInfo registerPluginInfo, T t);

}
