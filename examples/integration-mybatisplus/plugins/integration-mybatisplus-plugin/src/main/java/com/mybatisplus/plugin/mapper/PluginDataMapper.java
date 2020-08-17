package com.mybatisplus.plugin.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.codingdebugallday.plugin.framework.extension.mybatis.annotation.PluginMapper;
import com.mybatisplus.plugin.entity.PluginData;

/**
 * <p>
 * description
 * </p>
 *
 * @author isaac 2020/6/22 15:55
 * @since 1.0
 */
@PluginMapper
public interface PluginDataMapper extends BaseMapper<PluginData> {

    List<PluginData> getAll();

}
