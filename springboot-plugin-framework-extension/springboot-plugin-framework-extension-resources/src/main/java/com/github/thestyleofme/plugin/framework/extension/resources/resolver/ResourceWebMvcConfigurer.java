package com.github.thestyleofme.plugin.framework.extension.resources.resolver;

import com.github.thestyleofme.plugin.framework.constants.BaseConstants;
import com.github.thestyleofme.plugin.framework.extension.resources.StaticResourceExtension;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * <p>
 * 注册插件的WebMvc的配置
 * </p>
 *
 * @author isaac 2020/6/19 10:34
 * @since 1.0
 */
public class ResourceWebMvcConfigurer implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String pathPattern = BaseConstants.Symbol.SLASH + StaticResourceExtension.getPluginStaticResourcePathPrefix() + "/**";
        ResourceHandlerRegistration resourceHandlerRegistration = registry.addResourceHandler(pathPattern);
        CacheControl cacheControl = StaticResourceExtension.getPluginStaticResourcesCacheControl();
        if (cacheControl != null) {
            resourceHandlerRegistration.setCacheControl(cacheControl);
        } else {
            resourceHandlerRegistration.setCacheControl(CacheControl.noStore());
        }
        resourceHandlerRegistration
                .resourceChain(false)
                .addResolver(new PluginResourceResolver());
    }


}
