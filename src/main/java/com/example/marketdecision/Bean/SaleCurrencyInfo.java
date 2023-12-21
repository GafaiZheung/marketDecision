package com.example.marketdecision.Bean;

public class SaleCurrencyInfo
{
    private String saleDate;
    private String saleTime;
    private String goodsCode;
    private String saleNumber;
    private String unitPrice;
    private String saleType;
    private String isDiscount;

    public SaleCurrencyInfo(String saleDate, String saleTime, String goodsCode, String unitPrice, String saleType, String saleNumber, String isDiscount)
    {
        this.saleDate = saleDate;
        this.saleNumber = saleNumber;
        this.saleTime = saleTime;
        this.goodsCode = goodsCode;
        this.unitPrice = unitPrice;
        this.saleType = saleType;
        this.isDiscount = isDiscount;
    }

    public String getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(String saleDate) {
        this.saleDate = saleDate;
    }

    public String getSaleTime() {
        return saleTime;
    }

    public void setSaleTime(String saleTime) {
        this.saleTime = saleTime;
    }

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }

    public String getSaleNumber() {
        return saleNumber;
    }

    public void setSaleNumber(String saleNumber) {
        this.saleNumber = saleNumber;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getSaleType() {
        return saleType;
    }

    public void setSaleType(String saleType) {
        this.saleType = saleType;
    }

    public String getIsDiscount() {
        return isDiscount;
    }

    public void setIsDiscount(String isDiscount) {
        this.isDiscount = isDiscount;
    }
}
