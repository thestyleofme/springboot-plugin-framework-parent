package com.github.thestyleofme.plugin.framework.extension.mybatis.annotation;

import java.lang.annotation.*;

/**
 * <p>
 * Mybatis PluginMapper 接口的注解
 * </p>
 *
 * @author isaac 2020/6/18 16:00
 * @since 1.0
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
public @interface PluginMapper {
}
