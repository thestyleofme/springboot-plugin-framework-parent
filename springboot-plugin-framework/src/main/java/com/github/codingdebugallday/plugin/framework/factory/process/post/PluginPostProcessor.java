package com.github.codingdebugallday.plugin.framework.factory.process.post;

import java.util.List;

import com.github.codingdebugallday.plugin.framework.factory.PluginRegistryInfo;

/**
 * <p>
 * 插件后置处理者接口
 * </p>
 *
 * @author isaac 2020/6/16 15:21
 * @since 1.0
 */
public interface PluginPostProcessor {

    /**
     * 初始化
     */
    void initialize();


    /**
     * 处理该插件的注册
     *
     * @param pluginRegistryInfos 插件注册的信息
     */
    void register(List<PluginRegistryInfo> pluginRegistryInfos);


    /**
     * 处理该插件的卸载
     *
     * @param pluginRegistryInfos 插件注册的信息
     */
    void unregister(List<PluginRegistryInfo> pluginRegistryInfos);

}
