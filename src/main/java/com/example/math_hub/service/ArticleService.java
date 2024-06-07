package com.example.math_hub.service;

import com.example.math_hub.pojo.Article;
import com.example.math_hub.pojo.PageBean;

public interface ArticleService {
    void add(Article article);

    PageBean<Article> list(Integer pageNum, Integer pageSize, Integer category_id, String state);


    int existsByCategoryId(Integer id);

    void update(Article article);

    Article findById(Integer id);

    void delete(Integer id);
}
