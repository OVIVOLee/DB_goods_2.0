package com.keyi.db_goods.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keyi.db_goods.entity.Sale;
import com.keyi.db_goods.entity.SaleCGVo;
import com.keyi.db_goods.mapper.SaleMapper;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;

@Service
public class SaleService extends ServiceImpl<SaleMapper, Sale> {

    @Resource
    private SaleMapper saleMapper;

    public Page<SaleCGVo> getPageVo(Page<SaleCGVo> iPage,
                                    String saleId,
                                    String goodName,
                                    String clientName) {
        if ("".equals(goodName)) {
            goodName = "%";
        } else {
            goodName = "%" + goodName + "%";
        }

        if ("".equals(clientName)) {
            clientName = "%";
        } else {
            clientName = "%" + clientName + "%";
        }


        Integer saleId1 = null;
        if (!"".equals(saleId)) {
            if (NumberUtils.isParsable(saleId)) {

                saleId1 = Integer.valueOf(saleId);
            } else {
                saleId1 = -1;
            }
            return saleMapper.getPageVo1(iPage, saleId1, goodName,clientName);
        } else {
            return saleMapper.getPageVo2(iPage, goodName,clientName);
        }
    }
}
