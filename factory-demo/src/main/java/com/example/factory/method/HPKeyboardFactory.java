package com.example.factory.method;

/**
 * HP 键盘工厂实现类 - 工厂方法模式
 * 负责创建 HP 键盘实例
 */
public class HPKeyboardFactory implements IKeyboardFactory {

    @Override
    public Keyboard getInstance() {
        System.out.println("HP工厂正在生产HP键盘...");
        // 这里可以添加复杂的初始化逻辑
        // 比如读取配置文件、初始化驱动等
        return new HPKeyboard();
    }
}

