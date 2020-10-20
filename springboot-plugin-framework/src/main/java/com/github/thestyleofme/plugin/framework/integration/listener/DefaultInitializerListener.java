package com.github.thestyleofme.plugin.framework.integration.listener;

import com.github.thestyleofme.plugin.framework.extension.ExtensionInitializer;
import org.springframework.context.ApplicationContext;

/**
 * <p>
 * 默认的初始化监听者。内置注册
 * </p>
 *
 * @author isaac 2020/6/16 15:29
 * @since 1.0
 */
public class DefaultInitializerListener implements PluginInitializerListener {

    public final ApplicationContext applicationContext;

    public DefaultInitializerListener(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }


    @Override
    public void before() {
        // 初始化扩展注册信息
        ExtensionInitializer.initialize(applicationContext);
    }

    @Override
    public void complete() {
        // ignore
    }

    @Override
    public void failure(Throwable throwable) {
        // ignore
    }
}
