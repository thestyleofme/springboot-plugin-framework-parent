package com.github.codingdebugallday.plugin.framework.integration.operator.verify;

import java.nio.file.Path;
import java.util.Objects;

import com.github.codingdebugallday.plugin.framework.exceptions.PluginException;
import org.pf4j.*;

/**
 * <p>
 * 默认的插件校验器
 * </p>
 *
 * @author isaac 2020/6/16 15:40
 * @since 1.0
 */
public class DefaultPluginVerify extends PluginLegalVerify {

    private final PluginManager pluginManager;

    public DefaultPluginVerify(PluginManager pluginManager) {
        super(new CompoundPluginDescriptorFinder()
                .add(new ManifestPluginDescriptorFinder())
                .add(new PropertiesPluginDescriptorFinder()));
        Objects.requireNonNull(pluginManager);
        this.pluginManager = pluginManager;
    }


    @Override
    protected Path postVerify(Path path, PluginDescriptor pluginDescriptor) {
        PluginWrapper pluginWrapper = pluginManager.getPlugin(pluginDescriptor.getPluginId());
        if (pluginWrapper == null) {
            // 当前没有该插件包运行
            return path;
        }
        // 如果当前插件在当前环境存在, 则抛出异常
        PluginDescriptor runPluginDescriptor = pluginWrapper.getDescriptor();
        String errorMsg = "The plugin (" +
                "id:<" + runPluginDescriptor.getPluginId() +
                "> ; version <" + runPluginDescriptor.getVersion() +
                "> ) is already exist in the current environment。 " +
                "Please uninstall the plugin, then upload and update the plugin";
        throw new PluginException(errorMsg);
    }
}
