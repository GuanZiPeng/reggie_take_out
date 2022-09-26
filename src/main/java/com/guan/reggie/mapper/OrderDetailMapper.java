package com.guan.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guan.reggie.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单细节映射器
 *
 * @author GeZ
 * @date 2022/09/26
 */
@Mapper
public interface OrderDetailMapper extends BaseMapper<OrderDetail> {
}
