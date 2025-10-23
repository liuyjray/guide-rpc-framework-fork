#!/bin/bash

# Elasticsearch Demo 运行脚本

echo "=========================================="
echo "  Elasticsearch Demo 启动脚本"
echo "=========================================="
echo ""

# 检查 Elasticsearch 是否运行
echo "检查 Elasticsearch 服务状态..."
if ! curl -s http://localhost:9200/ > /dev/null 2>&1; then
    echo "❌ Elasticsearch 未运行!"
    echo "请先启动 Elasticsearch:"
    echo "  cd /Users/ray/elasticsearch/elasticsearch-7.10.1"
    echo "  ./bin/elasticsearch"
    exit 1
fi

echo "✅ Elasticsearch 正在运行"
echo ""

# 编译项目
echo "编译项目..."
mvn clean compile -q

if [ $? -ne 0 ]; then
    echo "❌ 编译失败!"
    exit 1
fi

echo "✅ 编译成功"
echo ""

# 运行程序
echo "运行示例程序..."
echo "=========================================="
echo ""

java -cp "target/classes:$(mvn dependency:build-classpath -DincludeScope=compile -Dmdep.outputFile=/dev/stdout -q)" \
    com.example.elasticsearch.ElasticsearchDemo 2>&1 | grep -v "ERROR StatusLogger"

echo ""
echo "=========================================="
echo "程序执行完毕"

