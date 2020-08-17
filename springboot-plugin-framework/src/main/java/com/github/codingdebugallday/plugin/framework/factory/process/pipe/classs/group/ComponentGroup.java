package com.github.codingdebugallday.plugin.framework.factory.process.pipe.classs.group;

import com.github.codingdebugallday.plugin.framework.factory.process.pipe.classs.PluginClassGroup;
import com.github.codingdebugallday.plugin.framework.realize.BasePlugin;
import com.github.codingdebugallday.plugin.framework.utils.AnnotationsUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 分组存在注解: Component Service
 * </p>
 *
 * @author isaac 2020/6/16 13:45
 * @since 1.0
 */
public class ComponentGroup implements PluginClassGroup {

    /**
     * spring 组件bean.
     * 包括Component、Service
     */
    public static final String GROUP_ID = "spring_component";

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
        return AnnotationsUtils.haveAnnotations(aClass, false, Component.class, Service.class);
    }
}
