package com.basic.example.plugin1.service;

import com.basic.example.main.config.PluginConfiguration;
import com.basic.example.main.plugin.ConsoleName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 主程序定义接口的实现类: ConsoleName
 * </p>
 *
 * @author isaac 2020/6/16 17:54
 * @since 1.0
 */
@Component
public class ConsoleNameImpl implements ConsoleName {

    @Autowired
    private PluginConfiguration pluginConfiguration;

    @Override
    public String name() {
        return "My name is Plugin1" + "->pluginArgConfiguration :" + pluginConfiguration.toString();
    }
}
