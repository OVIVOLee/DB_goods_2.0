package com.keyi.db_goods.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keyi.db_goods.entity.Good;
import com.keyi.db_goods.entity.GoodStockVo;
import com.keyi.db_goods.mapper.GoodMapper;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;

@Service
public class GoodService extends ServiceImpl<GoodMapper, Good> {

    @Resource
    private GoodMapper goodMapper;

    public Page<GoodStockVo> getPageVo(Page<GoodStockVo> iPage, String goodId, String goodName, String goodPlace) {
        if ("".equals(goodName))
            goodName = "%";
        else
            goodName = "%" + goodName + "%";

        if ("".equals(goodPlace))
            goodPlace = "%";
        else
            goodPlace = "%" + goodPlace + "%";

        Integer gid = null;
        if (!"".equals(goodId)) {
            if (NumberUtils.isParsable(goodId)) {

                gid = Integer.valueOf(goodId);
            } else {
                gid = -1;
            }
            return goodMapper.getPageVo1(iPage, gid, goodName, goodPlace);
        } else {
            return goodMapper.getPageVo2(iPage, goodName, goodPlace);
        }
    }

}
