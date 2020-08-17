package com.github.codingdebugallday.plugin.framework.factory.process.pipe.classs.group;

import java.util.Set;

import com.github.codingdebugallday.plugin.framework.factory.process.pipe.classs.PluginClassGroup;
import com.github.codingdebugallday.plugin.framework.realize.BasePlugin;
import com.github.codingdebugallday.plugin.framework.realize.ConfigBean;
import org.springframework.util.ClassUtils;

/**
 * <p>
 * 对接口ConfigBean实现的类分组
 * </p>
 *
 * @author isaac 2020/6/16 13:46
 * @see ConfigBean
 * @since 1.0
 */
public class ConfigBeanGroup implements PluginClassGroup {


    public static final String GROUP_ID = "config_bean";


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
        return allInterfacesForClassAsSet.contains(ConfigBean.class);
    }

}
