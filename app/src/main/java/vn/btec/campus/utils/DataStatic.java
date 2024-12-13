package vn.btec.campus.utils;

import android.content.Context;
import android.content.SharedPreferences;
import vn.btec.campus_expense_management.entities.User;

public class DataStatic {
    private static DataStatic instance;
    private User currentUser;
    private boolean isLogin;
    private boolean rememberMe;

    private static final String PREF_NAME = "CampusAppPrefs";
    private static final String KEY_REMEMBER_ME = "remember_me";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";

    private DataStatic() {}

    public static synchronized DataStatic getInstance() {
        if (instance == null) {
            instance = new DataStatic();
        }
        return instance;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    public boolean isRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }

    public void saveLoginCredentials(Context context, String email, String password, boolean rememberMe) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(KEY_REMEMBER_ME, rememberMe);
        if (rememberMe) {
            editor.putString(KEY_EMAIL, email);
            editor.putString(KEY_PASSWORD, password);
        } else {
            editor.remove(KEY_EMAIL);
            editor.remove(KEY_PASSWORD);
        }
        editor.apply();
        this.rememberMe = rememberMe;
    }

    public boolean checkSavedCredentials(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return pref.getBoolean(KEY_REMEMBER_ME, false);
    }

    public String[] getSavedCredentials(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String email = pref.getString(KEY_EMAIL, "");
        String password = pref.getString(KEY_PASSWORD, "");
        return new String[]{email, password};
    }
}
