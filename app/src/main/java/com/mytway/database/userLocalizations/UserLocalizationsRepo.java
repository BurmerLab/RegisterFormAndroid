package com.mytway.database.userLocalizations;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.mytway.database.DBHelper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class UserLocalizationsRepo {
    private DBHelper dbHelper;

    public UserLocalizationsRepo(Context context) {
        dbHelper = new DBHelper(context);
        try {
            dbHelper.open(context);
        } catch (SQLException e) {
            Log.i("Problem with dbHelper", String.valueOf(e));
            e.printStackTrace();
        }
    }

    public int insert(UserLocalizationsTable UserLocalizationsTable) {
        //Open connection to write data
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(com.mytway.database.userLocalizations.UserLocalizationsTable.KEY_USER_NAME, UserLocalizationsTable.userName);
        values.put(com.mytway.database.userLocalizations.UserLocalizationsTable.CREATION_DATE, UserLocalizationsTable.creationDate);
        values.put(com.mytway.database.userLocalizations.UserLocalizationsTable.LATITUDE, UserLocalizationsTable.latitude);
        values.put(com.mytway.database.userLocalizations.UserLocalizationsTable.LONGITUDE, UserLocalizationsTable.longitude);
        values.put(com.mytway.database.userLocalizations.UserLocalizationsTable.TIME_STATUS, UserLocalizationsTable.timeStatus);

        // Inserting Row
        long user_Id = db.insert(com.mytway.database.userLocalizations.UserLocalizationsTable.TABLE, null, values);
        db.close();
        return (int) user_Id;
    }

    public int delete(int user_Id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int deletingResult = db.delete(UserLocalizationsTable.TABLE, UserLocalizationsTable.KEY_ID + "= ?", new String[] { String.valueOf(user_Id) });
        db.close();

        return deletingResult;
    }

    public void deleteAllFromUserLocalizations() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(UserLocalizationsTable.TABLE, null, null);
        db.close(); // Closing database connection
    }

    public void dropTableUserLocalizations(String table) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + table);
        db.close(); // Closing database connection
    }



    public int update(UserLocalizationsTable UserLocalizationsTable) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(com.mytway.database.userLocalizations.UserLocalizationsTable.KEY_USER_NAME, UserLocalizationsTable.userName);
        values.put(com.mytway.database.userLocalizations.UserLocalizationsTable.CREATION_DATE, UserLocalizationsTable.creationDate);
        values.put(com.mytway.database.userLocalizations.UserLocalizationsTable.LATITUDE, UserLocalizationsTable.latitude);
        values.put(com.mytway.database.userLocalizations.UserLocalizationsTable.LONGITUDE, UserLocalizationsTable.longitude);
        values.put(com.mytway.database.userLocalizations.UserLocalizationsTable.TIME_STATUS, UserLocalizationsTable.timeStatus);

        int updateResult = db.update(com.mytway.database.userLocalizations.UserLocalizationsTable.TABLE, values, com.mytway.database.userLocalizations.UserLocalizationsTable.KEY_ID + "= ?",
                new String[] { String.valueOf(UserLocalizationsTable.localizationId) });
        db.close(); // Closing database connection

        return updateResult;
    }

    public int updateByUserName(UserLocalizationsTable UserLocalizationsTable) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(com.mytway.database.userLocalizations.UserLocalizationsTable.KEY_USER_NAME, UserLocalizationsTable.userName);
        values.put(com.mytway.database.userLocalizations.UserLocalizationsTable.CREATION_DATE, UserLocalizationsTable.creationDate);
        values.put(com.mytway.database.userLocalizations.UserLocalizationsTable.LATITUDE, UserLocalizationsTable.latitude);
        values.put(com.mytway.database.userLocalizations.UserLocalizationsTable.LONGITUDE, UserLocalizationsTable.longitude);
        values.put(com.mytway.database.userLocalizations.UserLocalizationsTable.TIME_STATUS, UserLocalizationsTable.timeStatus);

        int updateResult = db.update(com.mytway.database.userLocalizations.UserLocalizationsTable.TABLE, values, com.mytway.database.userLocalizations.UserLocalizationsTable.KEY_USER_NAME + "= ?", new String[] { String.valueOf(UserLocalizationsTable.userName) });
        db.close(); // Closing database connection

        return updateResult;
    }

    public ArrayList<HashMap<String, String>> getUserLocalizationsList() {
        //Open connection to read only
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                UserLocalizationsTable.KEY_ID + "," +
                UserLocalizationsTable.KEY_USER_NAME + "," +
                UserLocalizationsTable.CREATION_DATE + "," +
                UserLocalizationsTable.LATITUDE + "," +
                UserLocalizationsTable.LONGITUDE + "," +
                UserLocalizationsTable.TIME_STATUS + " " +
                " FROM " + UserLocalizationsTable.TABLE;

        //Student student = new Student();
        ArrayList<HashMap<String, String>> userLocalizationsTableList = new ArrayList<>();

        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> userLocalizationsTable = new HashMap<>();
                userLocalizationsTable.put("id", cursor.getString(cursor.getColumnIndex(UserLocalizationsTable.KEY_ID)));
                userLocalizationsTable.put("name", cursor.getString(cursor.getColumnIndex(UserLocalizationsTable.KEY_USER_NAME)));
                userLocalizationsTableList.add(userLocalizationsTable);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return userLocalizationsTableList;
    }

    public UserLocalizationsTable getUserLocalizationsById(int Id){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                UserLocalizationsTable.KEY_ID + "," +
                UserLocalizationsTable.KEY_USER_NAME + "," +
                UserLocalizationsTable.CREATION_DATE + "," +
                UserLocalizationsTable.LATITUDE + "," +
                UserLocalizationsTable.LONGITUDE + "," +
                UserLocalizationsTable.TIME_STATUS + " " +
                " FROM " + UserLocalizationsTable.TABLE
                + " WHERE " +
                UserLocalizationsTable.KEY_ID + "=?";// It's a good practice to use parameter ?, instead of concatenate string

        int iCount =0;
        UserLocalizationsTable UserLocalizationsTable = new UserLocalizationsTable();

        Cursor cursor = db.rawQuery(selectQuery, new String[] { String.valueOf(Id) } );

        if (cursor.moveToFirst()) {
            do {
                UserLocalizationsTable.localizationId = cursor.getInt(cursor.getColumnIndex(com.mytway.database.userLocalizations.UserLocalizationsTable.KEY_ID));
                UserLocalizationsTable.userName = cursor.getString(cursor.getColumnIndex(com.mytway.database.userLocalizations.UserLocalizationsTable.KEY_USER_NAME));
                UserLocalizationsTable.creationDate = cursor.getString(cursor.getColumnIndex(com.mytway.database.userLocalizations.UserLocalizationsTable.CREATION_DATE));
                UserLocalizationsTable.latitude = cursor.getString(cursor.getColumnIndex(com.mytway.database.userLocalizations.UserLocalizationsTable.LATITUDE));
                UserLocalizationsTable.longitude = cursor.getString(cursor.getColumnIndex(com.mytway.database.userLocalizations.UserLocalizationsTable.LONGITUDE));
                UserLocalizationsTable.timeStatus = cursor.getString(cursor.getColumnIndex(com.mytway.database.userLocalizations.UserLocalizationsTable.TIME_STATUS));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return UserLocalizationsTable;
    }

    public UserLocalizationsTable getUserByUserName(String userName){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT DISTINCT " +
                UserLocalizationsTable.KEY_ID + "," +
                UserLocalizationsTable.KEY_USER_NAME + "," +
                UserLocalizationsTable.CREATION_DATE + "," +
                UserLocalizationsTable.LATITUDE + "," +
                UserLocalizationsTable.LONGITUDE + "," +
                UserLocalizationsTable.TIME_STATUS + " " +
                " FROM " + UserLocalizationsTable.TABLE
                + " WHERE " +
                UserLocalizationsTable.KEY_USER_NAME + "=?";

        UserLocalizationsTable UserLocalizationsTable = new UserLocalizationsTable();

        Cursor cursor = db.rawQuery(selectQuery, new String[] { userName } );

        if (cursor.moveToFirst()) {
            do {
                UserLocalizationsTable.localizationId = cursor.getInt(cursor.getColumnIndex(com.mytway.database.userLocalizations.UserLocalizationsTable.KEY_ID));
                UserLocalizationsTable.userName = cursor.getString(cursor.getColumnIndex(com.mytway.database.userLocalizations.UserLocalizationsTable.KEY_USER_NAME));
                UserLocalizationsTable.creationDate = cursor.getString(cursor.getColumnIndex(com.mytway.database.userLocalizations.UserLocalizationsTable.CREATION_DATE));
                UserLocalizationsTable.latitude = cursor.getString(cursor.getColumnIndex(com.mytway.database.userLocalizations.UserLocalizationsTable.LATITUDE));
                UserLocalizationsTable.longitude = cursor.getString(cursor.getColumnIndex(com.mytway.database.userLocalizations.UserLocalizationsTable.LONGITUDE));
                UserLocalizationsTable.timeStatus = cursor.getString(cursor.getColumnIndex(com.mytway.database.userLocalizations.UserLocalizationsTable.TIME_STATUS));

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return UserLocalizationsTable;
    }

    public boolean isUserExistInLocalDatabase(String userName){
        UserLocalizationsTable UserLocalizationsTable = getUserByUserName(userName);
        return UserLocalizationsTable.userName != null;
    }

}
