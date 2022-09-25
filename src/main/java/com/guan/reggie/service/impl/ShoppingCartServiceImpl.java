package com.guan.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guan.reggie.entity.ShoppingCart;
import com.guan.reggie.mapper.ShoppingCartMapper;
import com.guan.reggie.service.ShoppingCartService;
import org.springframework.stereotype.Service;

/**
 * 购物车服务impl
 *
 * @author GeZ
 * @date 2022/09/25
 */
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}
