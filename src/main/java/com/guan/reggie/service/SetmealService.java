package com.guan.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guan.reggie.dto.SetmealDto;
import com.guan.reggie.entity.Setmeal;

/**
 * 套餐服务
 *
 * @author GeZ
 * @date 2022/09/23
 */
public interface SetmealService extends IService<Setmeal> {
    //保存套餐信息
    void saveWithDish(SetmealDto setmealDto);

    //查询套餐信息根据id
    SetmealDto getByIdWithDish(Long id);

    //修改套餐
    void editSetmeal(SetmealDto setmealDto);

    //删除套餐
    void deleteById(String[] split);

}
