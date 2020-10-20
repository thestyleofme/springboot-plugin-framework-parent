package com.github.thestyleofme.plugin.framework.factory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.thestyleofme.plugin.framework.enums.BuildTypeEnum;
import com.github.thestyleofme.plugin.framework.exceptions.PluginException;
import com.github.thestyleofme.plugin.framework.factory.process.pipe.PluginPipeProcessor;
import com.github.thestyleofme.plugin.framework.factory.process.pipe.PluginPipeProcessorFactory;
import com.github.thestyleofme.plugin.framework.factory.process.post.PluginPostProcessor;
import com.github.thestyleofme.plugin.framework.factory.process.post.PluginPostProcessorFactory;
import com.github.thestyleofme.plugin.framework.integration.listener.PluginListener;
import com.github.thestyleofme.plugin.framework.integration.listener.PluginListenerFactory;
import com.github.thestyleofme.plugin.framework.utils.AopUtils;
import org.pf4j.PluginWrapper;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

/**
 * <p>
 * 默认的插件处理者
 * </p>
 *
 * @author isaac 2020/6/16 15:23
 * @since 1.0
 */
public class DefaultPluginFactory implements PluginFactory {

    /**
     * 注册的插件集合
     */
    private final Map<String, PluginRegistryInfo> registerPluginInfoMap = new HashMap<>();
    private final GenericApplicationContext applicationContext;
    private final PluginPipeProcessor pluginPipeProcessor;
    private final PluginPostProcessor pluginPostProcessor;
    private final PluginListenerFactory pluginListenerFactory;

    /**
     * 0表示build、1 表示注册、2表示卸载
     */
    private Integer buildType = BuildTypeEnum.BUILD.ordinal();
    private final List<PluginRegistryInfo> buildContainer = new ArrayList<>();

    public DefaultPluginFactory(ApplicationContext applicationContext) {
        this(applicationContext, null);
    }

    public DefaultPluginFactory(ApplicationContext applicationContext,
                                PluginListenerFactory pluginListenerFactory) {
        this.pluginPipeProcessor = new PluginPipeProcessorFactory(applicationContext);
        this.pluginPostProcessor = new PluginPostProcessorFactory(applicationContext);
        this.applicationContext = (GenericApplicationContext) applicationContext;
        if (pluginListenerFactory == null) {
            this.pluginListenerFactory = new PluginListenerFactory();
        } else {
            this.pluginListenerFactory = pluginListenerFactory;
        }
        AopUtils.registered(applicationContext);
    }

    @Override
    public void initialize() {
        pluginPipeProcessor.initialize();
        pluginPostProcessor.initialize();
    }

    @Override
    public synchronized PluginFactory register(PluginWrapper pluginWrapper) {
        if (pluginWrapper == null) {
            throw new IllegalArgumentException("Parameter:pluginWrapper cannot be null");
        }
        if (registerPluginInfoMap.containsKey(pluginWrapper.getPluginId())) {
            throw new PluginException("The plugin '"
                    + pluginWrapper.getPluginId() + "' already exists, Can't register");
        }
        if (!buildContainer.isEmpty() && buildType == BuildTypeEnum.UNREGISTER.ordinal()) {
            throw new PluginException("Unable to Registry operate. Because there's no build");
        }
        PluginRegistryInfo registerPluginInfo = new PluginRegistryInfo(pluginWrapper);
        AopUtils.resolveAop(pluginWrapper);
        try {
            pluginPipeProcessor.register(registerPluginInfo);
            registerPluginInfoMap.put(pluginWrapper.getPluginId(), registerPluginInfo);
            buildContainer.add(registerPluginInfo);
            return this;
        } catch (Exception e) {
            pluginListenerFactory.failure(pluginWrapper.getPluginId(), e);
            throw e;
        } finally {
            buildType = 1;
            AopUtils.recoverAop();
        }
    }

    @Override
    public synchronized PluginFactory unregister(String pluginId) {
        PluginRegistryInfo registerPluginInfo = registerPluginInfoMap.get(pluginId);
        if (registerPluginInfo == null) {
            throw new PluginException("Not found plugin '" + pluginId + "' registered");
        }
        if (!buildContainer.isEmpty() && buildType == 1) {
            throw new PluginException("Unable to unregister operate. Because there's no build");
        }
        try {
            pluginPipeProcessor.unregister(registerPluginInfo);
            buildContainer.add(registerPluginInfo);
            return this;
        } catch (Exception e) {
            pluginListenerFactory.failure(pluginId, e);
            throw e;
        } finally {
            registerPluginInfoMap.remove(pluginId);
            buildType = 2;
        }
    }

    @Override
    public synchronized void build() {
        if (buildContainer.isEmpty()) {
            throw new PluginException("No Found registered or unregistered plugin. Unable to build");
        }
        // 构建注册的Class插件监听者
        pluginListenerFactory.buildListenerClass(applicationContext);
        try {
            if (buildType == 1) {
                registryBuild();
            } else {
                unregisterBuild();
            }
        } finally {
            buildContainer.clear();
            if (buildType == 1) {
                AopUtils.recoverAop();
            }
            buildType = 0;
        }
    }

    /**
     * 注册build
     */
    private void registryBuild() {
        pluginPostProcessor.register(buildContainer);
        for (PluginRegistryInfo pluginRegistryInfo : buildContainer) {
            pluginListenerFactory.register(pluginRegistryInfo.getPluginWrapper().getPluginId());
        }
    }

    /**
     * 卸载build
     */
    private void unregisterBuild() {
        pluginPostProcessor.unregister(buildContainer);
        for (PluginRegistryInfo pluginRegistryInfo : buildContainer) {
            pluginListenerFactory.unregister(pluginRegistryInfo.getPluginWrapper().getPluginId());
        }
    }


    @Override
    public void addListener(PluginListener pluginListener) {
        pluginListenerFactory.addPluginListener(pluginListener);
    }

    @Override
    public <T extends PluginListener> void addListener(Class<T> pluginListenerClass) {
        pluginListenerFactory.addPluginListener(pluginListenerClass);
    }

    @Override
    public void addListener(List<PluginListener> pluginListeners) {
        if (pluginListeners != null) {
            for (PluginListener pluginListener : pluginListeners) {
                pluginListenerFactory.addPluginListener(pluginListener);
            }
        }
    }

}
