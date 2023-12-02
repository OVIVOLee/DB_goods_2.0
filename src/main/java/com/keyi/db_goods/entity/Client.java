package com.keyi.db_goods.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "clients")
public class Client {
    @TableId(value = "cid",type = IdType.AUTO)
    private Integer cid;
    @TableField(value = "clientName")
    private String clientName;
    @TableField(value = "clientMobile")
    private String clientMobile;
    @TableField(value = "clientEmail")
    private String clientEmail;
}
