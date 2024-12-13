package vn.btec.campus.utils;

import vn.btec.campus_expense_management.entities.User;

public class DataStatic {
    private static DataStatic instance;
    private User currentUser;

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
}
