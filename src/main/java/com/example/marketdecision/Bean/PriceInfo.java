package com.example.marketdecision.Bean;

public class PriceInfo
{
    private String date;
    private String goodsCode;
    private String wholesale;
    public PriceInfo(String goodsCode, String date, String wholesale)
    {
        this.date = date;
        this.wholesale = wholesale;
        this.goodsCode = goodsCode;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }

    public String getWholesale() {
        return wholesale;
    }

    public void setWholesale(String wholesale) {
        this.wholesale = wholesale;
    }
}
