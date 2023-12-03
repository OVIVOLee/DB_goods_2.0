package com.keyi.db_goods.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
@TableName(value = "sales")
public class Sale {
    @TableId(value = "saleId", type = IdType.AUTO)
    private Integer saleId;
    @TableField(value = "goodId")
    private Integer goodId;
    @TableField(value = "clientId")
    private Integer clientId;
    @TableField(value = "saleSum")
    private Integer saleSum;
    @TableField(value = "saleNum")
    private Integer saleNum;
    @TableField(value = "salePrice")
    private Double salePrice;
    @TableField(value = "saleDate")
    private Timestamp saleDate;

    @TableField(exist = false)
    private List<Good> goods;
    @TableField(exist = false)
    private List<Client> clients;
}
