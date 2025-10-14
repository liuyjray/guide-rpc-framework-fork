#!/bin/bash

echo "🎭 Spring循环依赖演示程序"
echo "========================"

# 检查是否有Maven
if ! command -v mvn &> /dev/null; then
    echo "❌ 错误：未找到Maven，请先安装Maven"
    exit 1
fi

# 编译项目
echo "📦 正在编译项目..."
mvn compile

if [ $? -eq 0 ]; then
    echo "✅ 编译成功！"
    echo ""
    echo "🚀 正在运行演示..."
    echo ""
    mvn exec:java
else
    echo "❌ 编译失败，请检查代码"
    exit 1
fi

