package com.mybatisplus.plugin.service;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.thestyleofme.plugin.framework.extension.mybatis.support.mybatisplus.WrapperServiceImpl;
import com.mybatisplus.plugin.entity.PluginData;
import com.mybatisplus.plugin.mapper.PluginDataMapper;
import org.springframework.stereotype.Component;

/**
 * <p>
 * description
 * </p>
 *
 * @author isaac 2020/7/1 17:05
 * @since 1.0
 */
@Component
public class PluginDataServiceImpl extends WrapperServiceImpl<PluginDataMapper, PluginData>
        implements PluginDataService {

    public PluginDataServiceImpl(PluginDataMapper baseMapper) {
        super(baseMapper);
    }

    @Override
    public List<PluginData> getByName(String name) {
        Wrapper<PluginData> wrapper = Wrappers.<PluginData>query()
                .eq("name", name);
        return list(wrapper);
    }

    @Override
    public IPage<PluginData> getByPage(Long size, Long currentPage) {
        Page<PluginData> page = new Page<>();
        page.setSize(size);
        page.setCurrent(currentPage);
        return page(page);
    }

}
