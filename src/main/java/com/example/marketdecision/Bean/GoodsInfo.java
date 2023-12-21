package com.example.marketdecision.Bean;

public class GoodsInfo
{
    private String goodsCode;
    private String name;
    private String typeCode;
    private String typeName;

    public GoodsInfo(String goodsCode, String name, String typeCode, String typeName)
    {
        this.goodsCode = goodsCode;
        this.name = name;
        this.typeCode = typeCode;
        this.typeName = typeName;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}

