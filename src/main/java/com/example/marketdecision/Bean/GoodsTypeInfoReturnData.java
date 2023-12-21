package com.example.marketdecision.Bean;

import java.util.List;

public class GoodsTypeInfoReturnData
{
    private int status;
    private int total;
    private List<GoodsTypesInfo> goodsTypesInfos;

    public GoodsTypeInfoReturnData(int status, int total, List<GoodsTypesInfo> goodsTypesInfos)
    {
        this.status = status;
        this.goodsTypesInfos = goodsTypesInfos;
        this.total = total;
    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<GoodsTypesInfo> getGoodsTypesInfos() {
        return goodsTypesInfos;
    }

    public void setGoodsTypesInfos(List<GoodsTypesInfo> goodsTypesInfos) {
        this.goodsTypesInfos = goodsTypesInfos;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
