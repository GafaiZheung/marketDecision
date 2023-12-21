package com.example.marketdecision.Bean;

import java.security.PublicKey;
import java.util.List;

public class LossDataReturnData
{
    private int status;
    private int total;
    private List<LossData> lossData;

    public LossDataReturnData(int status, int total, List<LossData> lossData)
    {
        this.status = status;
        this.lossData = lossData;
        this.total = total;
    }


    public List<LossData> getLossData() {
        return lossData;
    }

    public void setLossData(List<LossData> lossData) {
        this.lossData = lossData;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
