package com.guan.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guan.reggie.common.R;
import com.guan.reggie.entity.Employee;
import com.guan.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.swing.plaf.ListUI;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 员工控制器
 *
 * @author GeZ
 * @date 2022/09/22
 */
@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    //用户登录
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
        //1、将页面提交的密码password进行md5加密处理
        String password  = DigestUtils.md5DigestAsHex(employee.getPassword().getBytes());
        //2、根据页面提交的用户名username查询数据库
        LambdaQueryWrapper<Employee> queryWrap = new LambdaQueryWrapper<>();
        queryWrap.eq(Employee::getUsername,employee.getUsername());
        Employee one = employeeService.getOne(queryWrap);
        //3、如果没有查询到则返回登录失败结果
        if (one == null) {
            return R.error("没有此用户！");
        }
        //4、密码比对，如果不一致则返回登录失败结果
        if (!one.getPassword().equals(password)){
            return R.error("登录密码错误！");
        }
        //5、查看员工状态，如果为已禁用状态，则返回员工已禁用结果
        if(one.getStatus()==0){
            return R.error("用户已被禁用！");
        }
        //6、登录成功，将员工id存入Session并返回登录成功结果
        request.getSession().setAttribute("employee",one.getId());
        return R.success(one);
    }

    //用户退出
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        //1、清理Session中的用户id
        request.getSession().removeAttribute("employee");
        //2、返回结果
        return R.success("退出成功");
    }

    //添加员工
    @PostMapping
    public R<String> save(HttpServletRequest request,@RequestBody Employee employee){
        //首先查询是否存在该员工
        LambdaQueryWrapper<Employee>queryWrap=new LambdaQueryWrapper<>();
        queryWrap.eq(Employee ::getUsername,employee.getUsername());
        Employee emp = employeeService.getOne(queryWrap);
        if (emp != null) {
            return R.error(employee.getUsername()+"用户已存在！");
        }
        try {
            //设置初始密码，对用户密码进行加密
            employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
            //注册时间
            //employee.setCreateTime(LocalDateTime.now());
            //上传数据时间
            //employee.setUpdateTime(LocalDateTime.now());
            //获取创建者的id
            //Long createUserId = (Long) request.getSession().getAttribute("employee");
            //添加创建者id，更新者id
            //employee.setCreateUser(createUserId);
            //employee.setUpdateUser(createUserId);
            //调用service保存员工
            employeeService.save(employee);
            return R.success("添加员工成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("添加员工失败！");
        }
    }

    //员工分页查询
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){

        //构造分页构造器
        Page pageInfo=new Page(page,pageSize);
        //构造条件构造器
        LambdaQueryWrapper<Employee> queryWrap=new LambdaQueryWrapper();
        //加入判断条件
        queryWrap.like(StringUtils.isNotEmpty(name),Employee::getName,name);
        //添加排序条件
        queryWrap.orderByDesc(Employee ::getUpdateTime);
        //执行查询
        try {
            employeeService.page(pageInfo,queryWrap);
            return R.success(pageInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("员工查询失败！");
        }
    }

    //根据员工id修改账号状态
    @PutMapping
    public R<String> update(HttpServletRequest request,@RequestBody Employee employee){
        //设置更新信息
        //employee.setUpdateTime(LocalDateTime.now());
        //employee.setUpdateUser((Long)request.getSession().getAttribute("employee"));
        try {
            employeeService.updateById(employee);
            return R.success("修改员工状态成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("修改员工状态失败！");
        }
    }

    //根据id查询员工信息
    @GetMapping("/{id}")
    public R<Employee>getById(@PathVariable Long id){
        try {
            Employee byId = employeeService.getById(id);
            return R.success(byId);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("查询用户失败！");
        }
    }
}
