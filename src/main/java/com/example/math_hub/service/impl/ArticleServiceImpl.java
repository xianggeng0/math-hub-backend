package com.example.math_hub.service.impl;

import com.example.math_hub.mapper.ArticleMapper;
import com.example.math_hub.pojo.Article;
import com.example.math_hub.pojo.PageBean;
import com.example.math_hub.service.ArticleService;
import com.example.math_hub.utils.ThreadLocalUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;
    @Override
    public void add(Article article) {
        article.setCreate_time(LocalDateTime.now());
        article.setUpdate_time(LocalDateTime.now());
        Map<String,Object> map = ThreadLocalUtil.get();
        Integer user_id = (Integer) map.get("id");
        article.setCreate_user(user_id);
       articleMapper.add(article);
    }

    @Override
    public PageBean<Article> list(Integer pageNum, Integer pageSize, Integer category_id, String state) {
        PageBean<Article> pageBean = new PageBean<>();
        PageHelper.startPage(pageNum,pageSize);
        Map<String,Object> map = ThreadLocalUtil.get();
        Integer user_id = (Integer) map.get("id");
        List<Article> a_list = articleMapper.list(user_id, category_id, state);
        Page<Article> p = (Page<Article>) a_list;
        pageBean.setTotal(p.getTotal());
        pageBean.setItems(p.getResult());
        return pageBean;
    }

    @Override
    public int existsByCategoryId(Integer categoryId) {
        return articleMapper.existsByCategoryId(categoryId) ;
    }

    @Override
    public void update(Article article) {
        article.setUpdate_time(LocalDateTime.now());
        articleMapper.update(article);
    }

    @Override
    public Article findById(Integer id) {
        Article a = articleMapper.findById(id);
        return a;
    }

    @Override
    public void delete(final Integer id) {
        articleMapper.delete(id);
    }

}
