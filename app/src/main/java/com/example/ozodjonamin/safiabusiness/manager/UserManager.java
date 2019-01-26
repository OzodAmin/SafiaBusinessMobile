package com.example.ozodjonamin.safiabusiness.manager;

import android.content.SharedPreferences;

import com.example.ozodjonamin.safiabusiness.model.User;

public class UserManager {

    private SharedPreferences user;
    private SharedPreferences.Editor editor;

    private static UserManager INSTANCE = null;

    private UserManager(SharedPreferences user) {
        this.user = user;
        this.editor = user.edit();
    }

    public static synchronized UserManager getInstance(SharedPreferences user) {
        if (INSTANCE == null) {
            INSTANCE = new UserManager(user);
        }
        return INSTANCE;
    }

    public void saveUser(User user){
        editor.putString("USER_NAME", user.getName()).commit();
        editor.putString("USER_EMAIL", user.getEmail()).commit();
        editor.putInt("USER_DISCOUNT", user.getDiscount()).commit();
    }

    public User getUserInformation(){
        User userModel = new User();
        userModel.setName(user.getString("USER_NAME", null));
        userModel.setEmail(user.getString("USER_EMAIL", null));
        userModel.setDiscount(user.getInt("USER_DISCOUNT", 0));
        return userModel;
    }
}
