package com.example.factory.simple;

import com.example.factory.common.BrandEnum;
import com.example.factory.common.Context;

/**
 * 简单工厂模式 - 键盘工厂
 * 负责创建不同品牌的键盘
 */
public class KeyboardFactory {

    /**
     * 根据品牌代码创建对应的键盘实例
     * @param brand 品牌代码
     * @return 键盘实例
     */
    public Keyboard getInstance(int brand) {
        if (BrandEnum.HP.getCode() == brand) {
            return new HPKeyboard();
        } else if (BrandEnum.LENOVO.getCode() == brand) {
            return new LenovoKeyboard();
        } else if (BrandEnum.DELL.getCode() == brand) {
            return new DellKeyboard();
        }
        throw new IllegalArgumentException("不支持的品牌代码: " + brand);
    }

    /**
     * 根据品牌枚举创建对应的键盘实例
     * @param brandEnum 品牌枚举
     * @return 键盘实例
     */
    public Keyboard getInstance(BrandEnum brandEnum) {
        if (brandEnum == null) {
            throw new IllegalArgumentException("品牌枚举不能为空");
        }
        return getInstance(brandEnum.getCode());
    }

    /**
     * 简单工厂模式演示
     */
    public static void main(String[] args) {
        System.out.println("=== 简单工厂模式演示 ===");

        KeyboardFactory keyboardFactory = new KeyboardFactory();
        Context context = new Context("Hello World", "typing");

        // 创建不同品牌的键盘
        System.out.println("\n1. 创建HP键盘:");
        Keyboard hpKeyboard = keyboardFactory.getInstance(BrandEnum.HP.getCode());
        hpKeyboard.print();
        hpKeyboard.input(context);

        System.out.println("\n2. 创建Lenovo键盘:");
        Keyboard lenovoKeyboard = keyboardFactory.getInstance(BrandEnum.LENOVO);
        lenovoKeyboard.print();
        lenovoKeyboard.input(context);

        System.out.println("\n3. 创建Dell键盘:");
        Keyboard dellKeyboard = keyboardFactory.getInstance(BrandEnum.DELL.getCode());
        dellKeyboard.print();
        dellKeyboard.input(context);

        // 演示错误情况
        System.out.println("\n4. 尝试创建不支持的品牌:");
        try {
            keyboardFactory.getInstance(999);
        } catch (IllegalArgumentException e) {
            System.out.println("捕获异常: " + e.getMessage());
        }
    }
}

