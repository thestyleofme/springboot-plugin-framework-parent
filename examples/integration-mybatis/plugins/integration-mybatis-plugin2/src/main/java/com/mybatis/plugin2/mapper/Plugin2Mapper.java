package com.mybatis.plugin2.mapper;

import java.util.List;

import com.github.codingdebugallday.extension.mybatis.annotation.PluginMapper;
import com.mybatis.plugin2.entity.Plugin2;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * description
 * </p>
 *
 * @author isaac 2020/6/19 14:14
 * @since 1.0
 */
@PluginMapper
public interface Plugin2Mapper {


    /**
     * 得到角色列表
     *
     * @return List
     */
    List<Plugin2> getList();

    /**
     * 通过id获取数据
     *
     * @param id id
     * @return Plugin2
     */
    Plugin2 getById(@Param("id") String id);

    /**
     * save
     *
     * @param id   id
     * @param name name
     */
    @Insert("INSERT INTO plugin2 VALUES (#{id}, #{name})")
    void save(@Param("id") String id, @Param("name") String name);

}
