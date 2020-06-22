package com.mybatisplus.plugin.service;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mybatisplus.plugin.entity.PluginData;

/**
 * <p>
 * description
 * </p>
 *
 * @author isaac 2020/6/22 15:55
 * @since 1.0
 */
public interface PluginDataService extends IService<PluginData> {

    /**
     * 通过名称查询
     *
     * @param name name
     * @return List<PluginData>
     */
    List<PluginData> getByName(String name);

    /**
     * 分页查询
     *
     * @param size        size
     * @param currentPage currentPage
     * @return IPage<PluginData>
     */
    IPage<PluginData> getByPage(Long size, Long currentPage);

}
