package com.example.project.Model;

public class MechanicModel {
    String mechanic_id,date,year_of_experience,phone,specialization,biography;

    public MechanicModel(String mechanic_id, String date, String year_of_experience, String phone, String specialization, String biography) {
        this.mechanic_id = mechanic_id;
        this.date = date;
        this.year_of_experience = year_of_experience;
        this.phone = phone;
        this.specialization = specialization;
        this.biography = biography;
    }

    public String getMechanic_id() {
        return mechanic_id;
    }

    public void setMechanic_id(String mechanic_id) {
        this.mechanic_id = mechanic_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getYear_of_experience() {
        return year_of_experience;
    }

    public void setYear_of_experience(String year_of_experience) {
        this.year_of_experience = year_of_experience;
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
}
