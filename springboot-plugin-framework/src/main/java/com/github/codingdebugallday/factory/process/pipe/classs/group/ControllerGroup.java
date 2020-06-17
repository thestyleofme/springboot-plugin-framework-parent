package com.github.codingdebugallday.factory.process.pipe.classs.group;

import com.github.codingdebugallday.factory.process.pipe.classs.PluginClassGroup;
import com.github.codingdebugallday.realize.BasePlugin;
import com.github.codingdebugallday.utils.AnnotationsUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 分组存在注解: @Controller @RestController
 * </p>
 *
 * @author isaac 2020/6/16 13:50
 * @since 1.0
 */
public class ControllerGroup implements PluginClassGroup {

    /**
     * spring @Controller @RestController 注解bean
     */
    public static final String GROUP_ID = "spring_controller";


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
        return AnnotationsUtils.haveAnnotations(aClass, false, RestController.class, Controller.class);
    }
}
