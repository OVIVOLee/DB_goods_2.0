package com.keyi.db_goods.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.keyi.db_goods.entity.*;
import com.keyi.db_goods.service.ClientService;
import com.keyi.db_goods.service.GoodService;
import com.keyi.db_goods.service.SaleService;
import com.keyi.db_goods.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sale")
public class SaleController {
    public static final Integer numError = -1;// 商品数量错误
    public static final Integer priceError = -2;// 商品价格错误
    public static final Integer DataError = -3;// 数据错误
    public static final Integer True = 1;
    @Autowired
    private SaleService saleService;
    @Autowired
    private GoodService goodService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private StockService stockService;

    // 1、增加
    @PostMapping
    public Integer save(@RequestBody Sale sale) {
        Integer flag = True;
        if (sale.getGoodId() == null || sale.getClientId() == null ||
                sale.getSaleNum() == null || sale.getSalePrice() == null)
            flag = DataError;

        else {
            if (sale.getSaleNum() <= 0)    // 判断一：商品数量大于零
                flag = numError;
            if (sale.getSalePrice() <= 0)  // 判断二：商品价格大于零
                flag = priceError;

            // 判断三：gid存在
            QueryWrapper<Good> wrapper = new QueryWrapper<>();
            wrapper.eq("gid", sale.getGoodId());
            if (!goodService.exists(wrapper))
                flag = DataError;
            // 判断四：clientId存在
            QueryWrapper<Client> wrapper1 = new QueryWrapper<>();
            wrapper1.eq("cid", sale.getClientId());
            if (!clientService.exists(wrapper1))
                flag = DataError;
            // 判断五：商品库存
            QueryWrapper<Stock> wrapper2 = new QueryWrapper<>();
            wrapper2.eq("goodId",sale.getGoodId());
            wrapper2.gt("stockNum",sale.getSaleNum());
            if(!stockService.exists(wrapper2))
                flag = DataError;

            if (flag == True)
                saleService.save(sale);
        }
        return flag;
    }

    // 2、删除
    @DeleteMapping("/id/{id}")
    public boolean deleteById(@PathVariable Integer id) {
        QueryWrapper<Sale> wrapper = new QueryWrapper<>();
        wrapper.eq("saleId", id);
        return saleService.remove(wrapper);
    }

    // 3、修改
    // 4、查询
    @GetMapping("/")
    public List<Sale> getSaleAll() {
        return saleService.list();
    }

    // 返回值包括 saleId、goodId、clientId、saleSum、saleNum、salePrice、saleDate
    //          goodName、clientName
    @GetMapping("/page")
    public Page<SaleCGVo> getPage(@RequestParam Integer pageNum,
                                  @RequestParam Integer pageSize,
                                  @RequestParam(defaultValue = "") String saleId,
//                                       @RequestParam(defaultValue = "") String goodId,
//                                       @RequestParam(defaultValue = "") String clientId,
                                  @RequestParam(defaultValue = "") String goodName,
                                  @RequestParam(defaultValue = "") String clientName) {
        Page<SaleCGVo> iPage = new Page<>(pageNum, pageSize);
        return saleService.getPageVo(iPage, saleId, goodName, clientName);
    }
}
