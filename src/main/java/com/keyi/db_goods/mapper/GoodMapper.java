package com.keyi.db_goods.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.keyi.db_goods.entity.Good;
import com.keyi.db_goods.entity.GoodStockVo;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface GoodMapper extends BaseMapper<Good> {

    @Update("ALTER TABLE goods AUTO_INCREMENT = 1;")
    void resetAuto();

    @Select("SELECT a.*,b.stockNum from goods a LEFT JOIN stock b ON a.gid=b.goodId " +
            "WHERE a.gid = #{goodId} AND a.goodName LIKE #{goodName} AND a.goodPlace LIKE #{goodPlace}")
    Page<GoodStockVo> getPageVo1(IPage<GoodStockVo> iPage, Integer goodId, String goodName, String goodPlace);

    @Select("SELECT a.*,b.stockNum from goods a LEFT JOIN stock b ON a.gid=b.goodId " +
            "WHERE a.goodName LIKE #{goodName} AND a.goodPlace LIKE #{goodPlace}")
    Page<GoodStockVo> getPageVo2(IPage<GoodStockVo> iPage, String goodName, String goodPlace);
}
