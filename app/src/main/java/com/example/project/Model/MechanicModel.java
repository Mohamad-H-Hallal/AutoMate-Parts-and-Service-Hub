package com.example.project.Model;

public class MechanicModel {
    private String name,email,account_type,icon,date,end_date,phone,specialization,biography,subscription;
    private  int mechanic_id,year_of_experience;
    private   double latitude,longitude;
    private  float rating;

    public MechanicModel(String name, String email, double latitude, double longitude, String account_type, String icon, String date,String end_date, String phone, String specialization, String biography,String subscription, int mechanic_id, int year_of_experience,float rating) {
        this.name = name;
        this.email = email;
        this.latitude = latitude;
        this.longitude = longitude;
        this.account_type = account_type;
        this.icon = icon;
        this.date = date;
        this.end_date=end_date;
        this.phone = phone;
        this.specialization = specialization;
        this.biography = biography;
        this.mechanic_id = mechanic_id;
        this.year_of_experience = year_of_experience;
        this.rating=rating;
        this.subscription=subscription;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getSubscription() {
        return subscription;
    }

    public void setSubscription(String subscription) {
        this.subscription = subscription;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getAccount_type() {
        return account_type;
    }

    public void setAccount_type(String account_type) {
        this.account_type = account_type;
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

    public int getMechanic_id() {
        return mechanic_id;
    }

    public void setMechanic_id(int mechanic_id) {
        this.mechanic_id = mechanic_id;
    }

    public int getYear_of_experience() {
        return year_of_experience;
    }

    public void setYear_of_experience(int year_of_experience) {
        this.year_of_experience = year_of_experience;
    }
}
