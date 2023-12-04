package com.keyi.db_goods.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.keyi.db_goods.entity.*;
import com.keyi.db_goods.mapper.SaleMapper;
import com.keyi.db_goods.service.ClientService;
import com.keyi.db_goods.service.GoodService;
import com.keyi.db_goods.service.SaleService;
import com.keyi.db_goods.service.StockService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.net.URLEncoder;
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
    @Autowired(required = false)
    private SaleMapper saleMapper;

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
            wrapper2.eq("goodId", sale.getGoodId());
            wrapper2.gt("stockNum", sale.getSaleNum());
            if (!stockService.exists(wrapper2))
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
        if (saleService.remove(wrapper)) {
            saleMapper.resetAuto();
            return true;
        }
        return false;
    }

    @PostMapping("/del/batch")//批量删除
    public boolean deleteBatch(@RequestBody List<Integer> ids) {
        if (saleService.removeBatchByIds(ids)) {
            saleMapper.resetAuto();
            return true;
        }
        return false;
    }

//    @PostMapping("/del/batch")//批量删除
//    public boolean deleteBatch(@RequestBody List<Integer> ids) {
//        return saleService.removeBatchByIds(ids);
//    }

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

    // 5、导出
    @GetMapping("/export")
    public void clientExport(HttpServletResponse response) throws Exception {
        // 从数据库查询出所有的数据
        List<Sale> list = saleService.list();
        // 在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        // 自定义标题别名
        writer.addHeaderAlias("saleId", "订单编号");
        writer.addHeaderAlias("goodId", "商品编号");
        writer.addHeaderAlias("clientId", "客户编号");
        writer.addHeaderAlias("saleNum", "销售价格");
        writer.addHeaderAlias("salePrice", "销售价格");
        writer.addHeaderAlias("saleSum", "销售总价");
        writer.addHeaderAlias("saleDate", "销售日期");
        writer.setOnlyAlias(true);
        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(list, true);
        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("销售信息", "UTF-8");
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
        QueryWrapper<Good> wrapper1 = new QueryWrapper<>();
        QueryWrapper<Client> wrapper2 = new QueryWrapper<>();
        // 方式1：(推荐) 通过 javabean的方式读取Excel内的对象，但是要求表头必须是英文，跟javabean的属性要对应起来
//        List<Client> list = reader.readAll(Client.class);

        // 方式2：忽略表头的中文，直接读取表的内容
        List<List<Object>> list = reader.read(1);
        List<Sale> sales = CollUtil.newArrayList();
        Integer num1 = null;
        Integer num2 = null;
        Integer num3 = null;
        Double num4 = null;
        for (List<Object> row : list) {
            if(row.get(0).toString()==null||row.get(1).toString()==null||
                    row.get(2).toString()==null||row.get(3).toString()==null)
                return false;
            if(row.get(0).toString().isEmpty()||row.get(1).toString().isEmpty()||
                    row.get(2).toString().isEmpty()||row.get(3).toString().isEmpty())
                return false;

            // 1、类型判断
            try {
                num1 = Integer.parseInt(row.get(0).toString());
                num2 = Integer.parseInt(row.get(1).toString());
                num3 = Integer.parseInt(row.get(2).toString());
                num4 = Double.parseDouble(row.get(3).toString());
            } catch (NumberFormatException e) {
                return false;
            }
            // 2、存在判断
            wrapper1.clear();
            wrapper1.eq("gid", num1);
            wrapper2.clear();
            wrapper2.eq("cid", num2);
            if (!goodService.exists(wrapper1) || !clientService.exists(wrapper2))
                return false;

            Sale sale = new Sale();
            sale.setGoodId(num1);
            sale.setClientId(num2);
            sale.setSaleNum(num3);
            sale.setSalePrice(num4);
            sales.add(sale);
        }

        saleService.saveBatch(sales);
        return true;
    }
}
