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

    public static final Integer EmptyError = -1; // 缺少必要数据
    public static final Integer ExistError = -2; // 该客户已存在
    public static final Integer MobileError=-3; // 电话号码具有唯一性
    public static final Integer True = 1;

    // 1、增加
    @PostMapping
    public int save(@RequestBody Client client) {
        if (client.getClientMobile() == null || client.getClientName() == null)
            return EmptyError;
        if (client.getClientMobile().isEmpty() || client.getClientName().isEmpty())
            return EmptyError;
        QueryWrapper<Client> wrapper = new QueryWrapper<>();
        wrapper.eq("clientMobile", client.getClientMobile());
        if (!clientService.exists(wrapper)){
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
        return clientService.remove(wrapper);
    }

    @PostMapping("/del/batch")//批量删除
    public boolean deleteBatch(@RequestBody List<Integer> ids) {
        return clientService.removeBatchByIds(ids);
    }

    // 3、修改
    @PostMapping("/update")
    public int update(@RequestBody Client client) {
        if (client.getClientMobile() != null) {
            if(!client.getClientMobile().isEmpty()){

                QueryWrapper<Client> wrapper = new QueryWrapper<>();
                wrapper.eq("clientMobile", client.getClientMobile());
                if(!clientService.exists(wrapper)){
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

}
