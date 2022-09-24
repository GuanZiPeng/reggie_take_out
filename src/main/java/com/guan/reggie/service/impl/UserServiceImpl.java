package com.guan.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guan.reggie.entity.User;
import com.guan.reggie.mapper.UserMapper;
import com.guan.reggie.service.UserService;
import org.springframework.stereotype.Service;

/**
 * 用户服务impl
 *
 * @author GeZ
 * @date 2022/09/24
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
