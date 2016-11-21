package com.mytway.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

//http://instinctcoder.com/android-studio-sqlite-database-example/
public class DBHelper extends SQLiteOpenHelper {

    private static final String TAG = "DBHelper";
    private static final int DATABASE_VERSION = 7;
    // Database Name
    private static final String DATABASE_NAME = "crud.db";

    private DBHelper databaseHelper;
    private SQLiteDatabase myDataBase;

    public DBHelper(Context context ) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //All necessary tables you like to create will create here
        String CREATE_TABLE_USER = "CREATE TABLE " + UserTable.TABLE  + "("
                + UserTable.KEY_ID  + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + UserTable.KEY_USER_NAME + " TEXT not null unique, "
                + UserTable.EMAIL + " TEXT, "
                + UserTable.PASSWORD + " TEXT, "
                + UserTable.TYPE_WORK + " TEXT, "
                + UserTable.LENGTH_TIME_WORK + " TEXT, "
                + UserTable.START_STANDARD_TIME + " TEXT, "
                + UserTable.WORK_PLACE_LATITUDE + " INTEGER, "
                + UserTable.WORK_PLACE_LONGITUDE + " INTEGER, "
                + UserTable.HOME_PLACE_LATITUDE + " INTEGER, "
                + UserTable.HOME_PLACE_LONGITUDE + " INTEGER, "
                + UserTable.WORK_WEEK + " TEXT, "
                + UserTable.WAY_DISTANCE + " TEXT, "
                + UserTable.WAY_DURATION + " TEXT )";
        db.execSQL(CREATE_TABLE_USER);
    }

    public DBHelper open() throws SQLException {
//        myDataBase = this.getWritableDatabase();
//        myDataBase =  getWritableDatabase();
//        Log.d(TAG, "DbHelper Opening Version: " + this.myDataBase.getVersion());
        return this;
    }

    //http://stackoverflow.com/questions/7647566/why-is-onupgrade-not-being-invoked-on-android-sqlite-database
//    @Override
//    public SQLiteDatabase getWritableDatabase() {
//        SQLiteDatabase myDataBase = this.getWritableDatabase();
//        int version = myDataBase.getVersion();
//        if (version != DATABASE_VERSION) {
//            myDataBase.beginTransaction();
//            try {
//                if (version == 0) {
//                    onCreate(myDataBase);
//                } else {
//                    if (version > DATABASE_VERSION) {
//                        onDowngrade(myDataBase, version, DATABASE_VERSION);
//                    } else {
//                        onUpgrade(myDataBase, version, DATABASE_VERSION);
//                    }
//                }
//                myDataBase.setVersion(DATABASE_VERSION);
//                myDataBase.setTransactionSuccessful();
//            } finally {
//                myDataBase.endTransaction();
//            }
//        }
//        onOpen(myDataBase);
//        return myDataBase;
//    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed, all data will be gone!!!
        db.execSQL("DROP TABLE IF EXISTS " + UserTable.TABLE);
        // Create tables again
        onCreate(db);
    }

}
