# Elasticsearch Demo

> 一个完整的 Elasticsearch 客户端示例项目，演示 CRUD 操作、批量处理和聚合查询。

[![Elasticsearch](https://img.shields.io/badge/Elasticsearch-7.10.1-green.svg)](https://www.elastic.co/elasticsearch/)
[![Java](https://img.shields.io/badge/Java-8-blue.svg)](https://www.oracle.com/java/)
[![Maven](https://img.shields.io/badge/Maven-3.x-red.svg)](https://maven.apache.org/)

## 项目结构

```
elasticsearch-demo/
├── src/main/java/com/example/elasticsearch/
│   ├── Shop.java                    # 商铺实体类
│   ├── ElasticsearchClient.java     # ES 客户端工具类
│   └── ElasticsearchDemo.java       # 示例程序
├── pom.xml                          # Maven 配置
└── README.md                        # 项目说明
```

## 功能特性

### 1. **ElasticsearchClient** - ES 客户端工具类

提供以下功能：

#### 基础操作
- ✅ **连接 Elasticsearch**: 初始化 RestHighLevelClient
- ✅ **插入文档**: `indexShop()` - 插入单个商铺数据
- ✅ **批量插入**: `bulkIndexShops()` - 批量插入多个文档 (Bulk API)
- ✅ **更新文档**: `updateShop()` - 更新指定字段
- ✅ **删除文档**: `deleteShop()` - 删除单个文档
- ✅ **批量删除**: `bulkDeleteShops()` - 批量删除多个文档

#### 查询操作
- ✅ **Match 查询**: `searchByMatch()` - 分词查询（推荐用于中文搜索）
- ✅ **Term 查询**: `searchByTerm()` - 精确匹配查询
- ✅ **查询所有**: `searchAll()` - 查询索引中的所有文档

#### 聚合查询
- ✅ **分组统计**: `aggregateByField()` - 按字段分组统计数量
- ✅ **平均值计算**: `aggregateAvg()` - 计算字段平均值

### 2. **Shop** - 商铺实体类

字段映射：
- `shopId` - 商铺ID
- `shopName` - 商铺名称
- `cityId` - 城市ID
- `address` - 地址
- `shopPoi` - 地理位置坐标

### 3. **ElasticsearchDemo** - 示例程序

演示了 11 个常见场景：
1. 查询所有商铺
2. 使用 match 查询搜索商铺名称
3. 使用 term 查询搜索城市ID
4. 插入新的商铺数据
5. 验证新插入的数据
6. **批量插入商铺数据** (Bulk API)
7. **更新文档** - 修改商铺信息
8. **聚合查询** - 按城市统计商铺数量
9. **聚合查询** - 计算平均 shopid
10. **删除单个文档**
11. **批量删除文档**

## 索引 Schema

### shop 索引结构

```json
PUT /shop
{
  "mappings": {
    "properties": {
      "shopid": {
        "type": "integer"
      },
      "shopname": {
        "type": "text",
        "fields": {
          "keyword": {
            "type": "keyword",
            "ignore_above": 256
          }
        }
      },
      "cityid": {
        "type": "integer"
      },
      "address": {
        "type": "text",
        "fields": {
          "keyword": {
            "type": "keyword",
            "ignore_above": 256
          }
        }
      },
      "shoppoi": {
        "type": "geo_point"
      }
    }
  }
}
```

### 字段说明

| 字段名 | 类型 | 说明 | 示例 |
|--------|------|------|------|
| `shopid` | `integer` | 商铺ID，唯一标识 | 1000 |
| `shopname` | `text` + `keyword` | 商铺名称，支持全文搜索和精确匹配 | "肯德基" |
| `cityid` | `integer` | 城市ID，用于分组统计 | 1 |
| `address` | `text` + `keyword` | 地址，支持全文搜索 | "胜辛路426号" |
| `shoppoi` | `geo_point` | 地理位置坐标 (纬度,经度) | "31.331,121.250" |

### 字段类型说明

- **text**: 会被分词，适合全文搜索（如商铺名称、地址）
- **keyword**: 不分词，适合精确匹配、排序、聚合
- **integer**: 整数类型，适合数值计算和范围查询
- **geo_point**: 地理位置类型，支持地理位置搜索

## 前置条件

### 1. 启动 Elasticsearch 服务

```bash
cd /Users/ray/elasticsearch/elasticsearch-7.10.1
./bin/elasticsearch
```

### 2. 创建索引（可选）

如果索引不存在，可以先创建索引并定义 mapping:

```bash
# 创建索引并定义 mapping
curl -X PUT 'localhost:9200/shop' -H 'Content-Type: application/json' -d '{
  "mappings": {
    "properties": {
      "shopid": {"type": "integer"},
      "shopname": {
        "type": "text",
        "fields": {"keyword": {"type": "keyword"}}
      },
      "cityid": {"type": "integer"},
      "address": {
        "type": "text",
        "fields": {"keyword": {"type": "keyword"}}
      },
      "shoppoi": {"type": "geo_point"}
    }
  }
}'
```

### 3. 插入测试数据

```bash
# 插入肯德基数据
curl -X POST 'localhost:9200/shop/_doc/1000' -H 'Content-Type: application/json' -d '{
  "shopid": 1000,
  "shopname": "肯德基",
  "cityid": 1,
  "address": "胜辛路426号2层222-3号商铺",
  "shoppoi": "31.331241607666016,121.25092315673828"
}'

curl -X POST 'localhost:9200/shop/_doc/1001' -H 'Content-Type: application/json' -d '{
  "shopid": 1001,
  "shopname": "肯德基",
  "cityid": 1,
  "address": "浦江镇召楼路1976号",
  "shoppoi": "31.05731773376465,121.5353012084961"
}'

curl -X POST 'localhost:9200/shop/_doc/1002' -H 'Content-Type: application/json' -d '{
  "shopid": 1002,
  "shopname": "肯德基",
  "cityid": 1,
  "address": "长宁区北新泾四村5号",
  "shoppoi": "31.21916961669922,121.36328125"
}'
```

### 4. 验证索引和数据

```bash
# 查看索引 mapping
curl -X GET 'localhost:9200/shop/_mapping?pretty'

# 查看所有数据
curl -X GET 'localhost:9200/shop/_search?pretty'

# 统计文档数量
curl -X GET 'localhost:9200/shop/_count?pretty'
```

## 运行方式

### 方式1: 使用 Maven 编译运行

```bash
# 进入项目根目录
cd /Users/ray/codebase/improve/guide-rpc-framework

# 编译项目
mvn clean compile -pl elasticsearch-demo

# 运行示例程序
mvn exec:java -pl elasticsearch-demo -Dexec.mainClass="com.example.elasticsearch.ElasticsearchDemo"
```

### 方式2: 使用 IDE 运行

1. 在 IDEA 中打开项目
2. 找到 `ElasticsearchDemo.java`
3. 右键 -> Run 'ElasticsearchDemo.main()'

## 示例输出

```
========== Elasticsearch Shop 索引查询示例 ==========

【示例1】查询所有商铺:
----------------------------------------
共找到 6 条结果:

Shop{shopId=1000, shopName='肯德基', cityId=1, address='胜辛路426号2层222-3号商铺', shopPoi='31.331241607666016,121.25092315673828'}
Shop{shopId=1001, shopName='肯德基', cityId=1, address='浦江镇召楼路1976号', shopPoi='31.05731773376465,121.5353012084961'}
...

【示例2】使用 match 查询搜索 '肯德基':
----------------------------------------
共找到 6 条结果:
...
```

## 核心代码说明

### 1. 查询操作

#### Match 查询 vs Term 查询

```java
// Match 查询 - 会分词，适合中文搜索
List<Shop> shops = esClient.searchByMatch("shop", "shopname", "肯德基");

// Term 查询 - 精确匹配，适合 keyword、数字等类型
List<Shop> shops = esClient.searchByTerm("shop", "cityid", 1);
```

### 2. 批量操作

#### 批量插入 (Bulk API)

```java
Map<String, Shop> bulkShops = new HashMap<>();
bulkShops.put("3000", new Shop(3000, "星巴克", 1, "南京西路1038号", "31.23,121.45"));
bulkShops.put("3001", new Shop(3001, "星巴克", 1, "淮海中路999号", "31.22,121.46"));

int count = esClient.bulkIndexShops("shop", bulkShops);
System.out.println("批量插入成功: " + count + " 条");
```

#### 批量删除

```java
List<String> deleteIds = Arrays.asList("3000", "3001", "3002");
int count = esClient.bulkDeleteShops("shop", deleteIds);
```

### 3. 更新文档

```java
Map<String, Object> updateFields = new HashMap<>();
updateFields.put("address", "新地址");
updateFields.put("shopname", "新店名");

boolean success = esClient.updateShop("shop", "2000", updateFields);
```

### 4. 删除文档

```java
boolean success = esClient.deleteShop("shop", "2000");
```

### 5. 聚合查询

#### 分组统计

```java
// 按城市ID统计商铺数量
Map<String, Long> cityStats = esClient.aggregateByField("shop", "cityid");
for (Map.Entry<String, Long> entry : cityStats.entrySet()) {
    System.out.println("城市ID " + entry.getKey() + ": " + entry.getValue() + " 家商铺");
}
```

#### 计算平均值

```java
// 计算平均 shopid
Double avgShopId = esClient.aggregateAvg("shop", "shopid");
System.out.println("平均 shopid: " + avgShopId);
```

### 为什么 term 查询搜不到中文？

- **text 类型字段会被分词**: "肯德基" → ["肯", "德", "基"]
- **term 查询不分词**: 精确查找 "肯德基" 这个完整词
- **解决方案**: 使用 `match` 查询或 `shopname.keyword` 字段

## 依赖说明

### 核心依赖

```xml
<!-- Elasticsearch REST High Level Client (已包含 elasticsearch 核心依赖) -->
<dependency>
    <groupId>org.elasticsearch.client</groupId>
    <artifactId>elasticsearch-rest-high-level-client</artifactId>
    <version>7.10.1</version>
</dependency>

<!-- Jackson for JSON processing -->
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.11.0</version>
</dependency>
```

### 依赖说明

- **elasticsearch-rest-high-level-client**: ES 官方 Java 客户端
  - 已经包含了 `elasticsearch` 核心依赖,无需单独引入
  - 提供了完整的 REST API 封装
  - 支持所有 ES 操作 (CRUD、搜索、聚合等)

- **jackson-databind**: JSON 序列化/反序列化
  - 用于 Java 对象与 JSON 之间的转换
  - ES Client 内部也使用 Jackson

### 为什么只需要 Client 依赖?

`elasticsearch-rest-high-level-client` 是一个**客户端库**,它通过 HTTP REST API 与 Elasticsearch 服务器通信:

```
Java 应用 (Client)  ←→  HTTP REST API  ←→  Elasticsearch 服务器
```

- **Client 端**: 只需要 `elasticsearch-rest-high-level-client` 依赖
- **Server 端**: 需要完整的 Elasticsearch 服务器安装包

这样可以保持客户端轻量级,避免引入不必要的服务器端依赖。

## 注意事项

1. **版本匹配**: 客户端版本应与 Elasticsearch 服务器版本一致（本项目使用 7.10.1）
2. **连接地址**: 默认连接 `localhost:9200`，可在代码中修改
3. **资源释放**: 使用完毕后记得调用 `close()` 关闭连接
4. **异常处理**: 生产环境建议添加更完善的异常处理和重试机制

## 扩展功能

已实现的功能：
- ✅ 批量插入数据 (Bulk API)
- ✅ 更新文档
- ✅ 删除文档
- ✅ 批量删除
- ✅ 聚合查询（分组统计、平均值）

可以继续添加以下功能：
- 🔍 地理位置搜索（基于 `shoppoi` 字段）
- 🎯 复杂的 bool 查询组合
- 🔄 批量更新 (Bulk Update)
- 📊 更多聚合类型（最大值、最小值、求和等）
- 🔎 高亮显示搜索结果
- 📄 滚动查询 (Scroll API)
- 🎨 自定义分词器

## 参考资料

- [Elasticsearch 官方文档](https://www.elastic.co/guide/en/elasticsearch/reference/7.10/index.html)
- [Java High Level REST Client](https://www.elastic.co/guide/en/elasticsearch/client/java-rest/7.10/java-rest-high.html)

