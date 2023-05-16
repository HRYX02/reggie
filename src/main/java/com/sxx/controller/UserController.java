package com.sxx.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sxx.common.R;
import com.sxx.entity.User;
import com.sxx.service.UserService;
import com.sxx.utiles.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author SxxStar
 * @description 用户登录Controller
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private JavaMailSender jms;

    @Value("${spring.mail.username}")
    private String from;
    @Autowired
    private UserService userService;

    @PostMapping("/sendMsg")
    private R<User> sendMsg(HttpServletRequest request, @RequestBody User user){
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPhone,user.getPhone());
        User userSelect = userService.getOne(queryWrapper);
        if (userSelect == null) {
            userSelect = new User();
            userSelect.setPhone(user.getPhone());
            userService.save(userSelect);
        }
        String code = ValidateCodeUtils.generateValidateCode4String(4);
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            // 接收地址
            message.setTo("2291874995@qq.com");
            // 标题
            message.setSubject("验证码");

            request.getSession().setAttribute(user.getPhone(),code);
            // 内容
            message.setText("您的验证码为"+code);
            log.info("您的验证码为"+code);
            jms.send(message);
            return R.success(user);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("验证码发送失败");
        }
    }
    @PostMapping("/login")
    private R<User> login(HttpServletRequest request, @RequestBody Map user){
        String phone = user.get("phone").toString();
        String code = user.get("code").toString();
        if (code.equals(request.getSession().getAttribute(phone))) {
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone,phone);
            User userSelect = userService.getOne(queryWrapper);
            request.getSession().setAttribute("userResult",userSelect.getId());
            return R.success(userSelect);
        }
        return R.error("登录失败");
    }

    @PostMapping("/loginout")
    public R<String> loginout(HttpServletRequest request){
        request.getSession().removeAttribute("userResult");
        return R.success("成功退出");
    }
}
