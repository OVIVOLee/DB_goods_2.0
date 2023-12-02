package com.keyi.db_goods.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class GoodStockVo implements Serializable {
    private Integer gid;
    private String goodName;
    private Double goodPrice;
    private String goodPlace;

    private Integer stockNum;
}
