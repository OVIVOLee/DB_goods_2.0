package com.keyi.db_goods.entity;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
public class SaleCGVo implements Serializable {
    private Integer saleId;
    private Integer goodId;
    private Integer clientId;
    private Integer saleSum;
    private Integer saleNum;
    private Double salePrice;
    private Timestamp saleDate;

    private String goodName;
    private String clientName;
}
