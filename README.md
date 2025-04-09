# react-native-geo-transition

### Geo fencing notification

It keep eyes on device location and generate notification when a device enters or exit a zone

## Installation

Not yet hosted on npm, you need to clone it and install it locally

### Run

#### Clone the project

```sh
git https://github.com/vikash-vaibhav-tcgls/react-native-geofence-transition.git
```

#### Create build

```sh
# navigate to library directory
yarn install

yarn prepare
```

Now, delete node_modules folders in library directory

### Now, navigate to your project where you want to install the library

### Run

```sh
yarn add /username/your/directory/path-to-cloned-library
```

And done.

## Some setup

Add permissions to manifest file
```manifest
  <uses-permission android:name="android.permission.POST_NOTIFICATIONS"/>
  <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
  <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
  <uses-permission android:name="android.permission.ACCESS_BACKGROUND_LOCATION" />
  <uses-permission android:name="android.permission.ACCESS_LOCATION_EXTRA_COMMANDS" />
```

Register a broadcast receiver service 

```manifest
    <manifest>
        <application>
      <receiver android:name="com.geotransition.GeofenceBroadcastReceiver"/>
        </application>
    </manifest>
```
## Usage

Register location for geo-fencing

```javascript
import {
  GeofenceEvent,
  registerGeofenceNotification,
  type GeofenceProps
} from 'react-native-geo-transition';

    await registerGeofenceNotification({
      event: GeofenceEvent.ENTRY,
      expireDuration: -1 // register for forever,
      geoRequestId: "any-id-string",
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
```

 ## 📍 Geofence Notification API

### 🔁 Geofence Event Types

| Enum                    | Value     | Description                         |
|-------------------------|-----------|-------------------------------------|
| `GeofenceEvent.ENTRY`   | `'entry'` | Triggered when user enters the geofence |
| `GeofenceEvent.EXIT`    | `'exit'`  | Triggered when user exits the geofence  |

---

### 🔔 Notification Type

| Property      | Type     | Required | Description                           |
|---------------|----------|----------|---------------------------------------|
| `title`       | `string` | ✅        | Title shown in notification           |
| `text`        | `string` | ✅        | Message content in notification       |
| `deeplinkUrl` | `string` | ❌        | Optional deep link to open on tap     |
| `iconName`    | `string` | ✅        | Name of icon resource for notification|

---

### 📦 Geofence Configuration (`GeofenceProps`)

| Property              | Type               | Required | Description                                     |
|-----------------------|--------------------|----------|-------------------------------------------------|
| `latitude`            | `number`           | ✅        | Center latitude of the geofence                |
| `longitude`           | `number`           | ✅        | Center longitude of the geofence               |
| `radius`              | `number`           | ✅        | Radius in meters                               |
| `expireDuration`      | `number`           | ✅        | Time before expiration in milliseconds, use `-1` for no expiry |
| `event`               | `GeofenceEvent`    | ✅        | Type of event to listen for (`ENTRY`/`EXIT`)   |
| `geoRequestId`        | `string`           | ✅        | Unique identifier for the geofence             |
| `notificationContent` | `NotificationType` | ✅        | Notification content on trigger                |

---

### 🧠 API Functions

| Function                              | Signature                                          | Description                                  |
|---------------------------------------|----------------------------------------------------|----------------------------------------------|
| `registerGeofenceNotification`        | `(config: GeofenceProps) => Promise<string>`       | Registers a geofence notification            |
| `removeRegisteredGeofence`           | `(requestId: string) => Promise<string>`           | Removes a specific geofence by ID            |
| `removeAllRegisteredGeofence`        | `() => Promise<string>`                            | Removes all registered geofences             |


## Contributing

Project contains an example, checkout the example folder you can try feature by running example app.

### Run

Clone library

```sh
git clone https://github.com/vikash-vaibhav-tcgls/react-native-geofence-transition.git
```

install dependencies

```sh
yarn install
```

Open terminal and navigate to /example folder

```sh
# run metro server
yarn start
```

This will open android emulator on your system after successful build

```sh
# build for android
yarn android
```

-- or you can open `/example/android` folder in android studio and run it

Make sure app has gps background location permissions and turned on.

Checkout /example/src/App.tsx file to customize filters, setting as your need

## License

MIT

---

Made with [create-react-native-library](https://github.com/callstack/react-native-builder-bob)
