package com.keyi.db_goods.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keyi.db_goods.entity.Stock;
import com.keyi.db_goods.mapper.StockMapper;
import org.springframework.stereotype.Service;

@Service
public class StockService extends ServiceImpl<StockMapper, Stock> {
}
