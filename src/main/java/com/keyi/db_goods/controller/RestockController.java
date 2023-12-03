package com.keyi.db_goods.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.keyi.db_goods.entity.Good;
import com.keyi.db_goods.entity.GoodRestockVo;
import com.keyi.db_goods.entity.Restock;
import com.keyi.db_goods.service.GoodService;
import com.keyi.db_goods.service.RestockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/restock")
public class RestockController {
    public static final Integer numError = -1;// 商品数量错误
    public static final Integer priceError = -2;// 商品价格错误
    public static final Integer ExistError = -3;// 存在错误
    public static final Integer True = 1;
    @Autowired
    private RestockService restockService;
    @Autowired
    private GoodService goodService;

    // 1、增加
    @PostMapping
    public Integer save(@RequestBody Restock restock) {
        Integer flag = True;
        if (restock.getGoodId() == null || restock.getRestockNum() == null ||
                restock.getRestockPrice() == null)
            flag = ExistError;
        else {
            if (restock.getRestockNum() <= 0)
                flag = numError;
            if (restock.getRestockPrice() <= 0)
                flag = priceError;

            QueryWrapper<Good> wrapper = new QueryWrapper<>();
            wrapper.eq("gid", restock.getGoodId());
            if (!goodService.exists(wrapper))
                flag = ExistError;


            if (flag == True)
                restockService.save(restock);
        }
        return flag;
    }

    // 2、删除
    @DeleteMapping("/id/{id}")
    public boolean deleteById(@PathVariable Integer id) {
        QueryWrapper<Restock> wrapper = new QueryWrapper<>();
        wrapper.eq("restockId", id);
        return restockService.remove(wrapper);
    }

    // 3、修改
    @PostMapping("/update")
    public boolean update(@RequestBody Restock restock) {
        QueryWrapper<Restock> wrapper = new QueryWrapper<>();
        wrapper.eq("restockId", restock.getRestockId());
        if (restockService.exists(wrapper))
            return restockService.updateById(restock);
        return false;
    }

    // 4、查询
    @GetMapping("/")
    public List<Restock> getRestockAll() {
        return restockService.list();
    }

    // 返回值包括 restockId、goodId、restockNum、restockPrice、restockSum、restockDate、goodName
    @GetMapping("/page")
    public Page<GoodRestockVo> getPage(@RequestParam Integer pageNum,
                                       @RequestParam Integer pageSize,
                                       @RequestParam(defaultValue = "") String restockId,
                                       @RequestParam(defaultValue = "") String goodName) {
        Page<GoodRestockVo> iPage = new Page<>(pageNum, pageSize);
        return restockService.getPageVo(iPage, restockId, goodName);
    }
}
