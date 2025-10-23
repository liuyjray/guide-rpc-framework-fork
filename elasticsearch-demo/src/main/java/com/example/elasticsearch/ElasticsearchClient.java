package com.example.elasticsearch;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.Avg;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Elasticsearch 客户端工具类
 */
public class ElasticsearchClient {

    private static final Logger logger = LoggerFactory.getLogger(ElasticsearchClient.class);

    private RestHighLevelClient client;
    private ObjectMapper objectMapper;

    /**
     * 构造函数，初始化 ES 客户端
     * @param host ES 服务器地址
     * @param port ES 服务器端口
     */
    public ElasticsearchClient(String host, int port) {
        this.client = new RestHighLevelClient(
                RestClient.builder(new HttpHost(host, port, "http"))
        );
        this.objectMapper = new ObjectMapper();
        logger.info("Elasticsearch 客户端初始化成功: {}:{}", host, port);
    }

    /**
     * 使用 match 查询搜索商铺（推荐用于中文搜索）
     * @param index 索引名称
     * @param fieldName 字段名
     * @param value 搜索值
     * @return 商铺列表
     */
    public List<Shop> searchByMatch(String index, String fieldName, String value) {
        List<Shop> shops = new ArrayList<>();
        try {
            SearchRequest searchRequest = new SearchRequest(index);
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

            // 使用 match 查询（会分词）
            sourceBuilder.query(QueryBuilders.matchQuery(fieldName, value));
            sourceBuilder.from(0);
            sourceBuilder.size(10);

            searchRequest.source(sourceBuilder);

            SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);

            logger.info("搜索完成: 找到 {} 条结果", response.getHits().getTotalHits().value);

            for (SearchHit hit : response.getHits().getHits()) {
                Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                Shop shop = objectMapper.convertValue(sourceAsMap, Shop.class);
                shops.add(shop);
            }
        } catch (IOException e) {
            logger.error("搜索失败: index={}, field={}, value={}", index, fieldName, value, e);
        }
        return shops;
    }

    /**
     * 使用 term 查询搜索（精确匹配，适用于 keyword 类型字段）
     * @param index 索引名称
     * @param fieldName 字段名
     * @param value 搜索值
     * @return 商铺列表
     */
    public List<Shop> searchByTerm(String index, String fieldName, Object value) {
        List<Shop> shops = new ArrayList<>();
        try {
            SearchRequest searchRequest = new SearchRequest(index);
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

            // 使用 term 查询（精确匹配）
            sourceBuilder.query(QueryBuilders.termQuery(fieldName, value));
            sourceBuilder.from(0);
            sourceBuilder.size(10);

            searchRequest.source(sourceBuilder);

            SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);

            logger.info("搜索完成: 找到 {} 条结果", response.getHits().getTotalHits().value);

            for (SearchHit hit : response.getHits().getHits()) {
                Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                Shop shop = objectMapper.convertValue(sourceAsMap, Shop.class);
                shops.add(shop);
            }
        } catch (IOException e) {
            logger.error("搜索失败: index={}, field={}, value={}", index, fieldName, value, e);
        }
        return shops;
    }

    /**
     * 查询所有文档
     * @param index 索引名称
     * @return 商铺列表
     */
    public List<Shop> searchAll(String index) {
        List<Shop> shops = new ArrayList<>();
        try {
            SearchRequest searchRequest = new SearchRequest(index);
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

            // 查询所有
            sourceBuilder.query(QueryBuilders.matchAllQuery());
            sourceBuilder.from(0);
            sourceBuilder.size(100);

            searchRequest.source(sourceBuilder);

            SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);

            logger.info("查询所有文档: 找到 {} 条结果", response.getHits().getTotalHits().value);

            for (SearchHit hit : response.getHits().getHits()) {
                Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                Shop shop = objectMapper.convertValue(sourceAsMap, Shop.class);
                shops.add(shop);
            }
        } catch (IOException e) {
            logger.error("查询所有文档失败: index={}", index, e);
        }
        return shops;
    }

    /**
     * 插入文档
     * @param index 索引名称
     * @param id 文档ID
     * @param shop 商铺对象
     * @return 是否成功
     */
    public boolean indexShop(String index, String id, Shop shop) {
        try {
            String jsonString = objectMapper.writeValueAsString(shop);
            IndexRequest request = new IndexRequest(index)
                    .id(id)
                    .source(jsonString, XContentType.JSON);

            IndexResponse response = client.index(request, RequestOptions.DEFAULT);
            logger.info("文档插入成功: index={}, id={}, result={}", index, id, response.getResult());
            return true;
        } catch (IOException e) {
            logger.error("插入文档失败: index={}, id={}", index, id, e);
            return false;
        }
    }

    /**
     * 批量插入文档 (Bulk API)
     * @param index 索引名称
     * @param shops 商铺列表 (Map<文档ID, Shop对象>)
     * @return 成功插入的数量
     */
    public int bulkIndexShops(String index, Map<String, Shop> shops) {
        try {
            BulkRequest bulkRequest = new BulkRequest();

            for (Map.Entry<String, Shop> entry : shops.entrySet()) {
                String id = entry.getKey();
                Shop shop = entry.getValue();
                String jsonString = objectMapper.writeValueAsString(shop);

                IndexRequest indexRequest = new IndexRequest(index)
                        .id(id)
                        .source(jsonString, XContentType.JSON);
                bulkRequest.add(indexRequest);
            }

            BulkResponse bulkResponse = client.bulk(bulkRequest, RequestOptions.DEFAULT);

            if (bulkResponse.hasFailures()) {
                logger.warn("批量插入部分失败: {}", bulkResponse.buildFailureMessage());
                return shops.size() - bulkResponse.getItems().length;
            }

            logger.info("批量插入成功: {} 条文档", shops.size());
            return shops.size();
        } catch (IOException e) {
            logger.error("批量插入失败", e);
            return 0;
        }
    }

    /**
     * 更新文档
     * @param index 索引名称
     * @param id 文档ID
     * @param updateFields 要更新的字段 (字段名 -> 新值)
     * @return 是否成功
     */
    public boolean updateShop(String index, String id, Map<String, Object> updateFields) {
        try {
            UpdateRequest updateRequest = new UpdateRequest(index, id)
                    .doc(updateFields);

            UpdateResponse response = client.update(updateRequest, RequestOptions.DEFAULT);
            logger.info("文档更新成功: index={}, id={}, result={}", index, id, response.getResult());
            return true;
        } catch (IOException e) {
            logger.error("更新文档失败: index={}, id={}", index, id, e);
            return false;
        }
    }

    /**
     * 删除文档
     * @param index 索引名称
     * @param id 文档ID
     * @return 是否成功
     */
    public boolean deleteShop(String index, String id) {
        try {
            DeleteRequest deleteRequest = new DeleteRequest(index, id);
            DeleteResponse response = client.delete(deleteRequest, RequestOptions.DEFAULT);
            logger.info("文档删除成功: index={}, id={}, result={}", index, id, response.getResult());
            return true;
        } catch (IOException e) {
            logger.error("删除文档失败: index={}, id={}", index, id, e);
            return false;
        }
    }

    /**
     * 批量删除文档
     * @param index 索引名称
     * @param ids 文档ID列表
     * @return 成功删除的数量
     */
    public int bulkDeleteShops(String index, List<String> ids) {
        try {
            BulkRequest bulkRequest = new BulkRequest();

            for (String id : ids) {
                DeleteRequest deleteRequest = new DeleteRequest(index, id);
                bulkRequest.add(deleteRequest);
            }

            BulkResponse bulkResponse = client.bulk(bulkRequest, RequestOptions.DEFAULT);

            if (bulkResponse.hasFailures()) {
                logger.warn("批量删除部分失败: {}", bulkResponse.buildFailureMessage());
                return ids.size() - bulkResponse.getItems().length;
            }

            logger.info("批量删除成功: {} 条文档", ids.size());
            return ids.size();
        } catch (IOException e) {
            logger.error("批量删除失败", e);
            return 0;
        }
    }

    /**
     * 聚合查询 - 按字段分组统计数量
     * @param index 索引名称
     * @param fieldName 聚合字段名
     * @return 聚合结果 (字段值 -> 文档数量)
     */
    public Map<String, Long> aggregateByField(String index, String fieldName) {
        Map<String, Long> result = new HashMap<>();
        try {
            SearchRequest searchRequest = new SearchRequest(index);
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

            // 添加聚合
            sourceBuilder.aggregation(
                    AggregationBuilders.terms("group_by_" + fieldName)
                            .field(fieldName)
                            .size(100)
            );
            sourceBuilder.size(0); // 不返回文档，只返回聚合结果

            searchRequest.source(sourceBuilder);
            SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);

            // 解析聚合结果
            Terms terms = response.getAggregations().get("group_by_" + fieldName);
            for (Terms.Bucket bucket : terms.getBuckets()) {
                result.put(bucket.getKeyAsString(), bucket.getDocCount());
            }

            logger.info("聚合查询完成: 找到 {} 个分组", result.size());
        } catch (IOException e) {
            logger.error("聚合查询失败: index={}, field={}", index, fieldName, e);
        }
        return result;
    }

    /**
     * 聚合查询 - 计算字段平均值
     * @param index 索引名称
     * @param fieldName 字段名
     * @return 平均值
     */
    public Double aggregateAvg(String index, String fieldName) {
        try {
            SearchRequest searchRequest = new SearchRequest(index);
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

            // 添加平均值聚合
            sourceBuilder.aggregation(
                    AggregationBuilders.avg("avg_" + fieldName)
                            .field(fieldName)
            );
            sourceBuilder.size(0);

            searchRequest.source(sourceBuilder);
            SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);

            // 解析聚合结果
            Avg avg = response.getAggregations().get("avg_" + fieldName);
            double avgValue = avg.getValue();

            logger.info("平均值聚合完成: {}={}", fieldName, avgValue);
            return avgValue;
        } catch (IOException e) {
            logger.error("平均值聚合失败: index={}, field={}", index, fieldName, e);
            return null;
        }
    }

    /**
     * 带高亮的搜索
     * @param index 索引名称
     * @param fieldName 字段名
     * @param value 搜索值
     * @return 搜索结果 Map (Shop对象 -> 高亮内容)
     */
    public Map<Shop, String> searchWithHighlight(String index, String fieldName, String value) {
        Map<Shop, String> result = new HashMap<>();
        try {
            SearchRequest searchRequest = new SearchRequest(index);
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

            // 使用 match 查询
            sourceBuilder.query(QueryBuilders.matchQuery(fieldName, value));

            // 配置高亮
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            highlightBuilder.field(fieldName);  // 高亮字段
            highlightBuilder.preTags("<em>");   // 高亮前缀标签
            highlightBuilder.postTags("</em>"); // 高亮后缀标签
            sourceBuilder.highlighter(highlightBuilder);

            sourceBuilder.from(0);
            sourceBuilder.size(10);

            searchRequest.source(sourceBuilder);
            SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);

            logger.info("高亮搜索完成: 找到 {} 条结果", response.getHits().getTotalHits().value);

            for (SearchHit hit : response.getHits().getHits()) {
                // 获取原始文档
                Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                Shop shop = objectMapper.convertValue(sourceAsMap, Shop.class);

                // 获取高亮内容
                Map<String, HighlightField> highlightFields = hit.getHighlightFields();
                String highlightText = "";
                if (highlightFields.containsKey(fieldName)) {
                    HighlightField highlightField = highlightFields.get(fieldName);
                    highlightText = highlightField.getFragments()[0].string();
                }

                result.put(shop, highlightText);
            }
        } catch (IOException e) {
            logger.error("高亮搜索失败: index={}, field={}, value={}", index, fieldName, value, e);
        }
        return result;
    }

    /**
     * 关闭客户端连接
     */
    public void close() {
        try {
            if (client != null) {
                client.close();
                logger.info("Elasticsearch 客户端已关闭");
            }
        } catch (IOException e) {
            logger.error("关闭 Elasticsearch 客户端失败", e);
        }
    }
}

