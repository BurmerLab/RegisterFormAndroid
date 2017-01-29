package com.mytway.widget;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.mytway.activity.R;
import com.mytway.activity.utils.NoPermissionsActivity;
import com.mytway.geolocalization.MytwayGeolocalizationService;

public class MyWidgetIntentReceiver extends BroadcastReceiver {
	public static int clickCount = 0;
	private String msg[] = null;

	@Override
	public void onReceive(Context context, Intent intent) {
//		if(intent.getAction().equals("PERMISSION")){
//			intent = new Intent(context, NoPermissionsActivity.class);
//			intent.putExtra("Permissions", "No");
//			context.startActivity(intent);
//		}

		//added for restart widget after bootloaded app
		if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
			Intent pushIntent = new Intent(context, MytwayGeolocalizationService.class);
			context.startService(pushIntent);
		}

	}

	private void updateWidgetPictureAndButtonListener(Context context, String messageText) {
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(),	R.layout.mytway5_table_middle_widget_layout);

		// updating view
		remoteViews.setTextViewText(R.id.title, messageText);

		// re-registering for click listener, remoteViews, R.id.envelopeimage
		remoteViews.setOnClickPendingIntent(R.id.sync_button, MyWidgetProvider.buildButtonPendingIntent(context, AppWidgetManager.getInstance(context),
				new int[]{0}, remoteViews, R.id.envelopeImage));

		remoteViews.setOnClickPendingIntent(R.id.image4, MyWidgetProvider.buildButtonSettingsPendingIntent(context, AppWidgetManager.getInstance(context),
				new int[]{0}, remoteViews, R.id.image4));

		MyWidgetProvider.pushWidgetUpdate(context.getApplicationContext(),	remoteViews);
	}

}
