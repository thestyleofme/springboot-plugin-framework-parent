package com.github.codingdebugallday.plugin.framework.integration.application;

import com.github.codingdebugallday.plugin.framework.exceptions.PluginException;
import com.github.codingdebugallday.plugin.framework.integration.listener.PluginInitializerListener;
import com.github.codingdebugallday.plugin.framework.integration.pf4j.Pf4jFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * <p>
 * 自动初始化的 PluginApplication。该PluginApplication 基于 Spring InitializingBean 自动初始化插件。
 * </p>
 *
 * @author isaac 2020/6/16 14:14
 * @since 1.0
 */
public class AutoPluginApplication extends DefaultPluginApplication
        implements PluginApplication, InitializingBean, ApplicationContextAware {

    private ApplicationContext applicationContext;
    private PluginInitializerListener pluginInitializerListener;

    public AutoPluginApplication() {
        super();
    }

    public AutoPluginApplication(Pf4jFactory pf4jFactory) {
        super(pf4jFactory);
    }

    /**
     * 设置插件初始化监听器
     *
     * @param pluginInitializerListener 插件监听器
     */
    public void setPluginInitializerListener(PluginInitializerListener pluginInitializerListener) {
        this.pluginInitializerListener = pluginInitializerListener;
    }


    @Override
    public void initialize(ApplicationContext applicationContext,
                           PluginInitializerListener listener) {
        // 此处不允许手动初始化！
        throw new PluginException("Cannot be initialized manually");
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * Spring boot bean属性被Set完后调用。会自动初始化插件
     */
    @Override
    public void afterPropertiesSet() {
        if (applicationContext == null) {
            throw new PluginException("Auto initialize failed. ApplicationContext Not injected.");
        }
        super.initialize(applicationContext, pluginInitializerListener);
    }
}
