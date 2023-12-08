package com.keyi.db_goods.controller;

import cn.hutool.core.util.StrUtil;
import com.keyi.db_goods.entity.dto.UserDTO;
import com.keyi.db_goods.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public boolean login(@RequestBody UserDTO userDTO) {
        String userName = userDTO.getUserName();
        String password = userDTO.getPassword();
        if (StrUtil.isBlank(userName) || StrUtil.isBlank(password))
            return false;
        return userService.login(userDTO);
    }
}
