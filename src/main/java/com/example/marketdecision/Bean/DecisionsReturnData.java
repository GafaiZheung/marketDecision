package com.example.marketdecision.Bean;

import java.util.List;

public class DecisionsReturnData
{
    private int status;
    private int total;
    private List<Decision> decisions;

    public DecisionsReturnData(int status, int total, List<Decision> decisions)
    {
        this.total = total;
        this.status = status;
        this.decisions = decisions;
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

    public List<Decision> getDecisions() {
        return decisions;
    }

    public void setDecisions(List<Decision> decisions) {
        this.decisions = decisions;
    }
}
