package com.example.factory.abstractfactory;

/**
 * HP 显示器实现类 - 抽象工厂模式
 */
public class HPMonitor implements Monitor {

    @Override
    public void play() {
        System.out.println("HP显示器正在播放内容...");
        System.out.println("使用HP DreamColor技术，提供专业级色彩准确度");
    }
}

