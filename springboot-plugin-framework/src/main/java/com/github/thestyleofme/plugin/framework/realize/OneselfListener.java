package com.github.thestyleofme.plugin.framework.realize;


import com.github.thestyleofme.plugin.framework.utils.OrderPriority;

/**
 * <p>
 * 监听本插件模块事件的监听者接口
 * </p>
 *
 * @author isaac 2020/6/16 13:51
 * @since 1.0
 */
public interface OneselfListener {

    /**
     * 执行优先级。用于多个监听器的时候
     *
     * @return OrderPriority
     */
    OrderPriority order();


    /**
     * 启动事件
     *
     * @param basePlugin 当前插件实现的BasePlugin类
     */
    void startEvent(BasePlugin basePlugin);


    /**
     * 停止事件
     *
     * @param basePlugin 当前插件实现的BasePlugin类
     */
    void stopEvent(BasePlugin basePlugin);

}
