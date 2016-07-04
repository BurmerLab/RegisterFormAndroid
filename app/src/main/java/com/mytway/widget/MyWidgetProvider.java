package com.mytway.widget;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.mytway.activity.R;
import com.mytway.activity.utils.NoPermissionsActivity;
import com.mytway.behaviour.RideProcessor;
import com.mytway.geolocalization.MytwayGeolocalization;
import com.mytway.utility.permission.PermissionUtil;

import java.util.ArrayList;
import java.util.List;

public class MyWidgetProvider extends AppWidgetProvider {

	private static final String TAG = MyWidgetProvider.class.getSimpleName();

	private static final int PERMISSION_REQUEST_CODE_LOCATION = 1;

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

		// initializing widget layout
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(),	R.layout.mytway5_table_middle_widget_layout);

		RideProcessor rideProcessor = new RideProcessor();

		MytwayGeolocalization geolocalization = new MytwayGeolocalization(context);

		android.location.Location location = geolocalization.getLocation();

		remoteViews.setTextViewText(R.id.firstTimeTextView, rideProcessor.rideProcess(context));


//		http://stackoverflow.com/questions/13491143/get-location-broadcastreceiver-locationlistener/13491407

//		if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
//				&& ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//			return;
//		}

//		String text = "LOL";
//		if (PermissionUtil.checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, context)
//				&& PermissionUtil.checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION, context)) {
//			//http://stackoverflow.com/questions/31502650/android-m-request-permission-non-activity
//			Log.d(TAG, "Jestem w permissionUtilIfie");
//
//			MytwayGeolocalization geolocalization = new MytwayGeolocalization(context);
//			double latitudeLocalization = geolocalization.getLatitude();
//			double longitudeLocalization = geolocalization.getLongitude();
//
//			Log.d(TAG, "Localization: lat: " + latitudeLocalization + " lon: " + longitudeLocalization);
//
//			text = String.valueOf(geolocalization.getLatitude());
//			Log.d(TAG, "text: " + text);
//		}else{
////			PermissionUtil.requestPermission(Manifest.permission.ACCESS_FINE_LOCATION,
////					PERMISSION_REQUEST_CODE_LOCATION,
////					context,
////					HomePlaceRegisterActivity.this,
////					"SOPRRY CAN HELP");
//		}


//		remoteViews.setTextViewText(R.id.firstTimeTextView, "KUPA");
//		remoteViews.setTextViewText(R.id.firstTimeTextView, text);

		//http://stackoverflow.com/questions/29821886/receiverrestrictedcontext-cannot-be-cast-to-android-app-activity
//		remoteViews.setTextViewText(R.id.title, rideProcessor.geolocalizationText(context, activity));

		// register for button event
		remoteViews.setOnClickPendingIntent(R.id.image32, buildButtonPendingIntent(context, appWidgetManager, appWidgetIds, remoteViews, R.id.image32));

		openNewActivity(context, appWidgetManager, appWidgetIds, remoteViews, R.id.image3, new String[0]);

//		Intent intent = new Intent("APPWIDGET_UPDATE");
//		intent.putExtra("newItemArrived", "Neue Frage erschienen");
//		sendBroadcast(intent);


		// request for widget update
		pushWidgetUpdate(context, remoteViews);
	}

	private static void openNewActivity(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds, RemoteViews remoteViews, int viewId, String[] missingPermissions) {
		Intent configIntent = new Intent(context, NoPermissionsActivity.class);
		configIntent.putExtra("MissingPermissions", missingPermissions);

		PendingIntent configPendingIntent = PendingIntent.getActivity(context, 0, configIntent, 0);
		remoteViews.setOnClickPendingIntent(viewId, configPendingIntent);
		appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
	}

	public static PendingIntent buildButtonPendingIntent(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds, RemoteViews remoteViews, int viewId) {
		++MyWidgetIntentReceiver.clickCount;

		// initiate widget update request

		Intent intent;

//		intent = new Intent();
//		intent.setAction(WidgetUtils.WIDGET_UPDATE_ACTION);
//		intent.putExtra("DUPA", "DUPKA" + MyWidgetIntentReceiver.clickCount);

		if (PermissionUtil.checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, context)
				&& PermissionUtil.checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION, context)) {

			MytwayGeolocalization geolocalization = new MytwayGeolocalization(context);
			double latitudeLocalization = geolocalization.getLatitude();
			double longitudeLocalization = geolocalization.getLongitude();

			intent = new Intent();
			intent.setAction(WidgetUtils.WIDGET_UPDATE_ACTION);
//			intent.putExtra("DUPA", "DUPKA" + MyWidgetIntentReceiver.clickCount);
			intent.putExtra("DUPA", "Lat:" + latitudeLocalization + " Lon: " + longitudeLocalization);
		}else{
			String[] missingPermissions =
					obtainMissingPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, context);

//			openNewActivity(context, appWidgetManager, appWidgetIds, remoteViews, viewId, missingPermissions);

			//Nie ma permissions, wyswietl activity!!
			intent = new Intent(context, NoPermissionsActivity.class);
			intent.setAction("PERMISSIONS");
			intent.putExtra("MissingPermissions", missingPermissions);

			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
			context.sendBroadcast(intent);
//			PermissionUtil.requestPermission(Manifest.permission.ACCESS_FINE_LOCATION,
//					PERMISSION_REQUEST_CODE_LOCATION,
//					context,
//					(Activity) context,
//					context.getString(R.string.localization_will_help_with_choose_your_work_place));//Localization will help with choose your work place
		}

		return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
	}

	public static String[] obtainMissingPermissions(String[] permissions, Context context) {
		String[] missingPermissions = new String[permissions.length];

		for(int i = 0; i < permissions.length; i++ ){
			if(! PermissionUtil.checkPermission(permissions[i], context)){
				missingPermissions[i] = permissions[i];
			}
		}
		return  missingPermissions;

	}

	private static CharSequence getDesc() {
		return "Sync to see some of our funniest joke collections";
	}

	private static CharSequence getTitle() {
		return "Funny Jokes";
	}

	public static void pushWidgetUpdate(Context context, RemoteViews remoteViews) {
		ComponentName myWidget = new ComponentName(context,	MyWidgetProvider.class);
		AppWidgetManager manager = AppWidgetManager.getInstance(context);
		manager.updateAppWidget(myWidget, remoteViews);
	}


}
