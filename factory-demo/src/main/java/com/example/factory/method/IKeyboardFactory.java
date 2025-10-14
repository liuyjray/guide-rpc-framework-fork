package com.example.factory.method;

/**
 * 抽象键盘工厂接口 - 工厂方法模式
 * 声明了工厂方法的接口
 */
public interface IKeyboardFactory {
    /**
     * 创建键盘实例的工厂方法
     * @return 键盘实例
     */
    Keyboard getInstance();
}

