package com.example.factory.method;

import com.example.factory.common.Context;

/**
 * HP 键盘实现类 - 工厂方法模式
 */
public class HPKeyboard implements Keyboard {

    @Override
    public void print() {
        System.out.println("HP键盘正在执行打印操作...");
        System.out.println("使用HP专有的打印驱动程序和优化算法");
    }

    @Override
    public void input(Context context) {
        System.out.println("HP键盘正在处理输入: " + context);
        System.out.println("HP键盘特有的输入处理逻辑和手感优化");
    }
}

