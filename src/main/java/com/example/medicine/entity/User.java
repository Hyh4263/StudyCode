package com.example.medicine.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 用户实体
 *
 * @author XUEW
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("user")
public class User {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 用户账号
     */
    private String userAccount;

    /**
     * 用户真实名字
     */
    private String userName;

    /**
     * 用户密码
     */
    private String userPwd;

    /**
     * 用户年龄
     */
    private Integer userAge;

    /**
     * 验证码
     */
    @TableField(exist = false)
    private String code;

    /**
     * 用户性别
     */
    private String userSex;

    /**
     * 用户邮箱
     */
    private String userEmail;

    /**
     * 用户电话
     */
    private String userTel;

    /**
     * 用户医师执业证号
     */
    @TableField(value = "medical_license_number")
    private String medicalLicenseNumber;

    /**
     * 用户身份证号
     */
    @TableField(value = "id_card")
    private String idCard;

    /**
     * 角色状态，0普通用户，1管理员，2超级管理员
     */
    private Integer roleStatus;

    /**
     * 用户状态,1正常 2禁用
     */
    private Integer status;

    /**
     * 图片的地址
     */
    private String imgPath;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 新密码
     */
    @TableField(exist = false)
    private String userNewPwd;


}
