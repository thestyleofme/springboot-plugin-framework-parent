package com.github.codingdebugallday.factory.process.pipe.classs.group;

import java.util.Set;

import com.github.codingdebugallday.factory.process.pipe.classs.PluginClassGroup;
import com.github.codingdebugallday.realize.BasePlugin;
import com.github.codingdebugallday.realize.OneselfListener;
import org.springframework.util.ClassUtils;

/**
 * 插件自己的监听器分组
 *
 * @author zhangzhuo
 * @version 2.2.1
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
