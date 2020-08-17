package com.github.codingdebugallday.plugin.framework.factory.process.pipe.classs.group;

import java.util.Set;

import com.github.codingdebugallday.plugin.framework.factory.process.pipe.classs.PluginClassGroup;
import com.github.codingdebugallday.plugin.framework.realize.BasePlugin;
import com.github.codingdebugallday.plugin.framework.realize.OneselfListener;
import org.springframework.util.ClassUtils;

/**
 * <p>
 * 插件自己的监听器分组
 * </p>
 *
 * @author isaac 2020/7/1 17:05
 * @since 1.0
 */
public class OneselfListenerGroup implements PluginClassGroup {

    public static final String GROUP_ID = "oneself_listener";

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
        if (aClass == null) {
            return false;
        }
        Set<Class<?>> allInterfacesForClassAsSet = ClassUtils.getAllInterfacesForClassAsSet(aClass);
        return allInterfacesForClassAsSet.contains(OneselfListener.class);
    }
}
