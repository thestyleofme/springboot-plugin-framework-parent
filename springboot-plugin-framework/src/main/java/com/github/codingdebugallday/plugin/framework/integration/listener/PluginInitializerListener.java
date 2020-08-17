package com.github.codingdebugallday.plugin.framework.integration.listener;

/**
 * <p>
 * 插件初始化监听者
 * </p>
 *
 * @author isaac 2020/6/16 14:12
 * @since 1.0
 */
public interface PluginInitializerListener {

    /**
     * 初始化之前
     */
    void before();


    /**
     * 初始化完成
     */
    void complete();

    /**
     * 初始化失败
     *
     * @param throwable 异常
     */
    void failure(Throwable throwable);

}
