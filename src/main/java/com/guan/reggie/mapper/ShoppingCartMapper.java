package com.guan.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guan.reggie.entity.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;

/**
 * 购物车映射器
 *
 * @author GeZ
 * @date 2022/09/25
 */
@Mapper
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {
}
