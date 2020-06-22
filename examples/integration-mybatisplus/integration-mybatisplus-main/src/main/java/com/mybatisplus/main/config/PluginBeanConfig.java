package com.mybatisplus.main.config;

import com.github.codingdebugallday.extension.mybatis.SpringBootMybatisExtension;
import com.github.codingdebugallday.integration.application.PluginApplication;
import com.github.codingdebugallday.integration.application.AutoPluginApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * 插件集成配置
 * </p>
 *
 * @author isaac 2020/6/22 13:40
 * @since 1.0
 */
@Configuration
public class PluginBeanConfig {

    /**
     * 定义插件应用。使用可以注入它操作插件。
     * @return PluginApplication
     */
    @Bean
    public PluginApplication pluginApplication(){
        // 实例化自动初始化插件的PluginApplication
        PluginApplication pluginApplication = new AutoPluginApplication();
        pluginApplication.addExtension(new SpringBootMybatisExtension());
        return pluginApplication;
    }

}
