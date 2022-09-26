package com.guan.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guan.reggie.entity.Orders;

/**
 * 订单服务
 *
 * @author GeZ
 * @date 2022/09/26
 */
public interface OrderService extends IService<Orders> {

    //用户下单
    void submit(Orders order);
}
