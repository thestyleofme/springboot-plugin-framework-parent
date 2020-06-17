package com.basic.example.plugin1.service;

import org.springframework.stereotype.Component;

/**
 * <p>
 * Service2
 * </p>
 *
 * @author isaac 2020/6/16 17:55
 * @since 1.0
 */
@Component("plugin2Service2")
public class Service2 {

    public String getName() {
        return Service2.class.getName();
    }

}
