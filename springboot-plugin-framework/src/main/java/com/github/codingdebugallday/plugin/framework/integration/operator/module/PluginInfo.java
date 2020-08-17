package com.github.codingdebugallday.plugin.framework.integration.operator.module;

import org.pf4j.PluginDescriptor;
import org.pf4j.PluginState;

/**
 * <p>
 * 插件信息
 * </p>
 *
 * @author isaac 2020/6/16 15:40
 * @since 1.0
 */
public class PluginInfo {

    /**
     * 插件基本信息
     */
    private final PluginDescriptor pluginDescriptor;

    /**
     * 插件状态
     */
    private final PluginState pluginState;

    /**
     * 插件路径
     */
    private final String path;

    /**
     * 允许模式
     */
    private final String runMode;


    public PluginInfo(PluginDescriptor pluginDescriptor,
                      PluginState pluginState,
                      String path,
                      String runMode) {
        this.pluginDescriptor = pluginDescriptor;
        this.pluginState = pluginState;
        this.path = path;
        this.runMode = runMode;
    }

    public PluginDescriptor getPluginDescriptor() {
        return pluginDescriptor;
    }

    public PluginState getPluginState() {
        return pluginState;
    }

    public String getPluginStateString() {
        return pluginState.toString();
    }

    public String getPath() {
        return path;
    }

    public String getRunMode() {
        return runMode;
    }

}
