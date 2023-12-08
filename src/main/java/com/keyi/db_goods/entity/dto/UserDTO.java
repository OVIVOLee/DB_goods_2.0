package com.keyi.db_goods.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "users")
public class UserDTO {
    @TableId(value = "uid", type = IdType.AUTO)
    private Integer uid;
    @TableField(value = "userName")
    private String userName;
    @TableField(value = "password")
    private String password;
}
