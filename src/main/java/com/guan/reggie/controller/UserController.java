package com.guan.reggie.controller;

import com.guan.reggie.common.R;
import com.guan.reggie.entity.User;
import com.guan.reggie.service.UserService;
import com.guan.reggie.utils.SMSUtils;
import com.guan.reggie.utils.ValidateCodeUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * 用户控制器
 *
 * @author GeZ
 * @date 2022/09/24
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session){
        //获取手机号
        String phone = user.getPhone();
        //生成验证码
        if (StringUtils.isNotBlank(phone)){
            String validateCode = String.valueOf(ValidateCodeUtils.generateValidateCode(4));
            //发送验证码
            SMSUtils.sendMessage("","",phone,validateCode);
            //将验证码存到session中
            session.setAttribute(phone,validateCode);
            return R.success("手机验证码发送成功");
        }
        return R.error("手机验证码发送失败");
    }

}
