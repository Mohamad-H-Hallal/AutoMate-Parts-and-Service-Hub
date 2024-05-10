package com.example.project.Model;

public class ScrapyardModel {
    String scrapyard_id,subscription,phone,biography,specialization,date;

    public ScrapyardModel(String scrapyard_id, String date, String specialization, String biography, String phone, String subscription) {
        this.scrapyard_id = scrapyard_id;
        this.date = date;
        this.specialization = specialization;
        this.biography = biography;
        this.phone = phone;
        this.subscription = subscription;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSubscription() {
        return subscription;
    }

    public void setSubscription(String subscription) {
        this.subscription = subscription;
    }

    public String getScrapyard_id() {
        return scrapyard_id;
    }

    public void setScrapyard_id(String scrapyard_id) {
        this.scrapyard_id = scrapyard_id;
    }
}
