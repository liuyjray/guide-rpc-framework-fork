package com.example.factory.abstractfactory;

/**
 * HP 键盘实现类 - 抽象工厂模式
 */
public class HPKeyboard implements Keyboard {

    @Override
    public void print() {
        System.out.println("HP键盘正在执行打印操作...");
        System.out.println("使用HP专有的打印技术和人体工学设计");
    }
}

