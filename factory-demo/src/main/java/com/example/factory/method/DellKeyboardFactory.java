package com.example.factory.method;

/**
 * Dell 键盘工厂实现类 - 工厂方法模式
 * 负责创建 Dell 键盘实例
 */
public class DellKeyboardFactory implements IKeyboardFactory {

    @Override
    public Keyboard getInstance() {
        System.out.println("Dell工厂正在生产Dell键盘...");
        // 这里可以添加复杂的初始化逻辑
        // 比如读取配置文件、初始化Alienware驱动等
        return new DellKeyboard();
    }
}

