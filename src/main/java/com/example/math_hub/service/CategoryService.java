package com.example.math_hub.service;

import com.example.math_hub.pojo.Category;

import java.util.List;

public interface CategoryService {
    void add(Category category);

    List<Category> list();

    Category findById(Integer id);

    void update(Category category);

    Category delete(Integer id);
}
