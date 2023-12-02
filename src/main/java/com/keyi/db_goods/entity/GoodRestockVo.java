package com.keyi.db_goods.entity;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
public class GoodRestockVo implements Serializable {
    private Integer restockId;
    private Integer goodId;
    private Integer restockNum;
    private Double restockPrice;
    private Double restockSum;
    private Timestamp restockDate;

    private String goodName;
}
