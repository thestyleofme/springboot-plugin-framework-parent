package com.github.codingdebugallday.plugin.framework.extension.resources;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.github.codingdebugallday.plugin.framework.extension.AbstractExtension;
import com.github.codingdebugallday.plugin.framework.extension.resources.resolver.ResourceWebMvcConfigurer;
import com.github.codingdebugallday.plugin.framework.factory.process.post.PluginPostProcessorExtend;
import org.springframework.context.ApplicationContext;
import org.springframework.http.CacheControl;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.config.annotation.DelegatingWebMvcConfiguration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * <p>
 * 插件静态资源访问的扩展插件
 * </p>
 *
 * @author isaac 2020/6/19 10:35
 * @since 1.0
 */
public class StaticResourceExtension extends AbstractExtension {

    private static final String KEY = "StaticResourceExtension";

    /**
     * 访问插件静态资源前缀。默认为: static-plugin。
     */
    private static String pluginStaticResourcePathPrefix = "static-plugin";

    /**
     * 访问静态资源的缓存控制。默认最大1小时。主要针对http协议的缓存。
     */
    private static CacheControl pluginStaticResourcesCacheControl =
            CacheControl.maxAge(1, TimeUnit.HOURS).cachePublic();


    @Override
    public String key() {
        return KEY;
    }

    @Override
    public void initialize(ApplicationContext applicationContext) {
        WebMvcConfigurer webMvcConfigurer = new ResourceWebMvcConfigurer();
        List<WebMvcConfigurer> webMvcConfigurers = new ArrayList<>();
        webMvcConfigurers.add(webMvcConfigurer);
        DelegatingWebMvcConfiguration support =
                applicationContext.getBean(DelegatingWebMvcConfiguration.class);
        support.setConfigurers(webMvcConfigurers);
    }


    @Override
    public List<PluginPostProcessorExtend> getPluginPostProcessor(ApplicationContext applicationContext) {
        final List<PluginPostProcessorExtend> pluginPostProcessorExtends = new ArrayList<>();
        pluginPostProcessorExtends.add(new PluginResourceResolverProcess());
        return pluginPostProcessorExtends;
    }

    /**
     * 设置访问插件静态资源前缀
     *
     * @param pluginStaticResourcePathPrefix 静态资源前缀。默认为: static-plugin。
     */
    public void setPathPrefix(String pluginStaticResourcePathPrefix) {
        if (!StringUtils.isEmpty(pluginStaticResourcePathPrefix)) {
            StaticResourceExtension.pluginStaticResourcePathPrefix = pluginStaticResourcePathPrefix;
        }
    }

    /**
     * 设置缓存控制
     *
     * @param pluginStaticResourcesCacheControl 访问静态资源的缓存控制。默认最大1小时。主要针对http协议的缓存。
     */
    public void setCacheControl(CacheControl pluginStaticResourcesCacheControl) {
        StaticResourceExtension.pluginStaticResourcesCacheControl = pluginStaticResourcesCacheControl;
    }

    public static String getPluginStaticResourcePathPrefix() {
        return pluginStaticResourcePathPrefix;
    }

    public static CacheControl getPluginStaticResourcesCacheControl() {
        return pluginStaticResourcesCacheControl;
    }

}
