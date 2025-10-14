package com.example.factory.abstractfactory;

/**
 * Dell 显示器实现类 - 抽象工厂模式
 */
public class DellMonitor implements Monitor {

    @Override
    public void play() {
        System.out.println("Dell显示器正在播放内容...");
        System.out.println("使用Dell UltraSharp技术，提供卓越的色彩表现");
    }
}

