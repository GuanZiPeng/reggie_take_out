package com.guan.reggie.controller;

import com.guan.reggie.common.R;
import com.guan.reggie.entity.User;
import com.guan.reggie.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public R<String> sendMsg(@RequestBody User user){

        return null;
    }
}
