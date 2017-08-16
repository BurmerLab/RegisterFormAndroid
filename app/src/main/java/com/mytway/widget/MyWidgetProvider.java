package com.mytway.widget;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.widget.RemoteViews;

import com.mytway.activity.R;
import com.mytway.activity.utils.NoPermissionsActivity;
import com.mytway.activity.utils.SettingsActivity;
import com.mytway.geolocalization.MytwayGeolocalizationService;
import com.mytway.properties.PropertiesValues;
import com.mytway.utility.permission.PermissionUtil;

import java.util.Calendar;

public class MyWidgetProvider extends AppWidgetProvider {

	private static final String TAG = MyWidgetProvider.class.getSimpleName();

	private PendingIntent service = null;

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds){

		final AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

		final Calendar TIME = Calendar.getInstance();
		TIME.set(Calendar.MINUTE, 0);
		TIME.set(Calendar.SECOND, 0);
		TIME.set(Calendar.MILLISECOND, 0);

		final Intent intentToMytwayGeolocalizationService = new Intent(context, MytwayGeolocalizationService.class);

		if (service == null){
			service = PendingIntent.getService(context, 0, intentToMytwayGeolocalizationService, PendingIntent.FLAG_CANCEL_CURRENT);
		}

		// initializing widget layout
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(),	R.layout.mytway5_table_middle_widget_layout);

		//refresh button
		Intent intentSync = new Intent(context, MyWidgetProvider.class);
		intentSync.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE); //You need to specify the action for the intent. Right now that intent is doing nothing for there is no action to be broadcasted.

		//tutuaj jest odswiezanie po kliknieciu na guzik :)
		remoteViews.setOnClickPendingIntent(R.id.refreshImage, MyWidgetProvider.buildButtonSettingsPendingIntent(context,
				AppWidgetManager.getInstance(context),
				new int[]{0}, remoteViews, R.id.refreshImage));


//		remoteViews.setOnClickPendingIntent(R.id.image4, MyWidgetProvider.buildButtonSettingsPendingIntent(context,
//				AppWidgetManager.getInstance(context),
//				new int[]{0}, remoteViews, R.id.image4));


		appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);

		//http://www.parallelrealities.co.uk/2011/09/using-alarmmanager-for-updating-android.html
		manager.setRepeating(AlarmManager.RTC, TIME.getTime().getTime(),
				PropertiesValues.INTERVAL_TO_REPEAT_SERVICE_METHOD_IN_SECONDS, service);

		// after click, move to permission activity:
//		openNewActivity(context, appWidgetManager, appWidgetIds, remoteViews, R.id.refreshImage, new String[0]);

		pushWidgetUpdate(context, remoteViews);
	}

	@Override
	public void onDisabled(Context context)
	{
		final AlarmManager m = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

		m.cancel(service);
	}

	public static void openNewActivity(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds, RemoteViews remoteViews, int viewId, String[] missingPermissions) {
		Intent configIntent = new Intent(context, NoPermissionsActivity.class);
		configIntent.putExtra("MissingPermissions", missingPermissions);

		PendingIntent configPendingIntent = PendingIntent.getActivity(context, 0, configIntent, 0);
		remoteViews.setOnClickPendingIntent(viewId, configPendingIntent);
		appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
	}

	public static PendingIntent buildButtonPendingIntent(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds, RemoteViews remoteViews, int viewId) {
		++MyWidgetIntentReceiver.clickCount;

		Intent intent;

		if (PermissionUtil.checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, context)
				&& PermissionUtil.checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION, context)) {

			MytwayGeolocalizationService geolocalization = new MytwayGeolocalizationService(context);
			geolocalization.getLocalization();
			double latitudeLocalization = geolocalization.getLatitude();
			double longitudeLocalization = geolocalization.getLongitude();

			intent = new Intent();
			intent.setAction(WidgetUtils.WIDGET_UPDATE_ACTION);
			intent.putExtra("DUPA", "Lat:" + latitudeLocalization + " Lon: " + longitudeLocalization);
		}else{
			String[] missingPermissions =
					obtainMissingPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, context);

			openNewActivity(context, appWidgetManager, appWidgetIds, remoteViews, viewId, missingPermissions);

			//Nie ma permissions, wyswietl activity!!
			intent = new Intent(context, NoPermissionsActivity.class);
			intent.setAction("PERMISSIONS");
			intent.putExtra("MissingPermissions", missingPermissions);

			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
			context.sendBroadcast(intent);
		}

		return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
	}

	public static PendingIntent refreshWidgetContent(Context context) {

		Intent pushIntent = new Intent(context, MytwayGeolocalizationService.class);
		context.startService(pushIntent);

		return PendingIntent.getBroadcast(context, 0, pushIntent, PendingIntent.FLAG_UPDATE_CURRENT);
	}

	public static PendingIntent buildButtonSettingsPendingIntent(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds, RemoteViews remoteViews, int viewId) {
		++MyWidgetIntentReceiver.clickCount;

//		Intent intent;

//		if (PermissionUtil.checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, context)
//				&& PermissionUtil.checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION, context)) {

			Intent intent = new Intent();
			intent.setAction(WidgetUtils.WIDGET_UPDATE_ACTION);
//		}else{
//			String[] missingPermissions =
//					obtainMissingPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, context);
//
//			openNewActivity(context, appWidgetManager, appWidgetIds, remoteViews, viewId, missingPermissions);
//
//			//Nie ma permissions, wyswietl activity!!
//			intent = new Intent(context, SettingsActivity.class);
//			intent.setAction("PERMISSIONS");
//			intent.putExtra("MissingPermissions", missingPermissions);
//
//			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			context.startActivity(intent);
//			context.sendBroadcast(intent);
//		}

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
