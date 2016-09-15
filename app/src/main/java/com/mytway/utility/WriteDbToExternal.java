package com.mytway.utility;

import android.Manifest;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.mytway.activity.R;
import com.mytway.utility.permission.PermissionUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class WriteDbToExternal {




    public static void writeToSD(Context context) throws IOException {
        File sd = Environment.getExternalStorageDirectory();
        final String DB_NAME = "crud.db";
        String DB_PATH ;

        if (sd.canWrite()) {
            String currentDBPath = DB_NAME;
            String backupDBPath = "backupCrudName.db";

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                DB_PATH = context.getFilesDir().getAbsolutePath().replace("files", "databases") + File.separator;
            }
            else {
                DB_PATH = context.getFilesDir().getPath() + context.getPackageName() + "/databases/";
            }

            File currentDB = new File(DB_PATH, currentDBPath);
            File backupDB = new File(sd, backupDBPath);

            if (currentDB.exists()) {
                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());

                src.close();
                dst.close();
            }
            Log.i("WriteDbToExternal", "Saved to: " + backupDB.getAbsolutePath());
            Log.i("WriteDbToExternal", "Saved to: " + backupDB.getParent());

        }


    }

}
