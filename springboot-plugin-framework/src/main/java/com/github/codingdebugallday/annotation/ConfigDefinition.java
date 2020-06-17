package com.github.codingdebugallday.annotation;

import java.lang.annotation.*;

/**
 * <p>
 * 插件配置文件对应的bean定义注解
 * </p>
 *
 * @author isaac 2020/6/16 13:43
 * @since 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ConfigDefinition {


    /**
     * 插件中的配置文件的名称
     *
     * @return String
     */
    String value();

    /**
     * 自定义 bean 名称
     *
     * @return String
     */
    String name() default "";

}
