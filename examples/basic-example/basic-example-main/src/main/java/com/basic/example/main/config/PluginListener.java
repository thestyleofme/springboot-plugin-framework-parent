package com.basic.example.main.config;

import com.github.thestyleofme.plugin.framework.integration.listener.PluginInitializerListener;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 插件监听者
 * </p>
 *
 * @author isaac 2020/6/16 17:32
 * @since 1.0
 */
@Component
public class PluginListener implements PluginInitializerListener {

    @Override
    public void before() {
        System.out.println("before");
    }

    @Override
    public void complete() {
        System.out.println("complete");
    }

    @Override
    public void failure(Throwable throwable) {
        System.out.println("failure:" + throwable.getMessage());
    }
}
