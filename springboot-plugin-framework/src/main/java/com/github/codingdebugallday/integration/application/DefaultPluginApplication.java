package com.github.codingdebugallday.integration.application;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

import com.github.codingdebugallday.exceptions.PluginException;
import com.github.codingdebugallday.integration.IntegrationConfiguration;
import com.github.codingdebugallday.integration.listener.PluginInitializerListener;
import com.github.codingdebugallday.integration.operator.DefaultPluginOperator;
import com.github.codingdebugallday.integration.operator.PluginOperator;
import com.github.codingdebugallday.integration.pf4j.DefaultPf4jFactory;
import com.github.codingdebugallday.integration.pf4j.Pf4jFactory;
import com.github.codingdebugallday.integration.user.DefaultPluginUser;
import com.github.codingdebugallday.integration.user.PluginUser;
import org.pf4j.PluginManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

/**
 * <p>
 * 默认的插件 PluginApplication
 * </p>
 *
 * @author isaac 2020/6/16 14:15
 * @since 1.0
 */
public class DefaultPluginApplication extends AbstractPluginApplication {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    protected Pf4jFactory integrationFactory;

    private PluginUser pluginUser;
    private PluginOperator pluginOperator;

    private final AtomicBoolean beInitialized = new AtomicBoolean(false);

    public DefaultPluginApplication() {
    }

    public DefaultPluginApplication(Pf4jFactory integrationFactory) {
        this.integrationFactory = integrationFactory;
    }


    @Override
    public synchronized void initialize(ApplicationContext applicationContext,
                                        PluginInitializerListener listener) {
        Objects.requireNonNull(applicationContext, "ApplicationContext can't be null");
        if (beInitialized.get()) {
            throw new PluginException("Plugin has been initialized");
        }
        IntegrationConfiguration configuration = getConfiguration(applicationContext);
        if (integrationFactory == null) {
            integrationFactory = new DefaultPf4jFactory(configuration);
        }
        PluginManager pluginManager = integrationFactory.getPluginManager();
        pluginUser = createPluginUser(applicationContext, pluginManager);
        pluginOperator = createPluginOperator(applicationContext, pluginManager, configuration);
        try {
            pluginOperator.initPlugins(listener);
            beInitialized.set(true);
        } catch (Exception e) {
            log.error("initPlugins error,", e);
        }
    }

    /**
     * 创建插件使用者。子类可扩展
     *
     * @param applicationContext Spring ApplicationContext
     * @param pluginManager      插件管理器
     * @return PluginUser
     */
    protected PluginUser createPluginUser(ApplicationContext applicationContext,
                                          PluginManager pluginManager) {
        return new DefaultPluginUser(applicationContext, pluginManager);
    }

    /**
     * 创建插件操作者。子类可扩展
     *
     * @param applicationContext Spring ApplicationContext
     * @param pluginManager      插件管理器
     * @param configuration      当前集成的配置
     * @return PluginOperator
     */
    protected PluginOperator createPluginOperator(ApplicationContext applicationContext,
                                                  PluginManager pluginManager,
                                                  IntegrationConfiguration configuration) {
        return new DefaultPluginOperator(
                applicationContext,
                configuration,
                pluginManager,
                this.listenerFactory
        );
    }


    @Override
    public PluginOperator getPluginOperator() {
        assertInjected();
        return pluginOperator;
    }

    @Override
    public PluginUser getPluginUser() {
        assertInjected();
        return pluginUser;
    }

    /**
     * 检查注入
     */
    private void assertInjected() {
        if (this.pluginUser == null) {
            throw new PluginException("PluginUser is null, Please check whether the DefaultPluginApplication is injected");
        }
        if (this.pluginOperator == null) {
            throw new PluginException("PluginOperator is null, Please check whether the DefaultPluginApplication is injected");
        }
    }

}
