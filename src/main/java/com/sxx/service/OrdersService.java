package com.sxx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sxx.entity.Orders;
import org.springframework.transaction.annotation.Transactional;

public interface OrdersService extends IService<Orders> {
    @Transactional(rollbackFor = Exception.class)
    void submit(Orders orders);
}
