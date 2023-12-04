package com.keyi.db_goods.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.keyi.db_goods.entity.Client;
import org.apache.ibatis.annotations.Update;

public interface ClientMapper extends BaseMapper<Client> {
    @Update("ALTER TABLE clients AUTO_INCREMENT = 1;")
    void resetAuto();
}
