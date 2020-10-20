package com.github.thestyleofme.plugin.framework.factory.process.pipe.classs.group;

import com.github.thestyleofme.plugin.framework.annotation.Supplier;
import com.github.thestyleofme.plugin.framework.factory.process.pipe.classs.PluginClassGroup;
import com.github.thestyleofme.plugin.framework.realize.BasePlugin;
import com.github.thestyleofme.plugin.framework.utils.AnnotationsUtils;

/**
 * <p>
 * 分组存在注解: @Supplier
 * </p>
 *
 * @author isaac 2020/6/16 13:52
 * @since 1.0
 */
public class SupplierGroup implements PluginClassGroup {

    /**
     * 自定义 @Supplier
     */
    public static final String GROUP_ID = "supplier";


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
        return AnnotationsUtils.haveAnnotations(aClass, false, Supplier.class);
    }

}
