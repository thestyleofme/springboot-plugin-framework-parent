package com.github.codingdebugallday.factory.process.pipe.bean;

import java.util.List;
import java.util.Objects;

import com.github.codingdebugallday.factory.PluginRegistryInfo;
import com.github.codingdebugallday.factory.process.pipe.PluginPipeProcessor;
import com.github.codingdebugallday.integration.application.PluginApplication;
import com.github.codingdebugallday.integration.user.PluginUser;
import com.github.codingdebugallday.realize.BasePlugin;
import com.github.codingdebugallday.realize.OneselfListener;
import com.github.codingdebugallday.utils.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

/**
 * <p>
 * 执行插件自监听器的停止事件的处理者
 * 必须在所有处理者中第一个执行 否则会导致所依赖的bean被卸载
 * </p>
 *
 * @author isaac 2020/6/16 14:05
 * @since 1.0
 */
public class OneselfListenerStopEventProcessor implements PluginPipeProcessor {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final PluginUser pluginUser;

    public OneselfListenerStopEventProcessor(ApplicationContext applicationContext) {
        Objects.requireNonNull(applicationContext);
        PluginApplication pluginApplication = applicationContext.getBean(PluginApplication.class);
        this.pluginUser = pluginApplication.getPluginUser();
    }


    @Override
    public void initialize() {
        // ignore
    }

    @Override
    public void register(PluginRegistryInfo pluginRegistryInfo) {
        // ignore
    }

    @Override
    public void unregister(PluginRegistryInfo pluginRegistryInfo) {
        BasePlugin basePlugin = pluginRegistryInfo.getBasePlugin();
        String pluginId = basePlugin.getWrapper().getPluginId();
        List<OneselfListener> oneselfListeners = pluginUser.getPluginBeans(pluginId, OneselfListener.class);
        oneselfListeners.stream()
                .sorted(CommonUtils.orderPriority(OneselfListener::order))
                .forEach(oneselfListener -> {
                    try {
                        oneselfListener.stopEvent(basePlugin);
                    } catch (Exception e) {
                        log.error("OneselfListener {} execute stopEvent exception. {}",
                                oneselfListener.getClass().getName(), e.getMessage(), e);
                    }
                });
    }
}
