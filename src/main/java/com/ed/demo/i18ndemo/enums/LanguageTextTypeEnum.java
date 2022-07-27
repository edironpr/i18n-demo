package com.ed.demo.i18ndemo.enums;

/**
 * @author ed
 * @date 2022/7/27
 */
@SuppressWarnings("AlibabaEnumConstantsMustHaveComment")
public enum LanguageTextTypeEnum {

    Frontend(1, "frontend"),
    Backend(2, "backend");

    private final int value;
    private final String desc;

    LanguageTextTypeEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public Integer getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static LanguageTextTypeEnum parseValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (LanguageTextTypeEnum e : LanguageTextTypeEnum.values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        return null;
    }

    public static LanguageTextTypeEnum[] getEnums() {
        return LanguageTextTypeEnum.values();
    }
}
