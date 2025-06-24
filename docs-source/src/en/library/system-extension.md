# system-extension

![Maven Central](https://img.shields.io/maven-central/v/com.highcapable.betterandroid/system-extension?logo=apachemaven&logoColor=orange&style=flat-square)
<span style="margin-left: 5px"/>
![Maven metadata URL](https://img.shields.io/maven-metadata/v?metadataUrl=https%3A%2F%2Fraw.githubusercontent.com%2FHighCapable%2Fmaven-repository%2Frefs%2Fheads%2Fmain%2Frepository%2Freleases%2Fcom%2Fhighcapable%2Fbetterandroid%2Fsystem-extension%2Fmaven-metadata.xml&logo=apachemaven&logoColor=orange&label=highcapable-maven-releases&style=flat-square)
<span style="margin-left: 5px"/>
![Android Min SDK](https://img.shields.io/badge/Min%20SDK-21-orange?logo=android&style=flat-square)

This is a dependency for system layer related extensions.

## Configure Dependency

You can add this module to your project using the following method.

### SweetDependency (Recommended)

Add dependency in your project's `SweetDependency` configuration file.

```yaml
libraries:
  com.highcapable.betterandroid:
    system-extension:
      version: +
```

Configure dependency in your project `build.gradle.kts`.

```kotlin
implementation(com.highcapable.betterandroid.system.extension)
```

### Version Catalog

Add dependency in your project's `gradle/libs.versions.toml`.

```toml
[versions]
system-extension = "<version>"

[libraries]
system-extension = { module = "com.highcapable.betterandroid:system-extension", version.ref = "system-extension" }
```

Configure dependency in your project `build.gradle.kts`.

```kotlin
implementation(libs.system.extension)
```

Please change `<version>` to the version displayed at the top of this document.

### Traditional Method

Configure dependency in your project `build.gradle.kts`.

```kotlin
implementation("com.highcapable.betterandroid:system-extension:<version>")
```

Please change `<version>` to the version displayed at the top of this document.

## Function Introduction

You can view the KDoc [click here](kdoc://system-extension).

### Application Extension

::: tip Content of This Section

[PackageInfoFlagsWrapper](kdoc://system-extension/system-extension/com.highcapable.betterandroid.system.extension.component/-package-info-flags-wrapper)

The `flags` attribute of `PackageInfo` wrapper.

[ApplicationInfoFlagsWrapper](kdoc://system-extension/system-extension/com.highcapable.betterandroid.system.extension.component/-application-info-flags-wrapper)

The `flags` attribute of `ApplicationInfo` wrapper.

[Application → getComponentName](kdoc://system-extension/system-extension/com.highcapable.betterandroid.system.extension.component/get-component-name)

[Application → hasPackage](kdoc://system-extension/system-extension/com.highcapable.betterandroid.system.extension.component/has-package)

[Application → hasLaunchActivity](kdoc://system-extension/system-extension/com.highcapable.betterandroid.system.extension.component/has-launch-activity)

[Application → getPackageInfo](kdoc://system-extension/system-extension/com.highcapable.betterandroid.system.extension.component/get-package-info)

[Application → getPackageInfoOrNull](kdoc://system-extension/system-extension/com.highcapable.betterandroid.system.extension.component/get-package-info-or-null)

[Application → getInstalledPackages](kdoc://system-extension/system-extension/com.highcapable.betterandroid.system.extension.component/get-installed-packages)

[Application → getInstalledPackagesOrNull](kdoc://system-extension/system-extension/com.highcapable.betterandroid.system.extension.component/get-installed-packages-or-null)

[Application → queryLaunchActivitiesForPackage](kdoc://system-extension/system-extension/com.highcapable.betterandroid.system.extension.component/query-launch-activities-for-package)

[Application → queryLaunchActivitiesForPackageOrNull](kdoc://system-extension/system-extension/com.highcapable.betterandroid.system.extension.component/query-launch-activities-for-package-or-null)

[Application → isComponentEnabled](kdoc://system-extension/system-extension/com.highcapable.betterandroid.system.extension.component/is-component-enabled)

[Application → enableComponent](kdoc://system-extension/system-extension/com.highcapable.betterandroid.system.extension.component/enable-component)

[Application → disableComponent](kdoc://system-extension/system-extension/com.highcapable.betterandroid.system.extension.component/disable-component)

[Application → resetComponent](kdoc://system-extension/system-extension/com.highcapable.betterandroid.system.extension.component/reset-component)

[Application → versionCodeCompat](kdoc://system-extension/system-extension/com.highcapable.betterandroid.system.extension.component/version-code-compat)

[Application → primaryCpuAbi](kdoc://system-extension/system-extension/com.highcapable.betterandroid.system.extension.component/primary-cpu-abi)

[Application → secondaryCpuAbi](kdoc://system-extension/system-extension/com.highcapable.betterandroid.system.extension.component/secondary-cpu-abi)

[Application → hasFlags](kdoc://system-extension/system-extension/com.highcapable.betterandroid.system.extension.component/has-flags)

Extensions for `Application`.

:::

`BetterAndroid` provides extended functions for `PackageManager`, `PackageInfo`, `ApplicationInfo` and other functions, so that you can use these functions more conveniently.

They are collectively classified as application extensions, meaning application-related functions.

Below are examples of how to use these extensions.

Get the `ComponentName` of a component class through generics.

> The following example

```kotlin
// Assume this is your context.
val context: Context
// Get the ComponentName of MainActivity.
val componentName = context.getComponentName<MainActivity>()
```

Determine whether the apps has been installed.

> The following example

```kotlin
// Assume this is your context.
val context: Context
// For example, determine whether Chrome is installed.
val hasChrome = context.packageManager.hasPackage("com.android.chrome")
```

Determine whether the apps has a startable `Activity`.

This function is mainly used to determine whether the app has an `Activity` that can be launched from the launcher.

> The following example

```kotlin
// Assume this is your context.
val context: Context
// For example, determine the com.mydemo.test app.
val hasLaunchActivity = context.packageManager.hasLaunchActivity("com.mydemo.test")
```

::: warning

Starting from Android 10, even if the app does not have an `Activity` that declares `ACTION_MAIN` and `CATEGORY_LAUNCHER`,
its icon may still be displayed on the launcher, and clicking it will open the application information interface, but this does not mean that it has a launchable `Activity`.

:::

Get apps package information.

`BetterAndroid` provides an overloaded method with the same name for `getPackageInfo`, you don’t need to consider compatibility issues, just use `PackageInfoFlagsWrapper` as the parameter of `flags`.

The reason for overloading this method is that in Android 13, the official method of `Int` type `flags` was invalidated and a new solution was enabled,
however, no compatibility processing tools were provided, but it was later canceled in Android 14, which will cause great trouble to developers.

> The following example

```kotlin
// Assume this is your context.
val context: Context
// For example, get Chrome's PackageInfo.
val packageInfo = context.packageManager.getPackageInfo("com.android.chrome")
// You can pass in one or more PackageInfoFlagsWrapper objects in the second parameter
// to set flags instead of using bit operations.
// PackageInfoFlagsWrapper is a wrapper mirror for all PackageInfo flags.
val packageInfo = context.packageManager
    .getPackageInfo("com.android.chrome", PackageInfoFlagsWrapper.GET_META_DATA)
```

::: tip

When you are not sure whether `PackageInfo` can be obtained successfully, you can replace the obtaining method with `getPackageInfoOrNull`,
so that if the acquisition fails, `null` will be returned instead of throwing an exception.

:::

Get a list of installed apps package information.

`BetterAndroid` also provides an overloaded method with the same name for `getInstalledPackages`. You don't need to consider compatibility issues, just use `PackageInfoFlagsWrapper` as the parameter of `flags`.

> The following example

```kotlin
// Assume this is your context.
val context: Context
// Get a list of all installed apps package information.
val packageInfos = context.packageManager.getInstalledPackages()
// Similarly, you can pass in one or more PackageInfoFlagsWrapper objects
// in the second parameter to set flags.
val packageInfos = context.packageManager.getInstalledPackages(PackageInfoFlagsWrapper.GET_META_DATA)
```

::: tip

Similarly, when you are not sure whether `PackageInfo` can be obtained successfully, you can replace the obtaining method with `getInstalledPackagesOrNull`.

:::

Query all launchable `Activities` of an app.

This method is implemented based on `queryIntentActivities` and `getLaunchIntentForPackage`, simplifying the acquisition process.

> The following example

```kotlin
// Assume this is your context.
val context: Context
// For example, query all launchable activities of Chrome.
val launchActivities = context.packageManager.queryLaunchActivitiesForPackage("com.android.chrome")
```

::: tip

Similarly, when you are not sure whether `ResolveInfo` can be obtained successfully, you can replace the obtaining method with `queryLaunchActivitiesForPackageOrNull`.

:::

Determine whether the component declared by the app is enabled or in the default state.

`BetterAndroid` encapsulates the `getComponentEnabledSetting` method, you can use the following methods to determine the component status faster.

The default state is the state declared by the app itself in `AndroidManifest.xml`, or the enabled state if not declared.

> The following example

```kotlin
// Assume this is your context.
val context: Context
// Get the ComponentName of MainActivity.
val mainComponent = context.getComponentName<MainActivity>()
// Determine whether MainActivity is enabled.
val isEnabled = context.packageManager.isComponentEnabled(mainComponent)
```

Enable, disable, or reset components declared by the app.

`BetterAndroid` encapsulates the `setComponentEnabledSetting` method, you can use the following methods to complete this operation faster.

The reset operation will reset to the default state, which is the state declared by the app itself in `AndroidManifest.xml`, or the enabled state if not declared.

> The following example

```kotlin
// Assume this is your context.
val context: Context
// Get the ComponentName of MainActivity.
val mainComponent = context.getComponentName<MainActivity>()
// Enable MainActivity.
context.packageManager.enableComponent(mainComponent)
// Disable MainActivity.
context.packageManager.disableComponent(mainComponent)
// Reset MainActivity.
context.packageManager.resetComponent(mainComponent)
```

:::danger

Your app does not have permission to enable or disable components of other apps unless it is in the system user group.

:::

Get the version code of the apps.

`longVersionCode` is a new feature introduced in Android 9, it is an expanded version of `versionCode` and is used to solve the problem of insufficient digits in `versionCode`.

Since `versionCode` has been marked as invalid, and it is too cumbersome for developers to use `PackageInfoCompat.getLongVersionCode` provided by `androidx`,
this method is basically difficult to find.

The use of two version numbers at the same time will also cause problems, developers cause trouble.

For this purpose, `BetterAndroid` encapsulates the compatible implementation of version number, you don't need to think about `versionCode` and `longVersionCode` now.

You can directly use `versionCodeCompat` to get the version number of the apps, and its type will always remain is `Long`.

> The following example

```kotlin
// Assume this is your context.
val context: Context
// For example, get the version number of Chrome.
val versionCode = context.packageManager.getPackageInfo("com.android.chrome").versionCodeCompat
```

Gets the CPU ABI name of the apps.

This is a hidden API, so `BetterAndroid` is obtained through reflection, and you may need to use it in some specific scenarios.

> The following example

```kotlin
// Assume this is your context.
val context: Context
// For example, get Chrome's PackageInfo.
val packageInfo = context.packageManager.getPackageInfo("com.android.chrome")
// Get the main CPU ABI name.
val primaryCpuAbi = packageInfo.applicationInfo.primaryCpuAbi
// Get the secondary CPU ABI name.
val secondaryCpuAbi = packageInfo.applicationInfo.secondaryCpuAbi
```

Determines whether `ApplicationInfo` contains the specified `flags`.

`BetterAndroid` encapsulates the method of judging `flags` through bit operations, you can use the following method to complete this operation faster.

> The following example

```kotlin
// Assume this is your context.
val context: Context
// For example, get Chrome's ApplicationInfo.
val applicationInfo = context.packageManager.getPackageInfo("com.android.chrome").applicationInfo
// Determine whether Chrome is a system app.
// You can pass in one or more ApplicationInfoFlagsWrapper objects
// to set flags instead of using bit operations.
// ApplicationInfoFlagsWrapper is a wrapper image for all ApplicationInfo flags.
// For ease of reading, all flags have the FLAG prefix removed in ApplicationInfoFlagsWrapper.
val isSystemApp = applicationInfo.hasFlags(ApplicationInfoFlagsWrapper.SYSTEM)
```

::: warning

In all of the above features, when it comes to querying package behavior outside of your own app,
Android 11 and later require the `QUERY_ALL_PACKAGES` permission or explicitly configure a `queries` property list.

Please refer to [Package visibility filtering on Android](https://developer.android.com/training/package-visibility).

:::

### Broadcast Extension

::: tip Content of This Section

[Broadcast → registerReceiver](kdoc://system-extension/system-extension/com.highcapable.betterandroid.system.extension.component/register-receiver)

[Broadcast → sendBroadcast](kdoc://system-extension/system-extension/com.highcapable.betterandroid.system.extension.component/send-broadcast)

[BroadcastReceiver](kdoc://system-extension/system-extension/com.highcapable.betterandroid.system.extension.component/-broadcast-receiver)

Extensions for Broadcast.

:::

Broadcast is a very important feature in Android, which allows apps to communicate with each other.

`BetterAndroid` provides a dynamic registration solution at runtime for broadcast, you can send broadcasts and create `BoardcastReceiver` more easily.

You can use the following methods to send and receive normal broadcasts without declaring them in `AndroidManifest.xml`.

For example, we want to send a broadcast to `com.example.app`.

> The following example

```kotlin
// Assume this is your context.
val context: Context
// Send a normal broadcast to com.example.app.
context.sendBroadcast("com.example.app") {
    // Set action.
    action = "com.example.app.action.KNOCK"
    // Pass a string parameter.
    putExtra("greetings", "Hey you!")
}
// Specifying the receiver's package name parameter is optional,
// if you not fill in a package name, this will be received by all receivers
// added with the following action.
context.sendBroadcast {
    // Set action.
    action = "com.example.app.action.KNOCK"
    // Pass a string parameter.
    putExtra("greetings", "Hey there!")
}
```

In `com.example.app`, we can receive this broadcast like this.

> The following example

```kotlin
// Assume this is your context.
val context: Context
// Create intent filter.
val filter = IntentFilter().apply {
    // Specify the sender's action.
    addAction("com.example.app.action.KNOCK")
}
// (Solution 1) Directly register the broadcast receiver.
// The callback here is the onReceive method, which is synchronous (main thread).
val receiver = context.registerReceiver(filter) { context, intent ->
    // Get the passed string parameter.
    val greetings = intent.getStringExtra("greetings")
    // Use Toast to display the received parameter.
    context.toast(greetings)
}
// (Solution 2) Create the broadcast receiver method body.
val body = BroadcastReceiver { context, intent ->
    // Get the passed string parameter.
    val greetings = intent.getStringExtra("greetings")
    // Use Toast to display the received parameter.
    context.toast(greetings)
}
val receiver = context.registerReceiver(filter, body = body)
// You can unregister the broadcast receiver when you no longer need to receive broadcasts.
context.unregisterReceiver(receiver)
```

::: tip

You can set the `exported` parameter (default is `true`) in the `registerReceiver` method to determine whether the current broadcast receiver needs to add `Context.RECEIVER_EXPORTED`.

If your broadcast is not open to the outside apps, you can set it to `false`.

:::

::: warning

In Android 14 or higher, a runtime-registered broadcast receiver must specify an exported behavior to receive broadcasts from another apps, if the current targets Android version 14 and above,
you must ensure that the `exported` parameter is `true` to receive broadcasts from another apps, otherwise an exception will be thrown directly.

Please refer to [Runtime-registered broadcasts receivers must specify export behavior](https://developer.android.com/about/versions/14/behavior-changes-14#runtime-receivers-exported).

:::

### Clipboard Extension

::: tip Content of This Section

[ClipDataItemBuilder](kdoc://system-extension/system-extension/com.highcapable.betterandroid.system.extension.component/-clip-data-item-builder)

`ClipData.Item` builder.

[Clipboard → clipboardManager](kdoc://system-extension/system-extension/com.highcapable.betterandroid.system.extension.component/clipboard-manager)

[Clipboard → copy](kdoc://system-extension/system-extension/com.highcapable.betterandroid.system.extension.component/copy)

[Clipboard → listOfItems](kdoc://system-extension/system-extension/com.highcapable.betterandroid.system.extension.component/list-of-items)

[Clipboard → ClipData](kdoc://system-extension/system-extension/com.highcapable.betterandroid.system.extension.component/-clip-data)

Extensions for clipboard.

:::

The clipboard is a very important function that is often used in app development, but its use is not very user-friendly.

You need to use `getSystemService` to get `ClipboardManager`, and then use `setPrimaryClip` to set the clipboard content.

Of course, you can also use `getPrimaryClip` to read the clipboard content.

Sometimes we only need to set or read a string, but these operations require writing a lot of code, which is very unfriendly to developers.

For this reason, `BetterAndroid` provides a simpler solution for the clipboard, you can directly use the following methods to set or read the clipboard content.

Read the contents of the clipboard.

Now, you can directly replace `ClipData.getItemAt` and `ClipData.getItemCount` with `ClipData.listOfItems`.

This method will return a `List<ClipData.Item>`, you can use `firstOrNull` to get the first `ClipData.Item`, or use `isEmpty` to directly determine whether there is content in the clipboard.

Its benefits it means that you no longer need to consider whether the array will out of bounds.

> The following example

```kotlin
// Assume this is your context.
val context: Context
// Get the clipboard manager.
val manager = context.clipboardManager
// Get the first ClipData.Item in the clipboard.
// Normally, you only need to get the first object.
val clipItem = manager.primaryClip?.listOfItems()?.firstOrNull()
// Get the copied text.
val copiedText = clipItem?.text
```

Copy a text to the clipboard.

You don't need to write `setPrimaryClip(ClipData.newPlainText("Lable", "Text"))` anymore, copying a text should be simple.

> The following example

```kotlin
// Assume this is your context.
val context: Context
// Copy a text to the clipboard.
context.clipboardManager.copy("Hello World!")
// Add a label to this text.
context.clipboardManager.copy("Hello World!", "MyText")
```

Copy HTML type text, `Uri`, `Intent` to the clipboard.

No matter what content you copy, you can do it using the `copy` method.

> The following example

```kotlin
// Assume this is your context.
val context: Context
// Copy HTML type text to the clipboard.
context.clipboardManager.copy("Hello World!", "<b>Hello World!</b>")
// Copy uri to clipboard.
context.clipboardManager.copy(Uri.parse("some://uri"), context.contentResolver)
// Copy intent to the clipboard.
context.clipboardManager.copy(Intent(Intent.ACTION_VIEW, Uri.parse("some://uri")))
```

Copies the contents of a custom `ClipData` to the clipboard.

You can use the `ClipData` method to create a new `ClipData` object and then copy it to the clipboard.

> The following example

```kotlin
// Assume this is your context.
val context: Context
// Create ClipData object.
val clipData = ClipData {
    addText("Hello World!")
    addHtmlText("Hello World!", "<b>Hello World!</b>")
    addUri(Uri.parse("some://uri"), context.contentResolver)
    addIntent(Intent(Intent.ACTION_VIEW, Uri.parse("some://uri")))
}
// Copy to clipboard.
context.clipboardManager.copy(clipData)
```

::: warning

In Android 10 or later, the contents of the clipboard cannot be read while your app is in the background unless your app is an input method (IME).

Please refer to [Limited access to clipboard data](https://developer.android.com/about/versions/10/privacy/changes#clipboard-data).

:::

### Intent Extension

::: tip Content of This Section

[Intent → getSerializableExtraCompat](kdoc://system-extension/system-extension/com.highcapable.betterandroid.system.extension.component/get-serializable-extra-compat)

[Intent → getSerializableCompat](kdoc://system-extension/system-extension/com.highcapable.betterandroid.system.extension.component/get-serializable-compat)

[Intent → getParcelableExtraCompat](kdoc://system-extension/system-extension/com.highcapable.betterandroid.system.extension.component/get-parcelable-extra-compat)

[Intent → getParcelableCompat](kdoc://system-extension/system-extension/com.highcapable.betterandroid.system.extension.component/get-parcelable-compat)

Extensions for `Intent`.

:::

Currently, the extension methods in `Intent` are only used to handle the acquisition methods of `Serializable` and `Parcelable` types.

They are marked as deprecated in Android 13 and the official does not provide any effective compatible handling method.

You can use the compatibility handling methods provided by `BetterAndroid` to obtain data of `Serializable` and `Parcelable` types.

> The following example

```kotlin
// Assume this is your intent.
val intent: Intent
// Get data of serializable type.
val myData = intent.getSerializableExtraCompat<MyData>("my_key_name")
val myData = intent.extras?.getSerializableCompat<MyData>("my_key_name")
// Get data of parcelable type.
val myData = intent.getParcelableExtraCompat<MyData>("my_key_name")
val myData = intent.extras?.getParcelableCompat<MyData>("my_key_name")
```

The following is a comparison table of the original method and the compatibility method.

| Original Method               | Compatibility Method                |
| ----------------------------- | ----------------------------------- |
| `Intent.getSerializableExtra` | `Intent.getSerializableExtraCompat` |
| `Bundle.getSerializable`      | `Bundle.getSerializableCompat`      |
| `Intent.getParcelableExtra`   | `Intent.getParcelableExtraCompat`   |
| `Bundle.getParcelable`        | `Bundle.getParcelableCompat`        |

### Service Extension

::: tip Content of This Section

[Service → startService](kdoc://system-extension/system-extension/com.highcapable.betterandroid.system.extension.component/start-service)

[Service → startForegroundService](kdoc://system-extension/system-extension/com.highcapable.betterandroid.system.extension.component/start-foreground-service)

[Service → startServiceOrElse](kdoc://system-extension/system-extension/com.highcapable.betterandroid.system.extension.component/start-service-or-else)

[Service → startForegroundServiceOrElse](kdoc://system-extension/system-extension/com.highcapable.betterandroid.system.extension.component/start-foreground-service-or-else)

Extensions for `Service`.

:::

Similar to how to start `Activity`, when we need to start a `Service`, we need to use `Intent` to create an `Intent(this, MyService::class.java)`, and then call `startService(intent)` to start it.

This may not be very friendly to write, so `BetterAndroid` provides an extension for `Service`, now you can directly use the following method to start a `Service`.

> The following example

```kotlin
// Assume this is your context.
val context: Context
// Assume MyService is your target service.
context.startService<MyService>()
// You can create an intent using the following method.
context.startService<MyService> {
    // Add some extra parameters here.
    putExtra("key", "value")
}
```

If you need to start a `Service` of an external app, you can use the following method.

> The following example

```kotlin
// Assume this is your context.
val context: Context
// Assume that the app package you need to start is named com.example.app.
// Assume that the service class you need to start is named com.example.app.MyService.
context.startService("com.example.app", "com.example.app.MyService")
// You can still create an intent using the following method.
context.startService("com.example.app", "com.example.app.MyService") {
    // Add some extra parameters here.
    putExtra("key", "value")
}
```

::: tip

You can use `startServiceOrElse` and `startForegroundServiceOrElse` instead of `startService` and `startForegroundService` to determine whether the `Service` can be started successfully.

If the startup fails, this method will not throw an exception but return `false`.

:::

::: warning

In Android 8 or higher, in some cases, you may need to use `startForegroundService` to start a foreground service.

Please refer to [Background Execution Limits](https://developer.android.com/about/versions/oreo/background).

:::

### System Infomation

::: tip Content of This Section

[SystemVersion](kdoc://system-extension/system-extension/com.highcapable.betterandroid.system.extension.tool/-system-version)

System version tool.

[SystemKind](kdoc://system-extension/system-extension/com.highcapable.betterandroid.system.extension.tool/-system-kind)

System kind tool.

[SystemProperties](kdoc://system-extension/system-extension/com.highcapable.betterandroid.system.extension.tool/-system-properties)

Android's `SystemProperties` tool.

:::

Maybe you are tired of `Build.VERSION.SDK_INT` and `Build.VERSION_CODES` floating around in your code, so from now on, you no longer need to use them.

`BetterAndroid` has prepared simpler writing methods for you to replace them.

Previously, we needed to determine the Android API level of the current system, which was basically done in the following ways.

> The following example

```kotlin
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
    // Execute relevant code.
}
// Or use hard-coded API version code.
if (Build.VERSION.SDK_INT >= 29) {
    // Execute relevant code.
}
```

Now, you can do this very easily in the following way.

> The following example

```kotlin
SystemVersion.require(SystemVersion.Q) {
    // Execute relevant code.
}
// Or use hard-coded API version code.
SystemVersion.require(29) {
    // Execute relevant code.
}
// result will get "target" when API is greater than or equal to 29, otherwise it will be "legacy".
val result = SystemVersion.require(SystemVersion.Q, "legacy") { "target" }
// If it is a nullable result, you can use the following method.
val myData: MyData?
val result = SystemVersion.requireOrNull(SystemVersion.Q, MyData()) { myData }
```

You can also use the following methods to judge.

> The following example

```kotlin
// Determine whether API is less than 29.
if (SystemVersion.isLowTo(SystemVersion.Q)) {
    // Execute relevant code.
}
// Determine whether API is greater than 29.
if (SystemVersion.isHighTo(SystemVersion.Q)) {
    // Execute relevant code.
}
// Determine whether API is less than or equal to 29.
if (SystemVersion.isLowOrEqualsTo(SystemVersion.Q)) {
    // Execute relevant code.
}
// Determine whether the API is greater than or equal to 29.
if (SystemVersion.isHighOrEqualsTo(SystemVersion.Q)) {
    // Execute relevant code.
}
// Determine whether the API is between 26 and 29.
if (SystemVersion.isBetween(SystemVersion.O..SystemVersion.Q)) {
    // Execute relevant code.
}
```

The following is the constant mapping comparison table for each API, after Android version update, `BetterAndroid` will update these constants synchronously.

| API Level | `SystemVersion` Name | `Build.VERSION_CODES` Name | Corresponding System Version |
| --------- | -------------------- | -------------------------- | ---------------------------- |
| 19        | `K`                  | `KITKAT`                   | 4.4.3, 4.4.4                 |
| 20        | `K_W`                | `KITKAT_WATCH`             | 4.4W                         |
| 21        | `L`                  | `LOLLIPOP`                 | 5.0, 5.0.2                   |
| 22        | `L_MR1`              | `LOLLIPOP_MR1`             | 5.1, 5.1.1                   |
| 23        | `M`                  | `M`                        | 6.0, 6.0.1                   |
| 24        | `N`                  | `N`                        | 7.0                          |
| 25        | `N_MR1`              | `N_MR1`                    | 7.1, 7.1.1, 7.1.2            |
| 26        | `O`                  | `O`                        | 8.0                          |
| 27        | `O_MR1`              | `O_MR1`                    | 8.1                          |
| 28        | `P`                  | `P`                        | 9                            |
| 29        | `Q`                  | `Q`                        | 10                           |
| 30        | `R`                  | `R`                        | 11                           |
| 31        | `S`                  | `S`                        | 12                           |
| 32        | `S_V2`               | `S_V2`                     | 12.1, 12L                    |
| 33        | `T`                  | `TIRAMISU`                 | 13                           |
| 34        | `U`                  | `UPSIDE_DOWN_CAKE`         | 14                           |

In addition to judging the API level, you can also use the following method to get the current Android version name.

> The following example

```kotlin
// Get the current Android version name.
// It is equivalent to Build.VERSION.RELEASE.
// For example, the version name of Android 10 is the string "10".
val versionName = SystemVersion.name
```

As various manufacturers have successively released more and more deeply customized Android systems for their own brand Android mobile phones,
sometimes it is very necessary for us to make targeted adaptations for the different functions of each customized version of the system,
but how to judge the type of these systems is a big question.

Usually, everyone’s solution is to determine the model of the device to determine what kind of customized system it is,
however, if the current device is not running the customized system you judged, such as the case where the user flashes the phone by himself, then this solution is will fail.

`BetterAndroid` provides you with a simple, fast and efficient solution by collecting corresponding features of various common custom systems.

The following is a simple example to determine the type of current system.

> The following example

```kotlin
// Determine whether the current system kind is MIUI.
if (SystemKind.equals(SystemKind.MIUI)) {
    // Execute relevant code.
}
```

Yes, it's that simple, if you need to judge multiple system types at the same time, you can also use the following method.

> The following example

```kotlin
// Get the current system kind.
val kind = SystemKind.current
// Determine the current system kind in batches.
when (kind) {
    SystemKind.MIUI -> {
        // Execute relevant code.
    }
    SystemKind.COLOROS -> {
        // Execute relevant code.
    }
    SystemKind.ORIGINOS -> {
        // Execute relevant code.
    }
}
```

The following is a comparison table of constants for currently collected system kinds, if you have features for more system kinds,
you are welcome to PR or go to [GitHub Issues](repo://issues) to make suggestions to us.

| `SystemKind` Name | System Kind                                                                                                        |
| ----------------- | ------------------------------------------------------------------------------------------------------------------ |
| `DEFAULT`         | Default, uncategorized. (Stock Android or AOSP-based Android system and system categories not currently collected) |
| `HARMONYOS`       | [HarmonyOS](https://www.harmonyos.com/) (Based on AOSP)                                                            |
| `EMUI`            | [EMUI](https://www.huaweicentral.com/emui)                                                                         |
| `MIUI`            | [MIUI](https://home.miui.com/)                                                                                     |
| `HYPEROS`         | [HyperOS](https://hyperos.mi.com/)                                                                                 |
| `COLOROS`         | [ColorOS](https://www.coloros.com/)                                                                                |
| `FUNTOUCHOS`      | [FuntouchOS](https://www.vivo.com/funtouchos)                                                                      |
| `ORIGINOS`        | [OriginOS](https://www.vivo.com/originos)                                                                          |
| `FLYME`           | [Flyme](https://flyme.com/)                                                                                        |
| `ONEUI`           | [OneUI](https://www.samsung.com/one-ui)                                                                            |
| `ZUI`             | [ZUI](https://zui.com/)                                                                                            |
| `REDMAGICOS`      | [RedMagicOS](https://www.nubia.com/)                                                                               |
| `NUBIAUI`         | [NubiaUI](https://www.nubia.com/)                                                                                  |
| `ROGUI`           | [RogUI](https://www.asus.com/)                                                                                     |
| `VISIONOS`        | [VisionOS](https://fans.hisense.com/forum-269-1.html)                                                              |

`SystemProperties` is a tool provided by Android that can read the contents of `build.prop` during runtime, but this function is not open to developers.

So in order to avoid using reflection to access `SystemProperties` every time, `BetterAndroid` mirrors all methods of `SystemProperties`.

Now, you can directly access `SystemProperties` using non-reflective means.

> The following example

```kotlin
// For example, get the build ID of the current system.
val buildId = SystemProperties.get("ro.build.id")
// Get the build type of the current system.
val buildTags = SystemProperties.get("ro.system.build.tags")
// Get the CPU ABI list supported by the current device.
val abis = SystemProperties.get("ro.system.product.cpu.abilist")
```

`BetterAndroid` also provides an extension usage for it.

> The following example

```kotlin
// Determine whether the properties key exists.
// For example, some unique key values in ROM.
val isExists = SystemProperties.contains("ro.miui.ui.version.name")
```