package com.example.cglibproxy;

/**
 * 产品服务类（没有接口，用于演示CGLIB代理）
 */
public class ProductService {

    /**
     * 根据产品ID查询产品信息
     */
    public String getProductById(Long productId) {
        System.out.println("正在查询产品ID: " + productId);
        return "产品" + productId + "的详细信息";
    }

    /**
     * 添加新产品
     */
    public boolean addProduct(String productName, double price) {
        System.out.println("正在添加产品: " + productName + ", 价格: " + price);
        return true;
    }

    /**
     * 删除产品
     */
    public boolean deleteProduct(Long productId) {
        System.out.println("正在删除产品ID: " + productId);
        return true;
    }

    /**
     * final方法，无法被代理
     */
    public final String getFinalMethod() {
        return "这是final方法，CGLIB无法代理";
    }
}

