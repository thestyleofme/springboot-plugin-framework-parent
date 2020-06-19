package com.github.codingdebugallday.extension.resources;

import java.util.List;

import com.github.codingdebugallday.extension.resources.resolver.PluginResourceResolver;
import com.github.codingdebugallday.factory.PluginRegistryInfo;
import com.github.codingdebugallday.factory.process.post.PluginPostProcessorExtend;
import com.github.codingdebugallday.utils.OrderPriority;

/**
 * <p>
 * 插件资源处理器
 * </p>
 *
 * @author isaac 2020/6/19 10:34
 * @since 1.0
 */
public class PluginResourceResolverProcess implements PluginPostProcessorExtend {

    private static final String KEY = "PluginResourceResolverProcess";


    PluginResourceResolverProcess() {
    }

    @Override
    public String key() {
        return KEY;
    }

    @Override
    public OrderPriority order() {
        return OrderPriority.getMiddlePriority();
    }

    @Override
    public void initialize() {
        // ignore
    }

    @Override
    public synchronized void register(List<PluginRegistryInfo> pluginRegistryInfos) {
        for (PluginRegistryInfo pluginRegistryInfo : pluginRegistryInfos) {
            PluginResourceResolver.parse(pluginRegistryInfo.getBasePlugin());
        }
    }

    @Override
    public void unregister(List<PluginRegistryInfo> pluginRegistryInfos) {
        for (PluginRegistryInfo pluginRegistryInfo : pluginRegistryInfos) {
            PluginResourceResolver.remove(pluginRegistryInfo.getPluginWrapper().getPluginId());
        }
    }
}
