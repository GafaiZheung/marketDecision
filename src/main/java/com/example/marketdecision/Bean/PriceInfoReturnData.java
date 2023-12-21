package com.example.marketdecision.Bean;

import java.util.List;

public class PriceInfoReturnData
{
    private int status;
    private int total;
    private List<PriceInfo> priceInfos;

    public PriceInfoReturnData(int status, int total, List<PriceInfo> priceInfos)
    {
        this.status = status;
        this.priceInfos = priceInfos;
        this.total = total;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<PriceInfo> getPriceInfos() {
        return priceInfos;
    }

    public void setPriceInfos(List<PriceInfo> priceInfos) {
        this.priceInfos = priceInfos;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
