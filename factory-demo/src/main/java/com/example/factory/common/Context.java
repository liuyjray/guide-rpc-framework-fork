package com.example.factory.common;

/**
 * 上下文类
 * 用于传递上下文信息
 */
public class Context {
    private String data;
    private String operation;

    public Context() {
    }

    public Context(String data, String operation) {
        this.data = data;
        this.operation = operation;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    @Override
    public String toString() {
        return "Context{" +
                "data='" + data + '\'' +
                ", operation='" + operation + '\'' +
                '}';
    }
}

