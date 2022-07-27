package com.ed.demo.i18ndemo.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * @author ed
 * @date 2022/7/27
 */
@SuppressWarnings("AlibabaEnumConstantsMustHaveComment")
public enum LanguageEnum {

    Chinese("汉语", "cn"),
    English("英语", "en"),
    Japanese("日语", "jp");

    private final String value;
    private final String desc;

    LanguageEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static LanguageEnum parseValue(String value) {
        if (StringUtils.isEmpty(value)) {
            return null;
        }
        for (LanguageEnum e : LanguageEnum.values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        return null;
    }

    public static LanguageEnum[] getEnums() {
        return LanguageEnum.values();
    }
}
