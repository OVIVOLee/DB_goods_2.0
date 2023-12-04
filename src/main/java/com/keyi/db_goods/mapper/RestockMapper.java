package com.keyi.db_goods.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.keyi.db_goods.entity.GoodRestockVo;
import com.keyi.db_goods.entity.Restock;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;


public interface RestockMapper extends BaseMapper<Restock> {
    @Update("ALTER TABLE restock AUTO_INCREMENT = 1;")
    void resetAuto();

    @Select("SELECT a.*,b.goodName from restock a LEFT JOIN goods b ON a.goodId=b.gid " +
            "WHERE a.restockId = #{restockId} AND b.goodName LIKE #{goodName}")
    Page<GoodRestockVo> getPageVo1(Page<GoodRestockVo> iPage, Integer restockId, String goodName);

    @Select("SELECT a.*,b.goodName from restock a LEFT JOIN goods b ON a.goodId=b.gid " +
            "WHERE b.goodName LIKE #{goodName}")
    Page<GoodRestockVo> getPageVo2(Page<GoodRestockVo> iPage, String goodName);
}
