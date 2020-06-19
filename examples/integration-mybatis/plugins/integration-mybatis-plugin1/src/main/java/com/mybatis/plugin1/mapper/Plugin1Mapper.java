package com.mybatis.plugin1.mapper;

import java.util.List;

import com.mybatis.plugin1.entity.Plugin1;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * description
 * </p>
 *
 * @author isaac 2020/6/19 14:02
 * @since 1.0
 */
@Mapper
public interface Plugin1Mapper {


    /**
     * 得到角色列表
     *
     * @return List
     */
    List<Plugin1> getList();

    /**
     * 通过id获取数据
     *
     * @param id id
     * @return Plugin2
     */
    Plugin1 getById(@Param("id") String id);

    /**
     * getByCondition
     *
     * @param plugin1 Plugin1
     * @return List<Plugin1>
     */
    List<Plugin1> getByCondition(Plugin1 plugin1);

    /**
     * insert
     *
     * @param id   id
     * @param name name
     */
    @Insert("INSERT INTO plugin1 VALUES (#{id}, #{name})")
    void insert(@Param("id") String id, @Param("name") String name);

}
