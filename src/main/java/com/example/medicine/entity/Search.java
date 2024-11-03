package com.example.medicine.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author Hyh
 * @description: 搜索
 */
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Search {
    /**
     * 药品ID
     */
    private Integer medicineId;

    /**
     * 疾病ID
     */
    private Integer illnessId;

    /**
     * 疾病所属种类id
     */
    private Integer kindId;

    /**
     * 疾病的名字
     */
    private String illnessName;

    /**
     * 引起的原因
     */
    private String includeReason;

    /**
     * 主要的症状
     */
    private String illnessSymptom;

    /**
     * 特殊的症状
     */
    private String specialSymptom;

    /**
     * 药物名字
     */
    private String medicineName;

    /**
     * 关键字搜索
     */
    private String keyword;

    /**
     * 药物的功效
     */
    private String medicineEffect;

    /**
     * 药物的品牌
     */
    private String medicineBrand;

    /**
     * 药物的相互作用
     */
    private String interaction;

    /**
     * 禁忌
     */
    private String taboo;

    /**
     * 用法用量
     */
    private String usAge;

    /**
     * 药物的类型，0代表西药，1中药，2中成药
     */
    private Integer medicineType;

    /**
     * 药物的图片地址
     */
    private String imgPath;

    /**
     * 药物的价格
     */
    private BigDecimal medicinePrice;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 疾病种类实体
     */
    @TableField(exist = false)
    private IllnessKind kind;

    /**
     * 疾病与药物关系实体
     */
    @TableField(exist = false)
    private IllnessMedicine illnessMedicine;
}
