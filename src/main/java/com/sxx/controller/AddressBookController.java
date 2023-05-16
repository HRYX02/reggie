package com.sxx.controller;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.sxx.common.BaseContext;
import com.sxx.common.R;
import com.sxx.entity.AddressBook;
import com.sxx.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/addressBook")
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;

    @PostMapping
    private R<String> save(HttpServletRequest request, @RequestBody AddressBook addressBook){
        log.info(addressBook.toString());
        String s = String.valueOf(BaseContext.getCurrentId());
        log.info("idididididid"+s);
        addressBookService.save(addressBook);
        return R.success("保存成功");
    }

    @GetMapping("/list")
    public R<List<AddressBook>> list(){
        List<AddressBook> list = addressBookService.list();
        return R.success(list);
    }

    @GetMapping("/{id}")
    public R<AddressBook> getById(@PathVariable Long id){
        AddressBook addressBook = addressBookService.getById(id);
        return R.success(addressBook);
    }

    @PutMapping
    public R<String> update(@RequestBody AddressBook addressBook){
        boolean isUpdate = addressBookService.updateById(addressBook);
        if (isUpdate) {
            return R.success("修改成功");
        }
        return R.error("修改失败");
    }
    @DeleteMapping
    public R<String> delete(Long ids){
        log.info("ids"+ids);
        boolean isRemove = addressBookService.removeById(ids);
        if (isRemove) {
            return R.success("删除成功");
        }
        return R.error("删除失败");
    }
    @PutMapping("/default")
    private R<String> setDefault(@RequestBody AddressBook addressBook){
        LambdaUpdateWrapper<AddressBook> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(AddressBook::getUserId,BaseContext.getCurrentId());
        updateWrapper.set(AddressBook::getIsDefault,0);
        addressBookService.update(updateWrapper);

        addressBook.setIsDefault(1);
        addressBookService.updateById(addressBook);
        return R.success("修改成功");
    }

    //TODO: 历史订单，最新订单
}
