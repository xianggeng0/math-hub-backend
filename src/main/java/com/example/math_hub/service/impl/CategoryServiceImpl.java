package com.example.math_hub.service.impl;

import com.example.math_hub.mapper.CategoryMapper;
import com.example.math_hub.pojo.Category;
import com.example.math_hub.service.ArticleService;
import com.example.math_hub.service.CategoryService;
import com.example.math_hub.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private ArticleService articleService;

    @Override
    public void add(Category category) {
        category.setCreate_time(LocalDateTime.now());
        category.setUpdate_time(LocalDateTime.now());

        Map<String,Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        category.setCreate_user(userId);
        categoryMapper.add(category);
    }

    @Override
    public List<Category> list() {
        Map<String,Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        return categoryMapper.list(userId);
    }

    @Override
    public Category findById(Integer id) {
        Category c_id = categoryMapper.findById(id);
        return c_id;
    }

    @Override
    public void update(Category category) {
        category.setUpdate_time(LocalDateTime.now());
        categoryMapper.update(category);
    }

    @Override
    @Transactional // 添加事务，保证数据一致性
    public Category delete(Integer id) {
        // 检查是否存在关联文章
        if (articleService.existsByCategoryId(id)>0) {
            throw new DataIntegrityViolationException("该分类下存在文章，无法删除！");
        }

        // 不存在关联文章，继续删除分类
        int rowsAffected = categoryMapper.delete(id);
        if (rowsAffected > 0) {
            return null; // 删除成功，返回 null 或其他你需要的处理方式
        } else {
            // 处理删除失败的情况，例如抛出异常或返回错误信息
            throw new RuntimeException("删除分类失败");
        }
    }
}
