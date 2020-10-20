package com.github.thestyleofme.plugin.framework.factory;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.util.StringUtils;

/**
 * <p>
 * 插件信息共享容器
 * </p>
 *
 * @author isaac 2020/6/16 10:26
 * @since 1.0
 */
public class PluginInfoContainer {

    private PluginInfoContainer() {
        throw new IllegalStateException();
    }

    /**
     * 全局插件中定义的BaneName
     */
    private static final Map<String, Set<String>> SPRING_REGISTER_BEAN_NAMES = new ConcurrentHashMap<>();

    /**
     * 添加注册的bean名称
     *
     * @param pluginId 插件id
     * @param beanName 注册的bean名称
     */
    public static void addRegisterBeanName(String pluginId, String beanName) {
        if (!StringUtils.isEmpty(beanName)) {
            Set<String> beanNames = SPRING_REGISTER_BEAN_NAMES.computeIfAbsent(pluginId, k -> new HashSet<>());
            beanNames.add(beanName);
        }
    }

    /**
     * 移除注册的bean名称
     *
     * @param pluginId 插件id
     * @param beanName 注册的bean名称
     */
    public static void removeRegisterBeanName(String pluginId, String beanName) {
        Set<String> beanNames = SPRING_REGISTER_BEAN_NAMES.get(pluginId);
        if (beanNames != null) {
            beanNames.remove(beanName);
        }
    }

    /**
     * 是否存在bean名称
     *
     * @param pluginId 插件id
     * @param beanName 注册的bean名称
     * @return true 存在。false不存在
     */
    public static boolean existRegisterBeanName(String pluginId, String beanName) {
        Set<String> beanNames = SPRING_REGISTER_BEAN_NAMES.get(pluginId);
        if (beanNames != null) {
            return beanNames.contains(beanName);
        } else {
            return false;
        }
    }

    /**
     * 是否存在bean名称
     *
     * @param beanName 注册的bean名称
     * @return true 存在。false不存在
     */
    public static boolean existRegisterBeanName(String beanName) {
        for (Set<String> beanNames : SPRING_REGISTER_BEAN_NAMES.values()) {
            if (beanNames.contains(beanName)) {
                return true;
            }
        }
        return false;
    }
}
