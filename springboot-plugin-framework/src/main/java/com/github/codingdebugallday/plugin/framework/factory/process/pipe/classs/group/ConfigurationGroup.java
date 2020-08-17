package com.github.codingdebugallday.plugin.framework.factory.process.pipe.classs.group;

import com.github.codingdebugallday.plugin.framework.factory.process.pipe.classs.PluginClassGroup;
import com.github.codingdebugallday.plugin.framework.realize.BasePlugin;
import com.github.codingdebugallday.plugin.framework.utils.AnnotationsUtils;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * 分组存在注解: @Configuration
 * </p>
 *
 * @author isaac 2020/6/16 13:50
 * @since 1.0
 */
public class ConfigurationGroup implements PluginClassGroup {

    /**
     * spring @CONFIGURATION 注解bean
     */
    public static final String GROUP_ID = "spring_configuration";


    @Override
    public String groupId() {
        return GROUP_ID;
    }

    @Override
    public void initialize(BasePlugin basePlugin) {
        // ignore
    }

    @Override
    public boolean filter(Class<?> aClass) {
        return AnnotationsUtils.haveAnnotations(aClass, false, Configuration.class);
    }
}
