package com.example.factory.singleton;

import com.example.factory.common.Context;

/**
 * Dell 键盘实现类 - 单例工厂模式
 */
public class DellKeyboard implements Keyboard {

    @Override
    public void print() {
        System.out.println("Dell键盘正在执行打印操作...");
        System.out.println("使用Dell专有的打印驱动程序和游戏优化技术");
    }

    @Override
    public void input(Context context) {
        System.out.println("Dell键盘正在处理输入: " + context);
        System.out.println("Dell键盘特有的输入处理逻辑和Alienware游戏体验");
    }
}

