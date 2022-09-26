package com.guan.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guan.reggie.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.core.annotation.Order;

/**
 * 订单映射器
 *
 * @author GeZ
 * @date 2022/09/26
 */
@Mapper
public interface OrderMapper extends BaseMapper<Orders> {
}
