package com.mytway.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

//http://instinctcoder.com/android-studio-sqlite-database-example/
public class DBHelper extends SQLiteOpenHelper {

    //version number to upgrade database version
    //each time if you Add, Edit table, you need to change the
    //version number.
    private static final int DATABASE_VERSION = 4;
    // Database Name
    private static final String DATABASE_NAME = "crud.db";

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

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed, all data will be gone!!!
        db.execSQL("DROP TABLE IF EXISTS " + UserTable.TABLE);
        // Create tables again
        onCreate(db);
    }

    public static void CopyDB(InputStream inputStream, OutputStream outputStream) throws IOException {
        // ---copy 1K bytes at a time---
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }
        inputStream.close();
        outputStream.close();
    }

    //todo: remove it!
    public static void copyDatabaseToSdCard(Context context) {
        try {
            String destinationPath = Environment.getExternalStorageDirectory().toString();
            File file = new File(destinationPath);
            if (!file.exists()) {
                file.mkdirs();
                file.createNewFile();
                // ---copy the db from the /data/data/ folder into
                // the sdcard databases folder--- here MyDB is database name
                DBHelper.CopyDB(new FileInputStream("/data/data/" + context.getPackageName()
                        + "/databases"), new FileOutputStream(destinationPath + "/MyDB"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
