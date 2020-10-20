package com.github.thestyleofme.plugin.framework.integration.pf4j.classloader;

import java.nio.file.Path;

import org.pf4j.*;

/**
 * <p>
 * description
 * </p>
 *
 * @author isaac 2020/9/9 15:41
 * @since 1.0.0
 */
public class MyJarPluginLoader extends JarPluginLoader {

    public MyJarPluginLoader(PluginManager pluginManager) {
        super(pluginManager);
    }

    @Override
    public ClassLoader loadPlugin(Path pluginPath, PluginDescriptor pluginDescriptor) {
        PluginClassLoader pluginClassLoader = new PluginClassLoader(pluginManager,
                pluginDescriptor,
                getClass().getClassLoader(),
                ClassLoadingStrategy.APD);
        pluginClassLoader.addFile(pluginPath.toFile());
        return pluginClassLoader;
    }
}
