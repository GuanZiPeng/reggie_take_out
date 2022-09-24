package com.guan.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guan.reggie.dto.DishDto;
import com.guan.reggie.entity.Dish;

/**
 * 菜品服务
 *
 * @author GeZ
 * @date 2022/09/23
 */
public interface DishService extends IService<Dish> {

    //新增菜品，同时插入菜品对应的口味数据，需要操作两张表，dish、dish_flavor
    public void saveWithFlavor(DishDto dishDto);

    //删除菜品
    void deleteByDishId(String[] ids);

    //修改菜品状态
    void upStatus(String[] split, int status);

    //根据id查询菜品信息以及口味信息
    DishDto getByIdWithFlavor(Long id);

    //修改菜品
    void updateWithFlavor(DishDto dishDto);
}
