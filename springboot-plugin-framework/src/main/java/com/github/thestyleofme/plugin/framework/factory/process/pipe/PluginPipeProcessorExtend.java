package com.github.thestyleofme.plugin.framework.factory.process.pipe;


import com.github.thestyleofme.plugin.framework.utils.OrderPriority;

/**
 * <p>
 * 单插件处理者扩展接口
 * </p>
 *
 * @author isaac 2020/6/16 11:52
 * @since 1.0
 */
public interface PluginPipeProcessorExtend extends PluginPipeProcessor {

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
