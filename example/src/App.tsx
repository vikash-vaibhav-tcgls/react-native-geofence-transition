import { View, StyleSheet, Button, Alert, Text, Linking } from 'react-native';
import { useEffect, useState } from 'react';
import GetLocation, { type Location } from 'react-native-get-location';
import {
  GeofenceEvent,
  registerGeofenceNotification,
  removeAllRegisteredGeofence,
} from 'react-native-geo-transition';

const REQUEST_ID = 'iiiiidddddd2121212';
export default function App() {
  useEffect(() => {
    Linking.getInitialURL().then((a) => console.log('aaaaaaa', a));
  }, []);
  const [currentLocation, setCurrentLocation] = useState<
    Location | undefined
  >();

  const getCurrentLocation = () => {
    GetLocation.getCurrentPosition({
      enableHighAccuracy: true,
      timeout: 60000,
    })
      .then((location) => {
        setCurrentLocation(location);
        console.log(location);
      })
      .catch((error) => {
        const { code, message } = error;
        console.warn(code, message);
      });
  };

  const register = async () => {
    if (!currentLocation) {
      Alert.alert('get current location first');
      return;
    }
    await registerGeofenceNotification({
      event: GeofenceEvent.ENTRY,
      expireDuration: -1,
      geoRequestId: REQUEST_ID,
      latitude: currentLocation.latitude,
      longitude: currentLocation.longitude,
      radius: 100,
      notificationContent: {
        text: 'you are here in the area',
        iconName: 'baseline_cake_24',
        title: 'Wow, you got here!',
        deeplinkUrl: 'ziki://k',
      },
    })
      .then((message) => {
        console.log('registered message', message);
      })
      .catch(console.log);

    await registerGeofenceNotification({
      event: GeofenceEvent.EXIT,
      expireDuration: -1,
      geoRequestId: REQUEST_ID + '3232',
      latitude: currentLocation.latitude,
      longitude: currentLocation.longitude,
      radius: 100,
      notificationContent: {
        text: 'You have exited the area',
        iconName: 'baseline_cake_24',
        title: 'You have exited the reason, Submit your feedback',
        deeplinkUrl: 'ziki://k',
      },
    })
      .then((message) => {
        console.log('registered message', message);
      })
      .catch(console.log);
  };

  return (
    <View style={styles.container}>
      <Button title="Current Location" onPress={getCurrentLocation} />
      <View style={styles.buttonStyle} />
      <Button
        title="Register geofence for current location"
        onPress={register}
      />
      <View style={styles.buttonStyle} />
      <Button
        title="Remove registration"
        onPress={removeAllRegisteredGeofence}
      />
      <Text>{JSON.stringify(currentLocation, null, 4)}</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  buttonStyle: {
    marginVertical: 12,
  },
});
