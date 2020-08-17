package com.github.codingdebugallday.plugin.framework.factory.process.pipe.classs.group;


import com.github.codingdebugallday.plugin.framework.annotation.Caller;
import com.github.codingdebugallday.plugin.framework.factory.process.pipe.classs.PluginClassGroup;
import com.github.codingdebugallday.plugin.framework.realize.BasePlugin;
import com.github.codingdebugallday.plugin.framework.utils.AnnotationsUtils;

/**
 * <p>
 * 分组存在注解: @Caller
 * </p>
 *
 * @author isaac 2020/6/16 13:41
 * @since 1.0
 */
public class CallerGroup implements PluginClassGroup {


    /**
     * 自定义 @Caller
     */
    public static final String GROUP_ID = "caller";


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
        return AnnotationsUtils.haveAnnotations(aClass, false, Caller.class);
    }

}
