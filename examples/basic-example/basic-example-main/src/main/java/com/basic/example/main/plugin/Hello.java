package com.basic.example.main.plugin;

import org.pf4j.ExtensionPoint;

/**
 * <p>
 * 测试非Spring管理的bean接口
 * 实现类需要使用 @Extension 注解
 * </p>
 *
 * @author isaac 2020/6/16 17:25
 * @since 1.0
 */
public interface Hello extends ExtensionPoint {

    /**
     * getName
     *
     * @return name
     */
    String getName();

}
