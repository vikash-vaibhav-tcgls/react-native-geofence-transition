package com.geotransition;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.util.Log;
import android.content.Context;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class Utils {
static String LOG_TAG = "geo-transitionnn";
static final String CHANNEL_ID = "GEOFENCE_CHANNEL";
  static boolean isLocationEnabled(Activity context){
    LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    if(locationManager==null){
      Log.e(LOG_TAG, "Need location permission");
      return false;
    }
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
  }

  static boolean checkLocationPermission(Activity context){
    if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q ){
      return  ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }
    return  context.checkSelfPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
      == PackageManager.PERMISSION_GRANTED;
  }

 static boolean isNotificationEnabled(Activity context){
    return NotificationManagerCompat.from(context).areNotificationsEnabled();
  }

  public static void sendNotification(Context context, NotificationCompat.Builder builder) {
    NotificationManager notificationManager = (NotificationManager)
      context.getSystemService(Context.NOTIFICATION_SERVICE);

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      NotificationChannel channel = new NotificationChannel(
        CHANNEL_ID, "Geofence Alerts", NotificationManager.IMPORTANCE_HIGH);
      notificationManager.createNotificationChannel(channel);
    }
    notificationManager.notify((int) System.currentTimeMillis(), builder.build());
  }

  public enum GeofenceTransitionType {
    BOTH("both"),
    ENTER("entry"),
    EXIT("exit");

    private final String value;

    GeofenceTransitionType(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }
  }

}
