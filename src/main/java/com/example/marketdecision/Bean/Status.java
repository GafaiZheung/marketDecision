package com.example.marketdecision.Bean;

public class Status
{
    private int status;
    private String description;

    public Status(int status, String description)
    {
        this.description = description;
        this.status = status;
    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
