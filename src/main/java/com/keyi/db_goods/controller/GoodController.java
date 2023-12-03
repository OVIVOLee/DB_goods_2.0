package com.keyi.db_goods.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.keyi.db_goods.entity.Good;
import com.keyi.db_goods.entity.GoodStockVo;
import com.keyi.db_goods.service.GoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/good")
public class GoodController {
    @Autowired
    private GoodService goodService;

    public static final int EmptyError = -1; // 缺少必要数据
    public static final int ExistError = -2; // 该商品名已存在
    public static final int True = 1;

    // 1、增加
    @PostMapping
    public int save(@RequestBody Good good) {
        if (good.getGoodName() == null || good.getGoodPrice() == null)
            return EmptyError;
        if (good.getGoodName().isEmpty())
            return EmptyError;
        QueryWrapper<Good> wrapper = new QueryWrapper<>();
        wrapper.eq("goodName", good.getGoodName());
        if (goodService.exists(wrapper))
            return ExistError;
        else{
            goodService.save(good);
            return True;
        }
    }

    // 2、删除
    @DeleteMapping("/id/{id}")
    public boolean deleteById(@PathVariable Integer id) {
        QueryWrapper<Good> wrapper = new QueryWrapper<>();
        wrapper.eq("gid", id);
        return goodService.remove(wrapper);
    }

    @DeleteMapping("/name/{name}")
    public boolean deleteByName(@PathVariable String name) {
        QueryWrapper<Good> wrapper = new QueryWrapper<>();
        wrapper.eq("goodName", name);
        return goodService.remove(wrapper);
    }

    @PostMapping("/del/batch")//批量删除
    public boolean deleteBatch(@RequestBody List<Integer> ids) {
        return goodService.removeBatchByIds(ids);
    }

    // 3、修改
    @PostMapping("/update")
    public int update(@RequestBody Good good) {
        if (good.getGoodName() == null || good.getGoodPrice() == null)
            return EmptyError;
        if (good.getGoodName().isEmpty())
            return EmptyError;

        QueryWrapper<Good> wrapper = new QueryWrapper<>();
        wrapper.eq("goodName", good.getGoodName());
        if (goodService.exists(wrapper))
        {
            goodService.updateById(good);
            return True;
        }
        return ExistError;
    }

    // 4、查询
    @GetMapping("/")
    public List<Good> getGoodAll() {
        return goodService.list();
    }

    // 返回值包括 gid、goodName、goodPrice、goodPlace、stockNum
    @GetMapping("/page")
    public Page<GoodStockVo> getPage(@RequestParam Integer pageNum,
                                     @RequestParam Integer pageSize,
                                     @RequestParam(defaultValue = "") String gid,
                                     @RequestParam(defaultValue = "") String goodName,
                                     @RequestParam(defaultValue = "") String goodPlace) {
        Page<GoodStockVo> iPage = new Page<>(pageNum, pageSize);
        return goodService.getPageVo(iPage, gid, goodName, goodPlace);
    }
}
//    @GetMapping("/page")
//    public IPage<Good> getPage(@RequestParam Integer pageNum,
//                               @RequestParam Integer pageSize,
//                               @RequestParam(defaultValue = "") String goodId,
//                               @RequestParam(defaultValue = "") String goodName) {
//        IPage<Good> page = new Page<>(pageNum, pageSize);
//        QueryWrapper<Good> queryWrapper = new QueryWrapper<>();
//        if (!"".equals(goodId)) {
//            if (NumberUtils.isParsable(goodId))
//                queryWrapper.eq("gid", Integer.valueOf(goodId));
//            else
//                queryWrapper.eq("gid", -1);
//        }
//        if (!"".equals(goodName))
//            queryWrapper.like("goodName", goodName);
//        return goodService.page(page, queryWrapper);
//    }
