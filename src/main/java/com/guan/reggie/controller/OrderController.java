package com.guan.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guan.reggie.common.BaseContext;
import com.guan.reggie.common.R;
import com.guan.reggie.entity.Orders;
import com.guan.reggie.service.OrderService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 订单控制器
 *
 * @author GeZ
 * @date 2022/09/26
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    //用户下单
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders order){
        //设置格式
        order.setOrderTime(LocalDateTime.now());
        order.setUserId(BaseContext.getCurrentId());
        try {
            orderService.submit(order);
            return R.success("支付成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("支付失败！");
        }
    }

    @GetMapping("/page")
    public R<Page> page(Integer page, Integer pageSize, Long number, String beginTime,String endTime){

        //创建分页对象
        Page<Orders> pageInfo = new Page<>(page,pageSize);
        //创建条件对象
        LambdaQueryWrapper<Orders>queryWrap=new LambdaQueryWrapper<>();
        queryWrap.eq(number!=null,Orders ::getNumber,number);
        queryWrap.between(StringUtils.isNotBlank(beginTime)&&StringUtils.isNotBlank(endTime),Orders::getOrderTime,beginTime,endTime);

        try {
            orderService.page(pageInfo,queryWrap);
            return R.success(pageInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("查询订单失败！");
        }
    }

    //修改订单状态
    @PutMapping
    public R<String> status(@RequestBody Orders orders){

        try {
            orderService.updateById(orders);
            return R.success("修改订单成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("修改订单失败！");
        }

    }
}
