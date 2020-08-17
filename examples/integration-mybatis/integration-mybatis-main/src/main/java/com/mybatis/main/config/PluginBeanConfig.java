package com.mybatis.main.config;

import java.util.concurrent.TimeUnit;

import com.github.codingdebugallday.plugin.framework.extension.mybatis.SpringBootMybatisExtension;
import com.github.codingdebugallday.plugin.framework.extension.resources.StaticResourceExtension;
import com.github.codingdebugallday.plugin.framework.integration.ConfigurationBuilder;
import com.github.codingdebugallday.plugin.framework.integration.IntegrationConfiguration;
import com.github.codingdebugallday.plugin.framework.integration.application.AutoPluginApplication;
import com.github.codingdebugallday.plugin.framework.integration.application.PluginApplication;
import org.pf4j.RuntimeMode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.CacheControl;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 插件集成配置
 * </p>
 *
 * @author isaac 2020/6/19 11:45
 * @since 1.0
 */
@Component
@ConfigurationProperties(prefix = "plugin")
public class PluginBeanConfig {

    /**
     * 运行模式
     *  开发环境: development、dev
     *  生产/部署 环境: deployment、prod
     */
    @Value("${runMode:dev}")
    private String runMode;

    /**
     * 插件的路径
     */
    @Value("${pluginPath:plugins}")
    private String pluginPath;

    /**
     * 插件文件的路径
     */
    @Value("${pluginConfigFilePath:pluginConfigs}")
    private String pluginConfigFilePath;

    @Bean
    public IntegrationConfiguration configuration(){
        return ConfigurationBuilder.toBuilder()
                .runtimeMode(RuntimeMode.byName(runMode))
                .pluginPath(pluginPath)
                .pluginConfigFilePath(pluginConfigFilePath)
                .uploadTempPath("temp")
                .backupPath("backupPlugin")
                .pluginRestControllerPathPrefix("/api/plugin")
                .enablePluginIdRestControllerPathPrefix(true)
                .build();
    }

    /**
     * 定义插件应用。使用可以注入它操作插件。
     * @return PluginApplication
     */
    @Bean
    public PluginApplication pluginApplication(){
        // 实例化自动初始化插件的PluginApplication
        PluginApplication pluginApplication = new AutoPluginApplication();
        pluginApplication.addExtension(new SpringBootMybatisExtension());
        // 新增静态资源扩展
        StaticResourceExtension staticResourceExtension = new StaticResourceExtension();
        staticResourceExtension.setPathPrefix("static");
        staticResourceExtension.setCacheControl(CacheControl.maxAge(1, TimeUnit.HOURS).cachePublic());
        pluginApplication.addExtension(staticResourceExtension);
        return pluginApplication;
    }

    public void setRunMode(String runMode) {
        this.runMode = runMode;
    }

    public void setPluginPath(String pluginPath) {
        this.pluginPath = pluginPath;
    }

    public void setPluginConfigFilePath(String pluginConfigFilePath) {
        this.pluginConfigFilePath = pluginConfigFilePath;
    }
}
