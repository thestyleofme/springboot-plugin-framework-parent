package com.mybatis.plugin1.rest;

import java.util.List;

import com.mybatis.plugin1.entity.Plugin1;
import com.mybatis.plugin1.mapper.Plugin1Mapper;
import com.mybatis.plugin1.service.TranService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * description
 * </p>
 *
 * @author isaac 2020/6/19 14:05
 * @since 1.0
 */
@RestController
@RequestMapping("/plugin1")
public class Plugin1Controller {

    @Autowired
    private Plugin1Mapper pluginMapperl;

    @Autowired
    private TranService testTransactional;

    @GetMapping
    public String hello() {
        return "hello, 这是集成mybatis 插件1";
    }

    @GetMapping("/list")
    public List<Plugin1> getList() {
        return pluginMapperl.getList();
    }

    @GetMapping("/{id}")
    public Plugin1 getConfig(@PathVariable("id") String id) {
        return pluginMapperl.getById(id);
    }

    @PostMapping("/bean")
    public List<Plugin1> getUserByIdBean(@RequestParam(value = "id", required = false) String id,
                                         @RequestParam(value = "name", required = false) String name) {
        Plugin1 p = new Plugin1();
        if (!StringUtils.isEmpty(id)) {
            p.setId(id);
        }
        if (!StringUtils.isEmpty(name)) {
            p.setName(name);
        }
        return pluginMapperl.getByCondition(p);
    }

    @GetMapping("/transactional")
    public void tran() {
        testTransactional.transactional();
    }
}
