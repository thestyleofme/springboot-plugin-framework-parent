package com.mybatisplus.plugin;

import java.util.HashSet;
import java.util.Set;

import com.github.codingdebugallday.plugin.framework.extension.mybatis.configuration.SpringBootMybatisConfig;
import com.github.codingdebugallday.plugin.framework.realize.BasePlugin;
import org.pf4j.PluginWrapper;

/**
 * <p>
 * description
 * </p>
 *
 * @author isaac 2020/6/22 15:56
 * @since 1.0
 */
public class MybatisPlusPlugin extends BasePlugin implements SpringBootMybatisConfig {

    private final Set<String> mybatisMapperXmlLocationsMatch = new HashSet<>();

    public MybatisPlusPlugin(PluginWrapper wrapper) {
        super(wrapper);
        mybatisMapperXmlLocationsMatch.add("classpath:mapper/*Mapper.xml");
    }

    @Override
    protected void startEvent() {
        // ignore
    }

    @Override
    protected void deleteEvent() {
        // ignore
    }

    @Override
    protected void stopEvent() {
        // ignore
    }

    @Override
    public Set<String> mybatisMapperXmlLocationsMatch() {
        return mybatisMapperXmlLocationsMatch;
    }
}

