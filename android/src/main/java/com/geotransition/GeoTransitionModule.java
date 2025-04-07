package com.geotransition;

import android.app.PendingIntent;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReadableMap;

class GeoTransitionModule extends ReactContextBaseJavaModule {
  final String NAME = "GeoTransition";
  final GeoTransitionImplementation geoTransitionImplementation;

  GeoTransitionModule(ReactApplicationContext reactContext) {
    super(reactContext);
    geoTransitionImplementation = new GeoTransitionImplementation(reactContext);
  }

  @NonNull
  @Override
  public String getName() {
    return NAME;
  }

  // Example method
  // See https://reactnative.dev/docs/native-modules-android
  @ReactMethod
  void multiply(Double a, Double b, Promise promise) {
    promise.resolve(a * b);
  }

  @ReactMethod
  void registerGeofenceNotification(ReadableMap props, Promise promise) {
    String[] neededKey = {"latitude", "longitude", "radius", "expireDuration", "event", "geoRequestId", "notificationContent"};
    for (String key : neededKey) {
      if (!props.hasKey(key)) {
        promise.reject("need_" + key, "missing " + key);
        return;
      }
    }
    geoTransitionImplementation.assignPromise(promise);
    PendingIntent intent = geoTransitionImplementation.getGeofencePendingIntent(props.getMap(neededKey[6]));
    geoTransitionImplementation.addGeofence(props.getDouble(neededKey[0]), props.getDouble(neededKey[1]), (float) props.getDouble(neededKey[2]), Math.round(props.getDouble(neededKey[3])), props.getString(neededKey[4]), props.getString(neededKey[5]), intent);
  }

  @ReactMethod
  void removeRegisteredGeofence(String requestId, Promise promise) {
    geoTransitionImplementation.assignPromise(promise);
    geoTransitionImplementation.removeAddedGeofenceById(requestId);
  }

  @ReactMethod
  void removeAllRegisteredGeofence(Promise promise) {
    geoTransitionImplementation.assignPromise(promise);
    geoTransitionImplementation.removeAllAddedGeofence();
  }


}
