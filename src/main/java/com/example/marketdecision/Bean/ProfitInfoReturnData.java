package com.example.marketdecision.Bean;

import java.util.List;

public class ProfitInfoReturnData
{
    private int status;
    private int total;
    private List<ProfitInfo> profitInfos;

    public ProfitInfoReturnData(int status, int total, List<ProfitInfo> profitInfos)
    {
        this.status = status;
        this.profitInfos = profitInfos;
        this.total = total;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<ProfitInfo> getProfitInfos() {
        return profitInfos;
    }

    public void setProfitInfos(List<ProfitInfo> profitInfos) {
        this.profitInfos = profitInfos;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
