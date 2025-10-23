package com.example.elasticsearch;

import java.util.*;

/**
 * Elasticsearch 示例程序
 * 演示如何连接本地 ES 并进行 CRUD 操作和聚合查询
 */
public class ElasticsearchDemo {

    public static void main(String[] args) {
        // 创建 ES 客户端，连接本地 Elasticsearch (localhost:9200)
        ElasticsearchClient esClient = new ElasticsearchClient("localhost", 9200);

        try {
            System.out.println("========== Elasticsearch Shop 索引查询示例 ==========\n");

            // 示例1: 查询所有商铺
            System.out.println("【示例1】查询所有商铺:");
            System.out.println("----------------------------------------");
            List<Shop> allShops = esClient.searchAll("shop");
            printShops(allShops);

            // 示例2: 使用 match 查询搜索商铺名称（推荐用于中文搜索）
            System.out.println("\n【示例2】使用 match 查询搜索 '肯德基':");
            System.out.println("----------------------------------------");
            List<Shop> matchShops = esClient.searchByMatch("shop", "shopname", "肯德基");
            printShops(matchShops);

            // 示例3: 使用 term 查询搜索城市ID（精确匹配）
            System.out.println("\n【示例3】使用 term 查询搜索 cityid=1:");
            System.out.println("----------------------------------------");
            List<Shop> termShops = esClient.searchByTerm("shop", "cityid", 1);
            printShops(termShops);

            // 示例4: 插入新的商铺数据
            System.out.println("\n【示例4】插入新的商铺数据:");
            System.out.println("----------------------------------------");
            Shop newShop = new Shop(
                    2000,
                    "麦当劳",
                    1,
                    "人民广场地铁站",
                    "31.23416900634766,121.47264099121094"
            );
            boolean success = esClient.indexShop("shop", "2000", newShop);
            System.out.println("插入结果: " + (success ? "成功" : "失败"));

            // 等待索引刷新
            Thread.sleep(1000);

            // 示例5: 验证新插入的数据
            System.out.println("\n【示例5】搜索新插入的 '麦当劳':");
            System.out.println("----------------------------------------");
            List<Shop> mcdonaldShops = esClient.searchByMatch("shop", "shopname", "麦当劳");
            printShops(mcdonaldShops);

            // 示例6: 批量插入文档
            System.out.println("\n【示例6】批量插入商铺数据:");
            System.out.println("----------------------------------------");
            Map<String, Shop> bulkShops = new HashMap<>();
            bulkShops.put("3000", new Shop(3000, "星巴克", 1, "南京西路1038号", "31.23,121.45"));
            bulkShops.put("3001", new Shop(3001, "星巴克", 1, "淮海中路999号", "31.22,121.46"));
            bulkShops.put("3002", new Shop(3002, "星巴克", 2, "北京路100号", "31.24,121.48"));

            int bulkCount = esClient.bulkIndexShops("shop", bulkShops);
            System.out.println("批量插入成功: " + bulkCount + " 条文档");

            // 等待索引刷新
            Thread.sleep(1000);

            // 示例7: 更新文档
            System.out.println("\n【示例7】更新文档 - 修改麦当劳的地址:");
            System.out.println("----------------------------------------");
            Map<String, Object> updateFields = new HashMap<>();
            updateFields.put("address", "人民广场地铁站1号出口");
            updateFields.put("shopname", "麦当劳(人民广场店)");

            boolean updateSuccess = esClient.updateShop("shop", "2000", updateFields);
            System.out.println("更新结果: " + (updateSuccess ? "成功" : "失败"));

            // 等待索引刷新
            Thread.sleep(1000);

            // 验证更新结果
            System.out.println("\n验证更新后的数据:");
            List<Shop> updatedShops = esClient.searchByTerm("shop", "shopid", 2000);
            printShops(updatedShops);

            // 示例8: 聚合查询 - 按城市统计商铺数量
            System.out.println("\n【示例8】聚合查询 - 按城市统计商铺数量:");
            System.out.println("----------------------------------------");
            Map<String, Long> cityStats = esClient.aggregateByField("shop", "cityid");
            for (Map.Entry<String, Long> entry : cityStats.entrySet()) {
                System.out.println("城市ID " + entry.getKey() + ": " + entry.getValue() + " 家商铺");
            }

            // 示例9: 聚合查询 - 计算平均 shopid
            System.out.println("\n【示例9】聚合查询 - 计算平均 shopid:");
            System.out.println("----------------------------------------");
            Double avgShopId = esClient.aggregateAvg("shop", "shopid");
            if (avgShopId != null) {
                System.out.println("平均 shopid: " + String.format("%.2f", avgShopId));
            }

            // 示例10: 删除单个文档
            System.out.println("\n【示例10】删除文档 - 删除麦当劳:");
            System.out.println("----------------------------------------");
            boolean deleteSuccess = esClient.deleteShop("shop", "2001");
            System.out.println("删除结果: " + (deleteSuccess ? "成功" : "失败"));

            // 等待索引刷新
            Thread.sleep(1000);

            // 示例11: 批量删除文档
            System.out.println("\n【示例11】批量删除文档 - 删除所有星巴克:");
            System.out.println("----------------------------------------");
            List<String> deleteIds = Arrays.asList("3000", "3001", "3002");
            int deleteCount = esClient.bulkDeleteShops("shop", deleteIds);
            System.out.println("批量删除成功: " + deleteCount + " 条文档");

            // 等待索引刷新
            Thread.sleep(1000);

            // 最终统计
            System.out.println("\n【最终统计】查询所有剩余商铺:");
            System.out.println("----------------------------------------");
            List<Shop> finalShops = esClient.searchAll("shop");
            System.out.println("剩余商铺总数: " + finalShops.size());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭客户端连接
            esClient.close();
        }
    }

    /**
     * 打印商铺列表
     */
    private static void printShops(List<Shop> shops) {
        if (shops.isEmpty()) {
            System.out.println("未找到任何结果");
            return;
        }

        System.out.println("共找到 " + shops.size() + " 条结果:\n");
        for (Shop shop : shops) {
            System.out.println(shop);
        }
    }
}

