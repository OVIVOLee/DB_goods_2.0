package com.keyi.db_goods.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.keyi.db_goods.entity.Good;
import com.keyi.db_goods.entity.GoodRestockVo;
import com.keyi.db_goods.entity.Restock;
import com.keyi.db_goods.mapper.RestockMapper;
import com.keyi.db_goods.service.GoodService;
import com.keyi.db_goods.service.RestockService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.net.URLEncoder;
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
    @Autowired(required = false)
    private RestockMapper restockMapper;

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
        if (restockService.remove(wrapper)) {
            restockMapper.resetAuto();
            return true;
        }
        return false;
    }

    @PostMapping("/del/batch")//批量删除
    public boolean deleteBatch(@RequestBody List<Integer> ids) {
        if (restockService.removeBatchByIds(ids)) {
            restockMapper.resetAuto();
            return true;
        }
        return false;
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

    // 5、导出
    @GetMapping("/export")
    public void restockExport(HttpServletResponse response) throws Exception {
        // 从数据库查询出所有的数据
        List<Restock> list = restockService.list();
        // 通过工具类创建writer 写出到磁盘路径
        // ExcelWriter writer = ExcelUtil.getWriter(filesUploadPath + "/用户信息.xlsx");
        // 在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名
        writer.addHeaderAlias("restockId", "进货单编号");
        writer.addHeaderAlias("goodId", "商品编号");
        writer.addHeaderAlias("restockNum", "进货数量");
        writer.addHeaderAlias("restockPrice", "进货价格");
        writer.addHeaderAlias("restockSum", "进货总价");
        writer.addHeaderAlias("restockDate", "日期");
        writer.setOnlyAlias(true);

        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(list, true);

        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("进货信息", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();
    }

    // 6、导入
    @PostMapping("/import")
    public boolean restockImport(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        QueryWrapper<Good> wrapper = new QueryWrapper<>();
        // 方式1：(推荐) 通过 javabean的方式读取Excel内的对象，但是要求表头必须是英文，跟javabean的属性要对应起来
//        List<Client> list = reader.readAll(Client.class);

        // 方式2：忽略表头的中文，直接读取表的内容
        List<List<Object>> list = reader.read(1);
        List<Restock> restocks = CollUtil.newArrayList();
        Integer num1 = null;
        Integer num2 = null;
        Double num3 = null;
        for (List<Object> row : list) {
            // 1、类型判断
            try {
                num1 = Integer.parseInt(row.get(0).toString());
                num2 = Integer.parseInt(row.get(1).toString());
                num3 = Double.parseDouble(row.get(2).toString());
            } catch (NumberFormatException e) {
                return false;
            }
            // 2、存在判断
            wrapper.clear();
            wrapper.eq("gid", num1);
            if (!goodService.exists(wrapper))
                return false;

            Restock restock = new Restock();
            restock.setGoodId(num1);
            restock.setRestockNum(num2);
            restock.setRestockPrice(num3);
            restocks.add(restock);
        }

        restockService.saveBatch(restocks);
        return true;
    }

}
