package com.example.medicine.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @Author Hyh
 * @description: 智能医生消息类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Content {
    private String content;
}
