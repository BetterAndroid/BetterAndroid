# ui-component

![Maven Central](https://img.shields.io/maven-central/v/com.highcapable.betterandroid/ui-component?logo=apachemaven&logoColor=orange&style=flat-square)
<span style="margin-left: 5px"/>
![Maven metadata URL](https://img.shields.io/maven-metadata/v?metadataUrl=https%3A%2F%2Fraw.githubusercontent.com%2FHighCapable%2Fmaven-repository%2Frefs%2Fheads%2Fmain%2Frepository%2Freleases%2Fcom%2Fhighcapable%2Fbetterandroid%2Fui-component%2Fmaven-metadata.xml&logo=apachemaven&logoColor=orange&label=highcapable-maven-releases&style=flat-square)
<span style="margin-left: 5px"/>
![Android Min SDK](https://img.shields.io/badge/Min%20SDK-21-orange?logo=android&style=flat-square)

This is a dependency for UI (user interface) related components.

## Configure Dependency

You can add this module to your project using the following method.

### Version Catalog (Recommended)

Add dependency in your project's `gradle/libs.versions.toml`.

```toml
[versions]
betterandroid-ui-component = "<version>"

[libraries]
betterandroid-ui-component = { module = "com.highcapable.betterandroid:ui-component", version.ref = "betterandroid-ui-component" }
```

Configure dependency in your project's `build.gradle.kts`.

```kotlin
implementation(libs.betterandroid.ui.component)
```

Please change `<version>` to the version displayed at the top of this document.

### Traditional Method

Configure dependency in your project's `build.gradle.kts`.

```kotlin
implementation("com.highcapable.betterandroid:ui-component:<version>")
```

Please change `<version>` to the version displayed at the top of this document.

## Function Introduction

You can view the KDoc [click here](kdoc://ui-component).

::: tip Looking for Adapter?

Adapter related features have been separated into an independent module [ui-component-adapter](./ui-component-adapter), which will be updated separately in the future.

:::

::: tip Looking for Insets?

Insets related features have been migrated to [ui-extension → Insets Extension](./ui-extension#insets-extension), which will be updated following this module in the future.

:::

### Activity

::: tip Contents of This Section

[AppBindingActivity](kdoc://ui-component/ui-component/com.highcapable.betterandroid.ui.component.activity/-app-binding-activity)

`Activity` with view binding (inherited from `AppCompatActivity`).

[AppViewsActivity](kdoc://ui-component/ui-component/com.highcapable.betterandroid.ui.component.activity/-app-views-activity)

Base view component `Activity` (inherited from `AppCompatActivity`).

[AppComponentActivity](kdoc://ui-component/ui-component/com.highcapable.betterandroid.ui.component.activity/-app-component-activity)

Basic component `Activity` (inherited from `ComponentActivity`).

Available for Jetpack Compose projects.

:::

::: tip

The preset components below all implement [IBackPressedController](kdoc://ui-component/ui-component/com.highcapable.betterandroid.ui.component.proxy/-i-back-pressed-controller),
[ISystemBarsController](kdoc://ui-component/ui-component/com.highcapable.betterandroid.ui.component.proxy/-i-system-bars-controller) interface.

You can find detailed usage methods in [System Event](#system-event) and [System Bars (Status Bars, Navigation Bars, etc)](#system-bars-status-bars-navigation-bars-etc) below.

:::

When using `ViewBinding`, you can use `AppBindingActivity` to quickly create an `Activity` with view binding.

In `AppBindingActivity`, you can directly use the `binding` property to obtain the view binding object without manually calling the `setContentView` method.

> The following example

```kotlin
class MainActivity : AppBindingActivity<ActivityMainBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.mainText.text = "Hello World!"
    }
}
```

::: tip

If you need to perform some custom operations before the view is loaded, in general, you might do this.

> The following example

```kotlin
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        doSomething()
        setContentView(R.layout.activity_main)
        findViewById<TextView>(R.id.main_text).text = "Hello World!"
    }

    private fun doSomething() {
        // Your code here.
    }
}
```

When using `AppBindingActivity`, you need to do this.

> The following example

```kotlin
class MainActivity : AppBindingActivity<ActivityMainBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.mainText.text = "Hello World!"
    }

    override fun onPrepareContentView(savedInstanceState: Bundle?): LayoutInflater {
        doSomething()
        // You can return the processed LayoutInflater instance,
        // which will be used to initialize the layout.
        return super.onPrepareContentView(savedInstanceState)
    }

    private fun doSomething() {
        // Your code here.
    }
}
```

:::

You can also use `AppViewsActivity` to create a basic `Activity` and use the `findViewById` method to get the `View`.

> The following example

```kotlin
class MainActivity : AppViewsActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<TextView>(R.id.main_text).text = "Hello World!"
    }
}
```

If your project is a Jetpack Compose project, you can use `AppComponentActivity` to create a basic `Activity`.

> The following example

```kotlin
class MainActivity : AppComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Text("Hello World!")
        }
    }
}
```

::: tip

For related extensions of Jetpack Compose, you can refer to [compose-extension](../library/compose-extension.md), [compose-multiplatform](../library/compose-multiplatform.md).

`BetterAndroid` also provides related extensions for `Activity`, you can refer to [ui-extension → Activity Extension](../library/ui-extension.md#activity-extension).

:::

### Fragment

::: tip Contents of This Section

[AppBindingFragment](kdoc://ui-component/ui-component/com.highcapable.betterandroid.ui.component.fragment/-app-binding-fragment)

`Fragment` with view binding.

[AppViewsFragment](kdoc://ui-component/ui-component/com.highcapable.betterandroid.ui.component.fragment/-app-views-fragment)

Base view component `Fragment`.

:::

::: tip

The preset components below all implement [IBackPressedController](kdoc://ui-component/ui-component/com.highcapable.betterandroid.ui.component.proxy/-i-back-pressed-controller),
[ISystemBarsController](kdoc://ui-component/ui-component/com.highcapable.betterandroid.ui.component.proxy/-i-system-bars-controller) interfaces.

You can find detailed usage methods in [System Event](#system-event) and [System Bars (Status Bars, Navigation Bars, etc)](#system-bars-status-bars-navigation-bars-etc) below.

:::

When using `ViewBinding`, you can use `AppBindingFragment` to quickly create a `Fragment` with view binding.

In `AppBindingFragment`, you can directly use the `binding` property to obtain the view binding object without manually overriding the `onCreateView` method.

You don’t need to consider the impact of `Fragment`’s lifecycle on `binding`, `BetterAndroid` has already handled these issues for you.

> The following example

```kotlin
class MainFragment : AppBindingFragment<FragmentMainBinding>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mainText.text = "Hello World!"
    }
}
```

You can also use `AppViewsFragment` to create a basic `Fragment`.

Similarly, you don't need to override the `onCreateView` method, just fill in the layout resource ID that needs to be bound directly into the constructor.

> The following example

```kotlin
class MainFragment : AppViewsFragment(R.layout.fragment_main) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<TextView>(R.id.main_text).text = "Hello World!"
    }
}
```

::: tip

`BetterAndroid` also provides related extensions for `Fragment`, you can refer to [ui-extension → Fragment Extension](../library/ui-extension.md#fragment-extension).

:::

### System Event

::: tip Contents of This Section

[BackPressedController](kdoc://ui-component/ui-component/com.highcapable.betterandroid.ui.component.backpress/-back-pressed-controller)

Back pressed event controller.

[OnBackPressedCallback](kdoc://ui-component/ui-component/com.highcapable.betterandroid.ui.component.backpress.callback/-on-back-pressed-callback)

Simple back pressed event callback.

[IBackPressedController](kdoc://ui-component/ui-component/com.highcapable.betterandroid.ui.component.proxy/-i-back-pressed-controller)

Back pressed event controller interface.

:::

An `OnBackPressedDispatcher` has been provided for developers in the `androidx` dependency `androidx.activity:activity`.

However, out of dissatisfaction with the official rash deprecated overwriting of the `onBackPressed` method, `BetterAndroid` encapsulated the `OnBackPressedDispatcher` related functions.

It supports the back pressed event callback function that is more suitable for Kotlin writing, and also adds the function of ignoring all callback events and directly releasing
the back pressed event, making it more flexible and easy to use.

`AppBindingActivity`, `AppViewsActivity`, `AppComponentActivity`, `AppBindingFragment`, `AppViewsFragment`
have implemented the `IBackPressedController` interface by default, you can directly use `backPressed` to obtain `BackPressedController`.

But you can still manually create a `BackPressedController` in `Activity`.

> The following example

```kotlin
class YourActivity : AppCompatActivity() {

    // Create a lazy object.
    val backPressed by lazy { BackPressedController.from(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Call backPressed here to implement related functions.
        backPressed
    }

    override fun onDestroy() {
        super.onDestroy()
        // Destroy backPressed, this will remove all callbacks.
        // Optional, prevent memory leaks.
        backPressed.destroy()
    }
}
```

The following is the basic usage of `BackPressedController`.

> The following example

```kotlin
// Add a back pressed callback
val callback = backPressed.addCallback {
    // Ignore the current callback within the callback and trigger the back operation.
    // For example, you can pop up a dialog here to ask the user whether to exit
    // and select "Yes" at this time.
    // The object passed in needs to be the backPressed that created this callback.
    trigger(backPressed)
    // Or remove itself at the same time after triggering.
    trigger(backPressed, removed = true)
    // Remove directly. (Not recommended, you should use backPressed.removeCallback)
    remove()
}
// You can also create a callback manually.
// Note: Please make sure to import com.highcapable.betterandroid.ui.component.backpress.callback
//       OnBackPressedCallback under the package name, NOT androidx.activity.OnBackPressedCallback
val callback = OnBackPressedCallback {
    // Your code here.
}
// Then add to backPressed.
backPressed.addCallback(callback)
// Remove a known callback.
backPressed.removeCallback(callback)
// Trigger the back operation of the system.
backPressed.trigger()
// You can set ignored to true to ignore all added callbacks and back directly.
backPressed.trigger(ignored = true)
// Determine whether there is currently an enabled callback
val hasEnabledCallbacks = backPressed.hasEnabledCallbacks
// Destroy, this will remove all callbacks.
backPressed.destroy()
```

::: warning

After using `BackPressedController`, the current `OnBackPressedDispatcher` has been automatically taken over by it.

You should not continue to use `onBackPressedDispatcher.addCallback(...)` as this will expose unknown (wild)
callbacks and prevent them from being cleanly removed.

:::

### Notification

::: tip Contents of This Section

[NotificationBuilder](kdoc://ui-component/ui-component/com.highcapable.betterandroid.ui.component.notification/-notification-builder)

System notification builder.

[NotificationChannelBuilder](kdoc://ui-component/ui-component/com.highcapable.betterandroid.ui.component.notification/-notification-channel-builder)

System notification channel builder.

[NotificationChannelGroupBuilder](kdoc://ui-component/ui-component/com.highcapable.betterandroid.ui.component.notification/-notification-channel-group-builder)

System notification channel group builder.

[NotificationImportance](kdoc://ui-component/ui-component/com.highcapable.betterandroid.ui.component.notification.type/-notification-importance)

System notification importance.

[NotificationPoster](kdoc://ui-component/ui-component/com.highcapable.betterandroid.ui.component.notification/-notification-poster)

System notification poster.

[Notification](kdoc://ui-component/ui-component/com.highcapable.betterandroid.ui.component.notification.factory)

Extension methods for the notification builds.

:::

It is not easy to create and send a notification in Android.

The biggest problem is that the creation of system notifications is complicated, management is confusing, and the API is difficult to be easily compatible with older versions.

Especially when developers see the two classes `NotificationCompat` and `NotificationChannelCompat`, they will feel at a loss to start.

So `BetterAndroid` comprehensively encapsulates the system notification-related APIs, basically covering all functions and calls that can be used in system notifications.

So you no longer need to consider compatibility issues with Android 8 and below systems like notification channels, `BetterAndroid` has already taken care of these issues for you.

In Kotlin you can create a system notification more easily.

> The following example

```kotlin
// Assume this is your current Context.
val context: Context
// Create the notification object that needs to be posted.
val notification = context.createNotification(
    // Create and set up notification channels.
    // A notification channel must exist in Android 8 and above systems.
    // In systems lower than Android 8, this feature will be automatically compatible.
    channel = NotificationChannel("my_channel_id") {
        // Set the notification channel name.
        // (this will be displayed in the system's notification settings)
        name = "My Channel"
        // Set notification channel description.
        // (this will be displayed in the system's notification settings)
        description = "My channel description."
        // The rest of the usage is consistent with NotificationChannelCompat.Builder.
    }
) {
    // Set the notification icon. (this will be displayed in the status bar and notification bar)
    // The notification icon must be a monochrome icon. (a vector image is recommended)
    smallIconResId = R.drawable.ic_my_notification
    // Set notification title.
    contentTitle = "My Notification"
    // Set notification content.
    contentText = "Hello World!"
    // The rest of the usage is consistent with NotificationCompat.Builder.
}
// Post notification using default notification ID.
notification.post()
// Post notification using custom notification ID.
val notifyId = 1
notification.post(notifyId)
// Cancel the current notification. (this will clear the notification from the system notification bar)
notification.cancel()
// Determine whether the current notification has been canceled.
val isCanceled = notification.isCanceled
```

::: warning

In Android 13 and above, you need to define and add runtime permission for notifications.

When this permission is not defined correctly, calling the `post` method will automatically ask you to add the permission to `AndroidManifest.xml`.

Please refer to [Notification runtime permission](https://developer.android.com/develop/ui/views/notifications/notification-permission).

:::

You can set importance for notifications in notification channels using the following methods.

> The following example

```kotlin
// Assume this is your current Context.
val context: Context
// Create the notification object that needs to be posted.
val notification = context.createNotification(
    // Create and set up notification channels.
    // In systems lower than Android 8, this feature will be automatically compatible.
    // Importance determines the importance of the notification,
    // which affects how the notification is displayed.
    // BetterAndroid sets the importance static variable in NotificationManager.
    // Encapsulated into NotificationImportance, you can set the
    // importance of notifications more conveniently.
    // Here we set NotificationImportance.HIGH (high importance),
    // this will display the notification as a banner in the system notification with a ring reminder.
    channel = NotificationChannel("my_channel_id", importance = NotificationImportance.HIGH) {
        name = "My Channel"
        description = "My channel description."
    }
) {
    smallIconResId = R.drawable.ic_my_notification
    contentTitle = "My Notification"
    contentText = "Hello World!"
}
// Post notification using default notification ID.
notification.post()
```

When encountering multiple groups of notifications, you can use the following methods to create a group of notification channels.

> The following example

```kotlin
// Assume this is your current Context.
val context: Context
// Create a notification channel group.
// In systems lower than Android 8, this feature will have no effect.
val channelGroup = NotificationChannelGroup("my_channel_group_id") {
    //Set the notification channel group name.
    // (this will be displayed in the system's notification settings)
    name = "My Channel Group"
    // Set notification channel group description.
    // (this will be displayed in the system's notification settings)
    description = "My channel group description."
}
// Create the first notification channel and specify the notification channel group.
val channel1 = NotificationChannel("my_channel_id_1", channelGroup) {
    name = "My Channel 1"
    description = "My channel description."
}
// Create the second notification channel and specify the notification channel group.
val channel2 = NotificationChannel("my_channel_id_2", channelGroup) {
    name = "My Channel 2"
    description = "My channel description."
}
// Use channel1 to create the first notification and post it.
context.createNotification(channel1) {
    smallIconResId = R.drawable.ic_my_notification
    contentTitle = "My Notification 1"
    contentText = "Hello World!"
}.post(1)
// Use channel2 to create the second notification and post it.
context.createNotification(channel2) {
    smallIconResId = R.drawable.ic_my_notification
    contentTitle = "My Notification 2"
    contentText = "Hello World!"
}.post(2)
```

The above will create a notification channel group and add two notification channels to it.

After the notification is posted, the system will automatically create a group classification for these two notification channels.

::: warning

The settings in the notification channel will only take effect when the notification channel is first created.

If the settings of the notification channel are modified by the user, these settings will not be overwritten again.

You cannot modify the settings of a notification channel that has already been created, but you can reassign it a new notification channel ID,
which will create a new notification channel.

Please refer to [Create and manage notification channels](https://developer.android.com/develop/ui/views/notifications/channels).

:::

In the above example, the notification object is managed automatically, if you want to manually create a notification object
without relying on the `context.createNotification` method, please refer to the following example.

> The following example

```kotlin
// Assume this is your current Context.
val context: Context
// Create the notification object that needs to be posted.
val notification = Notification(
    // Set Context.
    context = context,
    // Create and set up notification channels.
    channel = NotificationChannel("my_channel_id") {
        name = "My Channel"
        description = "My channel description."
    }
) {
    smallIconResId = R.drawable.ic_my_notification
    contentTitle = "My Notification"
    contentText = "Hello World!"
}
// Use the current notification as a post object.
val poster = notification.asPoster()
// Post notification using default notification ID.
poster.post()
// Post notification using custom notification ID.
val notifyId = 1
poster.post(notifyId)
// Cancel the current notification. (this will clear the notification from the system notification bar)
poster.cancel()
// Determine whether the current notification has been canceled.
val isCanceled = poster.isCanceled
```

::: tip

Objects created through `Notification`, `NotificationChannel`, `NotificationChannelGroup` are a wrapper for `NotificationCompat`, `NotificationChannelCompat`, and `NotificationChannelGroupCompat`.

You can use `instance` to get the actual object to perform some of your own operations.

You can also get the `NotificationManagerCompat` object through `Context.notificationManager` to perform some of your own operations.

:::

### System Bars (Status Bars, Navigation Bars, etc)

::: tip Contents of This Section

[SystemBarsController](kdoc://ui-component/ui-component/com.highcapable.betterandroid.ui.component.systembar/-system-bars-controller)

System bars controller.

[ISystemBarsController](kdoc://ui-component/ui-component/com.highcapable.betterandroid.ui.component.proxy/-i-system-bars-controller)

System bars controller interface.

[SystemBarStyle](kdoc://ui-component/ui-component/com.highcapable.betterandroid.ui.component.systembar.style/-system-bar-style)

System bar style.

[SystemBars](kdoc://ui-component/ui-component/com.highcapable.betterandroid.ui.component.systembar.type/-system-bars)

System bars type.

[SystemBarBehavior](kdoc://ui-component/ui-component/com.highcapable.betterandroid.ui.component.systembar.type/-system-bar-behavior)

System bars behavior.

:::

The serious adaptation problem of Android development lies in the confusion of terminal devices without unified development specifications.

In order to provide users with a better experience, when should the status bars and navigation bars be displayed or hidden,
the color and background of the status bars and navigation bars, etc.

These are all issues that developers need to consider during the development process.

So `BetterAndroid` docks and encapsulates the system bars adaptation solution provided by `androidx` and integrates it into `SystemBarsController`.

Now, you can call it very conveniently to easily implement a series of solutions for the operating system bars.

`SystemBarsController` supports at least Android 5.0 and solves compatibility issues in some manufacturers' customized systems.

`AppBindingActivity`, `AppViewsActivity`, `AppComponentActivity`, `AppBindingFragment`, `AppViewsFragment`
have implemented the `ISystemBarsController` interface by default, you can directly use `systemBars` to obtain `SystemBarsController`.

But you can still create a `SystemBarsController` manually using the `Activity.getWindow` object in `Activity`.

> The following example

```kotlin
class YourActivity : AppCompatActivity() {

    // Create a lazy object.
    val systemBars by lazy { SystemBarsController.from(window) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Create your binding.
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Initialize systemBars using the current root view.
        systemBars.init(binding.root)
    }

    override fun onDestroy() {
        super.onDestroy()
        // Destroy systemBars, which will restore the state before initialization.
        // Optional, to prevent memory leaks.
        systemBars.destroy()
    }
}
```

::: warning

When using the `init` method, it is recommended and recommended to pass in your own root view, otherwise `android.R.id.content` will be used by default as the root view.

You <u>**should avoid using it as the root view**</u>, this is uncontrollable, you should be able to maintain your own root view at any time in `Activity`.

If you are not using `ViewBinding`, `AppViewsActivity` and `AppComponentActivity` have overridden the `setContentView` method for you by default.

It will automatically load your root view into `SystemBarsController` when you use this method.

You can also manually override the `setContentView` method to achieve this functionality.

> The following example

```kotlin
override fun setContentView(layoutResID: Int) {
    super.setContentView(layoutResID)
    // The first child view is your root view.
    val rootView = findViewById<ViewGroup>(android.R.id.content).getChildAt(0)
    // Initialize systemBars using the current root view.
    systemBars.init(rootView)
}
```

:::

The following is a detailed usage introduction of `SystemBarsController`.

Initialize `SystemBarsController` and handle window insets `padding` of root view.

> The following example

```kotlin
// Assume this is your current root view.
val rootView: ViewGroup
// Initialize SystemBarsController.
// Your root view must have been set to a parent layout, otherwise an exception will be thrown.
systemBars.init(rootView)
// You can customize the window insets that handle the root view.
systemBars.init(rootView, edgeToEdgeInsets = { systemBars })
// If you don't want SystemBarsController to automatically handle the root view's window insets for you,
// you can directly set edgeToEdgeInsets to null.
systemBars.init(rootView, edgeToEdgeInsets = null)
```

::: warning

`SystemBarsController` will automatically set `Window.setDecorFitsSystemWindows(false)` during initialization
(on cutout display devices, `layoutInDisplayCutoutMode` will also be set to `LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES`),
you just need to set `edgeToEdgeInsets` in `init` (the default setting),
then your root view will have a window insets `padding` controlled by `safeDrawingIgnoringIme`,
which is why you should be able to maintain your own root view at any time in `Activity`.

If you set `edgeToEdgeInsets` to `null` in `init`, your root view will fully expand to full screen.

The above effect is equivalent to `enableEdgeToEdge` provided in `androidx.activity:activity`.

Without any action, your layout will be blocked by system bars or dangerous areas of the system (such as cutout displays), which will affect the user experience.

If you want to maintain and manage the `padding` of the current root view yourself, you must ensure that your interface elements can correctly adapt to the spacing provided by window insets.

You can go to the [ui-extension → Insets](./ui-extension#insets-extension) section of the previous section learn more about window insets.

You no longer need to use `enableEdgeToEdge`, `SystemBarsController` will hold this effect by default after initialization,
you should use `edgeToEdgeInsets` to control the window insets `padding` of the root view.

:::

::: tip

In Jetpack Compose, you can use `AppComponentActivity` to get a `SystemBarsController` initialized with `edgeToEdgeInsets = null`,
then use Jetpack Compose to set window insets.

`BetterAndroid` also provides extension support for it, for more functions, you can refer to [compose-multiplatform](./compose-multiplatform).

:::

Set the behavior of system bars.

This determines the system-controlled behavior when showing or hiding system bars.

> The following example

```kotlin
systemBars.behavior = SystemBarBehavior.SHOW_TRANSIENT_BARS_BY_SWIPE
```

The following are all behaviors provided in `SystemBarBehavior`, those marked with `*` are the default behaviors.

| Behavior                        | Description                                                                                                                                                   |
| ------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `DEFAULT`                       | Default behavior controlled by the system.                                                                                                                    |
| *`SHOW_TRANSIENT_BARS_BY_SWIPE` | A system bar that can be popped up by gesture sliding in full screen and displayed as a translucent system bar, and continues to hide after a period of time. |

Show, hide system bars.

> The following example

```kotlin
// Enter immersive mode (full screen mode).
// Hide status bars and navigation bars at the same time.
systemBars.hide(SystemBars.ALL)
// Separately control the status bars and navigation bars.
systemBars.hide(SystemBars.STATUS_BARS)
systemBars.hide(SystemBars.NAVIGATION_BARS)
// Exit immersive mode (full screen mode).
// Show status bars and navigation bars at the same time.
systemBars.show(SystemBars.ALL)
// Separately control the status bars and navigation bars.
systemBars.show(SystemBars.STATUS_BARS)
systemBars.show(SystemBars.NAVIGATION_BARS)
```

::: tip

If you need to control the showing and hiding of the input method (IME), you can refer to [ui-extension → View Extension](./ui-extension#view-extension).

:::

Set the style of the system bars.

You can customize the appearance of the status bars and navigation bars.

::: warning

In systems below Android 6.0, the content of the status bars does not support inversion, if you set a bright color, it will be automatically processed as a semi-transparent mask.

However, for MIUI and Flyme systems that have added the inverse color function themselves, they will use their own private solutions to achieve the inverse color effect.

In systems below Android 8, the content of the navigation bars does not support inversion, and the processing method is the same as above.

:::

> The following example

```kotlin
// Set the style of the status bars.
// Note: Please make sure to import com.highcapable.betterandroid.ui.component.systembar.style
//       SystemBarStyle under the package name, not androidx.activity.SystemBarStyle.
systemBars.statusBarStyle = SystemBarStyle(
    // Set background color.
    color = Color.WHITE,
    // Set content color.
    darkContent = true
)
// Set the style of the navigation bars.
systemBars.navigationBarStyle = SystemBarStyle(
    // Set background color.
    color = Color.WHITE,
    // Set content color.
    darkContent = true
)
// You can set the style of the status bars and navigation bars at once.
systemBars.setStyle(
    statusBar = SystemBarStyle(
        color = Color.WHITE,
        darkContent = true
    ),
    navigationBar = SystemBarStyle(
        color = Color.WHITE,
        darkContent = true
    )
)
// You can also set the style of the status bars and navigation bars at the same time.
systemBars.setStyle(
    style = SystemBarStyle(
        color = Color.WHITE,
        darkContent = true
    )
)
```

The following are the preset styles provided in `SystemBarStyle`, the ones marked with `*` are the default styles.

| Style              | Description                                                                                                                                   |
| ------------------ | --------------------------------------------------------------------------------------------------------------------------------------------- |
| `Auto`             | The system dark mode is a pure black background + light content color, and the light mode is a pure white background + dark content color.    |
| *`AutoTransparent` | The light content color is used in the system dark mode, and the dark content color is used in the light mode, with a transparent background. |
| `Light`            | Pure white background + dark content color.                                                                                                   |
| `LightScrim`       | Translucent pure white background + dark content color.                                                                                       |
| `LightTransparent` | Transparent background + dark content color.                                                                                                  |
| `Dark`             | Pure black background + light content color.                                                                                                  |
| `DarkScrim`        | Translucent solid black background + light content color.                                                                                     |
| `DarkTransparent`  | Transparent background + light content color.                                                                                                 |

::: tip

When the app is first cold-launched, the color of the system bars will be determined by the attributes you set in `styles.xml`.

In order to provide a better user experience during cold launch, you can refer to the following examples.

> The following example

```xml
<style name="Theme.MyApp.Demo" parent="Theme.MaterialComponents.DayNight.NoActionBar">
    <!-- Set the status bar color. -->
    <item name="android:statusBarColor">@color/colorPrimary</item>
    <!-- Set the navigation bar color. -->
    <item name="android:navigationBarColor">@color/colorPrimary</item>
    <!-- Set the status bar content color. -->
    <item name="android:windowLightStatusBar">true</item>
    <!-- Set the navigation bar content color. -->
    <item name="android:windowLightNavigationBar">true</item>
</style>
```

:::

Destroy `SystemBarsController`.

This will restore the state before initialization, including the status bars, navigation bars color, etc.

> The following example

```kotlin
// Destroy SystemBarsController, which will restore the state before initialization.
systemBars.destroy()
// You can use isDestroyed at any time to determine whether
// the current SystemBarsController has been destroyed.
val isDestroyed = systemBars.isDestroyed
```

::: warning

After using `SystemBarsController`, the `WindowInsetsController` of the current root view `rootView` has been automatically taken over by it.

Please do not manually set parameters such as `isAppearanceLightStatusBars` and `isAppearanceLightNavigationBars` in `WindowInsetsController`,
this may cause the actual effects of `statusBarStyle`, `navigationBarStyle`, `setStyle` and other functions to display abnormally.

:::