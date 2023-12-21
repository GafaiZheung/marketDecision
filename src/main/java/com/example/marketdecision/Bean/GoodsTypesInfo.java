package com.example.marketdecision.Bean;


public class GoodsTypesInfo
{
    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    String typeCode;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    String type;

    public GoodsTypesInfo(String typeCode, String type)
    {
        this.typeCode = typeCode;
        this.type = type;
    }
}
