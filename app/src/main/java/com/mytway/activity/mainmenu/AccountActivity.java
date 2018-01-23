package com.mytway.activity.mainmenu;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.mytway.activity.R;
import com.mytway.activity.application.MytwayActivity;


public class AccountActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);
    }

    //    ------------------THREE DOTS MENU--------------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_three_dots, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        Intent intent;
        switch(itemId){
            case R.id.homeScreen:
                Toast.makeText(this, "home clicked", Toast.LENGTH_SHORT).show();
                intent = new Intent(this, MytwayActivity.class);
                this.startActivity(intent);
                break;
            case R.id.updateUserScreen:
                Toast.makeText(this, "update user clicked", Toast.LENGTH_SHORT).show();
                intent = new Intent(this, AccountActivity.class);
                this.startActivity(intent);
                break;
//            case R.id.homeScreen:
//                Toast.makeText(this, "home clicked", Toast.LENGTH_SHORT).show();
        }


        return true;
    }

//    --------------------------------------------------------------------------

}
