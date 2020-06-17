package com.github.codingdebugallday.factory.process.pipe;


import com.github.codingdebugallday.factory.PluginRegistryInfo;

/**
 * <p>
 * 插件管道处理者接口
 * </p>
 *
 * @author isaac 2020/6/16 11:53
 * @since 1.0
 */
public interface PluginPipeProcessor {

    /**
     * 初始化
     */
    default void initialize() {
        // ignore
    }

    /**
     * 处理该插件的注册
     *
     * @param pluginRegistryInfo 插件注册的信息
     */
    void register(PluginRegistryInfo pluginRegistryInfo);

    /**
     * 处理该插件的卸载
     *
     * @param pluginRegistryInfo 插件注册的信息
     */
    void unregister(PluginRegistryInfo pluginRegistryInfo);

}
