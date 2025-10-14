package com.example.factory.method;

import com.example.factory.common.BrandEnum;

/**
 * 键盘工厂提供者 - 工厂方法模式的改进
 * 解决工厂方法模式中无法根据类型动态选择工厂的问题
 */
public class KeyboardFactoryProvider {

    /**
     * 根据品牌代码获取对应的工厂实例
     * @param brand 品牌代码
     * @return 对应的工厂实例
     */
    public static IKeyboardFactory getFactory(int brand) {
        if (BrandEnum.HP.getCode() == brand) {
            return new HPKeyboardFactory();
        } else if (BrandEnum.LENOVO.getCode() == brand) {
            return new LenovoKeyboardFactory();
        } else if (BrandEnum.DELL.getCode() == brand) {
            return new DellKeyboardFactory();
        }
        throw new IllegalArgumentException("不支持的品牌代码: " + brand);
    }

    /**
     * 根据品牌枚举获取对应的工厂实例
     * @param brandEnum 品牌枚举
     * @return 对应的工厂实例
     */
    public static IKeyboardFactory getFactory(BrandEnum brandEnum) {
        if (brandEnum == null) {
            throw new IllegalArgumentException("品牌枚举不能为空");
        }
        return getFactory(brandEnum.getCode());
    }

    /**
     * 演示工厂方法模式的动态选择能力
     */
    public static void demonstrateDynamicSelection() {
        System.out.println("=== 工厂方法模式 - 动态选择演示 ===");

        // 模拟用户输入或配置文件中的品牌选择
        int[] brandCodes = {
            BrandEnum.HP.getCode(),
            BrandEnum.LENOVO.getCode(),
            BrandEnum.DELL.getCode()
        };

        for (int brandCode : brandCodes) {
            System.out.println("\n根据品牌代码 " + brandCode + " 创建键盘:");

            // 动态获取工厂
            IKeyboardFactory factory = getFactory(brandCode);

            // 使用工厂创建产品
            Keyboard keyboard = factory.getInstance();
            keyboard.print();

            BrandEnum brand = BrandEnum.getByCode(brandCode);
            System.out.println("成功创建了 " + (brand != null ? brand.getName() : "未知") + " 品牌的键盘");
        }

        // 演示错误处理
        System.out.println("\n尝试使用不支持的品牌代码:");
        try {
            getFactory(999);
        } catch (IllegalArgumentException e) {
            System.out.println("捕获异常: " + e.getMessage());
        }
    }
}

