package com.github.thestyleofme.plugin.framework.integration.pf4j.classloader;

import java.nio.file.Path;

import org.pf4j.*;

/**
 * <p>
 * description
 * </p>
 *
 * @author isaac 2020/9/9 15:33
 * @since 1.0.0
 */
public class MyDevelopmentPluginLoader extends DevelopmentPluginLoader {

    public MyDevelopmentPluginLoader(PluginManager pluginManager) {
        super(pluginManager);
    }

    @Override
    protected PluginClassLoader createPluginClassLoader(Path pluginPath, PluginDescriptor pluginDescriptor) {
        return new PluginClassLoader(pluginManager,
                pluginDescriptor,
                getClass().getClassLoader(),
                ClassLoadingStrategy.APD);
    }
}
