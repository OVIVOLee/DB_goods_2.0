package com.keyi.db_goods.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.Quarter;
import com.keyi.db_goods.common.Result;
import com.keyi.db_goods.entity.Good;
import com.keyi.db_goods.entity.Restock;
import com.keyi.db_goods.entity.Sale;
import com.keyi.db_goods.service.ClientService;
import com.keyi.db_goods.service.GoodService;
import com.keyi.db_goods.service.RestockService;
import com.keyi.db_goods.service.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/echarts")
public class EchartsController {
    @Autowired
    private SaleService saleService;
    @Autowired
    private GoodService goodService;
    @Autowired
    private RestockService restockService;
    @Autowired
    private ClientService clientService;

    // 饼图：四个季度的营业额
    @GetMapping("/one")
    public Result EchatsOne() {
        List<Sale> sales = saleService.list();
        double q1 = 0;   // 第一季度
        double q2 = 0;   // 第二季度
        double q3 = 0;   // 第三季度
        double q4 = 0;   // 第四季度
        for (Sale sale : sales) {
            Date saleDate = sale.getSaleDate();
            Quarter quarter = DateUtil.quarterEnum(saleDate);
            switch (quarter) {
                case Q1:
                    q1 += sale.getSaleSum();
                    break;
                case Q2:
                    q2 += sale.getSaleSum();
                    break;
                case Q3:
                    q3 += sale.getSaleSum();
                    break;
                case Q4:
                    q4 += sale.getSaleSum();
                    break;
                default:
                    break;
            }
        }
        return Result.success(CollUtil.newArrayList(q1, q2, q3, q4));
    }

    // 柱状图：每个商品种类的销售量与进货量对比
    @GetMapping("/two")
    public Result EchatsTwo() {
        List<Sale> sales = saleService.list();
        List<Good> goods = goodService.list();
        List<Restock> restocks = restockService.list();
        int count = (int) goodService.count();

        // 名字列表
        String[] nameList = new String[count];
        int[] idList = new int[count];
        int i = 0;
        for (Good good : goods) {
            nameList[i] = good.getGoodName();
            idList[i] = good.getGid();
            i++;
        }

        // 进货量列表
        Map<Integer, Integer> restockMap = new HashMap<>();
        for (Restock restock : restocks) {
            Integer id = restock.getGoodId();
            restockMap.merge(id, restock.getRestockNum(), Integer::sum);
        }
        int[] restockList = new int[count];
        for (int j = 0; j < idList.length; j++) {
            if (restockMap.get(idList[j]) == null)
                restockList[j] = 0;
            else
                restockList[j] = restockMap.get(idList[j]);
        }

        // 销售量列表
        Map<Integer, Integer> saleMap = new HashMap<>();
        for (Sale sale : sales) {
            Integer id = sale.getGoodId();
            saleMap.merge(id, sale.getSaleNum(), Integer::sum);
        }
        int[] saleList = new int[count];
        for (int j = 0; j < idList.length; j++) {
            if (saleMap.get(idList[j]) == null)
                saleList[j] = 0;
            else
                saleList[j] = saleMap.get(idList[j]);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("name", nameList);
        map.put("restock", restockList);
        map.put("sale", saleList);

        System.out.println(nameList);
        System.out.println(restockList);
        System.out.println(saleList);

        return Result.success(map);
    }

    //客户总数量、商品种类数量、营业额
    @GetMapping("/three")
    public Result EchatsThree() {
        List<Sale> sales = saleService.list();

        long goodCount = goodService.count();
        long clientCount = clientService.count();

        Double saleSum = 0.0;
        for (Sale sale : sales) {
            saleSum += sale.getSaleSum();
        }

        return Result.success(CollUtil.newArrayList(clientCount, goodCount, saleSum));
    }
}
