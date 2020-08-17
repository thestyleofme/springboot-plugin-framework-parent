package com.github.codingdebugallday.plugin.framework.realize;

import com.github.codingdebugallday.plugin.framework.loader.PluginResourceLoadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * 扩展的BasePlugin信息
 * </p>
 *
 * @author isaac 2020/6/16 10:59
 * @since 1.0
 */
public final class BasePluginExtend {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final BasePlugin basePlugin;
    private final PluginResourceLoadFactory pluginResourceLoadFactory;
    private Long startTimestamp;
    private Long stopTimestamp;

    BasePluginExtend(BasePlugin basePlugin) {
        this.basePlugin = basePlugin;
        this.pluginResourceLoadFactory = new PluginResourceLoadFactory();
    }


    public long getStartTimestamp() {
        return startTimestamp;
    }

    public Long getStopTimestamp() {
        return stopTimestamp;
    }

    public PluginResourceLoadFactory getPluginResourceLoadFactory() {
        return pluginResourceLoadFactory;
    }

    void startEvent() {
        try {
            pluginResourceLoadFactory.load(basePlugin);
        } catch (Exception e) {
            log.error("load error", e);
        } finally {
            startTimestamp = System.currentTimeMillis();
        }
    }

    void deleteEvent() {
        try {
            pluginResourceLoadFactory.unload(basePlugin);
        } catch (Exception e) {
            log.error("unload error", e);
        }
    }

    void stopEvent() {
        try {
            pluginResourceLoadFactory.unload(basePlugin);
        } catch (Exception e) {
            log.error("unload error", e);
        } finally {
            stopTimestamp = System.currentTimeMillis();
        }
    }

}
