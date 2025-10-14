package com.example.factory.method;

import com.example.factory.common.Context;

/**
 * 键盘接口 - 工厂方法模式
 * 定义键盘的基本功能
 */
public interface Keyboard {
    /**
     * 打印功能
     */
    void print();

    /**
     * 输入功能
     * @param context 上下文信息
     */
    void input(Context context);
}

