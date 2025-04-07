import { NativeModules, Platform } from 'react-native';

const LINKING_ERROR =
  `The package 'react-native-geo-transition' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo Go\n';

const GeoTransition = NativeModules.GeoTransition
  ? NativeModules.GeoTransition
  : new Proxy(
      {},
      {
        get() {
          throw new Error(LINKING_ERROR);
        },
      }
    );

export enum GeofenceEvent {
  ENTRY = 'entry',
  EXIT = 'exit',
}

export type NotificationType = {
  title: string;
  text: string;
  deeplinkUrl?: string;
  iconName: string;
};

export type GeofenceProps = {
  latitude: number;
  longitude: number;
  radius: number;
  /**
   *  @expireDuration  = -1 for forever
   */
  expireDuration: number;
  event: GeofenceEvent;
  geoRequestId: string;
  notificationContent: NotificationType;
};

export function registerGeofenceNotification(
  config: GeofenceProps
): Promise<string> {
  return GeoTransition.registerGeofenceNotification(config);
}

export function removeRegisteredGeofence(requestId: string): Promise<string> {
  return GeoTransition.removeRegisteredGeofence(requestId);
}

export function removeAllRegisteredGeofence(): Promise<string> {
  return GeoTransition.removeAllRegisteredGeofence();
}
