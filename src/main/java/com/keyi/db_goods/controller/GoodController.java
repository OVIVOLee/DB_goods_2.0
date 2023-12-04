package com.keyi.db_goods.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.keyi.db_goods.entity.Good;
import com.keyi.db_goods.entity.GoodStockVo;
import com.keyi.db_goods.mapper.GoodMapper;
import com.keyi.db_goods.service.GoodService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

@RestController
@RequestMapping("/good")
public class GoodController {
    public static final int EmptyError = -1; // 缺少必要数据
    public static final int ExistError = -2; // 该商品名已存在
    public static final int True = 1;
    @Autowired
    private GoodService goodService;
    @Autowired(required = false)
    private GoodMapper goodMapper;

    // 1、增加
    @PostMapping
    public int save(@RequestBody Good good) {
        if (good.getGoodName() == null || good.getGoodPrice() == null || good.getGoodPlace() == null)
            return EmptyError;
        if (good.getGoodName().isEmpty() || good.getGoodPlace().isEmpty())
            return EmptyError;
        QueryWrapper<Good> wrapper = new QueryWrapper<>();
        wrapper.eq("goodName", good.getGoodName());
        if (goodService.exists(wrapper))
            return ExistError;
        else {
            goodService.save(good);
            return True;
        }
    }

    // 2、删除
    @DeleteMapping("/id/{id}")
    public boolean deleteById(@PathVariable Integer id) {
        QueryWrapper<Good> wrapper = new QueryWrapper<>();
        wrapper.eq("gid", id);
        if (goodService.remove(wrapper)) {
            goodMapper.resetAuto();
            return true;
        }
        return false;
    }

    @DeleteMapping("/name/{name}")
    public boolean deleteByName(@PathVariable String name) {
        QueryWrapper<Good> wrapper = new QueryWrapper<>();
        wrapper.eq("goodName", name);
        return goodService.remove(wrapper);
    }

    @PostMapping("/del/batch")//批量删除
    public boolean deleteBatch(@RequestBody List<Integer> ids) {
        if (goodService.removeBatchByIds(ids)) {
            goodMapper.resetAuto();
            return true;
        }
        return false;
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
        if (goodService.exists(wrapper)) {
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

    // 5、导出
    @GetMapping("/export")
    public void goodExport(HttpServletResponse response) throws Exception {
        // 从数据库查询出所有的数据
        List<Good> list = goodService.list();
        // 在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        // 自定义标题别名
        writer.addHeaderAlias("gid", "商品编号");
        writer.addHeaderAlias("goodName", "商品名");
        writer.addHeaderAlias("goodPrice", "商品价格");
        writer.addHeaderAlias("goodPlace", "商品产地");
        writer.setOnlyAlias(true);
        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(list, true);
        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("商品信息", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();
    }

    // 6、导入
    @PostMapping("/import")
    public boolean goodImport(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        QueryWrapper<Good> wrapper = new QueryWrapper<>();
        // 方式1：(推荐) 通过 javabean的方式读取Excel内的对象，但是要求表头必须是英文，跟javabean的属性要对应起来
//        List<Client> list = reader.readAll(Client.class);

        // 方式2：忽略表头的中文，直接读取表的内容
        List<List<Object>> list = reader.read(1);
        List<Good> goods = CollUtil.newArrayList();
        Integer count = null;
        for (List<Object> row1 : list) {
            count = 0;
            for (List<Object> row2 : list) {
                if (row1.get(0).toString().equals(row2.get(0).toString()))
                    count++;
            }
            if (count >= 2)
                return false;
        }
        for (List<Object> row : list) {
            wrapper.clear();
            wrapper.eq("goodName", row.get(0).toString());
            if (goodService.exists(wrapper))
                return false;

            Good good = new Good();
            good.setGoodName(row.get(0).toString());
            try {
                double num = Double.parseDouble(row.get(1).toString());
                good.setGoodPrice(Double.valueOf(row.get(1).toString()));
            } catch (NumberFormatException e) {
                return false;
            }
            good.setGoodPlace(row.get(2).toString());
            goods.add(good);
        }

        goodService.saveBatch(goods);
        return true;
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
