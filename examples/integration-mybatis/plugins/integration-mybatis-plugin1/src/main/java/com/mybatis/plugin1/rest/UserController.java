package com.mybatis.plugin1.rest;

import java.util.List;

import com.mybatis.main.entity.User;
import com.mybatis.main.mapper.UserMapper;
import com.mybatis.main.service.TestTestTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * description
 * </p>
 *
 * @author isaac 2020/6/19 14:05
 * @since 1.0
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserMapper userMapper;
    private final TestTestTransactional testTestTransactional;

    @Autowired
    public UserController(UserMapper userMapper, TestTestTransactional testTestTransactional) {
        this.userMapper = userMapper;
        this.testTestTransactional = testTestTransactional;
    }

    @GetMapping("/list")
    public List<User> getList() {
        return userMapper.getList();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable("id") String id) {
        return userMapper.getById(id);
    }

    @GetMapping("/transactional")
    public void testTestTransactional() {
        testTestTransactional.transactional();
    }
}
