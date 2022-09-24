package com.guan.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guan.reggie.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户映射器
 *
 * @author GeZ
 * @date 2022/09/24
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
