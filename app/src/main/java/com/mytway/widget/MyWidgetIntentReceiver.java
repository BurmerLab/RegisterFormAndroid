package com.mytway.widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.mytway.activity.R;
import com.mytway.activity.utils.NoPermissionsActivity;
import com.mytway.geolocalization.MytwayGeolocalization;

import org.w3c.dom.Text;

public class MyWidgetIntentReceiver extends BroadcastReceiver {
	public static int clickCount = 0;
	private String msg[] = null;

	private static final String TAG = "Message recieved";

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(WidgetUtils.WIDGET_UPDATE_ACTION)) {
			String text = intent.getStringExtra("DUPA");

			//dziala jesli user ma uprawnienia:
//			MytwayGeolocalization geolocalization = new MytwayGeolocalization(context);
//			double latitudeLocalization = geolocalization.getLatitude();
//			double longitudeLocalization = geolocalization.getLongitude();
//			text = "" +latitudeLocalization;
			//------------------------------------------

			updateWidgetPictureAndButtonListener(context, text);

			//http://stackoverflow.com/questions/13491143/get-location-broadcastreceiver-locationlistener/13491407

		}

		if(intent.getAction().equals("PERMISSION")){
			intent = new Intent(context, NoPermissionsActivity.class);
			intent.putExtra("Permissions", "No");
			context.startActivity(intent);
		}

		if (intent.getAction().equals("DUPKA")) {
			// Start your Location IntentService here
			Intent i = new Intent(context, MytwayGeolocalization.class);
			context.startService(i);
		}
	}

	private void updateWidgetPictureAndButtonListener(Context context, String messageText) {
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(),	R.layout.mytway5_table_middle_widget_layout);

		// updating view
		remoteViews.setTextViewText(R.id.title, messageText);
//		remoteViews.setTextViewText(R.id.desc, getDesc(context));


		// re-registering for click listener, remoteViews, R.id.image32
		remoteViews.setOnClickPendingIntent(R.id.sync_button, MyWidgetProvider.buildButtonPendingIntent(context, AppWidgetManager.getInstance(context),
				new int[]{0}, remoteViews, R.id.image32));

		MyWidgetProvider.pushWidgetUpdate(context.getApplicationContext(),	remoteViews);
	}

	private String getDesc(Context context) {
		// some static jokes from xml
		msg = context.getResources().getStringArray(R.array.news_headlines);
		if (clickCount >= msg.length) {
			clickCount = 0;
		}
		return msg[clickCount];
	}
}
