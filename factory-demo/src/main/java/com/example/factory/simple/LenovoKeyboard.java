package com.example.factory.simple;

import com.example.factory.common.Context;

/**
 * Lenovo 键盘实现类
 */
public class LenovoKeyboard implements Keyboard {

    @Override
    public void print() {
        System.out.println("Lenovo键盘正在执行打印操作...");
        System.out.println("使用Lenovo专有的打印驱动程序");
    }

    @Override
    public void input(Context context) {
        System.out.println("Lenovo键盘正在处理输入: " + context);
        System.out.println("Lenovo键盘特有的输入处理逻辑");
    }
}

