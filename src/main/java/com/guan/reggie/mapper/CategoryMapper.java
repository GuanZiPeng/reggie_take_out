package com.guan.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guan.reggie.entity.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 分类映射器
 *
 * @author GeZ
 * @date 2022/09/22
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
}
