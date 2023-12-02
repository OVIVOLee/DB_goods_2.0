package com.keyi.db_goods.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keyi.db_goods.entity.GoodRestockVo;
import com.keyi.db_goods.entity.GoodStockVo;
import com.keyi.db_goods.entity.Restock;
import com.keyi.db_goods.mapper.RestockMapper;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;

@Service
public class RestockService extends ServiceImpl<RestockMapper, Restock> {
    @Resource
    private RestockMapper restockMapper;

    public Page<GoodRestockVo> getPageVo(Page<GoodRestockVo> iPage, String restockId, String goodName) {
        if ("".equals(goodName))
            goodName = "%";
        else
            goodName = "%" + goodName + "%";

        Integer reId= null;
        if (!"".equals(restockId)) {
            if (NumberUtils.isParsable(restockId)) {

                reId = Integer.valueOf(restockId);
            } else {
                reId = -1;
            }
            return restockMapper.getPageVo1(iPage, reId, goodName);
        } else {
            return restockMapper.getPageVo2(iPage, goodName);
        }
    }
}
