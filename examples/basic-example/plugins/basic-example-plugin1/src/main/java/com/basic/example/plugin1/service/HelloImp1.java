package com.basic.example.plugin1.service;

import com.basic.example.main.plugin.Hello;

/**
 * <p>
 * description
 * </p>
 *
 * @author isaac 2020/6/16 17:54
 * @since 1.0
 */
public class HelloImp1 implements Hello {
    @Override
    public String getName() {
        return "my name is hello imp1 of plugin1";
    }
}
