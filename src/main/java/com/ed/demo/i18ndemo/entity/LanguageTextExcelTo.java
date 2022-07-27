package com.ed.demo.i18ndemo.entity;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @author ed
 * @date 2022/7/27
 */
@Data
@ExcelIgnoreUnannotated
public class LanguageTextExcelTo {

    @ExcelProperty(index = 0)
    private String key;

    @ExcelProperty(index = 1)
    private String cn;

    @ExcelProperty(index = 2)
    private String secondLang;

}
