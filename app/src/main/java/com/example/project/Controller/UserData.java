package com.example.project.Controller;

import android.content.Context;
import android.content.SharedPreferences;

public class UserData {

    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;



    public UserData(Context context) {
        sharedPreferences = context.getSharedPreferences("userData", Context.MODE_PRIVATE);
        editor = context.getSharedPreferences("userData", Context.MODE_PRIVATE).edit();
    }

    public void setUserData(String id, String role) {
        editor.putBoolean("isLogin", true);
        editor.putString("id", id);
        editor.putString("role", role);
        editor.apply();
    }
    public boolean isLogin() {

        return sharedPreferences.getBoolean("isLogin", false);
    }
    public String getId() {
        return sharedPreferences.getString("id", "");
    }
    public String getRole() {
        return sharedPreferences.getString("role", "");
    }
    public void logout() {
        editor.putBoolean("isLogin", false);
        editor.putString("id", "");
        editor.putString("role", "");
        editor.apply();
    }
}
