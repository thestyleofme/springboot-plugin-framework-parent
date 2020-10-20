package com.github.thestyleofme.plugin.framework.loader;

import com.github.thestyleofme.plugin.framework.realize.BasePlugin;
import com.github.thestyleofme.plugin.framework.utils.OrderPriority;

/**
 * <p>
 * 插件资源加载者统一定义的接口
 * </p>
 *
 * @author isaac 2020/6/16 11:08
 * @since 1.0
 */
public interface PluginResourceLoader {

    /**
     * 加载者的key
     *
     * @return String
     */
    String key();


    /**
     * 加载资源
     *
     * @param basePlugin 插件对象
     * @return 资源包装对象
     */
    ResourceWrapper load(BasePlugin basePlugin);

    /**
     * 卸载时的操作
     *
     * @param basePlugin      插件对象
     * @param resourceWrapper 资源包装者
     */
    void unload(BasePlugin basePlugin, ResourceWrapper resourceWrapper);


    /**
     * 执行顺序
     *
     * @return OrderPriority
     */
    OrderPriority order();
}
