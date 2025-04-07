package com.geotransition;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

public class GeofenceBroadcastReceiver extends BroadcastReceiver {

  private static final String TAG = "GeofenceReceiver";

  @Override
  public void onReceive(Context context, Intent intent) {

    GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);

    if (geofencingEvent == null || geofencingEvent.hasError()) {
      Log.e(TAG, "Geofencing error: " + geofencingEvent.getErrorCode());
      return;
    }

    int geofenceTransition = geofencingEvent.getGeofenceTransition();
    if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER || geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT
    ) {

      int iconResId = context.getApplicationContext().getResources().getIdentifier(intent.getStringExtra("iconName"), "drawable", context.getPackageName());
      NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, Utils.CHANNEL_ID)
        .setSmallIcon(iconResId)
        .setContentTitle(intent.getStringExtra("title"))
        .setContentText(intent.getStringExtra("text"))
        .setAutoCancel(true).setPriority(NotificationCompat.PRIORITY_DEFAULT).setDefaults(NotificationCompat.DEFAULT_ALL);

      String deeplinkUrl = intent.getStringExtra("deeplinkUrl");
      if (deeplinkUrl != null && !deeplinkUrl.isEmpty()) {
        Intent nIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(deeplinkUrl));
        nIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(
          context, 0, nIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        notificationBuilder.setContentIntent(pendingIntent);
      }
      Utils.sendNotification(context, notificationBuilder);
    } else {
      Log.e(TAG, "Errr at 41");
    }

  }
}
