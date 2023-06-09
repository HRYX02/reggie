package com.sxx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sxx.entity.ShoppingCart;

public interface ShoppingCartService extends IService<ShoppingCart> {
    ShoppingCart checkAdd(ShoppingCart shoppingCart);

    ShoppingCart reduce(ShoppingCart shoppingCart);
}
