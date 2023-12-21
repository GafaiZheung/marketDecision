package com.example.marketdecision.Bean;

import java.util.List;

public class ExcelRecvReturnData
{
    private int status;
    private List<String> targets;
    private List<String> sources;
    private String filePath;

    public ExcelRecvReturnData(int status, List<String> targets, List<String> sources, String filePath)
    {
        this.sources = sources;
        this.status = status;
        this.targets = targets;
        this.filePath = filePath;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<String> getTargets() {
        return targets;
    }

    public void setTargets(List<String> targets) {
        this.targets = targets;
    }

    public void setSources(List<String> sources) {
        this.sources = sources;
    }

    public List<String> getSources() {
        return sources;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
