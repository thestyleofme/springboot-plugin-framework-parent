package com.github.codingdebugallday.integration.operator.verify;

import java.nio.file.Path;
import java.util.Objects;

import com.github.codingdebugallday.exceptions.PluginException;
import org.pf4j.PluginDescriptor;
import org.pf4j.PluginDescriptorFinder;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 插件包合法校验
 * </p>
 *
 * @author isaac 2020/6/16 15:41
 * @since 1.0
 */
public class PluginLegalVerify implements PluginVerify {

    protected final PluginDescriptorFinder pluginDescriptorFinder;

    public PluginLegalVerify(PluginDescriptorFinder pluginDescriptorFinder) {
        Objects.requireNonNull(pluginDescriptorFinder);
        this.pluginDescriptorFinder = pluginDescriptorFinder;
    }

    @Override
    public Path verify(Path path) {
        if (path == null) {
            throw new IllegalArgumentException("path can not be null");
        }
        if (!pluginDescriptorFinder.isApplicable(path)) {
            // 插件包不合法
            throw new PluginException(path.toString() + " : plugin illegal");
        }
        PluginDescriptor pluginDescriptor = pluginDescriptorFinder.find(path);
        if (pluginDescriptor == null) {
            throw new PluginException(path.toString() + " : Not found plugin Descriptor");
        }
        if (StringUtils.isEmpty(pluginDescriptor.getPluginId())) {
            throw new PluginException(path.toString() + " : Plugin id can't be empty");
        }
        if (StringUtils.isEmpty(pluginDescriptor.getPluginClass())) {
            throw new PluginException(path.toString() + " : Not found plugin Class");
        }
        return postVerify(path, pluginDescriptor);
    }

    /**
     * 合法后的校验.可扩展校验
     *
     * @param path             路径
     * @param pluginDescriptor 插件解析者
     * @return 返回路径
     */
    protected Path postVerify(Path path, PluginDescriptor pluginDescriptor) {
        return path;
    }

}
