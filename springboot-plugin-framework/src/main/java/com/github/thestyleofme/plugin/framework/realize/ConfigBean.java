package com.github.thestyleofme.plugin.framework.realize;

/**
 * <p>
 * 插件可配置自定义bean的接口
 * 注意：该实现类只能注入插件中的配置文件和主程序bean 不能注入插件中其他的组件bean
 * bean 指的是Spring 容器中管理的bean
 * </p>
 *
 * @author isaac 2020/6/16 13:47
 * @since 1.0
 */
public interface ConfigBean {


    /**
     * 初始化。所有bean的初始化工作在此处实现
     */
    void initialize();

    /**
     * 销毁实现
     */
    void destroy();

}
