package com.example.project.Model;

public class DiagnosticDataModel {
    String vin,date_time,file,user_id;

    public DiagnosticDataModel(String vin, String date_time, String file, String user_id) {
        this.vin = vin;
        this.date_time = date_time;
        this.file = file;
        this.user_id = user_id;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
