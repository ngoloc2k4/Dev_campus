package vn.btec.campus_expense_management.database;

import static vn.btec.campus_expense_management.utils.hashPassword.hashPassword;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;
import java.util.ArrayList;
import vn.btec.campus_expense_management.entities.*;


import java.text.SimpleDateFormat;

import java.util.Date;

import java.util.Locale;


public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "CampusDB";
    private static final int DATABASE_VERSION = 1;

    // Transactions table
    private static final String TABLE_TRANSACTION = "transactions";
    private static final String COLUMN_TRANSACTION_ID = "id";
    private static final String COLUMN_AMOUNT = "amount";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_TYPE = "type";
    private static final String COLUMN_CATEGORY = "category";
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_EMAIL = "email";

    // User table
    private static final String TABLE_USER = "USER";
    private static final String COLUMN_FIRST_NAME = "firstName";
    private static final String COLUMN_LAST_NAME = "lastName";
    private static final String COLUMN_PASSWORD = "password";

    // Category table
    private static final String TABLE_CATEGORY = "CATEGORY";
    private static final String COLUMN_CATEGORY_ID = "category_id";
    private static final String COLUMN_CATEGORY_NAME = "name";

    // Goals table
    private static final String TABLE_GOALS = "GOALS";
    private static final String COLUMN_GOAL_ID = "goal_id";
    private static final String COLUMN_GOAL_NAME = "goal_name";
    private static final String COLUMN_GOAL_AMOUNT = "goal_amount";
    private static final String COLUMN_GOAL_CURRENT_AMOUNT = "current_amount";
    private static final String COLUMN_GOAL_ACHIEVED = "is_achieved";

    private final Context context;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create transactions table
        String CREATE_TRANSACTION_TABLE = "CREATE TABLE " + TABLE_TRANSACTION + "("
                + COLUMN_TRANSACTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_AMOUNT + " REAL, "
                + COLUMN_DESCRIPTION + " TEXT, "
                + COLUMN_DATE + " TEXT, "
                + COLUMN_TYPE + " INTEGER, "
                + COLUMN_USER_ID + " INTEGER, "
                + COLUMN_CATEGORY + " TEXT, "
                + "FOREIGN KEY(" + COLUMN_USER_ID + ") REFERENCES " + TABLE_USER + "(" + COLUMN_USER_ID + ")"
                + ")";
        db.execSQL(CREATE_TRANSACTION_TABLE);

        // Create user table
        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_FIRST_NAME + " TEXT, "
                + COLUMN_LAST_NAME + " TEXT, "
                + COLUMN_PASSWORD + " TEXT"
                + ")";
        db.execSQL(CREATE_USER_TABLE);

        // Create category table
        String CREATE_CATEGORY_TABLE = "CREATE TABLE " + TABLE_CATEGORY + "("
                + COLUMN_CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_CATEGORY_NAME + " TEXT UNIQUE NOT NULL, "
                + COLUMN_USER_ID + " INTEGER, "
                + "FOREIGN KEY(" + COLUMN_USER_ID + ") REFERENCES " + TABLE_USER + "(" + COLUMN_USER_ID + ")"
                + ")";
        db.execSQL(CREATE_CATEGORY_TABLE);

        // Create goals table
        String CREATE_GOALS_TABLE = "CREATE TABLE " + TABLE_GOALS + "("
                + COLUMN_GOAL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_GOAL_NAME + " TEXT, "
                + COLUMN_GOAL_AMOUNT + " REAL, "
                + COLUMN_GOAL_CURRENT_AMOUNT + " REAL, "
                + COLUMN_GOAL_ACHIEVED + " INTEGER DEFAULT 0, "
                + COLUMN_USER_ID + " INTEGER, "
                + "FOREIGN KEY(" + COLUMN_USER_ID + ") REFERENCES " + TABLE_USER + "(" + COLUMN_USER_ID + ")"
                + ")";
        db.execSQL(CREATE_GOALS_TABLE);

        insertDefaultCategories(db, null);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GOALS);
        onCreate(db);
    }

    // Insert a new transaction record
    public boolean insertTransaction(double amount, String description, String date, int type, int userId, String category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_AMOUNT, amount);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_TYPE, type);
        values.put(COLUMN_USER_ID, userId);
        values.put(COLUMN_CATEGORY, category);

        long result = db.insert(TABLE_TRANSACTION, null, values);
        db.close();
        return result != -1;
    }

    public boolean updateTransaction(int id, double amount, String description, String date, int type, int userId, String category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_AMOUNT, amount);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_TYPE, type);
        values.put(COLUMN_USER_ID, userId);
        values.put(COLUMN_CATEGORY, category);

        int rowsAffected = db.update(TABLE_TRANSACTION, values, COLUMN_TRANSACTION_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
        return rowsAffected > 0;
    }

    public boolean deleteTransaction(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_TRANSACTION, COLUMN_TRANSACTION_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
        return rowsDeleted > 0;
    }

    // Insert a new user record
    public boolean insertUser(String firstName, String lastName, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FIRST_NAME, firstName);
        values.put(COLUMN_LAST_NAME, lastName);

        String hashedPassword = hashPassword(password);
        values.put(COLUMN_PASSWORD, hashedPassword);

        long result = db.insert(TABLE_USER, null, values);
        db.close();
        return result != -1;
    }

    public boolean updateUser(int userId, String firstName, String lastName, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FIRST_NAME, firstName);
        values.put(COLUMN_LAST_NAME, lastName);
        String hashPassword = hashPassword(password);
        values.put(COLUMN_PASSWORD, hashPassword);

        int rowsAffected = db.update(TABLE_USER, values, COLUMN_USER_ID + " = ?", new String[]{String.valueOf(userId)});
        db.close();
        return rowsAffected > 0;
    }

    public boolean deleteUser(int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_USER, COLUMN_USER_ID + " = ?", new String[]{String.valueOf(userId)});
        db.close();
        return rowsDeleted > 0;
    }

    // Insert a new category record
    public boolean insertCategory(String name, int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORY_NAME, name);
        values.put(COLUMN_USER_ID, userId);

        long result = db.insert(TABLE_CATEGORY, null, values);
        db.close();
        return result != -1;
    }

    public boolean updateCategory(int categoryId, String name, int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORY_NAME, name);
        values.put(COLUMN_USER_ID, userId);

        int rowsAffected = db.update(TABLE_CATEGORY, values, COLUMN_CATEGORY_ID + " = ?", new String[]{String.valueOf(categoryId)});
        db.close();
        return rowsAffected > 0;
    }

    public boolean deleteCategory(int categoryId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_CATEGORY, COLUMN_CATEGORY_ID + " = ?", new String[]{String.valueOf(categoryId)});
        db.close();
        return rowsDeleted > 0;
    }

    public boolean insertGoal(String name, double amount, double currentAmount, int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_GOAL_NAME, name);
        values.put(COLUMN_GOAL_AMOUNT, amount);
        values.put(COLUMN_GOAL_CURRENT_AMOUNT, currentAmount);
        values.put(COLUMN_USER_ID, userId);

        long result = db.insert(TABLE_GOALS, null, values);
        db.close();
        return result != -1;
    }


    // Retrieve all categories as a List<Category>
    public List<Category> getCategoryList(int userId) {
        List<Category> categoryList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CATEGORY + " WHERE " + COLUMN_USER_ID + " = ?", new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(COLUMN_CATEGORY_ID));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY_NAME));

                Category category = new Category(id, name, userId);
                categoryList.add(category);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return categoryList;
    }

    public List<Transaction> getTransactionList(int userId) {
        List<Transaction> transactionList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_TRANSACTION + " WHERE " + COLUMN_USER_ID + " = ?", new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(COLUMN_TRANSACTION_ID));
                @SuppressLint("Range") double amount = cursor.getDouble(cursor.getColumnIndex(COLUMN_AMOUNT));
                @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION));
                @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE));
                @SuppressLint("Range") int type = cursor.getInt(cursor.getColumnIndex(COLUMN_TYPE));
                @SuppressLint("Range") String category = cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY));

                Transaction transaction = new Transaction(id, amount, description, date, type, userId, category);
                transactionList.add(transaction);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return transactionList;
    }

    public User getUserById(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        User user = null;
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USER + " WHERE " + COLUMN_USER_ID + " = ?", new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            @SuppressLint("Range") String firstName = cursor.getString(cursor.getColumnIndex(COLUMN_FIRST_NAME));
            @SuppressLint("Range") String lastName = cursor.getString(cursor.getColumnIndex(COLUMN_LAST_NAME));
            @SuppressLint("Range") String password = cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD));

            user = new User(userId, firstName, lastName, password);
        }

        cursor.close();
        db.close();
        return user;
    }

    public BalanceInfor getBalanceFromUserId(int userId) {
        User userFound = getUserById(userId);
        if (userFound != null) {
            List<Transaction> allTransactions = getTransactionList(userId);

            double expense = 0;
            double income = 0;

            for (Transaction transaction : allTransactions) {
                if (transaction.getType() == 0) {
                    expense += transaction.getAmount();
                } else if (transaction.getType() == 1) {
                    income += transaction.getAmount();
                }
            }

            double balance = income - expense;
            return new BalanceInfor(userId, balance, income, expense);
        }
        return null;
    }

    private void insertDefaultCategories(SQLiteDatabase db, Integer userId) {
        String[] defaultCategories = {"Food", "Transport", "Entertainment", "Utilities", "Health"};

        for (String categoryName : defaultCategories) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_CATEGORY_NAME, categoryName);
            values.put(COLUMN_USER_ID, userId);
            db.insert(TABLE_CATEGORY, null, values);
        }
    }


    // Advanced method to get transactions with more filtering and sorting options
    public List<Transaction> getTransactionsWithFilter(
            int user_id, // Updated parameter
            long startDate,
            long endDate,
            String transactionType, // "Income", "Expense", or null for all
            TransactionSortOption sortOption
    ) {
        List<Transaction> transactionList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Base query
        String query = "SELECT * FROM " + TABLE_TRANSACTION +
                " WHERE " + COLUMN_USER_ID + " = ? " +
                " AND strftime('%s', " + COLUMN_DATE + ") BETWEEN ? AND ?";

        // Add transaction type filter if specified
        if (transactionType != null) {
            int typeValue = transactionType.equals("Income") ? 1 : 0;
            query += " AND " + COLUMN_TYPE + " = " + typeValue;
        }

        // Add sorting based on sort option
        switch (sortOption) {
            case DATE_NEW_TO_OLD:
                query += " ORDER BY " + COLUMN_DATE + " DESC";
                break;
            case DATE_OLD_TO_NEW:
                query += " ORDER BY " + COLUMN_DATE + " ASC";
                break;
            case AMOUNT_HIGH_TO_LOW:
                query += " ORDER BY " + COLUMN_AMOUNT + " DESC";
                break;
            case AMOUNT_LOW_TO_HIGH:
                query += " ORDER BY " + COLUMN_AMOUNT + " ASC";
                break;
            case ALPHABETICAL_ASC:
                query += " ORDER BY " + COLUMN_DESCRIPTION + " ASC";
                break;
            case ALPHABETICAL_DESC:
                query += " ORDER BY " + COLUMN_DESCRIPTION + " DESC";
                break;
        }

        // Convert milliseconds to formatted date strings
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String startDateStr = dateFormat.format(new Date(startDate));
        String endDateStr = dateFormat.format(new Date(endDate));

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(user_id), startDateStr, endDateStr});

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(COLUMN_TRANSACTION_ID));
                @SuppressLint("Range") double amount = cursor.getDouble(cursor.getColumnIndex(COLUMN_AMOUNT));
                @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION));
                @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE));
                @SuppressLint("Range") int type = cursor.getInt(cursor.getColumnIndex(COLUMN_TYPE));
                @SuppressLint("Range") int userId = cursor.getInt(cursor.getColumnIndex(COLUMN_USER_ID));
                @SuppressLint("Range") String category = cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY));

                Transaction transaction = new Transaction(id, amount, description, date, type, userId, category);
                transactionList.add(transaction);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return transactionList;
    }

    // Advanced transaction filtering method
    public List<Transaction> getFilteredTransactions(
            int user_id, // Updated parameter
            Long startDate,     // Optional start date
            Long endDate,       // Optional end date
            String transactionType, // "Income", "Expense", or null
            String category,    // Optional category filter
            double minAmount,   // Optional minimum amount
            double maxAmount,   // Optional maximum amount
            String sortBy,      // "date", "amount", "category"
            boolean sortAscending // Sort direction
    ) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Transaction> filteredTransactions = new ArrayList<>();

        // Base query
        String query = "SELECT * FROM " + TABLE_TRANSACTION + " WHERE " + COLUMN_USER_ID + " = ?";
        List<String> selectionArgs = new ArrayList<>();
        selectionArgs.add(String.valueOf(user_id));

        // Date range filter
        if (startDate != null && endDate != null) {
            query += " AND " + COLUMN_DATE + " BETWEEN ? AND ?";
            selectionArgs.add(String.valueOf(startDate));
            selectionArgs.add(String.valueOf(endDate));
        }

        // Transaction type filter
        if (transactionType != null) {
            int typeValue = transactionType.equalsIgnoreCase("Income") ? 1 : 2;
            query += " AND " + COLUMN_TYPE + " = ?";
            selectionArgs.add(String.valueOf(typeValue));
        }

        // Category filter
        if (category != null && !category.isEmpty()) {
            query += " AND " + COLUMN_CATEGORY + " = ?";
            selectionArgs.add(category);
        }

        // Amount range filter
        if (minAmount > 0) {
            query += " AND " + COLUMN_AMOUNT + " >= ?";
            selectionArgs.add(String.valueOf(minAmount));
        }
        if (maxAmount > 0) {
            query += " AND " + COLUMN_AMOUNT + " <= ?";
            selectionArgs.add(String.valueOf(maxAmount));
        }

        // Sorting
        if (sortBy != null) {
            String orderBy = "";
            switch (sortBy.toLowerCase()) {
                case "date":
                    orderBy = COLUMN_DATE;
                    break;
                case "amount":
                    orderBy = COLUMN_AMOUNT;
                    break;
                case "category":
                    orderBy = COLUMN_CATEGORY;
                    break;
                default:
                    orderBy = COLUMN_DATE;
            }
            query += " ORDER BY " + orderBy + (sortAscending ? " ASC" : " DESC");
        }

        Cursor cursor = db.rawQuery(query, selectionArgs.toArray(new String[0]));

        if (cursor.moveToFirst()) {
            do {
                Transaction transaction = new Transaction();
                int idIndex = cursor.getColumnIndex(COLUMN_TRANSACTION_ID);
                int amountIndex = cursor.getColumnIndex(COLUMN_AMOUNT);
                int descriptionIndex = cursor.getColumnIndex(COLUMN_DESCRIPTION);
                int dateIndex = cursor.getColumnIndex(COLUMN_DATE);
                int typeIndex = cursor.getColumnIndex(COLUMN_TYPE);
                int categoryIndex = cursor.getColumnIndex(COLUMN_CATEGORY);
                int userIdIndex = cursor.getColumnIndex(COLUMN_USER_ID);

                if (idIndex != -1) transaction.setId(cursor.getInt(idIndex));
                if (amountIndex != -1) transaction.setAmount(cursor.getDouble(amountIndex));
                if (descriptionIndex != -1) transaction.setDescription(cursor.getString(descriptionIndex));
                if (dateIndex != -1) transaction.setDate(cursor.getString(dateIndex));
                if (typeIndex != -1) transaction.setType(cursor.getInt(typeIndex));
                if (categoryIndex != -1) transaction.setCategory(cursor.getString(categoryIndex));
                if (userIdIndex != -1) transaction.setUserId(cursor.getInt(userIdIndex));

                filteredTransactions.add(transaction);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return filteredTransactions;
    }

    public int getUserIdByEmailAndPassword(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        int userId = -1;
    
        String[] columns = {COLUMN_USER_ID};
        String selection = COLUMN_EMAIL + " = ? AND " + COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = {email, password};
    
        Cursor cursor = db.query(TABLE_USER, columns, selection, selectionArgs, null, null, null);
    
        if (cursor != null && cursor.moveToFirst()) {
            int userIdIndex = cursor.getColumnIndex(COLUMN_USER_ID);
            if (userIdIndex != -1) {
                userId = cursor.getInt(userIdIndex);
            }
            cursor.close();
        }
    
        db.close();
        return userId;
    }

    // Method to get transaction summary
    public TransactionSummary getTransactionSummary(int user_id, Long startDate, Long endDate) {
        SQLiteDatabase db = this.getReadableDatabase();
        TransactionSummary summary = new TransactionSummary();

        String baseQuery = "SELECT " +
                "SUM(CASE WHEN " + COLUMN_TYPE + " = 1 THEN " + COLUMN_AMOUNT + " ELSE 0 END) as total_income, " +
                "SUM(CASE WHEN " + COLUMN_TYPE + " = 2 THEN " + COLUMN_AMOUNT + " ELSE 0 END) as total_expense, " +
                "COUNT(*) as total_transactions " +
                "FROM " + TABLE_TRANSACTION + " WHERE " + COLUMN_USER_ID + " = ?";

        List<String> selectionArgs = new ArrayList<>();
        selectionArgs.add(String.valueOf(user_id));

        if (startDate != null && endDate != null) {
            baseQuery += " AND " + COLUMN_DATE + " BETWEEN ? AND ?";
            selectionArgs.add(String.valueOf(startDate));
            selectionArgs.add(String.valueOf(endDate));
        }

        Cursor cursor = db.rawQuery(baseQuery, selectionArgs.toArray(new String[0]));

        if (cursor.moveToFirst()) {
            int totalIncomeIndex = cursor.getColumnIndex("total_income");
            int totalExpenseIndex = cursor.getColumnIndex("total_expense");
            int totalTransactionsIndex = cursor.getColumnIndex("total_transactions");

            if (totalIncomeIndex >= 0) {
                summary.totalIncome = cursor.getDouble(totalIncomeIndex);
            }
            if (totalExpenseIndex >= 0) {
                summary.totalExpense = cursor.getDouble(totalExpenseIndex);
            }
            if (totalTransactionsIndex >= 0) {
                summary.totalTransactions = cursor.getInt(totalTransactionsIndex);
            }
        }

        cursor.close();
        db.close();

        return summary;
    }

}
