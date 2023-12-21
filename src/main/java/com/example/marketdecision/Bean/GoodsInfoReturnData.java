package com.example.marketdecision.Bean;

import java.util.List;

public class GoodsInfoReturnData
{
    private int status;
    private int total;
    private List<GoodsInfo> goodsInfos;

    public GoodsInfoReturnData(int status, int total, List<GoodsInfo> goodsInfos)
    {
        this.total = total;
        this.goodsInfos = goodsInfos;
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<GoodsInfo> getGoodsInfos() {
        return goodsInfos;
    }

    public void setGoodsInfos(List<GoodsInfo> goodsInfos) {
        this.goodsInfos = goodsInfos;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
