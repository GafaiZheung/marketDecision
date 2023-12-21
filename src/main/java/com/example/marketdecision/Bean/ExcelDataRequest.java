package com.example.marketdecision.Bean;

import java.util.List;

public class ExcelDataRequest {
    private String filePath;
    private List<List<String>> columnMappings;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public List<List<String>> getColumnMappings() {
        return columnMappings;
    }

    public void setColumnMappings(List<List<String>> columnMappings) {
        this.columnMappings = columnMappings;
    }
}

