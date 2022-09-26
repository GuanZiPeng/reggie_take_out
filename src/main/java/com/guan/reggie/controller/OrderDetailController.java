package com.guan.reggie.controller;

import com.guan.reggie.common.R;
import com.guan.reggie.service.OrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.*;

/**
 * 订单详细信息有关控制器
 *
 * @author GeZ
 * @date 2022/09/26
 */
@RestController
@RequestMapping("/orderDetail")
public class OrderDetailController {

    @Autowired
    private OrderDetailService orderDetailService;

}
