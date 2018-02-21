package com.mytway.database.userTimes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.mytway.database.DBHelper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class UserTimesRepo {
    private DBHelper dbHelper;

    public UserTimesRepo(Context context) {
        dbHelper = new DBHelper(context);
        try {
            dbHelper.open(context);
        } catch (SQLException e) {
            Log.i("Problem with dbHelper", String.valueOf(e));
            e.printStackTrace();
        }
    }

    public int insert(UserTimesTable UserTimesTable) {
        //Open connection to write data
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(com.mytway.database.userTimes.UserTimesTable.KEY_USER_NAME, UserTimesTable.userName);
        values.put(com.mytway.database.userTimes.UserTimesTable.CREATION_DATE,UserTimesTable.creationDate);
        values.put(com.mytway.database.userTimes.UserTimesTable.TIME_STATUS, UserTimesTable.timeStatus);
        values.put(com.mytway.database.userTimes.UserTimesTable.TIME, UserTimesTable.time);

        // Inserting Row
        long user_Id = db.insert(com.mytway.database.userTimes.UserTimesTable.TABLE, null, values);
        db.close();
        return (int) user_Id;
    }

    public int delete(int user_Id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // It's a good practice to use parameter ?, instead of concatenate string
        int result = db.delete(UserTimesTable.TABLE, UserTimesTable.KEY_ID + "= ?", new String[] { String.valueOf(user_Id) });
        db.close(); // Closing database connection
        return result;
    }

    public void deleteAllFromUser() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(UserTimesTable.TABLE, null, null);
        db.close(); // Closing database connection
    }

    public void dropTableUser(String table) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + table);
        db.close(); // Closing database connection
    }

    public int update(UserTimesTable UserTimesTable) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(com.mytway.database.userTimes.UserTimesTable.KEY_USER_NAME, UserTimesTable.userName);
        values.put(com.mytway.database.userTimes.UserTimesTable.CREATION_DATE,UserTimesTable.creationDate);
        values.put(com.mytway.database.userTimes.UserTimesTable.TIME_STATUS, UserTimesTable.timeStatus);
        values.put(com.mytway.database.userTimes.UserTimesTable.TIME, UserTimesTable.time);

        int updateResult = db.update(com.mytway.database.userTimes.UserTimesTable.TABLE, values, com.mytway.database.userTimes.UserTimesTable.KEY_ID + "= ?", new String[] { String.valueOf(UserTimesTable.userTimesId) });
        db.close(); // Closing database connection

        return updateResult;
    }

    public int updateByUserName(UserTimesTable UserTimesTable) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(com.mytway.database.userTimes.UserTimesTable.KEY_USER_NAME, UserTimesTable.userName);
        values.put(com.mytway.database.userTimes.UserTimesTable.CREATION_DATE,UserTimesTable.creationDate);
        values.put(com.mytway.database.userTimes.UserTimesTable.TIME_STATUS, UserTimesTable.timeStatus);
        values.put(com.mytway.database.userTimes.UserTimesTable.TIME, UserTimesTable.time);

        int updateResult = db.update(com.mytway.database.userTimes.UserTimesTable.TABLE, values, com.mytway.database.userTimes.UserTimesTable.KEY_USER_NAME + "= ?", new String[] { String.valueOf(UserTimesTable.userName) });
        db.close(); // Closing database connection

        return updateResult;
    }

    public ArrayList<HashMap<String, String>> getUserList() {
        //Open connection to read only
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                UserTimesTable.KEY_ID + "," +
                UserTimesTable.KEY_USER_NAME + "," +
                UserTimesTable.CREATION_DATE + "," +
                UserTimesTable.TIME_STATUS + "," +
                UserTimesTable.TIME + "" +
                " FROM " + UserTimesTable.TABLE;

        //Student student = new Student();
        ArrayList<HashMap<String, String>> userTimesTableList = new ArrayList<>();

        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> userTimesTable = new HashMap<>();
                userTimesTable.put("id", cursor.getString(cursor.getColumnIndex(UserTimesTable.KEY_ID)));
                userTimesTable.put("name", cursor.getString(cursor.getColumnIndex(UserTimesTable.KEY_USER_NAME)));
                userTimesTableList.add(userTimesTable);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return userTimesTableList;
    }

    public UserTimesTable getUserById(int Id){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                UserTimesTable.KEY_ID + ", " +
                UserTimesTable.KEY_USER_NAME + ", " +
                UserTimesTable.CREATION_DATE + ", " +
                UserTimesTable.TIME_STATUS + ", " +
                UserTimesTable.TIME + "" +
                " FROM " + UserTimesTable.TABLE
                + " WHERE " +
                UserTimesTable.KEY_ID + "=?";// It's a good practice to use parameter ?, instead of concatenate string

        int iCount =0;
        UserTimesTable userTimesTable = new UserTimesTable();

        Cursor cursor = db.rawQuery(selectQuery, new String[] { String.valueOf(Id) } );

        if (cursor.moveToFirst()) {
            do {
                userTimesTable.userTimesId = cursor.getInt(cursor.getColumnIndex(UserTimesTable.KEY_ID));
                userTimesTable.userName = cursor.getString(cursor.getColumnIndex(UserTimesTable.KEY_USER_NAME));
                userTimesTable.creationDate = cursor.getString(cursor.getColumnIndex(UserTimesTable.CREATION_DATE));
                userTimesTable.timeStatus = cursor.getString(cursor.getColumnIndex(UserTimesTable.TIME_STATUS));
                userTimesTable.time = cursor.getString(cursor.getColumnIndex(UserTimesTable.TIME));

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return userTimesTable;
    }

    public UserTimesTable getUserByUserName(String userName){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT DISTINCT " +
                UserTimesTable.KEY_ID + ", " +
                UserTimesTable.KEY_USER_NAME + ", " +
                UserTimesTable.CREATION_DATE + ", " +
                UserTimesTable.TIME_STATUS + ", " +
                UserTimesTable.TIME + "" +
                " FROM " + UserTimesTable.TABLE
                + " WHERE " +
                UserTimesTable.KEY_USER_NAME + "=?";

        UserTimesTable userTimesTable = new UserTimesTable();

        Cursor cursor = db.rawQuery(selectQuery, new String[] { userName } );

        if (cursor.moveToFirst()) {
            do {
                userTimesTable.userTimesId = cursor.getInt(cursor.getColumnIndex(UserTimesTable.KEY_ID));
                userTimesTable.userName = cursor.getString(cursor.getColumnIndex(UserTimesTable.KEY_USER_NAME));
                userTimesTable.creationDate = cursor.getString(cursor.getColumnIndex(UserTimesTable.CREATION_DATE));
                userTimesTable.timeStatus = cursor.getString(cursor.getColumnIndex(UserTimesTable.TIME_STATUS));
                userTimesTable.time = cursor.getString(cursor.getColumnIndex(UserTimesTable.TIME));

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return userTimesTable;
    }

    public boolean isUserExistInLocalDatabase(String userName){
        UserTimesTable UserTimesTable = getUserByUserName(userName);
        return UserTimesTable.userName != null;
    }

}
