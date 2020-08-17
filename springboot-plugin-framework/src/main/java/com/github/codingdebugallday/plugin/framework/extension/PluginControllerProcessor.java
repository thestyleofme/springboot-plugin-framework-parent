package com.github.codingdebugallday.plugin.framework.extension;

/**
 * <p>
 * controller 处理者
 * </p>
 *
 * @author isaac 2020/6/16 14:39
 * @since 1.0
 */
public interface PluginControllerProcessor {

    /**
     * 注册
     *
     * @param pluginId        插件id
     * @param controllerClass controller 类
     */
    void register(String pluginId, Class<?> controllerClass);

    /**
     * 卸载
     *
     * @param pluginId        插件id
     * @param controllerClass controller 类
     */
    void unregister(String pluginId, Class<?> controllerClass);

}
