package com.example.project.Model;

public class MechanicRatesModel {
    String user_id,mechanic_id,rate;

    public MechanicRatesModel(String user_id, String mechanic_id, String rate) {

        this.user_id = user_id;
        this.mechanic_id = mechanic_id;
        this.rate = rate;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getMechanic_id() {
        return mechanic_id;
    }

    public void setMechanic_id(String mechanic_id) {
        this.mechanic_id = mechanic_id;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }
}
