package com.example.marketdecision.Bean;

public class LossData
{
    private String goodsCode;
    private String goodsName;
    private float attritionRate;
    public LossData(String goodsCode, String goodsName, float attritionRate)
    {
        this.attritionRate = attritionRate;
        this.goodsName = goodsName;
        this.goodsCode = goodsCode;
    }

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public float getAttritionRate() {
        return attritionRate;
    }

    public void setAttritionRate(float attritionRate) {
        this.attritionRate = attritionRate;
    }
}
