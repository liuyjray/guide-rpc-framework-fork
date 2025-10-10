package com.example.common;

/**
 * 消息类型枚举
 * 用于定义网络通信中的消息类型
 *
 * @author JavaGuide
 */
public enum MessageType {

    /**
     * 请求消息
     */
    REQUEST((byte) 1, "请求消息"),

    /**
     * 响应消息
     */
    RESPONSE((byte) 2, "响应消息"),

    /**
     * 心跳消息
     */
    HEARTBEAT((byte) 3, "心跳消息"),

    /**
     * 错误消息
     */
    ERROR((byte) 4, "错误消息");

    private final byte code;
    private final String description;

    MessageType(byte code, String description) {
        this.code = code;
        this.description = description;
    }

    public byte getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    /**
     * 根据代码获取消息类型
     */
    public static MessageType getByCode(byte code) {
        for (MessageType type : values()) {
            if (type.code == code) {
                return type;
            }
        }
        throw new IllegalArgumentException("未知的消息类型代码: " + code);
    }

    @Override
    public String toString() {
        return "MessageType{" +
                "code=" + code +
                ", description='" + description + '\'' +
                '}';
    }
}

