package com.guan.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guan.reggie.dto.SetmealDto;
import com.guan.reggie.entity.Setmeal;
import com.guan.reggie.entity.SetmealDish;
import com.guan.reggie.mapper.SetmealMapper;
import com.guan.reggie.service.SetmealDishService;
import com.guan.reggie.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 套餐服务impl
 *
 * @author GeZ
 * @date 2022/09/23
 */
@Service
@Transactional
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;


    //保存套餐信息
    @Override
    public void saveWithDish(SetmealDto setmealDto) {
        //保存套餐基本信息
        this.save(setmealDto);

        //处理数据添加套餐id
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        for (SetmealDish setmealDish : setmealDishes) {
            setmealDish.setSetmealId(setmealDto.getId());
        }
        //保存套餐与菜品关联信息
        setmealDishService.saveBatch(setmealDishes);

    }

    //根据id查询套餐信息
    @Override
    public SetmealDto getByIdWithDish(Long id) {

        //查询套餐基本信息
        Setmeal byId = this.getById(id);
        //查询套餐与菜品关联信息
        LambdaQueryWrapper<SetmealDish> queryWrap = new LambdaQueryWrapper<>();
        queryWrap.eq(SetmealDish::getSetmealId, byId.getId());
        List<SetmealDish> list = setmealDishService.list(queryWrap);
        //拷贝数据
        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(byId, setmealDto);
        setmealDto.setSetmealDishes(list);
        return setmealDto;
    }

    //修改套餐
    @Override
    public void editSetmeal(SetmealDto setmealDto) {
        //修改套餐基本信息
        this.updateById(setmealDto);
        //修改套餐与菜品关系
        //删除套餐与菜品关系
        LambdaQueryWrapper<SetmealDish> queryWrap = new LambdaQueryWrapper<>();
        queryWrap.eq(SetmealDish::getSetmealId, setmealDto.getId());
        setmealDishService.remove(queryWrap);
        //更新菜品与套餐关系
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        for (SetmealDish setmealDish : setmealDishes) {
            setmealDish.setSetmealId(setmealDto.getId());
        }
        setmealDishService.saveBatch(setmealDishes);
    }

    //删除套餐
    @Override
    public void deleteById(String[] split) {
        for (String id : split) {
            //首先删除套餐与菜品关联关系
            LambdaQueryWrapper<SetmealDish> queryWrap=new LambdaQueryWrapper<>();
            queryWrap.eq(SetmealDish::getSetmealId,id);
            setmealDishService.remove(queryWrap);
            //然后删除套餐
            this.removeById(id);
        }
    }

}
