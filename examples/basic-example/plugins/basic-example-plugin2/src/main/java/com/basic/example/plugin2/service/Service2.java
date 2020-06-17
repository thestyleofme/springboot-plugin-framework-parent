package com.basic.example.plugin2.service;

import org.springframework.stereotype.Component;

/**
 * <p>
 * Service2
 * </p>
 *
 * @author isaac 2020/6/16 19:36
 * @since 1.0
 */
@Component
public class Service2 {

    public String getName() {
        return Service2.class.getName();
    }

}
