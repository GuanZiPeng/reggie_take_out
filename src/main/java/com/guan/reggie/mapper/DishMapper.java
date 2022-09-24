package com.guan.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guan.reggie.entity.Dish;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

/**
 * 菜品映射器
 *
 * @author GeZ
 * @date 2022/09/23
 */
@Mapper
public interface DishMapper extends BaseMapper<Dish> {

    @Update("update dish set status=#{status} where id =#{id}")
    void upStatus(String id, int status);
}
