package com.ed.demo.i18ndemo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ed
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "language_text")
public class LanguageText {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 1: Frontend  2: Backend
     */
    @TableField(value = "`type`")
    private Integer type;

    @TableField(value = "model")
    private String model;

    @TableField(value = "`key`")
    private String key;

    @TableField(value = "langs")
    private String langs;
}
