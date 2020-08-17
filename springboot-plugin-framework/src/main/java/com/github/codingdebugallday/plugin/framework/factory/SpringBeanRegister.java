package com.github.codingdebugallday.plugin.framework.factory;

import java.text.MessageFormat;
import java.util.function.Consumer;

import com.github.codingdebugallday.plugin.framework.exceptions.PluginException;
import com.github.codingdebugallday.plugin.framework.factory.process.pipe.bean.name.PluginAnnotationBeanNameGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

/**
 * <p>
 * Spring bean注册者
 * </p>
 *
 * @author isaac 2020/6/16 14:10
 * @since 1.0
 */
public class SpringBeanRegister {

    private static final Logger logger = LoggerFactory.getLogger(SpringBeanRegister.class);

    private final GenericApplicationContext applicationContext;

    public SpringBeanRegister(ApplicationContext applicationContext) {
        this.applicationContext = (GenericApplicationContext) applicationContext;
    }

    /**
     * 默认注册
     *
     * @param pluginId 插件id
     * @param aClass   类名
     * @return 注册的bean名称
     */
    public String register(String pluginId, Class<?> aClass) {
        return register(pluginId, null, aClass, null);
    }

    /**
     * 默认注册
     *
     * @param pluginId   插件id
     * @param suffixName bean 后缀名称
     * @param aClass     类名
     * @return 注册的bean名称
     */
    public String register(String pluginId, String suffixName, Class<?> aClass) {
        return register(pluginId, suffixName, aClass, null);
    }


    /**
     * 默认注册
     *
     * @param pluginId 插件id
     * @param aClass   类名
     * @param consumer 自定义处理AnnotatedGenericBeanDefinition
     * @return 注册的bean名称
     */
    public String register(String pluginId, Class<?> aClass, Consumer<AnnotatedGenericBeanDefinition> consumer) {
        return register(pluginId, null, aClass, consumer);
    }

    /**
     * 默认注册
     *
     * @param pluginId   插件id
     * @param suffixName bean 后缀名称
     * @param aClass     注册的类
     * @param consumer   自定义处理AnnotatedGenericBeanDefinition
     * @return 注册的bean名称
     */
    public String register(String pluginId, String suffixName, Class<?> aClass,
                           Consumer<AnnotatedGenericBeanDefinition> consumer) {
        AnnotatedGenericBeanDefinition beanDefinition = new AnnotatedGenericBeanDefinition(aClass);
        BeanNameGenerator beanNameGenerator =
                new PluginAnnotationBeanNameGenerator(pluginId, suffixName);
        String beanName = beanNameGenerator.generateBeanName(beanDefinition, applicationContext);
        if (PluginInfoContainer.existRegisterBeanName((beanName))) {
            logger.debug("Bean name {} already exist of {}", beanName, aClass.getName());
            return beanName;
        }
        if (consumer != null) {
            consumer.accept(beanDefinition);
        }
        applicationContext.registerBeanDefinition(beanName, beanDefinition);
        PluginInfoContainer.addRegisterBeanName(pluginId, beanName);
        return beanName;
    }

    /**
     * 指定bean名称注册
     *
     * @param pluginId 插件id
     * @param beanName 指定的bean名称
     * @param aClass   注册的类
     */
    public void registerOfSpecifyName(String pluginId, String beanName, Class<?> aClass) {
        registerOfSpecifyName(pluginId, beanName, aClass, null);
    }

    /**
     * 指定bean名称注册
     *
     * @param pluginId 插件id
     * @param beanName 指定的bean名称
     * @param aClass   注册的类
     * @param consumer 注册异常
     */
    public void registerOfSpecifyName(String pluginId,
                                      String beanName,
                                      Class<?> aClass,
                                      Consumer<AnnotatedGenericBeanDefinition> consumer) {
        AnnotatedGenericBeanDefinition beanDefinition = new
                AnnotatedGenericBeanDefinition(aClass);
        if (PluginInfoContainer.existRegisterBeanName((beanName))) {
            String error = MessageFormat.format("Bean name {0} already exist of {1}",
                    beanName, aClass.getName());
            throw new PluginException(error);
        }
        if (consumer != null) {
            consumer.accept(beanDefinition);
        }
        PluginInfoContainer.addRegisterBeanName(pluginId, beanName);
        applicationContext.registerBeanDefinition(beanName, beanDefinition);
    }


    /**
     * 卸载bean
     *
     * @param pluginId 插件id
     * @param beanName bean名称
     */
    public void unregister(String pluginId, String beanName) {
        PluginInfoContainer.removeRegisterBeanName(pluginId, beanName);
        applicationContext.removeBeanDefinition(beanName);
    }


}
