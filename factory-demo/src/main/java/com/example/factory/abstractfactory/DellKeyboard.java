package com.example.factory.abstractfactory;

/**
 * Dell 键盘实现类 - 抽象工厂模式
 */
public class DellKeyboard implements Keyboard {

    @Override
    public void print() {
        System.out.println("Dell键盘正在执行打印操作...");
        System.out.println("使用Dell专有的打印技术和Alienware背光效果");
    }
}

