package com.example.math_hub.mapper;

import com.example.math_hub.pojo.Article;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ArticleMapper {
    @Insert("insert into article(title,content,cover_img,state,category_id,create_user,create_time,update_time)" +
            "values(#{title},#{content},#{cover_img},#{state},#{category_id},#{create_user},#{create_time},#{update_time})")
    void add(Article article);

    List<Article> list(Integer user_id, Integer category_id, String state);

    int existsByCategoryId(Integer category_id);

    @Update("update article set title=#{title},content=#{content},cover_img=#{cover_img},state=#{state},category_id=#{category_id},update_time=#{update_time} where id=#{id}")
    void update(Article article);

    @Select("select * from article where id=#{id}")
    Article findById(Integer id);

    @Delete("delete from article where id=#{id}")
    void delete(Integer id);
}
