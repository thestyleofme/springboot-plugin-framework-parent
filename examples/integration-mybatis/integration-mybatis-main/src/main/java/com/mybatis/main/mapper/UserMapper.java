package com.mybatis.main.mapper;

import java.util.List;

import com.mybatis.main.entity.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * description
 * </p>
 *
 * @author isaac 2020/6/19 11:51
 * @since 1.0
 */
@Repository
public interface UserMapper {

    /**
     * 得到用户列表
     *
     * @return List
     */
    List<User> getList();

    /**
     * 通过id得到用户
     *
     * @param id id
     * @return User
     */
    @Select("select * from user where user_id = #{id}")
    User getById(@Param("id") String id);

}
