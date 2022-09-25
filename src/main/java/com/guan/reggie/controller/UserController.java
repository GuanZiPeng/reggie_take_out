package com.guan.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.guan.reggie.common.R;
import com.guan.reggie.entity.User;
import com.guan.reggie.service.UserService;
import com.guan.reggie.utils.SMSUtils;
import com.guan.reggie.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * 用户控制器
 *
 * @author GeZ
 * @date 2022/09/24
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    //发送手机验证码
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session){
        //获取手机号
        String phone = user.getPhone();
        //生成验证码
        if (StringUtils.isNotBlank(phone)){
            String validateCode = String.valueOf(ValidateCodeUtils.generateValidateCode(4));
            //发送验证码
            //SMSUtils.sendMessage("","",phone,validateCode);
            System.out.println("验证码："+validateCode);
            //将验证码存到session中
            session.setAttribute(phone,validateCode);
            return R.success("手机验证码发送成功");
        }
        return R.error("手机验证码发送失败");
    }

    //用户登录
    @PostMapping("/login")
    public R<User> login(@RequestBody Map user, HttpSession session){
        //获取手机号
        String phone = (String) user.get("phone");
        //获取验证码
        String code = (String) user.get("code");
        //获取session验证码
        Object validateCode = session.getAttribute(phone);
        System.out.println("sessionCode:"+validateCode);
        System.out.println("code:"+code);
        //进行比对
        if (validateCode!=null&&validateCode.equals(code)){
            //判断当前手机号在用户表是否存在
            LambdaQueryWrapper<User> queryWrap=new LambdaQueryWrapper<>();
            queryWrap.eq(User ::getPhone,phone);
            User one = userService.getOne(queryWrap);
            if (one == null) {
                //用户不存在
                one = new User();
                one.setPhone(phone);
                try {
                    userService.save(one);
                } catch (Exception e) {
                    e.printStackTrace();
                    return R.error("保存用户失败！");
                }
            }
            session.setAttribute("user",one.getId());
            return R.success(one);
        }
        return R.error("登录失败！");
    }

}
