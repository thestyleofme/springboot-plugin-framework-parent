package com.github.codingdebugallday.plugin.framework.integration.listener;

/**
 * <p>
 * 插件bean监听者
 * </p>
 *
 * @author isaac 2020/6/16 10:16
 * @since 1.0
 */
public interface PluginListener {


    /**
     * 注册插件
     *
     * @param pluginId 插件id
     */
    void register(String pluginId);

    /**
     * 卸载插件
     *
     * @param pluginId 插件id
     */
    void unregister(String pluginId);

    /**
     * 失败监听
     *
     * @param pluginId  插件id
     * @param throwable 异常信息
     */
    void failure(String pluginId, Throwable throwable);


}

