package com.example.marketdecision.Bean;

import java.util.List;

public class SaleCurrencyInfoReturnData
{
    private int status;
    private int total;
    private List<SaleCurrencyInfo> saleCurrencyInfos;

    public SaleCurrencyInfoReturnData(int status, int total, List<SaleCurrencyInfo> saleCurrencyInfos)
    {
        this.status = status;
        this.saleCurrencyInfos = saleCurrencyInfos;
        this.total = total;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<SaleCurrencyInfo> getSaleCurrencyInfos() {
        return saleCurrencyInfos;
    }

    public void setSaleCurrencyInfos(List<SaleCurrencyInfo> saleCurrencyInfos) {
        this.saleCurrencyInfos = saleCurrencyInfos;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
