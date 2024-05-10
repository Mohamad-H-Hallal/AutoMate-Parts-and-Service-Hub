package com.example.project.Model;

public class ScrapyardPaymentModel {
    String scrapyard_id,first_name,last_name,date,type,LTN;

    public ScrapyardPaymentModel(String scrapyard_id, String LTN, String type, String date, String last_name, String first_name) {
        this.scrapyard_id = scrapyard_id;
        this.LTN = LTN;
        this.type = type;
        this.date = date;
        this.last_name = last_name;
        this.first_name = first_name;
    }

    public String getScrapyard_id() {
        return scrapyard_id;
    }

    public void setScrapyard_id(String scrapyard_id) {
        this.scrapyard_id = scrapyard_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLTN() {
        return LTN;
    }

    public void setLTN(String LTN) {
        this.LTN = LTN;
    }
}
