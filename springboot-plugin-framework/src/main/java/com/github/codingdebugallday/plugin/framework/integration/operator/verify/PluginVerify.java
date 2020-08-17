package com.github.codingdebugallday.plugin.framework.integration.operator.verify;

import java.nio.file.Path;

/**
 * <p>
 * 插件合法校验接口
 * </p>
 *
 * @author isaac 2020/6/16 15:42
 * @see DefaultPluginVerify
 * @see PluginLegalVerify
 * @since 1.0
 */
public interface PluginVerify {

    /**
     * 校验插件包
     *
     * @param path 插件路径
     * @return 返回校验成功的路径
     */
    Path verify(Path path);

}
