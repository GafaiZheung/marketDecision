package com.example.marketdecision.Bean;

public class ProfitInfo
{
    private String date;
    private String goodsCode;
    private String name;
    private String typeName;
    private String wholeSale;
    private String loss;
    private String saleNumber;
    private String sumPrice;
    private String profit;

    public ProfitInfo(String date, String goodsCode, String name, String typeName, String wholeSale, String loss, String sumPrice, String saleNumber, String profit)
    {
        this.date = date;
        this.saleNumber = saleNumber;
        this.goodsCode = goodsCode;
        this.name = name;
        this.wholeSale = wholeSale;
        this.loss = loss;
        this.sumPrice = sumPrice;
        this.profit = profit;
        this.typeName = typeName;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWholeSale() {
        return wholeSale;
    }

    public void setWholeSale(String wholeSale) {
        this.wholeSale = wholeSale;
    }

    public String getSaleNumber() {
        return saleNumber;
    }

    public void setSaleNumber(String saleNumber) {
        this.saleNumber = saleNumber;
    }

    public String getLoss() {
        return loss;
    }

    public void setLoss(String loss) {
        this.loss = loss;
    }

    public String getSumPrice() {
        return sumPrice;
    }

    public void setSumPrice(String sumPrice) {
        this.sumPrice = sumPrice;
    }

    public String getProfit() {
        return profit;
    }

    public void setProfit(String profit) {
        this.profit = profit;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
