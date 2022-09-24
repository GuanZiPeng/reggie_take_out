package com.guan.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guan.reggie.entity.Employee;
import com.guan.reggie.mapper.EmployeeMapper;
import com.guan.reggie.service.EmployeeService;
import org.springframework.stereotype.Service;

/**
 * 员工服务impl
 *
 * @author GeZ
 * @date 2022/09/22
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService{
}
