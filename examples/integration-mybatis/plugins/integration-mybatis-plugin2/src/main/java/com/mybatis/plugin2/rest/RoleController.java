package com.mybatis.plugin2.rest;

import java.util.List;

import com.mybatis.main.entity.Role;
import com.mybatis.main.mapper.RoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * description
 * </p>
 *
 * @author isaac 2020/6/19 14:15
 * @since 1.0
 */
@RestController
@RequestMapping("/role")
public class RoleController {

    private final RoleMapper roleMapper;

    @Autowired
    public RoleController(RoleMapper roleMapper) {
        this.roleMapper = roleMapper;
    }

    @GetMapping("/list")
    public List<Role> getList() {
        return roleMapper.getList();
    }


}
