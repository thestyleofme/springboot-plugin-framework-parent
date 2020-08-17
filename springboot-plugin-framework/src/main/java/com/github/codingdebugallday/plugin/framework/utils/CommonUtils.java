package com.github.codingdebugallday.plugin.framework.utils;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

import org.springframework.util.CollectionUtils;

/**
 * <p>
 * 通用工具
 * </p>
 *
 * @author isaac 2020/6/16 11:48
 * @since 1.0
 */
public class CommonUtils {

    private CommonUtils() {
    }

    /**
     * list按照int排序 数字越大 越排在前面
     *
     * @param list      list集合
     * @param orderImpl 排序实现
     * @param <T>       T
     * @return List
     */
    public static <T> List<T> order(List<T> list, Function<T, Integer> orderImpl) {
        if (CollectionUtils.isEmpty(list)) {
            return list;
        }
        list.sort(Comparator.comparing(orderImpl, Comparator.nullsLast(Comparator.reverseOrder())));
        return list;
    }


    /**
     * 对 OrderPriority 进行排序操作
     *
     * @param order OrderPriority
     * @param <T>   当前操作要被排序的bean
     * @return Comparator
     */
    public static <T> Comparator<T> orderPriority(final Function<T, OrderPriority> order) {
        return Comparator.comparing(t -> {
            OrderPriority orderPriority = order.apply(t);
            if (orderPriority == null) {
                orderPriority = OrderPriority.getLowPriority();
            }
            return orderPriority.getPriority();
        }, Comparator.nullsLast(Comparator.reverseOrder()));
    }


}
