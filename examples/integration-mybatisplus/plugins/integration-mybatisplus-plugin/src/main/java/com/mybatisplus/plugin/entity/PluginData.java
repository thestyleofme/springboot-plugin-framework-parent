package com.mybatisplus.plugin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import org.apache.ibatis.type.Alias;

/**
 * <p>
 * description
 * </p>
 *
 * @author isaac 2020/6/22 15:54
 * @since 1.0
 */
@TableName("plugin_data")
@Alias("pluginData")
public class PluginData {

    @TableId(type = IdType.ASSIGN_UUID)
    private String pluginId;
    private String name;
    private Integer type;
    private String description;


    public String getPluginId() {
        return pluginId;
    }

    public void setPluginId(String pluginId) {
        this.pluginId = pluginId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
