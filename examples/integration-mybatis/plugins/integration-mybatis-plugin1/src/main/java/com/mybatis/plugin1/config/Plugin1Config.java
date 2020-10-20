package com.mybatis.plugin1.config;

import com.github.thestyleofme.plugin.framework.annotation.ConfigDefinition;

/**
 * <p>
 * description
 * </p>
 *
 * @author isaac 2020/6/19 14:01
 * @since 1.0
 */
@ConfigDefinition("plugin1.yml")
public class Plugin1Config {

    private String name;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Plugin2Config{" +
                "name='" + name + '\'' +
                '}';
    }
}
