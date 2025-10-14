package com.example.factory.method;

/**
 * Lenovo 键盘工厂实现类 - 工厂方法模式
 * 负责创建 Lenovo 键盘实例
 */
public class LenovoKeyboardFactory implements IKeyboardFactory {

    @Override
    public Keyboard getInstance() {
        System.out.println("Lenovo工厂正在生产Lenovo键盘...");
        // 这里可以添加复杂的初始化逻辑
        // 比如读取配置文件、初始化ThinkPad驱动等
        return new LenovoKeyboard();
    }
}

