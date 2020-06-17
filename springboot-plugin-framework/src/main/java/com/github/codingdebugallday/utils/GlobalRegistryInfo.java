package com.github.codingdebugallday.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 * 全局注册信息
 * </p>
 *
 * @author isaac 2020/6/16 13:59
 * @since 1.0
 */
public final class GlobalRegistryInfo {

    private GlobalRegistryInfo() {
    }

    /**
     * 全局插件安装次数
     */
    private static final Map<String, PluginOperatorInfo> PLUGIN_OPERATOR_INFO_MAP = new ConcurrentHashMap<>();

    /**
     * 全局扩展信息
     */
    private static final Map<String, Object> EXTENSION_MAP = new ConcurrentHashMap<>();


    /**
     * 添加操作插件信息
     *
     * @param pluginId     插件id
     * @param operatorType 操作类型
     * @param isLock       是否加锁
     */
    public static void addOperatorPluginInfo(String pluginId,
                                             PluginOperatorInfo.OperatorType operatorType,
                                             boolean isLock) {
        PluginOperatorInfo operatorPluginInfo = PLUGIN_OPERATOR_INFO_MAP.computeIfAbsent(pluginId,
                k -> new PluginOperatorInfo());
        operatorPluginInfo.setOperatorType(operatorType);
        operatorPluginInfo.setLock(isLock);
    }


    /**
     * 设置操作插件的信息
     *
     * @param pluginId 插件id
     * @param isLock   是否加锁
     */
    public static void setOperatorPluginInfo(String pluginId, boolean isLock) {
        PluginOperatorInfo operatorPluginInfo = PLUGIN_OPERATOR_INFO_MAP.get(pluginId);
        if (operatorPluginInfo != null) {
            operatorPluginInfo.setLock(isLock);
        }
    }


    /**
     * 获取插件安装次数
     *
     * @param pluginId 插件id
     * @return 操作插件类型
     */
    public static PluginOperatorInfo getPluginInstallNum(String pluginId) {
        return PLUGIN_OPERATOR_INFO_MAP.get(pluginId);
    }


    /**
     * 添加全局扩展数据
     *
     * @param key   扩展的key
     * @param value 扩展值
     */
    public static void addExtension(String key, Object value) {
        EXTENSION_MAP.put(key, value);
    }

    /**
     * 删除全局扩展数据
     *
     * @param key 扩展的key
     */
    public static void removeExtension(String key) {
        EXTENSION_MAP.remove(key);
    }

    /**
     * 获取全局扩展值
     *
     * @param key 全局扩展的key
     * @param <T> 返回值泛型
     * @return 扩展值
     */
    @SuppressWarnings("unchecked")
    public static <T> T getExtension(String key) {
        Object o = EXTENSION_MAP.get(key);
        if (o == null) {
            return null;
        } else {
            return (T) o;
        }
    }


}
