package com.guan.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.guan.reggie.common.BaseContext;
import com.guan.reggie.common.R;
import com.guan.reggie.entity.ShoppingCart;
import com.guan.reggie.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 购物车控制器
 *
 * @author GeZ
 * @date 2022/09/25
 */
@Slf4j
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    //添加购物车
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){
        log.info("购物车数据：{}",shoppingCart);
        //设置用户id
        shoppingCart.setUserId(BaseContext.getCurrentId());
        //判断用户是否已经点了该菜品，如果是改变菜品数量，不是保存
        Long dishId = shoppingCart.getDishId();
        //添加查询条件
        LambdaQueryWrapper<ShoppingCart>queryWrap=new LambdaQueryWrapper<>();

        queryWrap.eq(ShoppingCart::getUserId,shoppingCart.getUserId());
        if (dishId != null) {
            //添加到购物车的是菜品
            queryWrap.eq(ShoppingCart::getDishId,dishId);
        }else {
            //添加到购物车的是套餐
            queryWrap.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }
        ShoppingCart cartServiceOne = shoppingCartService.getOne(queryWrap);

        //查询当前菜品是否存在
        if (cartServiceOne != null) {
            cartServiceOne.setNumber(cartServiceOne.getNumber()+1);
            try {
                shoppingCartService.updateById(cartServiceOne);
                return R.success(cartServiceOne);
            } catch (Exception e) {
                e.printStackTrace();
                return R.error("添加信息失败！");
            }
        }else {
            try {
                shoppingCart.setNumber(1);
                shoppingCartService.save(shoppingCart);
                return R.success(shoppingCart);
            } catch (Exception e) {
                e.printStackTrace();
                return R.error("保存信息失败！");
            }
        }
    }
}
