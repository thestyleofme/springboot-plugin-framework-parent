package com.basic.example.main.config;

import com.github.thestyleofme.plugin.framework.integration.application.PluginApplication;
import com.github.thestyleofme.plugin.framework.integration.listener.PluginListener;
import com.github.thestyleofme.plugin.framework.integration.user.PluginUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * 插件监听者
 * </p>
 *
 * @author isaac 2020/6/16 17:28
 * @since 1.0
 */
public class ExamplePluginListener implements PluginListener {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final PluginUser pluginUser;

    public ExamplePluginListener(PluginApplication pluginApplication) {
        this.pluginUser = pluginApplication.getPluginUser();
    }

    @Override
    public void register(String pluginId) {
        logger.info("Listener: registry pluginId {}", pluginId);
    }

    @Override
    public void unregister(String pluginId) {
        logger.info("Listener: unRegistry pluginId {}", pluginId);
    }

    @Override
    public void failure(String pluginId, Throwable throwable) {
        // ignore
    }
}
