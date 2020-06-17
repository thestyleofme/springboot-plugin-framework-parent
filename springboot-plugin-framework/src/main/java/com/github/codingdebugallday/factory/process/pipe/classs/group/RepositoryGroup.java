package com.github.codingdebugallday.factory.process.pipe.classs.group;

import com.github.codingdebugallday.factory.process.pipe.classs.PluginClassGroup;
import com.github.codingdebugallday.realize.BasePlugin;
import com.github.codingdebugallday.utils.AnnotationsUtils;
import org.springframework.stereotype.Repository;

/**
 * 分组存在注解: @Repository
 *
 * @author zhangzhuo
 * @version 2.1.0
 */
public class RepositoryGroup implements PluginClassGroup {

    /**
     * spring @Repository 注解bean
     */
    public static final String GROUP_ID = "spring_repository";


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
        return AnnotationsUtils.haveAnnotations(aClass, false, Repository.class);
    }
}
