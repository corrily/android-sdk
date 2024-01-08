# Corrily SDK

## Installation

### Gradle

The preferred installation method is with Gradle. This is a tool for automating the distribution of Kotlin/Java code and is integrated into the Android Studio compiler. In Android Studio, do the following:

Open settings.gradle
Add maven `{ url 'CORRILY_MAVEN_URL' }` to your repositories { ... }
Add `implementation "CORRILY_PACKAGE_NAME:<INSERT-LATEST-VERSION>"` latest version
Make sure you press Sync Now
Edit your AndroidManifest.xml by adding:

```
<manifest ...>
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="com.android.vending.BILLING" />
```

## Usage

### Initialization

To initiate the CorrilySDK, call the `start` method with your `apiKey`. This should only be done once, typically when your app is launched.

```kotlin
CorrilySDK.start(context: Context, apiKey: String)
```

### User Identification

Corrily works with 2 types of user identifiers:

- anonymous `device_id`
- `user_id` of signed user

On the first launch SDK generates random deviceID and stores it in Keychain. Every Paywall request will pass this deviceID, and Corrily will always respond with the same Products to provide consistent user experience at the time of AB-experiments.

Once user logs in, you shoud configure it in SDK to add `user_id` to every Paywall request. This is especially imporant for cross-platforms apps, where user might see the Paywall on the Web and inside the App.

The setUser method allows you to add additional properties for your user. Moreover, it ensures that the user's prices are linked to their userId and persisted across sessions, even if they log out and re-login.

```kotlin
CorrilySDK.setUser(userId: String)
```

### Determining User's country

Both App Store and Corrily Platform supports country-based price localization. Therefore, each signed User or anonymous Device should be associated with a country.
By default, Corrily SDK tries to **fetch User's country from Telephony Service**, and use it to fetch Paywall from Corrily.

It's also possible to explicitly set User's country by running:

```kotlin
CorrilySDK.setUser(userId = "my_user_id", country = "US")
```

### Tracking Conversion in Corrily Dashboard

To unlock the full potential of Corrily analytics, it's important to let Corrily know about every new anonymous or registered user of the app by sending [identification request](https://docs.corrily.com/api-reference/set-user-characteristics). By default SDK won't send randomly generated `device_id` to server, because some apps always requre authentication, and don't need anonymous device_id mechanisms at all.

To notify Corrily back-end about a new anonymous device_id, you should call:

```kotlin
CorrilySDK.identifyUser()
```

or, if you want to explicitly provide country code for anonymous device_id, then:

```kotlin
CorrilySDK.identifyUser(country = "US")
```

<br>

The default behavior is the opposite for Users. `CorrilySDK.setUser` method [sends](https://docs.corrily.com/api-reference/set-user-characteristics) user information to the server under the hood. This is the correct behavior for most of the cases, but the developer has an option to disable [identification request](https://docs.corrily.com/api-reference/set-user-characteristics):

```swift
CorrilySDK.setUser(userId = "optional_user_id", disableIdentificationRequest: true)
```

### Fetching Products

To fetch the content to display on your Paywall, including list of Products, their features and paywall design details, use `requestPaywall` method.

```kotlin
CoroutineScope(Dispatchers.IO).launch {
  val response = CorrilySDK.requestPaywall()
}
```

Based on country and other User attributes, Corrily will dynamically determine the Products and other details to be displayed for a given User.
Corrily Platform allows you to show different Prices, sets of Products, and different Paywalls for a different Users,
varying them based on User's country, audience, experiment arm or individual config specified on User's page in dashboard.

Ream more about Paywalls Segmentation [in the docs](https://docs.corrily.com/paywall-builder/configure#segmentation-rules-for-paywalls).

It's also possible to explicitly provide `paywallId` to ignore segmentation rules:

```kotlin
val response = CorrilySDK.requestPaywall(paywallId = 1234)
```

### Paywall Template Rendering

<img src="https://github.com/corrily/ios-sdk/blob/main/docs/paywall_01.png?raw=true" alt="Corrily Paywall Template" style="max-height: 500px;">

To display the default paywall template View, use the renderPaywall method:

```kotlin
CorrilySDK.RenderPaywall(activity: Activity)
```

Based on country and other user attributes, Corrily will dynamically determine the Paywall to be displayed for a given user. Corrily Platform allows you to have multiple Paywalls and vary them depends on user's country, audience, or experiment arm. Ream more about [Paywalls Segmentation](https://docs.corrily.com/paywall-builder/configure#segmentation-rules-for-paywalls).

It's possible to explicitly provide `paywallId` too:

```swift
CorrilySDK.RenderPaywall(activity: Activity, paywallId = 1234)
```

### Paywall Custom Component Rendering

To render your own Paywall View, pass it to the renderPaywall method:

```kotlin
CorrilySDK.RenderPaywall() { factory in
    CustomView(factory: factory)
})
```

An example of the Custom Paywall View could be found [here](./Example/Corrily/Corrily/CustomView.swift).

### Setting a Fallback Paywall

In scenarios where the SDK is unable to retrieve the paywall details from the API, having a fallback paywall ensures your users have uninterrupted access. Use the setFallbackPaywall method to set this up:

```kotlin
CorrilySDK.setFallbackPaywall(jsonString)
```

The `jsonString` should be a stringified version of the JSON response you get from `/v1/paywall` endpoint. An example of the Custom Paywall View could be found [here](./Example/Corrily/Corrily/FallbackPaywallView.swift).

### Purchase

To initiate the purchase process, call the `purchase` method with the product `apiId`:

```kotlin
let billingManager = BillingManager.getInstance(context: Context)
billingManager.purchase("product_api_id_here")
```

Sometimes, users will need to restore their previous purchases, such as when moving to a new device. To accommodate this, use the restorePurchase method:

```kotlin
let billingManager = BillingManager.getInstance(context: Context)
billingManager.restorePurchase()
```
