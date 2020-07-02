package com.basic.example.plugin1.service;

import com.basic.example.main.plugin.Hello;
import org.pf4j.Extension;

/**
 * <p>
 * description
 * </p>
 *
 * @author isaac 2020/6/16 17:54
 * @since 1.0
 */
@Extension
public class HelloImp1 implements Hello {
    @Override
    public String getName() {
        return "my name is hello imp1 of plugin1";
    }
}
