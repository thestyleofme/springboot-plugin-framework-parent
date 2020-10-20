package com.github.thestyleofme.plugin.framework.factory.process.pipe.classs;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.github.thestyleofme.plugin.framework.exceptions.PluginException;
import com.github.thestyleofme.plugin.framework.extension.ExtensionInitializer;
import com.github.thestyleofme.plugin.framework.factory.PluginRegistryInfo;
import com.github.thestyleofme.plugin.framework.factory.process.pipe.PluginPipeProcessor;
import com.github.thestyleofme.plugin.framework.factory.process.pipe.classs.group.*;
import com.github.thestyleofme.plugin.framework.loader.PluginResourceLoadFactory;
import com.github.thestyleofme.plugin.framework.loader.ResourceWrapper;
import com.github.thestyleofme.plugin.framework.loader.load.PluginClassLoader;
import com.github.thestyleofme.plugin.framework.realize.BasePlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 插件类加载处理者
 * </p>
 *
 * @author isaac 2020/6/16 11:55
 * @since 1.0
 */
public class PluginClassProcess implements PluginPipeProcessor {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * 其他类
     */
    public static final String OTHER = "other";

    private final List<PluginClassGroup> pluginClassGroups = new ArrayList<>();

    @Override
    public void initialize() {
        pluginClassGroups.add(new ComponentGroup());
        pluginClassGroups.add(new ControllerGroup());
        pluginClassGroups.add(new RepositoryGroup());
        pluginClassGroups.add(new ConfigurationGroup());
        pluginClassGroups.add(new ConfigDefinitionGroup());
        pluginClassGroups.add(new ConfigBeanGroup());
        pluginClassGroups.add(new SupplierGroup());
        pluginClassGroups.add(new CallerGroup());
        pluginClassGroups.add(new OneselfListenerGroup());
        // 添加扩展
        pluginClassGroups.addAll(ExtensionInitializer.getClassGroupExtends());
    }

    @Override
    public void register(PluginRegistryInfo pluginRegistryInfo) {
        BasePlugin basePlugin = pluginRegistryInfo.getBasePlugin();
        PluginResourceLoadFactory pluginResourceLoadFactory = basePlugin.getBasePluginExtend()
                .getPluginResourceLoadFactory();
        ResourceWrapper resourceWrapper = pluginResourceLoadFactory.getPluginResources(PluginClassLoader.DEFAULT_KEY);
        if (resourceWrapper == null) {
            return;
        }
        List<Resource> pluginResources = resourceWrapper.getResources();
        if (pluginResources == null) {
            return;
        }
        for (PluginClassGroup pluginClassGroup : pluginClassGroups) {
            try {
                pluginClassGroup.initialize(basePlugin);
            } catch (Exception e) {
                log.error("PluginClassGroup {} initialize exception. {}", pluginClassGroup.getClass(),
                        e.getMessage(), e);
            }
        }
        handle(resourceWrapper, basePlugin, pluginRegistryInfo);
    }

    private void handle(ResourceWrapper resourceWrapper,
                        BasePlugin basePlugin,
                        PluginRegistryInfo pluginRegistryInfo) {
        Set<String> classPackageNames = resourceWrapper.getClassPackageNames();
        for (String classPackageName : classPackageNames) {
            Class<?> aClass;
            try {
                aClass = Class.forName(classPackageName, false,
                        basePlugin.getWrapper().getPluginClassLoader());
            } catch (ClassNotFoundException e) {
                throw new PluginException(e);
            }
            boolean findGroup = false;
            for (PluginClassGroup pluginClassGroup : pluginClassGroups) {
                if (pluginClassGroup == null || StringUtils.isEmpty(pluginClassGroup.groupId())) {
                    continue;
                }
                if (pluginClassGroup.filter(aClass)) {
                    pluginRegistryInfo.addGroupClasses(pluginClassGroup.groupId(), aClass);
                    findGroup = true;
                }
            }
            if (!findGroup) {
                pluginRegistryInfo.addGroupClasses(OTHER, aClass);
            }
            pluginRegistryInfo.addClasses(aClass);
        }
    }

    @Override
    public void unregister(PluginRegistryInfo registerPluginInfo) {
        registerPluginInfo.cleanClasses();
    }

}
