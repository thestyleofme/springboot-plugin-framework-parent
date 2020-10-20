package com.mybatis.plugin1;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.github.thestyleofme.plugin.framework.extension.mybatis.configuration.SpringBootMybatisConfig;
import com.github.thestyleofme.plugin.framework.extension.resources.StaticResourceConfig;
import com.github.thestyleofme.plugin.framework.realize.BasePlugin;
import com.mybatis.plugin1.entity.Plugin1;
import org.pf4j.PluginWrapper;

/**
 * <p>
 * description
 * </p>
 *
 * @author isaac 2020/6/19 14:06
 * @since 1.0
 */
public class ExamplePlugin1 extends BasePlugin
        implements SpringBootMybatisConfig, StaticResourceConfig {

    private final Set<String> mybatisMapperXmlLocationsMatch = new HashSet<>();
    private final Set<String> typeAliasesPackage = new HashSet<>();
    private final Map<String, Class<?>> typeAliasesClass = new HashMap<>();
    private final Set<String> locations = new HashSet<>();


    public ExamplePlugin1(PluginWrapper wrapper) {
        super(wrapper);
        mybatisMapperXmlLocationsMatch.add("classpath:mapper/*Mapper.xml");
        typeAliasesPackage.add("com.mybatis.plugin1.entity");

        typeAliasesClass.put("plugin1", Plugin1.class);
        locations.add("classpath:static");
        locations.add("file:E:\\youdaofiles");
    }


    @Override
    public Set<String> mybatisMapperXmlLocationsMatch() {
        return mybatisMapperXmlLocationsMatch;
    }

    @Override
    public Map<String, Class<?>> aliasMapping() {
        return typeAliasesClass;
    }

    @Override
    public Set<String> typeAliasesPackage() {
        return typeAliasesPackage;
    }

    @Override
    public Set<String> locations() {
        return locations;
    }

}
