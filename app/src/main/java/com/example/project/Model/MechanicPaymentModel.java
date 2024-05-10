package com.example.project.Model;

public class MechanicPaymentModel {
    String mechanic_id,first_name,last_name,date,type,LTN;

    public MechanicPaymentModel(String mechanic_id, String first_name, String last_name, String date, String type, String LTN) {
        this.mechanic_id = mechanic_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.date = date;
        this.type = type;
        this.LTN = LTN;
    }

    public String getMechanic_id() {
        return mechanic_id;
    }

    public void setMechanic_id(String mechanic_id) {
        this.mechanic_id = mechanic_id;
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
