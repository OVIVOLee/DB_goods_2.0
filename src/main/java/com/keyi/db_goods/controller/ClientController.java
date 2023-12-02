package com.keyi.db_goods.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.keyi.db_goods.entity.Client;
import com.keyi.db_goods.service.ClientService;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/client")
public class ClientController {
    @Autowired
    private ClientService clientService;

    // 1、增加
    @PostMapping
    public boolean save(@RequestBody Client client) {
        QueryWrapper<Client> wrapper = new QueryWrapper<>();
        wrapper.eq("clientMobile", client.getClientMobile());
        if (clientService.exists(wrapper))
            return false;
        else {
            return clientService.save(client);
        }
    }

    // 2、删除
    @DeleteMapping("/id/{id}")
    public boolean deleteById(@PathVariable Integer id) {
        QueryWrapper<Client> wrapper = new QueryWrapper<>();
        wrapper.eq("cid", id);
        return clientService.remove(wrapper);
    }

    // 3、修改
    @PostMapping("/update")
    public boolean update(@RequestBody Client client) {
        QueryWrapper<Client> wrapper = new QueryWrapper<>();
        wrapper.eq("cid", client.getCid());
        if (clientService.exists(wrapper)) {    // 判断cid
            wrapper.clear();
            wrapper.eq("clientMobile", client.getClientMobile());

            if(clientService.exists(wrapper)){  // 判断是否存在将要修改的clientMobile值
                wrapper.eq("cid", client.getCid());
                if(!clientService.exists(wrapper))  // 判断是否可以修改
                    return false;
            }
            return clientService.updateById(client);
        }
        return false;
    }

    // 4、查询
    @GetMapping("/")
    public List<Client> getGoodAll() {
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

}
