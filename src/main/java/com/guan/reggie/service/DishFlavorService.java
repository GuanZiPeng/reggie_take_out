package com.guan.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guan.reggie.entity.DishFlavor;

/**
 * 菜味道服务
 *
 * @author GeZ
 * @date 2022/09/23
 */
public interface DishFlavorService extends IService<DishFlavor> {
    //删除菜品与口味关联信息
    void removeByDish(String id);
}
