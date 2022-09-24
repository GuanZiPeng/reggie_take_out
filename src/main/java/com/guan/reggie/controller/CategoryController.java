package com.guan.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guan.reggie.common.R;
import com.guan.reggie.entity.Category;
import com.guan.reggie.entity.Employee;
import com.guan.reggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 分类控制器
 *
 * @author GeZ
 * @date 2022/09/22
 */
@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    //新增分类
    @PostMapping
    public R<String> save(@RequestBody Category category) {
//        //查询是否有此分类
//        LambdaQueryWrapper<Category>queryWrap=new LambdaQueryWrapper<>();
//        queryWrap.eq(Category ::getName,category.getName());
//        Category one = categoryService.getOne(queryWrap);
//        if (one != null) {
//            return R.error("此分类已存在！");
//        }
        //添加分类
//        try {
//            categoryService.save(category);
//            return R.success("添加分类成功！");
//        } catch (Exception e) {
//            e.printStackTrace();
//            return R.error("添加分类失败！");
//        }
        categoryService.save(category);
        return R.success("添加分类成功！");
    }

    //分类分页查询
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize) {
        //构造分页构造器
        Page<Category> pageInfo=new Page(page,pageSize);
        //构造条件构造器
        LambdaQueryWrapper<Category> queryWrap=new LambdaQueryWrapper();
        //添加排序条件
        queryWrap.orderByAsc(Category ::getSort);
        //执行查询
        categoryService.page(pageInfo, queryWrap);
        return R.success(pageInfo);
    }

    //根据id删除分类
    @DeleteMapping
    public R<String> deleteById(Long ids){
//        try {
//            categoryService.remove(ids);
//            return R.success("删除分类成功！");
//        } catch (Exception e) {
//            e.printStackTrace();
//            return R.error("删除分类失败！");
//        }
        categoryService.remove(ids);
        return R.success("删除分类成功！");
    }

    //修改分类
    @PutMapping
    public R<String> update(@RequestBody Category category){
        try {
            categoryService.updateById(category);
            return R.success("修改分类成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("修改分类失败！");
        }
    }

    //根据类型查询菜品分类
    @GetMapping("/list")
    public R<List<Category>> list(Category category) {

        LambdaQueryWrapper<Category>queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(category.getType()!=null,Category::getType,category.getType());
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        try {
            List<Category> list = categoryService.list(queryWrapper);
            return R.success(list);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("查询菜品分类失败！");
        }

    }

}
