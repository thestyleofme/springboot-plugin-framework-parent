package com.github.codingdebugallday.integration.application;

import com.github.codingdebugallday.extension.AbstractExtension;
import com.github.codingdebugallday.integration.PluginListenerContext;
import com.github.codingdebugallday.integration.listener.PluginInitializerListener;
import com.github.codingdebugallday.integration.operator.PluginOperator;
import com.github.codingdebugallday.integration.user.PluginUser;
import org.springframework.context.ApplicationContext;

/**
 * <p>
 * 插件应用
 * </p>
 *
 * @author isaac 2020/6/16 14:15
 * @since 1.0
 */
public interface PluginApplication extends PluginListenerContext {

    /**
     * 初始化
     *
     * @param applicationContext Spring上下文
     * @param listener           插件初始化监听者
     */
    void initialize(ApplicationContext applicationContext, PluginInitializerListener listener);


    /**
     * 获得插插件操作者
     *
     * @return 插件操作者
     */
    PluginOperator getPluginOperator();

    /**
     * 获得插插件操作者
     *
     * @return 插件操作者
     */
    PluginUser getPluginUser();

    /**
     * 添加扩展
     *
     * @param extension 扩展类
     */
    void addExtension(AbstractExtension extension);

}
