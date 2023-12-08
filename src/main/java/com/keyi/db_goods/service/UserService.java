package com.keyi.db_goods.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keyi.db_goods.entity.dto.UserDTO;
import com.keyi.db_goods.mapper.UserMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService extends ServiceImpl<UserMapper, UserDTO> {
    public boolean login(UserDTO userDTO) {
        QueryWrapper<UserDTO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userName", userDTO.getUserName());
        queryWrapper.eq("password", userDTO.getPassword());
        List<UserDTO> list = list(queryWrapper);
        return list.size() != 0;
    }
}
