package com.github.codingdebugallday.plugin.framework.factory.process.pipe.bean.configuration;

import com.github.codingdebugallday.plugin.framework.realize.BasePlugin;

/**
 * <p>
 * 配置解析者
 * </p>
 *
 * @author isaac 2020/6/16 14:04
 * @since 1.0
 */
public interface ConfigurationParser {

    /**
     * 配置解析
     *
     * @param basePlugin             插件信息
     * @param pluginConfigDefinition 插件配置定义
     * @return 解析后映射值的对象
     */
    Object parse(BasePlugin basePlugin, PluginConfigDefinition pluginConfigDefinition);

}
