package com.github.thestyleofme.plugin.framework.annotation;

import java.lang.annotation.*;

/**
 * <p>
 * 调用者的注解 配合@Supplier注解使用 两者结合实现插件中的方法调用
 * </p>
 *
 * @author isaac 2020/6/16 13:42
 * @since 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Caller {

    /**
     * 调用者的全局唯一key 也就是Supplier 中定义的key.
     *
     * @return String
     */
    String value();

    /**
     * 调用者方法注解 配合 @Supper.Method 使用 如果不定义 则以方法名称为准
     */
    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface Method {

        /**
         * 方法名
         *
         * @return String
         */
        String value();
    }

}
