package com.mybatisplus.plugin.rest;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mybatisplus.plugin.entity.PluginData;
import com.mybatisplus.plugin.service.PluginDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * description
 * </p>
 *
 * @author isaac 2020/6/22 15:53
 * @since 1.0
 */
@RestController
@RequestMapping("pluginData")
public class PluginDataController {

    @Autowired
    private PluginDataService pluginDataService;

    @GetMapping
    public List<PluginData> getAll() {
        return pluginDataService.list();
    }

    @GetMapping("page/{size}/{currentPage}")
    public IPage<PluginData> getPage(@PathVariable("size") Long size,
                                     @PathVariable("currentPage") Long currentPage) {
        return pluginDataService.getByPage(size, currentPage);
    }

    @GetMapping("name/{name}")
    public List<PluginData> getByName(@PathVariable("name") String name) {
        return pluginDataService.getByName(name);
    }

    @PostMapping()
    public Boolean save(PluginData pluginData) {
        return pluginDataService.save(pluginData);
    }

    @DeleteMapping("{id}")
    public Boolean delete(@PathVariable("id") String id) {
        return pluginDataService.removeById(id);
    }

}
