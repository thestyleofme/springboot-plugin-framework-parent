package com.github.codingdebugallday.plugin.framework.integration;

/**
 * <p>
 * 默认的插件集成配置 给非必须配置设置了默认值
 * </p>
 *
 * @author isaac 2020/6/16 14:11
 * @since 1.0
 */
public abstract class AbstractIntegrationConfiguration implements IntegrationConfiguration {

    @Override
    public String uploadTempPath() {
        return "temp";
    }

    @Override
    public String backupPath() {
        return "backupPlugin";
    }

    @Override
    public String pluginRestControllerPathPrefix() {
        return "/plugins";
    }

    @Override
    public boolean enablePluginIdRestControllerPathPrefix() {
        return true;
    }
}
