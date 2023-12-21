package com.example.marketdecision.Bean;

public class Decision
{
    private String date;
    private String goodsCode;
    private String name;
    private String wholeSale;
    private String loss;
    private String saleNumber;//现有销量
    private String formerUnitPrice;//原价
    private String unitPrice;//定价
    private String isSend;//策略
    public Decision(String date, String goodsCode, String name, String wholeSale, String loss, String saleNumber, String formerUnitPrice, String unitPrice, String isSend)
    {
        this.date = date;
        this.goodsCode = goodsCode;
        this.loss = loss;
        this.name = name;
        this.wholeSale = wholeSale;
        this.saleNumber = saleNumber;
        this.formerUnitPrice = formerUnitPrice;
        this.unitPrice = unitPrice;
        this.isSend = isSend;
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

    public String getLoss() {
        return loss;
    }

    public void setLoss(String loss) {
        this.loss = loss;
    }

    public String getSaleNumber() {
        return saleNumber;
    }

    public void setSaleNumber(String saleNumber) {
        this.saleNumber = saleNumber;
    }

    public String getFormerUnitPrice() {
        return formerUnitPrice;
    }

    public void setFormerUnitPrice(String formerUnitPrice) {
        this.formerUnitPrice = formerUnitPrice;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getIsSend() {
        return isSend;
    }

    public void setIsSend(String isSend) {
        this.isSend = isSend;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
