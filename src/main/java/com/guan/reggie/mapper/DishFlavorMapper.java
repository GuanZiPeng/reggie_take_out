package com.guan.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guan.reggie.entity.DishFlavor;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

/**
 * 菜味道映射器
 *
 * @author GeZ
 * @date 2022/09/23
 */
@Mapper
public interface DishFlavorMapper extends BaseMapper<DishFlavor> {

    //删除口味与菜品关联关系
    @Delete("delete from dish_flavor where dish_id = #{id}")
    void removeByDish(String id);
}
