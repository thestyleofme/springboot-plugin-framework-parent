package com.github.codingdebugallday.extension.mybatis;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.github.codingdebugallday.extension.mybatis.utils.MybatisInjectWrapper;
import com.github.codingdebugallday.factory.PluginInfoContainer;
import com.github.codingdebugallday.factory.PluginRegistryInfo;
import com.github.codingdebugallday.factory.SpringBeanRegister;
import com.github.codingdebugallday.factory.process.pipe.PluginPipeProcessorExtend;
import com.github.codingdebugallday.factory.process.pipe.bean.name.PluginAnnotationBeanNameGenerator;
import com.github.codingdebugallday.realize.BasePlugin;
import com.github.codingdebugallday.utils.OrderPriority;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.context.annotation.AnnotationScopeMetadataResolver;
import org.springframework.context.annotation.ScopeMetadata;
import org.springframework.context.annotation.ScopeMetadataResolver;
import org.springframework.context.support.GenericApplicationContext;

/**
 * <p>
 * 插件 mybatis mapper 注册者
 * </p>
 *
 * @author isaac 2020/6/18 17:23
 * @since 1.0
 */
public class PluginMybatisMapperProcessor implements PluginPipeProcessorExtend {

    private static final String KEY = "PluginMybatisMapperProcessor";

    private final ScopeMetadataResolver scopeMetadataResolver = new AnnotationScopeMetadataResolver();

    private final GenericApplicationContext applicationContext;
    private final MybatisInjectWrapper mybatisInjectWrapper;

    PluginMybatisMapperProcessor(ApplicationContext applicationContext) {
        this.applicationContext = (GenericApplicationContext) applicationContext;
        SpringBeanRegister springBeanRegister = new SpringBeanRegister(applicationContext);
        springBeanRegister.register(KEY, MybatisInjectWrapper.class);
        mybatisInjectWrapper = applicationContext.getBean(MybatisInjectWrapper.class);
    }

    /**
     * 判断mybatis依赖是否存在
     */
    private void mybatisExist() {
        applicationContext.getBean(SqlSessionFactory.class);
        applicationContext.getBean(SqlSessionTemplate.class);
    }

    @Override
    public void initialize() {
        // ignore
    }

    @Override
    public void register(PluginRegistryInfo pluginRegistryInfo) {
        mybatisExist();
        List<Class<?>> groupClasses = pluginRegistryInfo.getGroupClasses(com.github.codingdebugallday.extension.mybatis.PluginMapperGroup.GROUP_ID);
        if (groupClasses == null || groupClasses.isEmpty()) {
            return;
        }
        BasePlugin basePlugin = pluginRegistryInfo.getBasePlugin();
        String pluginId = pluginRegistryInfo.getPluginWrapper().getPluginId();
        Set<String> beanNames = new HashSet<>();
        for (Class<?> groupClass : groupClasses) {
            if (groupClass == null) {
                continue;
            }
            BeanNameGenerator beanNameGenerator = new PluginAnnotationBeanNameGenerator(pluginId, basePlugin.getWrapper().getPluginId());
            AnnotatedGenericBeanDefinition abd = new AnnotatedGenericBeanDefinition(groupClass);
            ScopeMetadata scopeMetadata = this.scopeMetadataResolver.resolveScopeMetadata(abd);
            abd.setScope(scopeMetadata.getScopeName());
            String beanName = beanNameGenerator.generateBeanName(abd, applicationContext);
            BeanDefinitionHolder definitionHolder = new BeanDefinitionHolder(abd, beanName);
            AnnotationConfigUtils.processCommonDefinitionAnnotations(abd);
            BeanDefinitionReaderUtils.registerBeanDefinition(definitionHolder, applicationContext);
            mybatisInjectWrapper.processBeanDefinitions(definitionHolder, groupClass);
            beanNames.add(beanName);
            PluginInfoContainer.addRegisterBeanName(pluginId, beanName);
        }
        pluginRegistryInfo.addProcessorInfo(KEY, beanNames);
    }

    @Override
    public void unregister(PluginRegistryInfo pluginRegistryInfo) {
        Set<String> beanNames = pluginRegistryInfo.getProcessorInfo(KEY);
        if (beanNames == null) {
            return;
        }
        String pluginId = pluginRegistryInfo.getPluginWrapper().getPluginId();
        for (String beanName : beanNames) {
            applicationContext.removeBeanDefinition(beanName);
            PluginInfoContainer.removeRegisterBeanName(pluginId, beanName);
        }
    }

    @Override
    public String key() {
        return KEY;
    }

    @Override
    public OrderPriority order() {
        return OrderPriority.getHighPriority();
    }
}
