package com.example.medicine.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.Date;

/**
 * @author Hyh
 * @date 2024/4/7
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "image")
public class Image {

    @TableId(type = IdType.AUTO)
    private Integer id;
    // 用户id
    private Integer userId;
    // 图片名称
    private String name;
    // 图片 描述
    private String description;
    // 存储路径
    private String path;
    // 地址
    private String url;
    // 创建时间
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date createTime;
}
