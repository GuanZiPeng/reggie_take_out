package com.guan.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guan.reggie.common.CustomException;
import com.guan.reggie.entity.Category;
import com.guan.reggie.entity.Dish;
import com.guan.reggie.entity.Setmeal;
import com.guan.reggie.mapper.CategoryMapper;
import com.guan.reggie.service.CategoryService;
import com.guan.reggie.service.DishService;
import com.guan.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 分类服务impl
 *
 * @author GeZ
 * @date 2022/09/22
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    //删除分类
    @Override
    public void remove(Long ids) {
        //查询此分类是否关联菜品，如果已经关联，抛出异常
        LambdaQueryWrapper<Dish> dishQueryWrap = new LambdaQueryWrapper<>();
        //添加查询条件
        dishQueryWrap.eq(Dish::getCategoryId, ids);
        int count = dishService.count(dishQueryWrap);
        //进行判断
        if (count > 0) {
            //已经关联了菜品
            throw new CustomException("当前分类下关联了菜品不能删除");
        }
        //查询此分类是否关联套餐，如果已经关联，抛出异常
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //添加查询条件
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId, ids);
        count = setmealService.count(setmealLambdaQueryWrapper);
        //判断
        if (count > 0) {
            //已经关联了套餐
            throw new CustomException("当前分类下关联了套餐不能删除");

        }
        //正常删除分类
        super.removeById(ids);
    }
}
