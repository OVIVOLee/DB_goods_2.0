package com.keyi.db_goods.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
@TableName(value = "restock")
public class Restock {
    @TableId(value = "restockId",type = IdType.AUTO)
    private Integer restockId;
    @TableField(value = "goodId")
    private Integer goodId;
    @TableField(value = "restockNum")
    private Integer restockNum;
    @TableField(value = "restockPrice")
    private Double restockPrice;
    @TableField(value = "restockSum")
    private Double restockSum;
    @TableField(value = "restockDate")
    private Timestamp restockDate;

    @TableField(exist = false)
    private List<Good> goods;
}
