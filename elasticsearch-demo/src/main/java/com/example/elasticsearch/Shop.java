package com.example.elasticsearch;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 商铺实体类
 */
public class Shop {

    @JsonProperty("shopid")
    private Integer shopId;

    @JsonProperty("shopname")
    private String shopName;

    @JsonProperty("cityid")
    private Integer cityId;

    @JsonProperty("address")
    private String address;

    @JsonProperty("shoppoi")
    private String shopPoi;

    public Shop() {
    }

    public Shop(Integer shopId, String shopName, Integer cityId, String address, String shopPoi) {
        this.shopId = shopId;
        this.shopName = shopName;
        this.cityId = cityId;
        this.address = address;
        this.shopPoi = shopPoi;
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getShopPoi() {
        return shopPoi;
    }

    public void setShopPoi(String shopPoi) {
        this.shopPoi = shopPoi;
    }

    @Override
    public String toString() {
        return "Shop{" +
                "shopId=" + shopId +
                ", shopName='" + shopName + '\'' +
                ", cityId=" + cityId +
                ", address='" + address + '\'' +
                ", shopPoi='" + shopPoi + '\'' +
                '}';
    }
}

