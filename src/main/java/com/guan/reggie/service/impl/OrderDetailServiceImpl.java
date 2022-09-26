package com.guan.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guan.reggie.entity.OrderDetail;
import com.guan.reggie.mapper.OrderDetailMapper;
import com.guan.reggie.service.OrderDetailService;
import org.springframework.stereotype.Service;

/**
 * 订单细节服务impl
 *
 * @author GeZ
 * @date 2022/09/26
 */
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
