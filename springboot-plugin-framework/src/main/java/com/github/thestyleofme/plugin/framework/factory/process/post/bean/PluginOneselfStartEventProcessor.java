package com.github.thestyleofme.plugin.framework.factory.process.post.bean;

import java.util.List;
import java.util.Objects;

import com.github.thestyleofme.plugin.framework.factory.PluginRegistryInfo;
import com.github.thestyleofme.plugin.framework.factory.process.pipe.bean.OneselfListenerStopEventProcessor;
import com.github.thestyleofme.plugin.framework.factory.process.post.PluginPostProcessor;
import com.github.thestyleofme.plugin.framework.integration.application.PluginApplication;
import com.github.thestyleofme.plugin.framework.integration.user.PluginUser;
import com.github.thestyleofme.plugin.framework.realize.BasePlugin;
import com.github.thestyleofme.plugin.framework.realize.OneselfListener;
import com.github.thestyleofme.plugin.framework.utils.AopUtils;
import com.github.thestyleofme.plugin.framework.utils.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

/**
 * <p>
 * 插件中 OneselfListener 监听者处理者。主要执行监听器的启动事件。
 * </p>
 *
 * @author isaac 2020/6/16 15:17
 * @see OneselfListenerStopEventProcessor 触发停止事件
 * @since 1.0
 */
public class PluginOneselfStartEventProcessor implements PluginPostProcessor {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final PluginUser pluginUser;

    public PluginOneselfStartEventProcessor(ApplicationContext applicationContext) {
        Objects.requireNonNull(applicationContext);
        PluginApplication pluginApplication = applicationContext.getBean(PluginApplication.class);
        this.pluginUser = pluginApplication.getPluginUser();
    }

    @Override
    public void initialize() {
        // ignore
    }

    @Override
    public void register(List<PluginRegistryInfo> pluginRegistryInfos) {
        for (PluginRegistryInfo pluginRegistryInfo : pluginRegistryInfos) {
            AopUtils.resolveAop(pluginRegistryInfo.getPluginWrapper());
            try {
                BasePlugin basePlugin = pluginRegistryInfo.getBasePlugin();
                String pluginId = basePlugin.getWrapper().getPluginId();

                List<OneselfListener> oneselfListeners = pluginUser.getPluginBeans(pluginId, OneselfListener.class);
                oneselfListeners.stream()
                        .sorted(CommonUtils.orderPriority(OneselfListener::order))
                        .forEach(oneselfListener -> {
                            try {
                                oneselfListener.startEvent(basePlugin);
                            } catch (Exception e) {
                                log.error("OneselfListener {} execute startEvent exception. {}",
                                        oneselfListener.getClass().getName(), e.getMessage(), e);
                            }
                        });
            } finally {
                AopUtils.recoverAop();
            }
        }
    }

    @Override
    public void unregister(List<PluginRegistryInfo> pluginRegistryInfos) {
        // 此处不卸载调用
    }

}
