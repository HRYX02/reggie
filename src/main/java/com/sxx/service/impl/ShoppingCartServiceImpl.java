package com.sxx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sxx.common.BaseContext;
import com.sxx.dao.ShoppingCartDao;
import com.sxx.entity.ShoppingCart;
import com.sxx.service.ShoppingCartService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartDao, ShoppingCart> implements ShoppingCartService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ShoppingCart checkAdd(ShoppingCart shoppingCart){
        shoppingCart.setUserId(BaseContext.getCurrentId());
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();

        if (shoppingCart.getDishId() != null) {
            queryWrapper.eq(ShoppingCart::getDishId, shoppingCart.getDishId()).eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        } else {
            queryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId()).eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        }

        ShoppingCart cartServiceOne = this.getOne(queryWrapper);

        if (cartServiceOne != null) {

            // 如果有该菜品就应该更新从数据库中拿的数据而不是从前端拿来的！！！
            cartServiceOne.setNumber(cartServiceOne.getNumber() + 1);
            this.updateById(cartServiceOne);
        }else {
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            this.save(shoppingCart);
            cartServiceOne = shoppingCart;
        }
        return cartServiceOne;
    }

    @Override
    public ShoppingCart reduce(ShoppingCart shoppingCart){
        shoppingCart.setUserId(BaseContext.getCurrentId());
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        if (shoppingCart.getDishId() != null) {
            queryWrapper.eq(ShoppingCart::getDishId, shoppingCart.getDishId()).eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        } else {
            queryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId()).eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        }
        ShoppingCart cartServiceOne = this.getOne(queryWrapper);
        int count = cartServiceOne.getNumber() - 1;

        // 判断如果减了一份菜品，该菜品数量为0，就直接将该菜品删除
        if (count == 0) {

            // 把0返回给前端
            cartServiceOne.setNumber(0);
            this.removeById(cartServiceOne);
        }else{
            cartServiceOne.setNumber(cartServiceOne.getNumber() -1);
            this.updateById(cartServiceOne);
        }
        return cartServiceOne;
    }

}
