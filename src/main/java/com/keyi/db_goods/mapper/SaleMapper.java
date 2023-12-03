package com.keyi.db_goods.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.keyi.db_goods.entity.GoodRestockVo;
import com.keyi.db_goods.entity.Sale;
import com.keyi.db_goods.entity.SaleCGVo;
import org.apache.ibatis.annotations.Select;

public interface SaleMapper extends BaseMapper<Sale> {
    @Select("SELECT a.*,b.goodName,c.clientName from sales a, goods b, clients c " +
            "WHERE a.goodId=b.gid AND a.clientId=c.cid " +
            "AND a.saleId = #{saleId} " +
            "AND b.goodName LIKE #{goodName} " +
            "AND c.clientName LIKE #{clientName}")
    Page<SaleCGVo> getPageVo1(Page<SaleCGVo> iPage, Integer saleId,
                                   String goodName,String clientName);

    @Select("SELECT a.*,b.goodName,c.clientName from sales a, goods b, clients c " +
            "WHERE a.goodId=b.gid AND a.clientId=c.cid " +
            "AND b.goodName LIKE #{goodName} " +
            "AND c.clientName LIKE #{clientName}")
    Page<SaleCGVo> getPageVo2(Page<SaleCGVo> iPage,
                              String goodName, String clientName);

}
