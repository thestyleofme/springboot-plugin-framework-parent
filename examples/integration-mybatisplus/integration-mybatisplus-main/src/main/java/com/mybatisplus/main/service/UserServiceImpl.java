package com.mybatisplus.main.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mybatisplus.main.entity.User;
import com.mybatisplus.main.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
 * <p>
 * description
 * </p>
 *
 * @author isaac 2020/6/22 13:47
 * @since 1.0
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
