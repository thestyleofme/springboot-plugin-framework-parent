package com.basic.example.plugin2.service;

import com.basic.example.main.plugin.ConsoleName;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 插件实现
 * </p>
 *
 * @author isaac 2020/6/16 19:35
 * @since 1.0
 */
@Component("plugin2ConsoleNameImpl")
public class ConsoleNameImpl implements ConsoleName {

    @Override
    public String name() {
        return "My name is Plugin2";
    }
}
