package com.guan.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guan.reggie.dto.DishDto;
import com.guan.reggie.entity.Dish;
import com.guan.reggie.entity.DishFlavor;
import com.guan.reggie.mapper.DishMapper;
import com.guan.reggie.service.DishFlavorService;
import com.guan.reggie.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 菜品服务impl
 *
 * @author GeZ
 * @date 2022/09/23
 */
@Service
@Transactional
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private DishMapper dishMapper;

    //新增菜品，同时保存对应的口味数据
    @Override
    public void saveWithFlavor(DishDto dishDto) {

        //保存菜品口味基本信息
        this.save(dishDto);
        //保存菜品id
        Long dishId = dishDto.getId();
        List<DishFlavor> flavors = dishDto.getFlavors();
        for (DishFlavor flavor : flavors) {
            flavor.setDishId(dishId);
        }
        //保存菜品口味信息
        dishFlavorService.saveBatch(flavors);
    }

    //删除菜品
    @Override
    public void deleteByDishId(String[] ids) {
        for (String id : ids) {
            //首先删除口味关联，然后删除菜品
            dishFlavorService.removeByDish(id);
            this.removeById(id);
        }
    }

    //修改套餐状态
    @Override
    public void upStatus(String[] split, int status) {
        for (String id : split) {
            dishMapper.upStatus(id,status);
        }
    }

    //根据id查询菜品信息以及口味信息
    @Override
    public DishDto getByIdWithFlavor(Long id) {
        //查询菜品基本信息
        Dish dish = this.getById(id);
        //查询菜品口味信息
        LambdaQueryWrapper<DishFlavor>queryWrap=new LambdaQueryWrapper<>();
        queryWrap.eq(DishFlavor ::getDishId,dish.getId());
        List<DishFlavor> list = dishFlavorService.list(queryWrap);
        //进行对象拷贝
        DishDto dishDto=new DishDto();
        BeanUtils.copyProperties(dish,dishDto);
        dishDto.setFlavors(list);
        return dishDto;
    }

    //更新菜品信息以及口味信息
    @Override
    public void updateWithFlavor(DishDto dishDto) {

        //更新菜品信息
        this.updateById(dishDto);
        //更新菜品口信息
        //清理之前的口味信息
        LambdaQueryWrapper<DishFlavor>queryWrap=new LambdaQueryWrapper<>();
        queryWrap.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavorService.remove(queryWrap);
        //更新口味信息
        List<DishFlavor> flavors = dishDto.getFlavors();
        for (DishFlavor flavor : flavors) {
            flavor.setDishId(dishDto.getId());
        }
        dishFlavorService.saveBatch(flavors);
    }
}
