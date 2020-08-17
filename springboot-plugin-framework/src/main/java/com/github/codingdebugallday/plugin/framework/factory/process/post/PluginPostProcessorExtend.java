package com.github.codingdebugallday.plugin.framework.factory.process.post;

import com.github.codingdebugallday.plugin.framework.utils.OrderPriority;

/**
 * <p>
 * 后置插件处理者
 * </p>
 *
 * @author isaac 2020/6/16 15:22
 * @since 1.0
 */
public interface PluginPostProcessorExtend extends PluginPostProcessor {

    /**
     * 扩展key
     *
     * @return String
     */
    String key();

    /**
     * 执行顺序
     *
     * @return OrderPriority
     */
    OrderPriority order();

}
