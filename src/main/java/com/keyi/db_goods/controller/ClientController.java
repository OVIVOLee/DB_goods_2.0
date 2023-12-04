package com.keyi.db_goods.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.keyi.db_goods.entity.Client;
import com.keyi.db_goods.mapper.ClientMapper;
import com.keyi.db_goods.service.ClientService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

@RestController
@RequestMapping("/client")
public class ClientController {
    public static final Integer EmptyError = -1; // 缺少必要数据
    public static final Integer ExistError = -2; // 该客户已存在
    public static final Integer MobileError = -3; // 电话号码具有唯一性
    public static final Integer True = 1;
    @Autowired
    private ClientService clientService;
    @Autowired(required = false)
    private ClientMapper clientMapper;

    // 1、增加
    @PostMapping
    public int save(@RequestBody Client client) {
        if (client.getClientMobile() == null || client.getClientName() == null)
            return EmptyError;
        if (client.getClientMobile().isEmpty() || client.getClientName().isEmpty())
            return EmptyError;
        QueryWrapper<Client> wrapper = new QueryWrapper<>();
        wrapper.eq("clientMobile", client.getClientMobile());
        if (!clientService.exists(wrapper)) {
            clientService.save(client);
            return True;
        }
        return ExistError;
    }

    // 2、删除
    @DeleteMapping("/id/{id}")
    public boolean deleteById(@PathVariable Integer id) {
        QueryWrapper<Client> wrapper = new QueryWrapper<>();
        wrapper.eq("cid", id);
        if (clientService.remove(wrapper)) {
            clientMapper.resetAuto();
            return true;
        }
        return false;
    }

    @PostMapping("/del/batch")//批量删除
    public boolean deleteBatch(@RequestBody List<Integer> ids) {
        if (clientService.removeBatchByIds(ids)) {
            clientMapper.resetAuto();
            return true;
        }
        return false;
    }

    // 3、修改
    @PostMapping("/update")
    public int update(@RequestBody Client client) {
        if (client.getClientMobile() != null) {
            if (!client.getClientMobile().isEmpty()) {

                QueryWrapper<Client> wrapper = new QueryWrapper<>();
                wrapper.eq("clientMobile", client.getClientMobile());
                if (!clientService.exists(wrapper)) {
                    clientService.updateById(client);
                    return True;
                }
                return MobileError;
            }
        }
        return EmptyError;
    }

    // 4、查询
    @GetMapping("/")
    public List<Client> getClientAll() {
        return clientService.list();
    }

    @GetMapping("/page")
    public IPage<Client> getPage(@RequestParam Integer pageNum,
                                 @RequestParam Integer pageSize,
                                 @RequestParam(defaultValue = "") String cid,
                                 @RequestParam(defaultValue = "") String clientName,
                                 @RequestParam(defaultValue = "") String clientMobile,
                                 @RequestParam(defaultValue = "") String clientEmail) {
        IPage<Client> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Client> queryWrapper = new QueryWrapper<>();
        if (!"".equals(cid)) {
            if (NumberUtils.isParsable(cid)) {
                queryWrapper.eq("cid", Integer.valueOf(cid));
            } else {
                queryWrapper.eq("cid", -1);
            }
        }
        if (!"".equals(clientName))
            queryWrapper.like("clientName", clientName);
        if (!"".equals(clientMobile))
            queryWrapper.like("clientMobile", clientMobile);
        if (!"".equals(clientEmail))
            queryWrapper.like("clientEmail", clientEmail);
        return clientService.page(page, queryWrapper);
    }

    // 5、导出
    @GetMapping("/export")
    public void clientExport(HttpServletResponse response) throws Exception {
        // 从数据库查询出所有的数据
        List<Client> list = clientService.list();
        // 在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        // 自定义标题别名
        writer.addHeaderAlias("cid", "客户编号");
        writer.addHeaderAlias("clientName", "客户姓名");
        writer.addHeaderAlias("clientMobile", "电话");
        writer.addHeaderAlias("clientEmail", "邮箱");
        writer.setOnlyAlias(true);
        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(list, true);
        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("客户信息", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();
    }

    // 6、导入
    @PostMapping("/import")
    public boolean clientImport(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        QueryWrapper<Client> wrapper = new QueryWrapper<>();
        // 方式1：(推荐) 通过 javabean的方式读取Excel内的对象，但是要求表头必须是英文，跟javabean的属性要对应起来
//        List<Client> list = reader.readAll(Client.class);

        // 方式2：忽略表头的中文，直接读取表的内容
        List<List<Object>> list = reader.read(1);
        List<Client> clients = CollUtil.newArrayList();
        Integer count = null;
        for (List<Object> row1 : list) {
            if(row1.get(0).toString()==null||row1.get(1).toString()==null)
                return false;
            if(row1.get(0).toString().isEmpty()||row1.get(1).toString().isEmpty())
                return false;

            count = 0;
            for (List<Object> row2 : list) {
                if (row1.get(1).toString().equals(row2.get(1).toString()))
                    count++;
            }
            if (count >= 2)
                return false;
        }
        for (List<Object> row : list) {
            wrapper.clear();
            wrapper.eq("clientMobile", row.get(1).toString());
            if (clientService.exists(wrapper))
                return false;

            Client client = new Client();
            client.setClientName(row.get(0).toString());
            client.setClientMobile(row.get(1).toString());
            if(row.get(2).toString()!=null)
            {
                if(!row.get(2).toString().isEmpty()){
                    client.setClientEmail(row.get(2).toString());
                }
            }
            clients.add(client);
        }

        clientService.saveBatch(clients);
        return true;
    }
}