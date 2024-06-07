package com.example.math_hub.mapper;

import com.example.math_hub.pojo.Category;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CategoryMapper {
    @Insert("insert into category(category_name,category_alias,create_user,create_time,update_time)" +
            "values(#{category_name},#{category_alias},#{create_user},#{create_time},#{update_time})")
    void add(Category category);

    @Select("select * from category where create_user = #{userId}")
    List<Category> list(Integer userId);

    @Select("select * from category where id = #{id}")
    Category findById(Integer id);

    @Update("update category set category_name=#{category_name},category_alias=#{category_alias},update_time=#{update_time} where id=#{id}")
    void update(Category category);

    int delete(Integer id);
}
