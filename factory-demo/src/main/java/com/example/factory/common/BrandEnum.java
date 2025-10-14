package com.example.factory.common;

/**
 * 品牌枚举类
 * 用于标识不同的品牌
 */
public enum BrandEnum {
    HP(1, "HP"),
    DELL(2, "DELL"),
    LENOVO(3, "LENOVO");

    private final int code;
    private final String name;

    BrandEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static BrandEnum getByCode(int code) {
        for (BrandEnum brand : values()) {
            if (brand.getCode() == code) {
                return brand;
            }
        }
        return null;
    }
}

