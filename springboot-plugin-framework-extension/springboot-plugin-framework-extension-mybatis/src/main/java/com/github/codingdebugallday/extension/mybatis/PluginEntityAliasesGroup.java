package com.github.codingdebugallday.extension.mybatis;

import java.util.Set;

import com.github.codingdebugallday.extension.mybatis.configuration.SpringBootMybatisConfig;
import com.github.codingdebugallday.factory.process.pipe.classs.PluginClassGroupExtend;
import com.github.codingdebugallday.realize.BasePlugin;
import com.github.codingdebugallday.utils.AnnotationsUtils;
import org.apache.ibatis.type.Alias;

/**
 * <p>
 * 插件中的实体类别名
 * </p>
 *
 * @author isaac 2020/6/18 16:51
 * @since 1.0
 */
public class PluginEntityAliasesGroup implements PluginClassGroupExtend {

    public static final String DEFAULT_KEY = "plugin_mybatis_alias";

    private Set<String> typeAliasesPackage;

    @Override
    public String groupId() {
        return DEFAULT_KEY;
    }

    @Override
    public void initialize(BasePlugin basePlugin) {
        if(basePlugin instanceof SpringBootMybatisConfig){
            SpringBootMybatisConfig config = (SpringBootMybatisConfig) basePlugin;
            typeAliasesPackage = config.typeAliasesPackage();
        }
    }

    @Override
    public boolean filter(Class<?> aClass) {
        if(AnnotationsUtils.haveAnnotations(aClass, false, Alias.class)){
            return true;
        }
        if(typeAliasesPackage == null || typeAliasesPackage.isEmpty()){
            return false;
        }
        for (String packageName : typeAliasesPackage) {
            if(aClass.getPackage().getName().equals(packageName)){
                return true;
            }
        }
        return false;
    }

    @Override
    public String key() {
        return "PluginEntityAliasesGroup";
    }

}
