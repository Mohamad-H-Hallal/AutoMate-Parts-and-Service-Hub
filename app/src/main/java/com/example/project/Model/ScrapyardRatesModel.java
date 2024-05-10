package com.example.project.Model;

public class ScrapyardRatesModel {
    String user_id,scrapyard_id,rate;

    public ScrapyardRatesModel(String user_id, String rate, String scrapyard_id) {
        this.user_id = user_id;
        this.rate = rate;
        this.scrapyard_id = scrapyard_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getScrapyard_id() {
        return scrapyard_id;
    }

    public void setScrapyard_id(String scrapyard_id) {
        this.scrapyard_id = scrapyard_id;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }
}
