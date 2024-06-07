package com.example.math_hub.controller;

import com.example.math_hub.pojo.Category;
import com.example.math_hub.pojo.Result;
import com.example.math_hub.service.ArticleService;
import com.example.math_hub.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ArticleService articleService;
    @PostMapping
    public Result add(@RequestBody @Validated(Category.Add.class) Category category){
        categoryService.add(category);
        return Result.success();
    }
    @GetMapping
    public Result<List<Category>> list(){
       List<Category> categoryList = categoryService.list();
       return Result.success(categoryList);
    }

    @GetMapping("/detail")
    public Result<Category> detail(Integer id){
        Category c_id = categoryService.findById(id);
        return Result.success(c_id);
}
    @PutMapping
    public Result update(@RequestBody @Validated(Category.Update.class) Category category){
        categoryService.update(category);
        return Result.success();
    }

    @DeleteMapping
    public Result delete(@RequestParam Integer id){
        try {
            categoryService.delete(id);

            // 如果删除成功，且没有抛出异常，则说明没有关联文章
            return Result.success("删除成功!");

        } catch (DataIntegrityViolationException e) {
            // 捕获外键约束异常
            return Result.error("该分类下存在文章，无法删除！");

        } catch (Exception e) {
            // 捕获其他异常
            return Result.error("删除分类失败,可能是因为根本没有这个分类哟(_^_)");
        }
    }
}
