package com.github.codingdebugallday.extension.resources;

import java.util.Set;

/**
 * <p>
 * 插件静态资源的配置
 * </p>
 *
 * @author isaac 2020/6/19 10:35
 * @since 1.0
 */
public interface StaticResourceConfig {


    /**
     * 静态文件路径
     * classpath: /static/
     * file: D://path/test
     *
     * @return 路径集合
     */
    Set<String> locations();

}
