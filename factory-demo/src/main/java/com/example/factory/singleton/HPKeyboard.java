package com.example.factory.singleton;

import com.example.factory.common.Context;

/**
 * HP 键盘实现类 - 单例工厂模式
 */
public class HPKeyboard implements Keyboard {

    @Override
    public void print() {
        System.out.println("HP键盘正在执行打印操作...");
        System.out.println("使用HP专有的打印驱动程序和企业级安全技术");
    }

    @Override
    public void input(Context context) {
        System.out.println("HP键盘正在处理输入: " + context);
        System.out.println("HP键盘特有的输入处理逻辑和商务优化");
    }
}

