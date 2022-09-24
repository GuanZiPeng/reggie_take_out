package com.guan.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guan.reggie.entity.DishFlavor;
import com.guan.reggie.mapper.DishFlavorMapper;
import com.guan.reggie.service.DishFlavorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 菜味道服务impl
 *
 * @author GeZ
 * @date 2022/09/23
 */
@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {

    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    //删除菜品与口味关联信息
    @Override
    public void removeByDish(String id) {
        dishFlavorMapper.removeByDish(id);
    }
}
