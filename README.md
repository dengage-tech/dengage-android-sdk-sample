# Dengage Android Sdk 
## Useful Links
- [SDK Documentation](https://dev.dengage.com/mobile-sdk/android/)
- [Admin Panel](https://appdev.dengage.com/)

## Sdk Implementation
- Add dengage sdk dependency to your project level gradle file
```
dependencies {
    ...
    implementation 'com.github.whitehorse-technology:dengage.android.sdk:x.y.z'
    ...
}
```
- Add other dependencies for sdk usage
```
dependencies {
    ...
    
    // firebase
    implementation 'com.google.firebase:firebase-core:18.0.2'
    implementation "com.google.firebase:firebase-messaging:21.0.1"
    implementation 'com.google.firebase:firebase-analytics:18.0.2'
    
    // huawei
    implementation 'com.huawei.hms:push:5.0.1.300'
    implementation 'com.huawei.hms:ads-identifier:3.4.30.307'
    
    // gson
    implementation 'com.google.code.gson:gson:2.8.6'
    
    ...
}
```

## Sample App Content

### 1 - Adding Manifest Rules (See [AndroidManifest](https://github.com/dengage-tech/dengage-android-sdk-sample/blob/master/app/src/main/AndroidManifest.xml))
- `FcmMessagingService` -> Fcm Messaging Service for handling push messages comes from firebase
- `HmsMessagingService` -> Hms Messaging Service for handling push messages comes from huawei messaging service
- `PushNotificationReceiver` -> For handling push notifications comes to messaging service classes. Contains notification message ui creating
- `den_event_api_url` (meta-data) -> Event api url of Dengage (Optional parameter)
- `den_push_api_url`  (meta-data) -> Push api url of Dengage (Optional parameter)

### 2 - Initiating Dengage Classes (See class [App](https://github.com/dengage-tech/dengage-android-sdk-sample/blob/master/app/src/main/java/com/dengage/android/kotlin/sample/App.kt))
- `DengageManager` -> Initiate this class once in application runtime for using Dengage functions
- `DengageEvent`   -> Initiate this class once in application runtime for using Dengage events
- `registerActivityLifecycleCallbacks` -> Register activity lifecycle callback to handle application bring-to-foreground time

### 3 - Sending Page View Events (See class [BaseDataBindingFragment](https://github.com/dengage-tech/dengage-android-sdk-sample/blob/master/app/src/main/java/com/dengage/android/kotlin/sample/ui/base/BaseDataBindingFragment.kt))
- `sendPageView` -> Send page view event with page type or name

### 4 - Showing Device Subscription Parameters (See class [DeviceInfoFragment](https://github.com/dengage-tech/dengage-android-sdk-sample/blob/master/app/src/main/java/com/dengage/android/kotlin/sample/ui/fragment/DeviceInfoFragment.kt))
Some variables on your device subscription were shown in this page. You can check these parameters if any error occured.

### 5 - Changing Contact Key (See class [ContactKeyFragment](https://github.com/dengage-tech/dengage-android-sdk-sample/blob/master/app/src/main/java/com/dengage/android/kotlin/sample/ui/fragment/ContactKeyFragment.kt))
You can change your contact key on Dengage after user login, register, logout etc. Assume this parameter as user unique key.

### 6 - Listing Inbox Messages & Actions of Inbox Messages (See class [InboxMessagesFragment](https://github.com/dengage-tech/dengage-android-sdk-sample/blob/master/app/src/main/java/com/dengage/android/kotlin/sample/ui/fragment/InboxMessagesFragment.kt))
- `DengageManager.getInboxMessages` -> Use this method for fetching inbox messages with pagination.
    #### Method Params
    - `limit` -> inbox message item limit per page
    - `offset` -> inbox message offset for page beginning
    - `DengageCallback<MutableList<InboxMessage>>` -> for inbox messages response

- `DengageManager.setInboxMessageAsClicked` -> Call this method with inbox message id if inbox message is clicked
- `DengageManager.deleteInboxMessage` -> Call this method with inbox message id if inbox message is deleted

### 7 - Sending Custom Events (See class [CustomEventFragment](https://github.com/dengage-tech/dengage-android-sdk-sample/blob/master/app/src/main/java/com/dengage/android/kotlin/sample/ui/fragment/CustomEventFragment.kt))
- `DengageManager.sendCustomEvent` -> Use this method for sending custom events
    #### Method Params
    - `tableName` -> table name of custom event 
    - `key` -> contact key of your user
    - `data` -> custom event data as map

### 8 - In App Message (See class [InAppMessageFragment](https://github.com/dengage-tech/dengage-android-sdk-sample/blob/master/app/src/main/java/com/dengage/android/kotlin/sample/ui/fragment/InAppMessageFragment.kt))
- `DengageManager.setNavigation` -> Call this method for showing in app message on current page
    #### Method Params
    - `activity` -> `AppCompatActivity` for showing in app message 
    - `screenName` -> screen name of current screen

- You can change some colors with overriding color values with the same resource names (See on [colors.xml](https://github.com/dengage-tech/dengage-android-sdk-sample/blob/master/app/src/main/res/values/colors.xml))
    - `in_app_message_color_status_bar` -> In app message dialog has a status bar itself, you should set android status bar color 

### 9 - Send Tags (See class [TagsFragment](https://github.com/dengage-tech/dengage-android-sdk-sample/blob/master/app/src/main/java/com/dengage/android/kotlin/sample/ui/fragment/TagsFragment.kt))
- `DengageManager.setTags` -> Use this method for sending tags
    #### Method Params
    - `tags` -> hashmap of tag parameters below
        - `tag` -> tag name
        - `value` -> tag value
        - `changeTime` -> tag change time
        - `changeValue` -> tag change value
        - `removeTime` -> tag remove time