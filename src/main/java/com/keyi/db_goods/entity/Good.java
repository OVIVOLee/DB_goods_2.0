package com.keyi.db_goods.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.List;

@Data
@TableName(value = "goods")
public class Good {
    @TableId(value = "gid",type = IdType.AUTO)
    private Integer gid;
    @TableField(value = "goodName")
    private String goodName;
    @TableField(value = "goodPrice")
    private Double goodPrice;
    @TableField(value = "goodPlace")
    private String goodPlace;

    @TableField(exist = false)
    private List<Stock> stocks;
}
