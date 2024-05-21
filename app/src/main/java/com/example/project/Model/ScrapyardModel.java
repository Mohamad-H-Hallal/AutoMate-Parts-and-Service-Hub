package com.example.project.Model;

public class ScrapyardModel {
    private String name,email,account_type,icon,date,end_date,phone,specialization,biography,subscription;
    private int scrapyard_id;
    private double latitude,longitude;
    private float rating;

    public ScrapyardModel(String name, String email, String account_type, String icon, String date,String end_date, String phone, String specialization, String biography, String subscription, int scrapyard_id, double latitude, double longitude, float rating) {
        this.name = name;
        this.email = email;
        this.account_type = account_type;
        this.icon = icon;
        this.date = date;
        this.end_date=end_date;
        this.phone = phone;
        this.specialization = specialization;
        this.biography = biography;
        this.subscription = subscription;
        this.scrapyard_id = scrapyard_id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.rating = rating;
    }
    public ScrapyardModel() {

    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAccount_type() {
        return account_type;
    }

    public void setAccount_type(String account_type) {
        this.account_type = account_type;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getSubscription() {
        return subscription;
    }

    public void setSubscription(String subscription) {
        this.subscription = subscription;
    }

    public int getScrapyard_id() {
        return scrapyard_id;
    }

    public void setScrapyard_id(int scrapyard_id) {
        this.scrapyard_id = scrapyard_id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
