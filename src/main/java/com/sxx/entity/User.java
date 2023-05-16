package com.sxx.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
/**
 * @author TEIC-Skills
 * @description 用户信息
 */
@Data
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId
    private Long id;


    /**
     * @description 姓名
     */
    private String name;


    /**
     * @description 手机号
     */
    private String phone;


    /**
     * @description 性别 0 女 1 男
     */
    private String sex;


    /**
     * @description 身份证号
     */
    private String idNumber;


    /**
     * @description 头像
     */
    private String avatar;


    /**
     * @description 状态 0:禁用，1:正常
     */
    private Integer status;
}
