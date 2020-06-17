package com.basic.example.plugin1.config;

/**
 * <p>
 * 插件子类配置文件对应bean定义
 * </p>
 *
 * @author isaac 2020/6/16 17:53
 * @since 1.0
 */
public class SubConfig {

    private String subName;

    public String getSubName() {
        return subName;
    }

    public void setSubName(String subName) {
        this.subName = subName;
    }

    @Override
    public String toString() {
        return "SubConfig{" +
                "subName='" + subName + '\'' +
                '}';
    }
}
