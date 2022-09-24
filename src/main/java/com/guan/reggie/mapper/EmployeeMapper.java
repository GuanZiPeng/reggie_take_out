package com.guan.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guan.reggie.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

/**
 * 员工映射器
 *
 * @author GeZ
 * @date 2022/09/22
 */
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
