# Setting up your google maps API key.

Create a file in this directory with the following name:
`google_maps_api.xml`

Add the following code to that file:

```xml
<resources>
    <!--
    TODO: Before you run your application, you need a Google Maps API key.

    To get one, follow this link, follow the directions and press "Create" at the end:

    https://console.developers.google.com/flows/enableapi?apiid=maps_android_backend&keyType=CLIENT_SIDE_ANDROID&r=CB:A1:12:34:F0:E9:99:7C:31:FD:A9:37:F4:3E:BA:02:4F:17:27:79;com.example.orbis

    You can also add your credentials to an existing key, using these values:

    Package name:
    B0:82:03:C1:AB:1B:B7:73:9F:50:F3:55:64:EA:9C:26:5D:CD:12:6C

    SHA-1 certificate fingerprint:
    B0:82:03:C1:AB:1B:B7:73:9F:50:F3:55:64:EA:9C:26:5D:CD:12:6C

    Alternatively, follow the directions here:
    https://developers.google.com/maps/documentation/android/start#get-key

    Once you have your key (it starts with "AIza"), replace the "google_maps_key"
    string in this file.
    -->
    <string name="google_maps_key" templateMergeStrategy="preserve" translatable="false">google_maps_key</string>
</resources>
```

Follow the instructions withing the comments of the new file. Go to Build > Clean project, build your project and launch to verify that your key is working correctly.

- Jari Keting