package com.github.codingdebugallday.plugin.framework.integration.pf4j;

import com.github.codingdebugallday.plugin.framework.integration.application.DefaultPluginApplication;
import org.pf4j.PluginManager;

/**
 * <p>
 * Pf4j 集成工厂。获取Pf4j的PluginManager对象
 * </p>
 *
 * @author isaac 2020/6/16 15:56
 * @see DefaultPluginApplication
 * @since 1.0
 */
public interface Pf4jFactory {

    /**
     * 得到插件管理者
     *
     * @return 插件管理者
     */
    PluginManager getPluginManager();

}
