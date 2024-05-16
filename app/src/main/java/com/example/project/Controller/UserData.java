package com.example.project.Controller;

import android.content.Context;
import android.content.SharedPreferences;

public class UserData {

    private static SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    public UserData(Context context) {
        sharedPreferences = context.getSharedPreferences("userData", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setUserData(int id, String accountType) {
        editor.putBoolean("isLogin", true);
        editor.putInt("id", id);
        editor.putString("accountType", accountType);
        editor.apply();
    }

    public boolean isLogin() {
        return sharedPreferences.getBoolean("isLogin", false);
    }

    public static int getId() {
        return sharedPreferences.getInt("id", 0);
    }

    public static String getAccountType() {
        return sharedPreferences.getString("accountType", "");
    }

    public void logout() {
        editor.putBoolean("isLogin", false);
        editor.remove("id");
        editor.remove("accountType");
        editor.apply();
    }
}
