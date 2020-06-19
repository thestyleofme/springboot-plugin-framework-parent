package com.mybatis.plugin2;

import java.util.HashSet;
import java.util.Set;

import com.github.codingdebugallday.extension.mybatis.configuration.SpringBootMybatisConfig;
import com.github.codingdebugallday.extension.resources.StaticResourceConfig;
import com.github.codingdebugallday.realize.BasePlugin;
import org.pf4j.PluginWrapper;

/**
 * <p>
 * description
 * </p>
 *
 * @author isaac 2020/6/19 14:15
 * @since 1.0
 */
public class ExamplePlugin2 extends BasePlugin
        implements SpringBootMybatisConfig, StaticResourceConfig {

    private final Set<String> mybatisMapperXmlLocationsMatch = new HashSet<>();
    private final Set<String> locations = new HashSet<>();

    public ExamplePlugin2(PluginWrapper wrapper) {
        super(wrapper);
        mybatisMapperXmlLocationsMatch.add("classpath:mapper/*Mapper.xml");
        locations.add("classpath:/static");
    }

    @Override
    protected void startEvent() {
        //
    }

    @Override
    protected void deleteEvent() {
        //
    }

    @Override
    protected void stopEvent() {
        //
    }

    @Override
    public Set<String> mybatisMapperXmlLocationsMatch() {
        return mybatisMapperXmlLocationsMatch;
    }

    @Override
    public Set<String> locations() {
        return locations;
    }
}
