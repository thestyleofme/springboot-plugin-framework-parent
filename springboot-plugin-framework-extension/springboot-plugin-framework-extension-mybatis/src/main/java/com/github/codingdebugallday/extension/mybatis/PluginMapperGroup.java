package com.github.codingdebugallday.extension.mybatis;

import com.github.codingdebugallday.extension.mybatis.annotation.PluginMapper;
import com.github.codingdebugallday.factory.process.pipe.classs.PluginClassGroupExtend;
import com.github.codingdebugallday.realize.BasePlugin;
import com.github.codingdebugallday.utils.AnnotationsUtils;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 插件中的Mapper接口分组
 * </p>
 *
 * @author isaac 2020/6/18 16:53
 * @since 1.0
 */
public class PluginMapperGroup implements PluginClassGroupExtend {

    public static final String GROUP_ID = "plugin_mybatis_mapper";

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
        return AnnotationsUtils.haveAnnotations(aClass, false, PluginMapper.class) ||
                AnnotationsUtils.haveAnnotations(aClass, false, Mapper.class);
    }

    @Override
    public String key() {
        return "PluginMybatisMapperGroup";
    }
}
