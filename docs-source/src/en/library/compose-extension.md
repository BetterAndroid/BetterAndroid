# compose-extension

![GitHub release](https://img.shields.io/github/v/release/BetterAndroid/BetterAndroid?display_name=release&logo=github&color=green&filter=compose-extension-*)
<span style="margin-left: 5px"/>
![Android Min SDK](https://img.shields.io/badge/Min%20SDK-21-orange?logo=android)

This is a dependency for Jetpack Compose related functional extensions and supports multiple platforms.

Currently supported platforms: Android, iOS, Desktop (JVM).

::: warning

This module is still under development and testing, and the API may change before the `1.0.0` version is officially released.

We are welcome you to make suggestions to us at [GitHub Issues](repo://issues).

:::

## Configure Dependency

You can add this module to your project using the following method.

This is a Kotlin Multiplatform dependency, you need the `org.jetbrains.kotlin.multiplatform` plugin to apply the relevant dependencies.

### SweetDependency (Recommended)

Add dependencies to your project `SweetDependency` configuration file.

```yaml
libraries:
  com.highcapable.betterandroid:
    # commonMain
    compose-extension:
      version: +
    # androidMain
    compose-extension-android:
      version-ref: <this>::compose-extension
    # iosArm64Main
    compose-extension-iosarm64:
      version-ref: <this>::compose-extension
    # iosX64Main
    compose-extension-iosx64:
      version-ref: <this>::compose-extension
    # iosSimulatorArm64Main
    compose-extension-iossimulatorarm64:
      version-ref: <this>::compose-extension
    # desktopMain
    compose-extension-desktop:
      version-ref: <this>::compose-extension
```

Configure dependencies in your project `build.gradle.kts`.

If you use multi-platform dependencies in a regular project, you only need to deploy the corresponding platform suffix dependencies as needed.

```kotlin
implementation(com.highcapable.betterandroid.compose.extension.android)
implementation(com.highcapable.betterandroid.compose.extension.iosarm64)
implementation(com.highcapable.betterandroid.compose.extension.iosx64)
implementation(com.highcapable.betterandroid.compose.extension.iossimulatorarm64)
implementation(com.highcapable.betterandroid.compose.extension.desktop)
```

If you use multi-platform dependencies in a multi-platform project, you need to add the `compose-extension` dependency in `commonMain`.

```kotlin
kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(com.highcapable.betterandroid.compose.extension)
            }
        }
    }
}
```

### Traditional Method

Configure dependencies in your project `build.gradle.kts`.

If you use multi-platform dependencies in a regular project, you only need to deploy the corresponding platform suffix dependencies as needed.

```kotlin
implementation("com.highcapable.betterandroid:compose-extension-android:<version>")
implementation("com.highcapable.betterandroid:compose-extension-iosarm64:<version>")
implementation("com.highcapable.betterandroid:compose-extension-iosx64:<version>")
implementation("com.highcapable.betterandroid:compose-extension-iossimulatorarm64:<version>")
implementation("com.highcapable.betterandroid:compose-extension-desktop:<version>")
```

If you use multi-platform dependencies in a multi-platform project, you need to add the `compose-extension` dependency in `commonMain`.

```kotlin
kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("com.highcapable.betterandroid:compose-extension:<version>")
            }
        }
    }
}
```

Please change `<version>` to the version displayed at the top of this document.

## Function Introduction

You can view the KDoc [click here](kdoc://compose-extension).

### Color Extension

::: tip Contents of This Section

[Color → isBrightColor](kdoc://compose-extension/compose-extension/com.highcapable.betterandroid.compose.extension.ui/is-bright-color)

[Color → toHexColor](kdoc://compose-extension/compose-extension/com.highcapable.betterandroid.compose.extension.ui/to-hex-color)

[Color → mixColorOf](kdoc://compose-extension/compose-extension/com.highcapable.betterandroid.compose.extension.ui/mix-color-of)

[Color → orNull](kdoc://compose-extension/compose-extension/com.highcapable.betterandroid.compose.extension.ui/or-null)

[Color → toPlatformColor (desktop)](kdoc://compose-extension/compose-extension/com.highcapable.betterandroid.compose.extension.ui/[desktop]to-platform-color)

[Color → toPlatformColor (ios)](kdoc://compose-extension/compose-extension/com.highcapable.betterandroid.compose.extension.ui/[ios]to-platform-color)

[Color → toComposeColor](kdoc://compose-extension/compose-extension/com.highcapable.betterandroid.compose.extension.ui/to-compose-color)

Extensions for `Color`.

:::

`Color` in Jetpack Compose encapsulates color-related implementations, which solves the problem of using the `Integer` type to pass colors in native Android.

`BetterAndroid` also provides extensions for `Color` similar to those in [ui-extension → Color Extension](../library/ui-extension.md#color-extension), making it easier use.

Since `Color` provides the `alpha` property, the `toAlphaColor` extension provided natively is no longer needed in `Color`.

Here are some relevant example uses of color extensions.

Determine how bright the color is.

This is useful when you need to decide whether to use dark text based on how bright the color is.

> The following example

```kotlin
// Assume we have the following colors.
val color = Color.White
// To determine how bright it is, you just need to use the following method.
// You will definitely get a true because this is a white color.
val isBright = color.isBrightColor
```

Convert color to HEX string.

> The following example

```kotlin
// Assume we have the following colors.
val color = Color.White
// To convert it to a HEX string you just need to use the following method.
// You will get a "#FFFFFFFF" with transparency.
val hexString = color.toHexColor()
```

Mix two colors.

> The following example

```kotlin
// Assume we have the following colors.
val color1 = Color.White
val color2 = Color.Black
// You can mix them very easily using.
val mixColor = mixColorOf(color1, color2)
// You can also set the mixing ratio, the default is 0.5f.
val mixColor = mixColorOf(color1, color2, 0.2f)
```

Determine whether `Color` is unspecified.

When `Color` is `Color.Unspecified`, you can use the following method to get an object that can be `null` in this state.

> The following example

```kotlin
// Assume we have the following colors.
val color = Color.Unspecified
// null will be returned in this state, then you can use ?: to pass the default value in undefined cases.
val myColor = color.orNull() ?: Color.White
```

On non-Android platforms, you can convert the corresponding `Color` object to the platform's color object in the following way.

> The following example

```kotlin
// Assume we have the following colors.
val color = Color.White
// For example, you can convert it to a color object in JavaFX.
val awtColor = color.toPlatformColor()
// In iOS, you can convert it to a UIColor object.
val uiColor = color.toPlatformColor()
```

::: tip

In Android platform, you can use the `Color.toArgb()` method to convert a `Color` object to an `Integer` type.

:::

Likewise, you can convert the platform's color objects into Compose's `Color` objects in the following way.

> The following example

```kotlin
// Assume this is a color object in JavaFX.
val awtColor = Color.WHITE
// Assume this is a color object in iOS.
val uiColor = UIColor.whiteColor()
// Convert it to a Compose Color object.
val composeColor = awtColor.toComposeColor()
val composeColor = uiColor.toComposeColor()
```

::: tip

In Android platform, you can use a form like `Color(0xFFFFFFFF)` to convert an `Long` type color into a `Color` object.

:::

### Border Extension

::: tip Contents of This Section

[Border → borderOrElse](kdoc://compose-extension/compose-extension/com.highcapable.betterandroid.compose.extension.ui/border-or-else)

[BorderStroke → solidColor](kdoc://compose-extension/compose-extension/com.highcapable.betterandroid.compose.extension.ui/solid-color)

Extensions for `Border` and `BorderStroke`.

:::

You can add a border to a component through the `Modifier.border(...)` method, but when the border size is `0.dp`, the border will still exist.

To solve this problem, `BetterAndroid` provides the `borderOrElse` extension, which will no longer add borders to components when the border size is `0.dp`.

> The following example

```kotlin
Box(
    modifier = Modifier
        .size(50.dp)
        // Here, the size of the border is 0.dp, so the component will not have a border.
        .borderOrElse(0.dp, RectangleShape)
) {
    // Some content.
}
```

Gets the color in `brush` (`SolidColor`) from an existing `BorderStroke` object.

`SolidColor` is a type of `Brush`, which can be used to fill color, a border created by `BorderStroke(10.dp, Color.White)` will not be able to easily obtain the color from `brush`.

`BetterAndroid` provides an extension for this, now you can get the color in `brush` using the following method.

> The following example

```kotlin
// Assume this is your BorderStroke object.
val border = BorderStroke(10.dp, Color.White)
// When the brush is determined to be SolidColor, you can use the following method to get the color in it.
// If it cannot be obtained, Color.Unspecified will be returned.
val color = border.solidColor
// You can also set its default value when it cannot be obtained.
val color = border.solidColor(Color.Black)
```

### Padding Extension

::: tip Contents of This Section

[ComponentPadding](kdoc://compose-extension/compose-extension/com.highcapable.betterandroid.compose.extension.ui/-component-padding)

Componentized `padding`, inherited from `PaddingValues`.

:::

`PaddingValues` is the `padding` usage provided in Jetpack Compose, which can be set directly to `Modifier.padding(...)`.

However, `PaddingValues` does not provide functions such as `copy`, once set, it cannot be modified, which is very inconvenient.

So `BetterAndroid` inherited from `PaddingValues` and rewrote `ComponentPadding` to make it easier to use.

In terms of usage, `ComponentPadding` is exactly the same as `PaddingValues`, and you can also set it directly into `Modifier.padding(...)`.

> The following example

```kotlin
// Create a ComponentPadding.
val padding = ComponentPadding(10.dp)
// Set padding for each direction.
val padding = ComponentPadding(
    start = 15.dp,
    top = 10.dp,
    bottom = 12.dp,
    end = 16.dp
)
// Set horizontal and vertical padding.
val padding = ComponentPadding(
    horizontal = 10.dp,
    vertical = 15.dp
)
// Create a new ComponentPadding via copy.
val paddingCopy = padding.copy(top = 15.dp)
// Set to component.
Box(
     modifier = Modifier.padding(padding)
) {
    // Some content.
}
```

`BetterAndroid` also encapsulates `calculateLeftPadding` and `calculateRightPadding` into Composeable methods,
you no longer need to use `LayoutDirection` to calculate `padding` in the LTR and RTL directions.

> The following example

```kotlin
@Composable
fun MyComponent() {
    // Assume this is your ComponentPadding.
    val padding: ComponentPadding
    // Get left.
    val left = padding.left
    // Get right.
    val right = padding.right
}
```

You can also convert it to `PaddingValues` using the following method.

> The following example

```kotlin
// Assume this is your ComponentPadding.
val padding: ComponentPadding
// Convert to PaddingValues.
val paddingValues = padding.toPaddingValues()
```

### Foundation Extension

::: tip Contents of This Section

[Foundation → componentState](kdoc://compose-extension/compose-extension/com.highcapable.betterandroid.compose.extension.ui/component-state)

[Foundation → clickable](kdoc://compose-extension/compose-extension/com.highcapable.betterandroid.compose.extension.ui/clickable)

[Foundation → combinedClickable](kdoc://compose-extension/compose-extension/com.highcapable.betterandroid.compose.extension.ui/combined-clickable)

[Foundation → toggleable](kdoc://compose-extension/compose-extension/com.highcapable.betterandroid.compose.extension.ui/toggleable)

[Foundation → selectable](kdoc://compose-extension/compose-extension/com.highcapable.betterandroid.compose.extension.ui/selectable)

Extensions related to foundation.

:::

There is a lack of a "disabled" state in Jetpack Compose components, `BetterAndroid` provides the following ways to achieve this effect by adjusting the transparency of the component.

Its actual function is actually a visual enabling or disabling transparency effect, and does not set any state on the component.

> The following example

```kotlin
Box(
    modifier = Modifier
        .size(50.dp)
        // Set the state of the component through componentState.
        // You can also set disabledAlpha to adjust the transparency of
        // the component in the disabled state.
        .componentState(enabled = false)
) {
    // Some content.
}
```

In the original `clickable`, `combinedClickable`, `toggleable`, and `selectable`, you need to manually set the default value for them.

If you only use these functions in a simple scenario, it will be more cumbersome.

`BetterAndroid` provides extensions of the same name for the above functions,
now you can use these functions more conveniently without having to think about using `remember` and other methods to set their state.

> The following example

```kotlin
Box(
     modifier = Modifier
        .size(50.dp)
        // Set the click event of the component through clickable.
        .clickable {
            // Handle click event.
        }
) {
    // Some content.
}
```

::: warning

You need to change the package name referenced by these functions from `androidx.compose.foundation` to `com.highcapable.betterandroid.compose.extension.ui`.

:::

### ImageVector Extension

::: tip Contents of This Section

[ImageVector → ImageVector](kdoc://compose-extension/compose-extension/com.highcapable.betterandroid.compose.extension.ui/-image-vector)

Extensions for `ImageVector`.

:::

Jetpack Compose natively provides an `ImageVector` that can create vector images, but you need to use `ImageVector.Builder` to create it.

This method does not seem very friendly, so `BetterAndroid` provides a method of the same name of `ImageVector` for this purpose.

> The following example

```kotlin
// Create a vector image through ImageVector.
val myVector = ImageVector(
    name = "my_vector",
    defaultWidth = 32.dp,
    defaultHeight = 32.dp,
    viewportWidth = 48f,
    viewportHeight = 48f
) {
    path(
        fillAlpha = 1.0f,
        stroke = SolidColor(Color.White),
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Round,
        strokeLineJoin = StrokeJoin.Round,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero
    ) {
        // ...
    }
    // ...
}
```

### Unit Extension

::: tip Contents of This Section

[Unit → orNull](kdoc://compose-extension/compose-extension/com.highcapable.betterandroid.compose.extension.ui/or-null)

Extensions for `Unit`.

:::

In all units that can use `isSpecified` for judgment, Jetpack Compose provides the `takeOrElse` method, but it is not concise and easy to understand.

So `BetterAndroid` provides the `orNull` method for this, you can use it to get an object that can be `null` in this state.

Below is a comparison of using `takeOrElse` versus `orNull`.

> The following example

```kotlin
// Assume we have the following units.
val dp: Dp
val sp: TextUnit
// Use takeOrElse.
val dpValue = dp.takeOrElse { 10.dp }
val spValue = sp.takeOrElse { 10.sp }
// Use orNull.
val dpValue = dp.orNull() ?: 10.dp
val spValue = sp.orNull() ?: 10.sp
```

You will find that `orNull` is more concise and easier to understand.

### Dialog, Popup Component Extension

::: tip Contents of This Section

[DialogPropertiesWrapper](kdoc://compose-extension/compose-extension/com.highcapable.betterandroid.compose.extension.ui.window/-dialog-properties-wrapper)

A wrapper for `DialogProperties` in the Android platform.

[DialogPropertiesWrapper.AndroidProperties](kdoc://compose-extension/compose-extension/com.highcapable.betterandroid.compose.extension.ui.window/-dialog-properties-wrapper/-android-properties)

Limited properties for Android platform in `DialogProperties`.

[SecureFlagPolicyWrapper](kdoc://compose-extension/compose-extension/com.highcapable.betterandroid.compose.extension.ui.window/-secure-flag-policy-wrapper)

A wrapper for `SecureFlagPolicy` in the Android platform.

[Dialog → Dialog](kdoc://compose-extension/compose-extension/com.highcapable.betterandroid.compose.extension.ui.window/-dialog)

Extensions for `Dialog`.

[Popup → Popup](kdoc://compose-extension/compose-extension/com.highcapable.betterandroid.compose.extension.ui.window/-popup)

Extensions for `Popup`.

:::

The extensions described in this section are available for use in Kotlin Multiplatform.

When creating `Dialog` and `Popup`, `commonMain` does not handle the special functions in the Android platform well.

`BetterAndroid` provides functions related to the Android platform for this purpose, you can use them directly in `commonMain` without having to adapt them separately for Android.

`DialogPropertiesWrapper` mirrors all properties from `DialogProperties` and encapsulates Android platform-specific properties into `DialogPropertiesWrapper.AndroidProperties`.

In the `Dialog` method provided by `BetterAndroid`, you can directly pass in the `DialogPropertiesWrapper` object.

> The following example

```kotlin
// Create a dialog.
Dialog(
    onDismissRequest = {
       // Handle related events.
    },
    properties = DialogPropertiesWrapper(
        dismissOnBackPress = true,
        dismissOnClickOutside = true,
        usePlatformDefaultWidth = true,
        usePlatformInsets = true,
        scrimColor = Color.Black.copy(alpha = 0.6f),
        // Set special properties for the Android platform.
        androidProperties = DialogPropertiesWrapper.AndroidProperties(
            secureFlagPolicy = SecureFlagPolicyWrapper.Inherit,
            decorFitsSystemWindows = true
        )
    )
) {
    // Some content.
}
```

::: warning

Currently, only `alpha` in `scrimColor` in `DialogPropertiesWrapper` can take effect on the Android platform.

:::

For `Popup`, the `onPreviewKeyEvent` and `onKeyEvent` parameters do not exist on the Android platform, which will causes `commonMain` to fail to compile when distributed to Android.

To fix this problem, `BetterAndroid` simulates `onPreviewKeyEvent` and `onKeyEvent` for the Android platform, now you don't need to worry about compatibility issues.

> The following example

```kotlin
// Create a popup.
Popup(
    onDismissRequest = {
       // Handle related events.
    },
    properties = PopupProperties(),
    onPreviewKeyEvent = {
       // Handle related events.
       false
    },
    onKeyEvent = {
       // Handle related events.
       false
    }
) {
    // Some content.
}
```

::: warning

You need to change the package name referenced by `Dialog` and `Popup` above from `androidx.compose.ui.window` to `com.highcapable.betterandroid.compose.extension.ui.window`.

:::

## Multi-Platform Support

In order to allow developers who are adapted to native Android development to adapt to various platforms faster,
`BetterAndroid` seamlessly provides multi-platform support with the help of the cross-platform features of Kotlin Multiplatform and Jetpack Compose.

Although the extension support for different platforms also belongs to [compose-extension](#compose-extension),
they will be introduced independently from the content in [Function Introduction](#function-introduction).

Functions related to multi-platform support will continue to be developed in [compose-extension](#compose-extension), the following are the functions currently being developed and completed.

We welcome more developers to participate in the development, if possible, you are welcome to submit a PR to contribute to this project or make suggestions to us through [GitHub Issues](repo://issues).

Features marked with "✅" indicate available, "❎" indicates not supported by the platform, "🚧" indicates preparation or under development (WIP), and "🔨" indicates planned development.

| Feature Name                   | Description                                                   | Android |  iOS  | Desktop |
| ------------------------------ | ------------------------------------------------------------- | :-----: | :---: | :-----: |
| `BackHandler`                  | System back pressed event for Android.                        |    ✅    |   ❎   |    ❎    |
| `PlatformWindowInsets`         | Window insets for mobile platforms.                           |    🚧    |   🚧   |    ❎    |
| `PlatformNotificationManager`  | Notifications post & management to the system.                |    🔨    |   🔨   |    🔨    |
| `PlatformSystemBarsController` | System bar-related controller for mobile platforms.           |    ✅    |   ✅   |    ❎    |
| `PlatformDisplayController`    | Screen rotation & brightness controller for mobile platforms. |    🚧    |   🚧   |    ❎    |
| `PlatformHwSensorController`   | Hardware sensors (such as vibration) for mobile platforms.    |    🔨    |   🔨   |    ❎    |

The features that have been developed will be described in detail below.

### System Event

::: tip Contents of This Section

[BackHandler → BackHandler](kdoc://compose-extension/compose-extension/com.highcapable.betterandroid.compose.extension.platform.backpress/-back-handler)

Extensions for system back pressed event.

:::

Although `androidx.activity:activity-compose` provides `BackHandler`, it can only be used in Android.

`BetterAndroid` provides multi-platform distribution support for `BackHandler`, you can use it directly in `commonMain`, but it will only take effect on the Android platform.

`BackHandler` is implemented in Android using `BackPressedController` in [ui-component → System Event](../library/ui-component.md#system-event),
you can refer to [ui-component → Activity](../library/ui-component.md#activity) to use `AppComponentActivty` as the drawing object of Compose to get a better experience.

Below is a usage example that is identical to the `BackHandler` usage provided in `androidx.activity:activity-compose`.

> The following example

```kotlin
// Create enabled state.
var enabled by remember { mutableStateOf(true) }
// Create BackHandler.
BackHandler(enabled) {
    // Handle back pressed event.
}
```

### System Bars (Status Bars, Navigation Bars, Home Indicator, etc)

::: tip Contents of This Section

[PlatformSystemBarsController](kdoc://compose-extension/compose-extension/com.highcapable.betterandroid.compose.extension.platform.systembar/-platform-system-bars-controller)

System bars controller for Android and iOS.

[NativeSystemBarsController](kdoc://compose-extension/compose-extension/com.highcapable.betterandroid.compose.extension.platform.systembar/-native-system-bars-controller)

The native system bars controller corresponding to Android and iOS.

[PlatformSystemBarStyle](kdoc://compose-extension/compose-extension/com.highcapable.betterandroid.compose.extension.platform.systembar/-platform-system-bar-style)

System bars style.

[PlatformSystemBars](kdoc://compose-extension/compose-extension/com.highcapable.betterandroid.compose.extension.platform.systembar/-platform-system-bars)

System bars type.

[PlatformSystemBarBehavior](kdoc://compose-extension/compose-extension/com.highcapable.betterandroid.compose.extension.platform.systembar/-platform-system-bar-behavior)

System bars behavior.

[SystemBars → rememberSystemBarsController](kdoc://compose-extension/compose-extension/com.highcapable.betterandroid.compose.extension.platform.systembar/remember-system-bars-controller)

[SystemBars → nativeController](kdoc://compose-extension/compose-extension/com.highcapable.betterandroid.compose.extension.platform.systembar/native-controller)

Extensions for system bars.

> iOS Platform Special Components

[SystemBarsController](kdoc://compose-extension/compose-extension/com.highcapable.betterandroid.compose.extension.platform.component.systembar/-system-bars-controller)

System bars controller.

[SystemBarStyle](kdoc://compose-extension/compose-extension/com.highcapable.betterandroid.compose.extension.platform.component.systembar.style/-system-bar-style)

System bars style.

[SystemBars](kdoc://compose-extension/compose-extension/com.highcapable.betterandroid.compose.extension.platform.component.systembar.type/-system-bars)

System bars type.

[SystemBarBehavior](kdoc://compose-extension/compose-extension/com.highcapable.betterandroid.compose.extension.platform.component.systembar.type/-system-bar-behavior)

System bars behavior.

[AppComponentUIViewController](kdoc://compose-extension/compose-extension/com.highcapable.betterandroid.compose.extension.platform.component.uiviewcontroller/-app-component-u-i-view-controller)

`UIViewController` with system bars controller.

[UIViewController → AppComponentUIViewController](kdoc://compose-extension/compose-extension/com.highcapable.betterandroid.compose.extension.platform.uiviewcontroller/-app-component-u-i-view-controller)

Extensions for `UIViewController` of creating Compose drawing portals.

:::

In both Android and iOS, you need to control and adjust the status bars and navigation bars (home indicator) provided by the system to give users a better experience.

For this purpose, `BetterAndroid` provides you with `PlatformSystemBarsController`, which can easily control the system bars of each platform through a common API.

The working principle of `PlatformSystemBarsController` is to distribute it to the corresponding native controller according to different platforms through `NativeSystemBarsController`.

When using it for the first time, you need to configure platform-specific configuration of `PlatformSystemBarsController`.

**Android Platform**

Whether it is automatic or manual configuration, you need to import the [ui-component](../library/ui-component.md) module in the corresponding Android project first.

**1. Automatic Configuration**

Please refer to [ui-component → Activity](../library/ui-component.md#activity) and use `AppComponentActivty` as the drawing portals of Compose.

**2. Manual Configuration**

You need to use `ComponentActivity` as your `Activity` and implement `ISystemBarsController` on it interface.

> The following example

```kotlin
class MainActivity : ComponentActivity(), ISystemBarsController {

    override val systemBars by lazy { SystemBarsController.from(window) }
}
```

After completing the above steps, you can refer to the initialization method in [ui-component → System Bars (Status Bars, Navigation Basr, etc)](../library/ui-component.md#system-bars-status-bars-navigation-bars-etc) initialize it manually.

`PlatformSystemBarsController` will automatically recognize `Activity`s that use the `ISystemBarsController` interface.

**3. No Configuration**

You can use `PlatformSystemBarsController` directly without any configuration, when called,
it will wait for the composition to complete and then create and initialize a new `SystemBarsController` for the current `Activity`.

::: warning

This behavior is experimental and uncontrollable, it may affect the lifecycle of Compose, and we do not recommend you use it this way.

:::

**iOS Platform**

In iOS projects, you must manually configure Compose's drawing portals.

First, create an `AppComponentUIViewController` in the `App.ios.kt` file in `iosMain` using the following method.

> Examples are as follows

```kotlin
fun createUIViewController() = AppComponentUIViewController {
    // Your composable content here.
}
```

Then, create an iOS project using UIKit and configure the `AppDelegate.swift` file as follows.

> The following example

```swift
import UIKit
import shared // Here is your shared module name.

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate {

    var window: UIWindow?

    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions:
        [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        // Create a new UI window.
        window = UIWindow(frame: UIScreen.main.bounds)
        // Set the root view controller.
        window?.rootViewController = App_iosKt.createUIViewController()
        // Make the window visible.
        window?.makeKeyAndVisible()
        return true
    }
}
```

::: warning

Don't use an `UIViewControllerRepresentable` to create an `UIViewController` for Swift UI,
that will cause the system bars controller related functions to become invalid, because Swift UI will take over the state of the entire view, at this time,
you can only control system bars in Swift UI.

:::

::: tip

If you feel that the above configuration process is too cumbersome, you can refer to [Quick Start → Project Template](../guide/quick-start.md#project-template) to create a project.

:::

After the above configuration is completed, to use `PlatformSystemBarsController` in Compose, you can easily use the following methods to create and obtain it.

> The following example

```kotlin
// Get PlatformSystemBarsController through state management.
val systemBars = rememberSystemBarsController()
```

::: warning

When using `rememberSystemBarsController`, if the native `SystemBarsController` has not been initialized, it will automatically call `init` for initialization.

To avoid problems with interface performance, you should ensure that initialization operations are completed during the platform-specific configuration phase,
otherwise you should ensure that `rememberSystemBarsController` is called before all content starts to be drawn.

:::

The following is a detailed usage introduction of `PlatformSystemBarsController`.

Most of the usage here will be consistent with [ui-component → System Bars (Status Bars, Navigation Bars, etc)](../library/ui-component.md#system-bars-status-bars-navigation-bars-etc).

Set the behavior of system bars.

This determines the system-controlled behavior when showing or hiding system bars.

> The following example

```kotlin
systemBars.behavior = PlatformSystemBarBehavior.Immersive
```

The following are all behaviors provided in `PlatformSystemBarBehavior`, those marked with `*` are the default behaviors.

| Behavior     | Platform | Description                                                                                                                                                   |
| ------------ | :------: | ------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `Default`    |   All    | Default behavior controlled by the system.                                                                                                                    |
| *`Immersive` | Android  | A system bar that can be popped up by gesture sliding in full screen and displayed as a translucent system bar, and continues to hide after a period of time. |
|              |   iOS    | The system bars can be revealed temporarily with system gestures when the status bars hide, but disappears after a period of time.                            |

Show, hide system bars.

> The following example

```kotlin
// Enter immersive mode (full screen mode).
// Hide status bars and navigation bars, home indicator at the same time.
systemBars.hide(PlatformSystemBars.All)
// Separately control the status bars and navigation bars, home indicator.
systemBars.hide(PlatformSystemBars.StatusBars)
systemBars.hide(PlatformSystemBars.NavigationBars)
// Exit immersive mode (full screen mode).
// Show status bars and navigation bars, home indicator at the same time.
systemBars.show(PlatformSystemBars.ALL)
// Separately control the status bars and navigation bars, home indicator.
systemBars.show(PlatformSystemBars.StatusBars)
systemBars.show(PlatformSystemBars.NavigationBars)
```

Set the style of the system bars.

You can customize the appearance of the status bars and navigation bars, home indicator.

::: warning

In systems below Android 6.0, the content of the status bars does not support inversion, if you set a bright color, it will be automatically processed as a semi-transparent mask.

However, for MIUI and Flyme systems that have added the inverse color function themselves, they will use their own private solutions to achieve the inverse color effect.

In systems below Android 8, the content of the navigation bars does not support inversion, and the processing method is the same as above.

In iOS, home indicator does not support setting `darkContent`, its color is controlled by the system, but you can set the background color for it.

:::

> The following example

```kotlin
// Set the style of the status bars.
systemBars.statusBarStyle = PlatformSystemBarStyle(
    // Set background color.
    color = Color.White,
    // Set content color.
    darkContent = true
)
// Set the style of the navigation bars, home indicator.
systemBars.navigationBarStyle = PlatformSystemBarStyle(
    // Set background color.
    color = Color.White,
    // Set content color.
    darkContent = true
)
// You can set the style of the status bars and navigation bars, home indicator at once.
systemBars.setStyle(
    statusBar = PlatformSystemBarStyle(
        color = Color.White,
        darkContent = true
    ),
    navigationBar = PlatformSystemBarStyle(
        color = Color.White,
        darkContent = true
    )
)
// You can also set the style of the status bars and navigation bars,
// home indicator at the same time.
systemBars.setStyle(
    style = PlatformSystemBarStyle(
        color = Color.White,
        darkContent = true
    )
)
```

The following are the preset styles provided in `PlatformSystemBarStyle`, the ones marked with `*` are the default styles.

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

Get the native controller.

You can obtain the native controller in the corresponding platform through the following methods, using `commonMain` or in unsupported platforms will always return `null`.

> The following example

```kotlin
// Get the native controller.
val nativeController = systemBars.nativeController
```

::: tip

`BetterAndroid` also provides a native `SystemBarsController` for iOS,
its usage is basically the same as [ui-component → System Bars (Status Bars, Navigation Bars, etc)](../library/ui-component.md#system-bars-status-bars-navigation-bars-etc)
except for the initialization function, normally you should not need to use it directly, and it will not be introduced in detail here.

:::