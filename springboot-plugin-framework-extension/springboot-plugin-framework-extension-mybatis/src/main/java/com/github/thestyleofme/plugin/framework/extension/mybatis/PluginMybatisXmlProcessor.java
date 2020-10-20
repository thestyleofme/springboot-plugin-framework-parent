package com.github.thestyleofme.plugin.framework.extension.mybatis;

import java.util.List;

import com.github.thestyleofme.plugin.framework.extension.mybatis.utils.MybatisXmlProcess;
import com.github.thestyleofme.plugin.framework.factory.PluginRegistryInfo;
import com.github.thestyleofme.plugin.framework.factory.process.pipe.PluginPipeProcessorExtend;
import com.github.thestyleofme.plugin.framework.loader.PluginResourceLoadFactory;
import com.github.thestyleofme.plugin.framework.loader.ResourceWrapper;
import com.github.thestyleofme.plugin.framework.realize.BasePlugin;
import com.github.thestyleofme.plugin.framework.utils.OrderPriority;
import org.apache.ibatis.session.SqlSessionFactory;
import org.pf4j.PluginWrapper;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;

/**
 * <p>
 * mybatis xml 处理者
 * </p>
 *
 * @author isaac 2020/6/18 17:33
 * @since 1.0
 */
public class PluginMybatisXmlProcessor implements PluginPipeProcessorExtend {

    private final MybatisXmlProcess mybatisXmlProcess;
    private final SqlSessionFactory sqlSessionFactory;

    PluginMybatisXmlProcessor(ApplicationContext mainApplicationContext) {
        sqlSessionFactory = mainApplicationContext.getBean(SqlSessionFactory.class);
        this.mybatisXmlProcess = MybatisXmlProcess.getInstance(sqlSessionFactory);
    }

    @Override
    public String key() {
        return "PluginMybatisXmlProcessor";
    }

    @Override
    public OrderPriority order() {
        return OrderPriority.getLowPriority();
    }

    @Override
    public void initialize() {
        //
    }

    @Override
    public void register(PluginRegistryInfo pluginRegistryInfo) {
        if (mybatisXmlProcess == null || sqlSessionFactory == null) {
            return;
        }
        BasePlugin basePlugin = pluginRegistryInfo.getBasePlugin();
        PluginWrapper pluginWrapper = pluginRegistryInfo.getPluginWrapper();
        PluginResourceLoadFactory pluginResourceLoadFactory = basePlugin.getBasePluginExtend().getPluginResourceLoadFactory();

        ResourceWrapper resourceWrapper = pluginResourceLoadFactory.getPluginResources(PluginMybatisXmlLoader.DEFAULT_KEY);
        if (resourceWrapper == null) {
            return;
        }
        processAliases(pluginRegistryInfo);
        List<Resource> pluginResources = resourceWrapper.getResources();
        if (pluginResources == null || pluginResources.isEmpty()) {
            return;
        }
        mybatisXmlProcess.loadXmlResource(
                pluginRegistryInfo.getPluginWrapper().getPluginId(),
                pluginResources, pluginWrapper.getPluginClassLoader());
    }

    private void processAliases(PluginRegistryInfo pluginRegistryInfo) {
        // ignore
    }

    @Override
    public void unregister(PluginRegistryInfo pluginRegistryInfo) {
        mybatisXmlProcess.unregister(pluginRegistryInfo.getPluginWrapper().getPluginId());
    }

}
