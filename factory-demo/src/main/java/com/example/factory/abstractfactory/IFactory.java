package com.example.factory.abstractfactory;

/**
 * 抽象工厂接口 - 抽象工厂模式
 * 声明了创建抽象产品对象的操作接口
 */
public interface IFactory {

    /**
     * 创建主机
     * @return 主机实例
     */
    MainFrame createMainFrame();

    /**
     * 创建显示器
     * @return 显示器实例
     */
    Monitor createMonitor();

    /**
     * 创建键盘
     * @return 键盘实例
     */
    Keyboard createKeyboard();
}

