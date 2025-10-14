package com.example.spring.circular;

/**
 * 对象工厂接口
 *
 * 表示一个可以按需创建对象的工厂。
 * 这个接口用于三级缓存中，存储可以在需要时创建早期Bean引用的工厂方法。
 *
 * 记忆技巧：
 * - 把这个想象成房子类比中的"建房图纸"
 * - 包含如何创建对象的指令
 * - 存储在三级缓存中直到需要时使用
 *
 * @author demo
 */
@FunctionalInterface
public interface ObjectFactory<T> {

    /**
     * 创建并返回一个对象实例
     *
     * @return 创建的对象实例
     */
    T getObject();
}

