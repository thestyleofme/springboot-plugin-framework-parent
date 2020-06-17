package com.github.codingdebugallday.factory.process.pipe.classs.group;

import com.github.codingdebugallday.annotation.ConfigDefinition;
import com.github.codingdebugallday.factory.process.pipe.classs.PluginClassGroup;
import com.github.codingdebugallday.realize.BasePlugin;
import com.github.codingdebugallday.utils.AnnotationsUtils;

/**
 * <p>
 * 分组存在注解: @ConfigDefinition
 * </p>
 *
 * @author isaac 2020/6/16 13:48
 * @since 1.0
 */
public class ConfigDefinitionGroup implements PluginClassGroup {

    /**
     * 自定义插件配置文件bean @ConfigDefinition
     */
    public static final String GROUP_ID = "config_definition";


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
        return AnnotationsUtils.haveAnnotations(aClass, false, ConfigDefinition.class);
    }

}
