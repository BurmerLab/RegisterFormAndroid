package com.mytway.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mytway.database.userLocalizations.UserLocalizationsTable;
import com.mytway.database.userTimes.UserTimesTable;

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


//    if not exists - only temporary
    String CREATE_TABLE_USER = "CREATE TABLE if not exists " + UserTable.TABLE  + "("
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

    String CREATE_TABLE_USER_TIMES = "CREATE TABLE if not exists " + UserTimesTable.TABLE  + "("
            + UserTimesTable.KEY_ID  + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + UserTimesTable.KEY_USER_NAME + " TEXT not null unique, "
            + UserTimesTable.CREATION_DATE + " TEXT, "
            + UserTimesTable.TIME_STATUS + " TEXT, "
            + UserTimesTable.TIME + " TEXT )";

    String CREATE_TABLE_USER_LOCALIZATIONS = "CREATE TABLE if not exists " + UserLocalizationsTable.TABLE  + "("
            + UserLocalizationsTable.KEY_ID  + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + UserLocalizationsTable.KEY_USER_NAME + " TEXT not null unique, "
            + UserLocalizationsTable.CREATION_DATE + " TEXT, "
            + UserLocalizationsTable.LATITUDE + " TEXT, "
            + UserLocalizationsTable.LONGITUDE + " TEXT, "
            + UserLocalizationsTable.TIME_STATUS + " TEXT )";

    @Override
    public void onCreate(SQLiteDatabase db) {
        //temporary only for one time execution to create new table
//        db.execSQL("DROP TABLE IF EXISTS " + UserTable.TABLE);
//        db.execSQL("DROP TABLE IF EXISTS " + UserTimesTable.TABLE);
//        db.execSQL("DROP TABLE IF EXISTS " + UserLocalizationsTable.TABLE);


        //All necessary tables you like to create will create here
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_USER_TIMES);
        db.execSQL(CREATE_TABLE_USER_LOCALIZATIONS);
    }

    public DBHelper open(Context context) throws SQLException {
//        myDataBase = this.getWritableDatabase();
//        myDataBase =  getWritableDatabase();
//        Log.d(TAG, "DbHelper Opening Version: " + this.myDataBase.getVersion());
        return this;
    }

    //http://stackoverflow.com/questions/7647566/why-is-onupgrade-not-being-invoked-on-android-sqlite-database
//    public SQLiteDatabase getWritableDatabase(Context context) {
//        SQLiteOpenHelper helper = new DBHelper(context);
//        SQLiteDatabase myDataBase = helper.getWritableDatabase();
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
        db.execSQL("DROP TABLE IF EXISTS " + UserTimesTable.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + UserLocalizationsTable.TABLE);


        // Create tables again
        onCreate(db);
    }

}
