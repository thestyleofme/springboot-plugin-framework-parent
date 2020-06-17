package com.github.codingdebugallday.loader.load;

import java.io.IOException;
import java.util.Set;

import com.github.codingdebugallday.exceptions.PluginException;
import com.github.codingdebugallday.loader.PluginResourceLoader;
import com.github.codingdebugallday.loader.ResourceWrapper;
import com.github.codingdebugallday.realize.BasePlugin;
import com.github.codingdebugallday.utils.OrderPriority;
import com.github.codingdebugallday.utils.ScanUtils;
import org.pf4j.RuntimeMode;

/**
 * <p>
 * 插件类文件加载器
 * </p>
 *
 * @author isaac 2020/6/16 11:31
 * @since 1.0
 */
public class PluginClassLoader implements PluginResourceLoader {

    public static final String DEFAULT_KEY = "PluginClassProcess";

    @Override
    public String key() {
        return DEFAULT_KEY;
    }

    @Override
    public ResourceWrapper load(BasePlugin basePlugin) {
        RuntimeMode runtimeMode = basePlugin.getWrapper().getRuntimeMode();
        Set<String> classPackageName = null;
        try {
            if (runtimeMode == RuntimeMode.DEPLOYMENT) {
                // 生产环境
                classPackageName = ScanUtils.scanClassPackageName(
                        basePlugin.scanPackage(), basePlugin.getWrapper().getPluginClassLoader());

            } else if (runtimeMode == RuntimeMode.DEVELOPMENT) {
                // 开发环境
                classPackageName = ScanUtils.scanClassPackageName(
                        basePlugin.scanPackage(), basePlugin.getClass());
            }
        } catch (IOException e) {
            throw new PluginException(e);
        }
        ResourceWrapper resourceWrapper = new ResourceWrapper();
        resourceWrapper.addClassPackageNames(classPackageName);
        return resourceWrapper;
    }

    @Override
    public void unload(BasePlugin basePlugin, ResourceWrapper resourceWrapper) {
        // Do nothing
    }

    @Override
    public OrderPriority order() {
        return OrderPriority.getHighPriority();
    }
}
