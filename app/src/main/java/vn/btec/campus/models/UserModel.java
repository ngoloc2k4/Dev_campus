
package vn.btec.campus_expense_management.models;

import android.content.Context;
import vn.btec.campus_expense_management.database.DatabaseHelper;
import vn.btec.campus_expense_management.entities.User;

public class UserModel {
    private DatabaseHelper dbHelper;

    public UserModel(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public User getUserByEmailAndPassword(String email, String password) {
        int userId = dbHelper.getUserIdByEmailAndPassword(email, password);
        if (userId != -1) {
            return dbHelper.getUserById(userId);
        }
        return null;
    }
}