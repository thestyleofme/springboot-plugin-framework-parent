package com.github.thestyleofme.plugin.framework.extension.mybatis;

import java.util.ArrayList;
import java.util.List;

import com.github.thestyleofme.plugin.framework.extension.AbstractExtension;
import com.github.thestyleofme.plugin.framework.factory.process.pipe.PluginPipeProcessorExtend;
import com.github.thestyleofme.plugin.framework.factory.process.pipe.classs.PluginClassGroupExtend;
import com.github.thestyleofme.plugin.framework.loader.PluginResourceLoader;
import org.springframework.context.ApplicationContext;

/**
 * <p>
 * spring boot mybatis 扩展
 * </p>
 *
 * @author isaac 2020/6/18 17:33
 * @since 1.0
 */
public class SpringBootMybatisExtension extends AbstractExtension {

    private static final String KEY = "SpringBootMybatisExtension";

    @Override
    public String key() {
        return KEY;
    }

    @Override
    public List<PluginResourceLoader> getPluginResourceLoader() {
        final List<PluginResourceLoader> pluginResourceLoaders = new ArrayList<>();
        pluginResourceLoaders.add(new PluginMybatisXmlLoader());
        return pluginResourceLoaders;
    }

    @Override
    public List<PluginClassGroupExtend> getPluginClassGroup(ApplicationContext mainApplicationContext) {
        final List<PluginClassGroupExtend> pluginClassGroups = new ArrayList<>();
        pluginClassGroups.add(new PluginMapperGroup());
        pluginClassGroups.add(new PluginEntityAliasesGroup());
        return pluginClassGroups;
    }

    @Override
    public List<PluginPipeProcessorExtend> getPluginPipeProcessor(ApplicationContext applicationContext) {
        final List<PluginPipeProcessorExtend> pluginPipeProcessorExtends = new ArrayList<>();
        pluginPipeProcessorExtends.add(new PluginMybatisMapperProcessor(applicationContext));
        pluginPipeProcessorExtends.add(new PluginMybatisXmlProcessor(applicationContext));
        pluginPipeProcessorExtends.add(new PluginMybatisEntityProcessor(applicationContext));
        return pluginPipeProcessorExtends;
    }


}
