package com.github.thestyleofme.plugin.framework.utils;

/**
 * <p>
 * 执行顺序
 * </p>
 *
 * @author isaac 2020/6/16 13:59
 * @since 1.0
 */
public class OrderExecution {

    private OrderExecution() {
    }

    /**
     * 低优先级
     */
    public static final int LOW = Integer.MAX_VALUE;


    /**
     * 中优先级
     */
    public static final int MIDDLE = Integer.MAX_VALUE;


    /**
     * 高优先级
     */
    public static final int HIGH = Integer.MIN_VALUE;


}
