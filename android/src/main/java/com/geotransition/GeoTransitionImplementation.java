package com.geotransition;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import com.facebook.react.bridge.Promise;
import androidx.core.app.ActivityCompat;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableMap;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import java.util.Collections;


public class GeoTransitionImplementation {
  private Promise _promise = null;
  private ReactApplicationContext reactContext;
  private GeofencingClient geofencingClient;
  GeoTransitionImplementation(ReactApplicationContext reactContext) {
    this.reactContext = reactContext;
    geofencingClient = LocationServices.getGeofencingClient(reactContext);
  }


  @SuppressLint("MissingPermission")
  void addGeofence(double latitude, double longitude, float radius, long expireDuration, String event, String geoRequestId, PendingIntent geofenceIntent) {

    if (!Utils.checkLocationPermission(this.reactContext.getCurrentActivity())) {
      rejectPromise("location_permission", "Need background location permission");
      return;
    }

    if (!Utils.isNotificationEnabled(this.reactContext.getCurrentActivity())) {
      rejectPromise("notification_permission", "Need Notification permission");
      return;
    }

    LocationManager locationManager = (LocationManager) this.reactContext.getSystemService(Context.LOCATION_SERVICE);
    locationManager.sendExtraCommand(LocationManager.GPS_PROVIDER, "delete_aiding_data", null);

    Geofence.Builder geofence = new Geofence.Builder()
      .setRequestId(geoRequestId)
      .setCircularRegion(latitude, longitude, radius)
      .setExpirationDuration(expireDuration);

    GeofencingRequest.Builder geofencingRequest = new GeofencingRequest.Builder();
    geofencingRequest.setInitialTrigger(0);

    if (event.equals(Utils.GeofenceTransitionType.ENTER.getValue())) {
      geofence.setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER);
    } else if (event.equals(Utils.GeofenceTransitionType.EXIT.getValue())) {
      geofence.setTransitionTypes(Geofence.GEOFENCE_TRANSITION_EXIT);
    } else {
      geofence.setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT);
    }
    geofencingRequest.addGeofence(geofence.build());
    if (ActivityCompat.checkSelfPermission(reactContext, Manifest.permission.ACCESS_FINE_LOCATION)
      == PackageManager.PERMISSION_GRANTED) {
      geofencingClient.addGeofences(geofencingRequest.build(),geofenceIntent)
        .addOnSuccessListener(unused -> resolvePromise("Success"))
        .addOnFailureListener(e -> rejectPromise("fencing_Failed", e.toString()));
    }else{
      rejectPromise("fine_location_permission", "Need fine location permission");
    }
  }

  PendingIntent getGeofencePendingIntent(ReadableMap content) {

    Intent intent = new Intent(this.reactContext, GeofenceBroadcastReceiver.class);
    if(content!=null){
      intent.putExtra("title",content.getString("title"));
      intent.putExtra("text", content.getString("text"));
      intent.putExtra("iconName", content.getString("iconName"));
      if(content.hasKey("deeplinkUrl")){
        intent.putExtra("deeplinkUrl", content.getString("deeplinkUrl"));
      }
    }
    return PendingIntent.getBroadcast(this.reactContext, (int) System.currentTimeMillis(), intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);
  }

  void removeAddedGeofenceById(String geoRequestId) {
    geofencingClient.removeGeofences(Collections.singletonList(geoRequestId))
      .addOnSuccessListener(message -> resolvePromise("Deleted"))
      .addOnFailureListener(ex -> rejectPromise("not_deleted", ex.toString()));
  }

  void removeAllAddedGeofence(){
    geofencingClient.removeGeofences(getGeofencePendingIntent(null))
      .addOnSuccessListener(unused ->resolvePromise("Deleted"))
      .addOnFailureListener(ex -> rejectPromise("not_deleted", ex.toString()));

  }


  void resolvePromise(Object data) {
    if (_promise != null)
      _promise.resolve(data);
    this._promise = null;
  }

  void rejectPromise(String erroCode, String message) {
    if (_promise != null)
      _promise.reject(erroCode, message);
    this._promise = null;
  }

  void assignPromise(Promise promise) {
    this._promise = promise;
  }


}
