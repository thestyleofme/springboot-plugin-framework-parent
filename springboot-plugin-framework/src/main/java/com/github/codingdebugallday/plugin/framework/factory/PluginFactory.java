package com.github.codingdebugallday.plugin.framework.factory;

import com.github.codingdebugallday.plugin.framework.integration.PluginListenerContext;
import org.pf4j.PluginWrapper;

/**
 * <p>
 * 插件注册者接口
 * </p>
 *
 * @author isaac 2020/6/16 10:17
 * @since 1.0
 */
public interface PluginFactory extends PluginListenerContext {

    /**
     * 工厂初始化
     */
    void initialize();

    /**
     * 注册插件。
     *
     * @param pluginWrapper 插件
     * @return 插件工厂
     */
    PluginFactory register(PluginWrapper pluginWrapper);

    /**
     * 注销插件。
     *
     * @param pluginId 插件id
     * @return 插件工厂
     */
    PluginFactory unregister(String pluginId);

    /**
     * 注册或者注销后的构建调用
     */
    void build();

}
