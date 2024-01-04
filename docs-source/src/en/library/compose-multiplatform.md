# compose-multiplatform

![Maven Central](https://img.shields.io/maven-central/v/com.highcapable.betterandroid/compose-multiplatform?logo=apachemaven&logoColor=orange&color=orange)
<span style="margin-left: 5px"/>
![Android Min SDK](https://img.shields.io/badge/Min%20SDK-21-orange?logo=android)

This is a dependency for Jetpack Compose's multi-platform features.

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
    compose-multiplatform:
      version: +
    # androidMain
    compose-multiplatform-android:
      version-ref: <this>::compose-multiplatform
    # iosArm64Main
    compose-multiplatform-iosarm64:
      version-ref: <this>::compose-multiplatform
    # iosX64Main
    compose-multiplatform-iosx64:
      version-ref: <this>::compose-multiplatform
    # iosSimulatorArm64Main
    compose-multiplatform-iossimulatorarm64:
      version-ref: <this>::compose-multiplatform
    # desktopMain
    compose-multiplatform-desktop:
      version-ref: <this>::compose-multiplatform
```

Configure dependencies in your project `build.gradle.kts`.

If you use multi-platform dependencies in a regular project, you only need to deploy the corresponding platform suffix dependencies as needed.

```kotlin
implementation(com.highcapable.betterandroid.compose.multiplatform.android)
implementation(com.highcapable.betterandroid.compose.multiplatform.iosarm64)
implementation(com.highcapable.betterandroid.compose.multiplatform.iosx64)
implementation(com.highcapable.betterandroid.compose.multiplatform.iossimulatorarm64)
implementation(com.highcapable.betterandroid.compose.multiplatform.desktop)
```

If you use multi-platform dependencies in a multi-platform project, you need to add the `compose-multiplatform` dependency in `commonMain`.

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
implementation("com.highcapable.betterandroid:compose-multiplatform-android:<version>")
implementation("com.highcapable.betterandroid:compose-multiplatform-iosarm64:<version>")
implementation("com.highcapable.betterandroid:compose-multiplatform-iosx64:<version>")
implementation("com.highcapable.betterandroid:compose-multiplatform-iossimulatorarm64:<version>")
implementation("com.highcapable.betterandroid:compose-multiplatform-desktop:<version>")
```

If you use multi-platform dependencies in a multi-platform project, you need to add the `compose-multiplatform` dependency in `commonMain`.

```kotlin
kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("com.highcapable.betterandroid:compose-multiplatform:<version>")
            }
        }
    }
}
```

Please change `<version>` to the version displayed at the top of this document.

## Function Introduction

You can view the KDoc [click here](kdoc://compose-multiplatform).

In order to allow developers who are adapted to native Android development to adapt to various platforms faster,
`BetterAndroid` seamlessly provides multi-platform support with the help of the cross-platform features of Kotlin Multiplatform and Jetpack Compose.

The following are the functions currently being developed and completed.

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

### Initial Configuration

::: tip Contents of This Section

> iosMain

[AppComponentUIViewController](kdoc://compose-multiplatform/compose-multiplatform/com.highcapable.betterandroid.compose.multiplatform.platform.uiviewcontroller/-app-component-u-i-view-controller)

Basic component `UIViewController`.

[UIViewController → AppComponentUIViewController](kdoc://compose-multiplatform/compose-multiplatform/com.highcapable.betterandroid.compose.multiplatform.platform/-app-component-u-i-view-controller)

Extensions for `UIViewController` to creating a starting point for Compose.

:::

Before starting, we recommend that you configure your project structure as follows.

::: tip

We recommend and refer to [Quick Start → Project Template](../guide/quick-start.md#project-template) to create a project without manual configuration.

You can also use the [Kotlin Multiplatform Wizard](https://kmp.jetbrains.com) to automatically help you complete the Gradle script compilation configuration of the iOS project.

:::

#### General Section

Please create an `App.kt` in `commonMain` which will serve as the starting point for Compose.

> The following example

```kotlin
@Composable
fun App() {
    // Your composables content here.
}
```

#### Android Platform

First, please create an entry to the shared code in `App.android.kt` in `androidMain`.

> The following example

```kotlin
@Composable
fun MainView() = App()
```

Then, please import the [ui-component](../library/ui-component.md) module in the corresponding Android project.

Next, please refer to [ui-component → Activity](../library/ui-component.md#activity) to use `AppComponentActivty` as the starting point for Compose.

> The following example

```kotlin
class MainActivity : AppComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainView()
        }
    }
}
```

#### iOS Platform

::: warning

You need to have a computer or virtual machine that can run macOS and have the Xcode environment configured correctly.

:::

First, you need to manually create an iOS App Xcode project, select Swift as the language, and select `Storyboard` in the `Interface` column.

Then, create an `AppComponentUIViewController` in the `App.ios.kt` file in `iosMain` using the following method.

> The following example

```kotlin
fun createUIViewController() = AppComponentUIViewController {
    App()
}
```

Next, make the following configurations to the `AppDelegate.swift` file in your iOS project.

If your iOS project was created using Swift UI, please create this file manually and remove the `App.swift` related files created using Swift UI.

> The following example

```swift
import UIKit
import shared // Here is your Kotlin Multiplatform shared module name.

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate {

    var window: UIWindow?

    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions:
        [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        // Create a new UIWindow.
        window = UIWindow(frame: UIScreen.main.bounds)
        // Set the controller of the root view.
        // App_iosKt is the automatically generated Kotlin code in shared.h.
        window?.rootViewController = App_iosKt.createUIViewController()
        // Make it visible.
        window?.makeKeyAndVisible()
        return true
    }
}
```

::: warning

Do not use `UIViewControllerRepresentable` to create `UIViewController` for Swift UI, although this is currently the code that the Kotlin Multiplatform Wizard automatically generates for you.

But this will cause the related functions of [System Bars (Status Bars, Navigation Bars, Home Indicator, etc)](#system-bars-status-bars-navigation-bars-home-indicator-etc) to fail,
because Swift UI will take over the state of the entire view, at this time, you can only control system bars in Swift UI.

:::

#### Desktop Platform

First, create an entry to the shared code in `App.desktop.kt` in `desktopMain`.

> The following example

```kotlin
@Composable
fun MainView() = App()
```

Then, please use the following method to create the starting point of Compose in the corresponding Java project `Main.kt`.

> The following example

```kotlin
fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        // Set window title.
        title = "My App",
        // Set window size.
        state = rememberWindowState(width = 300.dp, height = 500.dp)
    ) {
        MainView()
    }
}
```

### System Event

::: tip Contents of This Section

> commonMain

[BackHandler → BackHandler](kdoc://compose-multiplatform/compose-multiplatform/com.highcapable.betterandroid.compose.multiplatform.backpress/-back-handler)

Extensions for system back pressed event.

:::

Although `androidx.activity:activity-compose` provides `BackHandler`, it can only be used in Android.

`BetterAndroid` provides multi-platform distribution support for `BackHandler`, you can use it directly in `commonMain`, but it will only take effect on the Android platform.

`BackHandler` is implemented in Android using `BackPressedController` in [ui-component → System Event](../library/ui-component.md#system-event),
you can refer to [ui-component → Activity](../library/ui-component.md#activity) to use `AppComponentActivty` or manually implement the `IBackPressedController` interface for your `Activity`.

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

> commonMain

[PlatformSystemBarsController](kdoc://compose-multiplatform/compose-multiplatform/com.highcapable.betterandroid.compose.multiplatform.systembar/-platform-system-bars-controller)

System bars controller for Android and iOS.

[NativeSystemBarsController](kdoc://compose-multiplatform/compose-multiplatform/com.highcapable.betterandroid.compose.multiplatform.systembar/-native-system-bars-controller)

Native system bars controller corresponding to Android and iOS.

[PlatformSystemBarStyle](kdoc://compose-multiplatform/compose-multiplatform/com.highcapable.betterandroid.compose.multiplatform.systembar/-platform-system-bar-style)

System bars style.

[PlatformSystemBars](kdoc://compose-multiplatform/compose-multiplatform/com.highcapable.betterandroid.compose.multiplatform.systembar/-platform-system-bars)

System bars type.

[PlatformSystemBarBehavior](kdoc://compose-multiplatform/compose-multiplatform/com.highcapable.betterandroid.compose.multiplatform.systembar/-platform-system-bar-behavior)

System bars behavior.

[SystemBars → rememberSystemBarsController](kdoc://compose-multiplatform/compose-multiplatform/com.highcapable.betterandroid.compose.multiplatform.systembar/remember-system-bars-controller)

[SystemBars → nativeController](kdoc://compose-multiplatform/compose-multiplatform/com.highcapable.betterandroid.compose.multiplatform.systembar/native-controller)

Extensions for system bars.

> iosMain

[SystemBarsController](kdoc://compose-multiplatform/compose-multiplatform/com.highcapable.betterandroid.compose.multiplatform.platform.systembar/-system-bars-controller)

System bars controller.

[SystemBarStyle](kdoc://compose-multiplatform/compose-multiplatform/com.highcapable.betterandroid.compose.multiplatform.platform.systembar.style/-system-bar-style)

System bars style.

[SystemBars](kdoc://compose-multiplatform/compose-multiplatform/com.highcapable.betterandroid.compose.multiplatform.platform.systembar.type/-system-bars)

System bars type.

[SystemBarBehavior](kdoc://compose-multiplatform/compose-multiplatform/com.highcapable.betterandroid.compose.multiplatform.platform.systembar.type/-system-bar-behavior)

System bars behavior.

:::

In both Android and iOS, you need to control and adjust the status bars and navigation bars (home indicator) provided by the system to give users a better experience.

For this purpose, `BetterAndroid` provides you with `PlatformSystemBarsController`, which can easily control the system bars of each platform through a common API.

The working principle of `PlatformSystemBarsController` is to distribute it to the corresponding native controller according to different platforms through `NativeSystemBarsController`.

Before starting to use it, make sure you have read [Initial Configuration](#initial-configuration) and completed the relevant configuration for the current project.

`PlatformSystemBarsController` uses `SystemBarsController` in [ui-component → System bars (Status bars, Navigation bars, etc)](../library/ui-component.md#system-bars-status-bars-navigation-bars-etc)
in Android accomplish, you can refer to [ui-component → Activity](../library/ui-component.md#activity) to use `AppComponentActivty` or
manually implement the `ISystemBarsController` interface for your `Activity`.

You can get the `PlatformSystemBarsController` object globally in Compose in the following way.

> The following example

```kotlin
// Get PlatformSystemBarsController through state management.
val systemBars = rememberSystemBarsController()
```

::: warning

When using `rememberSystemBarsController`, if the native `SystemBarsController` has not been initialized, it will automatically call `init` for initialization.

To avoid problems with interface performance, you should ensure that initialization operations are completed during the configuration phase of each platform,
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