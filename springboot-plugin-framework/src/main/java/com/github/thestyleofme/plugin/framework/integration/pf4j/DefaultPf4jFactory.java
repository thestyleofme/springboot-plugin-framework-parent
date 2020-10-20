package com.github.thestyleofme.plugin.framework.integration.pf4j;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import com.github.thestyleofme.plugin.framework.exceptions.PluginException;
import com.github.thestyleofme.plugin.framework.integration.IntegrationConfiguration;
import com.github.thestyleofme.plugin.framework.integration.pf4j.classloader.MyDefaultPluginLoader;
import com.github.thestyleofme.plugin.framework.integration.pf4j.classloader.MyDevelopmentPluginLoader;
import com.github.thestyleofme.plugin.framework.integration.pf4j.classloader.MyJarPluginLoader;
import org.pf4j.*;

/**
 * <p>
 * 默认的插件集成工厂
 * </p>
 *
 * @author isaac 2020/6/16 15:55
 * @since 1.0
 */
public class DefaultPf4jFactory implements Pf4jFactory {

    private final IntegrationConfiguration configuration;

    public DefaultPf4jFactory(IntegrationConfiguration configuration) {
        this.configuration = configuration;
    }


    @Override
    public PluginManager getPluginManager() {
        if (configuration == null) {
            throw new NullPointerException("IntegrationConfiguration is null");
        }
        RuntimeMode environment = configuration.environment();
        if (environment == null) {
            throw new PluginException("Configuration RuntimeMode is null" + configuration.environment());
        }
        if (RuntimeMode.DEVELOPMENT == environment) {
            // 开发环境下的插件管理者
            Path path = Paths.get(getDevPluginDir(configuration));
            return new DefaultPluginManager(path) {
                @Override
                public RuntimeMode getRuntimeMode() {
                    System.setProperty("pf4j.mode", RuntimeMode.DEVELOPMENT.toString());
                    return RuntimeMode.DEVELOPMENT;
                }

                @Override
                protected PluginDescriptorFinder createPluginDescriptorFinder() {
                    return new CompoundPluginDescriptorFinder()
                            .add(new ResolvePropertiesPluginDescriptorFinder())
                            .add(new ManifestPluginDescriptorFinder());
                }

                @Override
                protected PluginLoader createPluginLoader() {
                    return new CompoundPluginLoader()
                            .add(new MyDevelopmentPluginLoader(this), this::isDevelopment)
                            .add(new MyJarPluginLoader(this), this::isNotDevelopment)
                            .add(new MyDefaultPluginLoader(this), this::isNotDevelopment);
                }
            };
        } else if (RuntimeMode.DEPLOYMENT == environment) {
            // 运行环境下的插件管理者
            Path path = Paths.get(getProdPluginDir(configuration));
            return new DefaultPluginManager(path) {
                @Override
                protected PluginRepository createPluginRepository() {
                    return new CompoundPluginRepository()
                            .add(new JarPluginRepository(getPluginsRoot()));
                }

                @Override
                protected PluginLoader createPluginLoader() {
                    return new CompoundPluginLoader()
                            .add(new MyDevelopmentPluginLoader(this), this::isDevelopment)
                            .add(new MyJarPluginLoader(this), this::isNotDevelopment)
                            .add(new MyDefaultPluginLoader(this), this::isNotDevelopment);
                }
            };
        } else {
            throw new PluginException("Not found run environment " + configuration.environment());
        }
    }


    private String getDevPluginDir(IntegrationConfiguration configuration) {
        String pluginDir = configuration.pluginPath();
        if (Objects.equals("", pluginDir)) {
            pluginDir = "./plugins/";
        }
        return pluginDir;
    }


    private String getProdPluginDir(IntegrationConfiguration configuration) {
        String pluginDir = configuration.pluginPath();
        if (Objects.equals("", pluginDir)) {
            pluginDir = "plugins";
        }
        return pluginDir;
    }

}
