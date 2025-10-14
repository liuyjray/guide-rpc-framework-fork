package com.example.factory.singleton;

import com.example.factory.common.BrandEnum;

/**
 * 单例工厂模式 - 键盘工厂
 * 结合了单例模式和工厂模式的优点
 *
 * 特点：
 * 1. 工厂本身是单例的，节省内存资源
 * 2. 可以控制产品实例的创建策略（单例产品或多例产品）
 * 3. 线程安全的实现
 */
public class SingletonKeyboardFactory {

    // 工厂实例（单例）
    private static volatile SingletonKeyboardFactory instance;

    // 产品实例缓存（可选择是否让产品也是单例）
    private volatile HPKeyboard hpKeyboard;
    private volatile DellKeyboard dellKeyboard;
    private volatile LenovoKeyboard lenovoKeyboard;

    // 私有构造函数，防止外部实例化
    private SingletonKeyboardFactory() {
        System.out.println("SingletonKeyboardFactory 实例被创建");
    }

    /**
     * 获取工厂实例（双重检查锁定）
     * @return 工厂实例
     */
    public static SingletonKeyboardFactory getInstance() {
        if (instance == null) {
            synchronized (SingletonKeyboardFactory.class) {
                if (instance == null) {
                    instance = new SingletonKeyboardFactory();
                }
            }
        }
        return instance;
    }

    /**
     * 创建键盘实例（每次都创建新实例）
     * @param brand 品牌代码
     * @return 键盘实例
     */
    public Keyboard createKeyboard(int brand) {
        if (BrandEnum.HP.getCode() == brand) {
            System.out.println("创建新的HP键盘实例");
            return new HPKeyboard();
        } else if (BrandEnum.LENOVO.getCode() == brand) {
            System.out.println("创建新的Lenovo键盘实例");
            return new LenovoKeyboard();
        } else if (BrandEnum.DELL.getCode() == brand) {
            System.out.println("创建新的Dell键盘实例");
            return new DellKeyboard();
        }
        throw new IllegalArgumentException("不支持的品牌代码: " + brand);
    }

    /**
     * 获取键盘实例（单例产品，懒加载）
     * @param brand 品牌代码
     * @return 键盘实例
     */
    public Keyboard getSingletonKeyboard(int brand) {
        if (BrandEnum.HP.getCode() == brand) {
            if (hpKeyboard == null) {
                synchronized (this) {
                    if (hpKeyboard == null) {
                        System.out.println("创建HP键盘单例实例");
                        hpKeyboard = new HPKeyboard();
                    }
                }
            } else {
                System.out.println("返回已存在的HP键盘单例实例");
            }
            return hpKeyboard;
        } else if (BrandEnum.LENOVO.getCode() == brand) {
            if (lenovoKeyboard == null) {
                synchronized (this) {
                    if (lenovoKeyboard == null) {
                        System.out.println("创建Lenovo键盘单例实例");
                        lenovoKeyboard = new LenovoKeyboard();
                    }
                }
            } else {
                System.out.println("返回已存在的Lenovo键盘单例实例");
            }
            return lenovoKeyboard;
        } else if (BrandEnum.DELL.getCode() == brand) {
            if (dellKeyboard == null) {
                synchronized (this) {
                    if (dellKeyboard == null) {
                        System.out.println("创建Dell键盘单例实例");
                        dellKeyboard = new DellKeyboard();
                    }
                }
            } else {
                System.out.println("返回已存在的Dell键盘单例实例");
            }
            return dellKeyboard;
        }
        throw new IllegalArgumentException("不支持的品牌代码: " + brand);
    }

    /**
     * 根据品牌枚举创建键盘实例
     * @param brandEnum 品牌枚举
     * @return 键盘实例
     */
    public Keyboard createKeyboard(BrandEnum brandEnum) {
        if (brandEnum == null) {
            throw new IllegalArgumentException("品牌枚举不能为空");
        }
        return createKeyboard(brandEnum.getCode());
    }

    /**
     * 根据品牌枚举获取单例键盘实例
     * @param brandEnum 品牌枚举
     * @return 键盘实例
     */
    public Keyboard getSingletonKeyboard(BrandEnum brandEnum) {
        if (brandEnum == null) {
            throw new IllegalArgumentException("品牌枚举不能为空");
        }
        return getSingletonKeyboard(brandEnum.getCode());
    }

    /**
     * 清除所有产品缓存
     */
    public synchronized void clearCache() {
        System.out.println("清除所有键盘实例缓存");
        hpKeyboard = null;
        dellKeyboard = null;
        lenovoKeyboard = null;
    }

    /**
     * 获取缓存状态信息
     */
    public void printCacheStatus() {
        System.out.println("=== 缓存状态 ===");
        System.out.println("HP键盘缓存: " + (hpKeyboard != null ? "已缓存" : "未缓存"));
        System.out.println("Dell键盘缓存: " + (dellKeyboard != null ? "已缓存" : "未缓存"));
        System.out.println("Lenovo键盘缓存: " + (lenovoKeyboard != null ? "已缓存" : "未缓存"));
    }
}

