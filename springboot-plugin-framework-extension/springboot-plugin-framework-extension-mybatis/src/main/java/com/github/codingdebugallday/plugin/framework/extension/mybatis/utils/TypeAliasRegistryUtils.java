package com.github.codingdebugallday.plugin.framework.extension.mybatis.utils;

import java.lang.reflect.Field;
import java.util.Map;

import com.github.codingdebugallday.plugin.framework.exceptions.PluginException;
import org.apache.ibatis.type.TypeAliasRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * mybatis 别名工具类
 * </p>
 *
 * @author isaac 2020/6/18 16:47
 * @since 1.0
 */
public class TypeAliasRegistryUtils {

    private static final Logger LOG = LoggerFactory.getLogger(TypeAliasRegistryUtils.class);

    private TypeAliasRegistryUtils() {

    }

    /**
     * 通过反射获取别名注册器 TypeAliasRegistry 中存储别名的 typeAliases Map集合。
     *
     * @param typeAliasRegistry 别名注册器
     * @return typeAliases Map集合。
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Class<?>> getTypeAliases(TypeAliasRegistry typeAliasRegistry) {
        if (typeAliasRegistry == null) {
            throw new PluginException("TypeAliasRegistry can not is null");
        }
        try {
            Field field = typeAliasRegistry.getClass().getDeclaredField("typeAliases");
            // 设置对象的访问权限，保证对private的属性的访问
            field.setAccessible(true);
            Object fieldObject = field.get(typeAliasRegistry);
            if (fieldObject instanceof Map) {
                return (Map<String, Class<?>>) fieldObject;
            } else {
                LOG.warn("Not found TypeAliasRegistry typeAliases");
                throw new PluginException("Not found TypeAliasRegistry typeAliases");
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new PluginException("field reflect error", e);
        }
    }


}
