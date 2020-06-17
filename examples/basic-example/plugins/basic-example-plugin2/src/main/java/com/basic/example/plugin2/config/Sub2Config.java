package com.basic.example.plugin2.config;

/**
 * <p>
 * 插件2配置文件子bean映射类
 * </p>
 *
 * @author isaac 2020/6/16 19:34
 * @since 1.0
 */
public class Sub2Config {

    private String subName;

    public String getSubName() {
        return subName;
    }

    public void setSubName(String subName) {
        this.subName = subName;
    }

    @Override
    public String toString() {
        return "Sub2Config{" +
                "subName='" + subName + '\'' +
                '}';
    }
}
