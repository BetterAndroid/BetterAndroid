# compose-multiplatform

![Maven Central](https://img.shields.io/maven-central/v/com.highcapable.betterandroid/compose-multiplatform?logo=apachemaven&logoColor=orange&style=flat-square)
<span style="margin-left: 5px"/>
![Maven metadata URL](https://img.shields.io/maven-metadata/v?metadataUrl=https%3A%2F%2Fraw.githubusercontent.com%2FHighCapable%2Fmaven-repository%2Frefs%2Fheads%2Fmain%2Frepository%2Freleases%2Fcom%2Fhighcapable%2Fbetterandroid%2Fcompose-multiplatform%2Fmaven-metadata.xml&logo=apachemaven&logoColor=orange&label=highcapable-maven-releases&style=flat-square)
<span style="margin-left: 5px"/>
![Android Min SDK](https://img.shields.io/badge/Min%20SDK-21-orange?logo=android&style=flat-square)

这是针对 Jetpack Compose 多平台功能的一个依赖。

目前支持的平台：Android、iOS、Desktop (JVM)。

::: warning

此模块尚在开发与测试，在 `1.0.0` 版本正式发布前 API 可能会发生变化，欢迎前往 [GitHub Issues](repo://issues) 向我们提出建议。

:::

## 配置依赖

你可以使用如下方式将此模块添加到你的项目中。

这是一个 Kotlin Multiplatform 依赖，你需要 `org.jetbrains.kotlin.multiplatform` 插件来应用相关依赖。

### Version Catalog (推荐)

在你的项目 `gradle/libs.versions.toml` 中添加依赖。

```toml
[versions]
betterandroid-compose-multiplatform = "<version>"

[libraries]
# commonMain
betterandroid-compose.multiplatform = { module = "com.highcapable.betterandroid:compose.multiplatform", version.ref = "betterandroid-compose-multiplatform" }
# androidMain
betterandroid-compose.multiplatform-android = { module = "com.highcapable.betterandroid:compose.multiplatform-android", version.ref = "betterandroid-compose-multiplatform" }
# iosArm64Main
betterandroid-compose.multiplatform-iosarm64 = { module = "com.highcapable.betterandroid:compose.multiplatform-iosarm64", version.ref = "betterandroid-compose-multiplatform" }
# iosX64Main
betterandroid-compose.multiplatform-iosx64 = { module = "com.highcapable.betterandroid:compose.multiplatform-iosx64", version.ref = "betterandroid-compose-multiplatform" }
# iosSimulatorArm64Main
betterandroid-compose.multiplatform-iossimulatorarm64 = { module = "com.highcapable.betterandroid:compose.multiplatform-iossimulatorarm64", version.ref = "betterandroid-compose-multiplatform" }
# desktopMain
betterandroid-compose.multiplatform-desktop = { module = "com.highcapable.betterandroid:compose.multiplatform-desktop", version.ref = "betterandroid-compose-multiplatform" }
```

在你的项目 `build.gradle.kts` 中配置依赖。

如果你在普通的项目中使用多平台依赖，你只需要按需部署对应平台后缀的依赖即可。

```kotlin
implementation(libs.betterandroid.compose.multiplatform.android)
implementation(libs.betterandroid.compose.multiplatform.iosarm64)
implementation(libs.betterandroid.compose.multiplatform.iosx64)
implementation(libs.betterandroid.compose.multiplatform.iossimulatorarm64)
implementation(libs.betterandroid.compose.multiplatform.desktop)
```

如果你在多平台项目中使用多平台依赖，你需要在 `commonMain` 中添加 `compose-multiplatform` 依赖。

```kotlin
kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.betterandroid.compose.multiplatform)
            }
        }
    }
}
```

请将 `<version>` 修改为此文档顶部显示的版本。

### 传统方式

在你的项目 `build.gradle.kts` 中配置依赖。

如果你在普通的项目中使用多平台依赖，你只需要按需部署对应平台后缀的依赖即可。

```kotlin
implementation("com.highcapable.betterandroid:compose-multiplatform-android:<version>")
implementation("com.highcapable.betterandroid:compose-multiplatform-iosarm64:<version>")
implementation("com.highcapable.betterandroid:compose-multiplatform-iosx64:<version>")
implementation("com.highcapable.betterandroid:compose-multiplatform-iossimulatorarm64:<version>")
implementation("com.highcapable.betterandroid:compose-multiplatform-desktop:<version>")
```

如果你在多平台项目中使用多平台依赖，你需要在 `commonMain` 中添加 `compose-multiplatform` 依赖。

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

请将 `<version>` 修改为此文档顶部显示的版本。

## 功能介绍

你可以 [点击这里](kdoc://compose-multiplatform) 查看 KDoc。

为了能让适应了原生 Android 开发的开发者们能够更快地对各种平台进行适配，借助于 Kotlin Multiplatform 与 Jetpack Compose 的跨平台特性，`BetterAndroid` 无缝地提供了多平台支持。

以下是目前正在开发、开发完成的功能。

我们欢迎更多的开发者参与到开发中来，如果可能，欢迎提交 PR 为此项目贡献或通过 [GitHub Issues](repo://issues) 向我们提出建议。

标识为 “✅” 的功能表示可用，“❎” 表示平台不支持，“🚧” 表示准备或正在开发中 (WIP)，“🔨” 表示计划开发。

| 功能名称                       | 描述                                         | Android |  iOS  | Desktop |
| ------------------------------ | -------------------------------------------- | :-----: | :---: | :-----: |
| `BackHandler`                  | 为 Android 提供系统返回事件的支持            |    ✅    |   ❎   |    ❎    |
| `PlatformWindowInsets`         | 为移动平台提供对 Window Insets 的支持        |    🚧    |   🚧   |    ❎    |
| `PlatformNotificationManager`  | 为系统提供对发送通知、通知管理的支持         |    🔨    |   🔨   |    🔨    |
| `PlatformSystemBarsController` | 为移动平台提供对系统栏相关功能控制的支持     |    ✅    |   ✅   |    ❎    |
| `PlatformDisplayController`    | 为移动平台提供对屏幕旋转、屏幕亮度控制的支持 |    🚧    |   🚧   |    ❎    |
| `PlatformHwSensorController`   | 为移动平台提供对硬件传感器的支持 (例如振动)  |    🔨    |   🔨   |    ❎    |

已经开发完成的功能将在下方对使用方法进行详细介绍。

### 初始化配置

::: tip 本节内容

> iosMain

[AppComponentUIViewController](kdoc://compose-multiplatform/compose-multiplatform/com.highcapable.betterandroid.compose.multiplatform.platform.uiviewcontroller/-app-component-u-i-view-controller)

基础组件 `UIViewController`。

[UIViewController → AppComponentUIViewController](kdoc://compose-multiplatform/compose-multiplatform/com.highcapable.betterandroid.compose.multiplatform.platform/-app-component-u-i-view-controller)

适用于创建 Compose 起始点的 `UIViewController` 扩展。

:::

在开始之前，我们建议你按照以下方式对项目结构进行配置。

::: tip

我们建议并参考 [快速开始 → 项目模版](../guide/quick-start.md#项目模版) 来创建项目以免去手动配置。

你也可以使用 [Kotlin Multiplatform Wizard](https://kmp.jetbrains.com) 以自动帮你完成 iOS 项目部分的 Gradle 脚本编译配置。

:::

#### 通用部分

请在 `commonMain` 中创建一个 `App.kt`，它将作为 Compose 的起始点。

> 示例如下

```kotlin
@Composable
fun App() {
    // 你的 Compose 内容
}
```

#### Android 平台

首先，请在对应的 Android 项目中导入 [ui-component](../library/ui-component.md) 模块。

然后，请参考 [ui-component → Activity](../library/ui-component.md#activity) 使用 `AppComponentActivty` 作为 Compose 的起始点。

> 示例如下

```kotlin
class MainActivity : AppComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            App()
        }
    }
}
```

#### iOS 平台

::: warning

你需要有一台能够运行 macOS 并正确配置 Xcode 环境的电脑或虚拟机。

:::

首先，你需要手动创建一个 iOS App 的 Xcode 项目，语言请选择 Swift，并在 `Interface` 一栏选择 `Storyboard`。

然后，请在 `iosMain` 中的 `MainViewController.kt` 文件中使用以下方式创建一个 `AppComponentUIViewController`。

> 示例如下

```kotlin
fun createMainViewController() = AppComponentUIViewController {
    App()
}
```

然后，请确定你在 `build.gradle.kts` 中为 iOS 项目设置的共享模块名称。

> 示例如下

```kotlin
kotlin {
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            // 设置共享模块名称
            baseName = "ComposeApp"
            // 建议设置为静态库
            isStatic = true
        }
    }
}
```

接下来，请在 iOS 项目中对 `AppDelegate.swift` 文件进行以下配置。

如果你的 iOS 项目是使用 Swift UI 创建的，请手动创建这个文件并移除使用 Swift UI 创建的 `SwiftApp.swift` 相关文件。

> 示例如下

```swift
import UIKit
import ComposeApp // 这里为你的共享模块名称

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate {

    var window: UIWindow?

    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions:
        [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        // 创建一个新的 UIWindow
        window = UIWindow(frame: UIScreen.main.bounds)
        // 设置根视图的控制器
        // MainViewControllerKt 是 ComposeApp.h 中自动生成的 Kotlin 代码
        window?.rootViewController = MainViewControllerKt.createMainViewController()
        // 使其可见
        window?.makeKeyAndVisible()
        return true
    }
}
```

::: warning

不要使用 `UIViewControllerRepresentable` 为 Swift UI 创建 `UIViewController`，虽然这是目前 Kotlin Multiplatform Wizard 自动为你生成的代码，
但是这会导致 [系统栏 (状态栏、导航栏、Home Indicator 等)](#系统栏-状态栏、导航栏、home-indicator-等) 相关功能失效，
因为 Swift UI 会接管整个视图的状态，此时，你只能在 Swift UI 中控制系统栏。

:::

#### Desktop 平台

请在对应的 Java 项目 `Main.kt` 中使用以下方式创建 Compose 的起始点。

> 示例如下

```kotlin
fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        // 设置窗口标题
        title = "My App",
        // 设置窗口大小
        state = rememberWindowState(width = 300.dp, height = 500.dp)
    ) { 
        App()
    }
}
```

### 返回事件

::: tip 本节内容

> commonMain

[BackHandler → BackHandler](kdoc://compose-multiplatform/compose-multiplatform/com.highcapable.betterandroid.compose.multiplatform.backpress/-back-handler)

适用于系统返回事件的扩展。

:::

虽然 `androidx.activity:activity-compose` 提供了 `BackHandler`，但是它只能在 Android 中使用。

`BetterAndroid` 提供了 `BackHandler` 的多平台分发支持，你可以直接在 `commonMain` 中使用它，但是它仅会在 Android 平台中生效。

如果你正在寻找 Android 侧的返回事件处理方式，可以参考 [ui-extension → BackPressed 扩展](../library/ui-extension.md#backpressed-扩展)。

下面是一个使用示例，它与 `androidx.activity:activity-compose` 中提供的 `BackHandler` 用法完全相同。

> 示例如下

```kotlin
// 创建启用状态
var enabled by remember { mutableStateOf(true) }
// 创建 BackHandler
BackHandler(enabled) {
    // 处理返回事件
}
```

### 系统栏 (状态栏、导航栏、Home Indicator 等)

::: tip 本节内容

> commonMain

[PlatformSystemBarsController](kdoc://compose-multiplatform/compose-multiplatform/com.highcapable.betterandroid.compose.multiplatform.systembar/-platform-system-bars-controller)

Android、iOS 的系统栏控制器。

[NativeSystemBarsController](kdoc://compose-multiplatform/compose-multiplatform/com.highcapable.betterandroid.compose.multiplatform.systembar/-native-system-bars-controller)

Android、iOS 对应的原生系统栏控制器。

[PlatformSystemBarStyle](kdoc://compose-multiplatform/compose-multiplatform/com.highcapable.betterandroid.compose.multiplatform.systembar/-platform-system-bar-style)

系统栏的样式。

[PlatformSystemBars](kdoc://compose-multiplatform/compose-multiplatform/com.highcapable.betterandroid.compose.multiplatform.systembar/-platform-system-bars)

系统栏的类型。

[PlatformSystemBarBehavior](kdoc://compose-multiplatform/compose-multiplatform/com.highcapable.betterandroid.compose.multiplatform.systembar/-platform-system-bar-behavior)

系统栏的行为。

[SystemBars → rememberSystemBarsController](kdoc://compose-multiplatform/compose-multiplatform/com.highcapable.betterandroid.compose.multiplatform.systembar/remember-system-bars-controller)

[SystemBars → nativeController](kdoc://compose-multiplatform/compose-multiplatform/com.highcapable.betterandroid.compose.multiplatform.systembar/native-controller)

适用于系统栏的扩展。

> iosMain

[SystemBarsController](kdoc://compose-multiplatform/compose-multiplatform/com.highcapable.betterandroid.compose.multiplatform.platform.systembar/-system-bars-controller)

系统栏控制器。

[SystemBarStyle](kdoc://compose-multiplatform/compose-multiplatform/com.highcapable.betterandroid.compose.multiplatform.platform.systembar.style/-system-bar-style)

系统栏的样式。

[SystemBars](kdoc://compose-multiplatform/compose-multiplatform/com.highcapable.betterandroid.compose.multiplatform.platform.systembar.type/-system-bars)

系统栏的类型。

[SystemBarBehavior](kdoc://compose-multiplatform/compose-multiplatform/com.highcapable.betterandroid.compose.multiplatform.platform.systembar.type/-system-bar-behavior)

系统栏的行为。

:::

在 Android、iOS 中，你都需要对系统提供的状态栏、导航栏 (Home Indicator) 进行控制和调整以便给用户带来更好的体验。

为此 `BetterAndroid` 为你提供了 `PlatformSystemBarsController`，它能够通过通用 API 来轻松实现对每个平台系统栏的控制。

`PlatformSystemBarsController` 的工作原理是通过 `NativeSystemBarsController` 根据平台的不同而分发到对应的原生控制器。

在开始使用前，确保你已经阅读了 [初始化配置](#初始化配置) 并对当前项目完成了相关配置。

`PlatformSystemBarsController` 在 Android 中使用 [ui-component → 系统栏 (状态栏、导航栏等)](../library/ui-component.md#系统栏-状态栏、导航栏等) 中的 `SystemBarsController` 实现，
你可以参考 [ui-component → Activity](../library/ui-component.md#activity) 使用 `AppComponentActivty` 或为你的 `Activity` 手动实现 `ISystemBarsController` 接口。

你可以通过以下方式来在 Compose 中全局得到 `PlatformSystemBarsController` 对象。

> 示例如下

```kotlin
// 通过状态管理获取 PlatformSystemBarsController
val systemBars = rememberSystemBarsController()
```

::: warning

在使用 `rememberSystemBarsController` 时，如果原生的 `SystemBarsController` 未被初始化，它将会自动调用 `init` 进行初始化，
为了避免界面效果出现问题，你应该确保在每个平台的配置阶段就已经完成了初始化操作，否则你应该确保 `rememberSystemBarsController` 在所有内容开始绘制前进行调用。

:::

下面是 `PlatformSystemBarsController` 的详细用法介绍。

这里的大部分用法将与 [ui-component → 系统栏 (状态栏、导航栏等)](../library/ui-component.md#系统栏-状态栏、导航栏等) 中保持一致。

设置系统栏的行为。

这决定了显示或隐藏系统栏时由系统控制的行为。

> 示例如下

```kotlin
systemBars.behavior = PlatformSystemBarBehavior.Immersive
```

以下是 `PlatformSystemBarBehavior` 中提供的全部行为，标有 `*` 的为默认行为。

| 行为         |  平台   | 描述                                                                         |
| ------------ | :-----: | ---------------------------------------------------------------------------- |
| `Default`    |  全部   | 由系统控制的默认行为                                                         |
| *`Immersive` | Android | 在全屏时可由手势滑动弹出并显示为半透明的系统栏，并在一段时间后继续隐藏       |
|              |   iOS   | 当状态栏隐藏时，可以通过系统手势暂时显示系统栏，但一段时间后系统栏会继续隐藏 |

显示、隐藏系统栏。

> 示例如下

```kotlin
// 进入沉浸模式 (全屏模式)
// 同时隐藏状态栏和导航栏、Home Indicator
systemBars.hide(PlatformSystemBars.All)
// 单独控制状态栏和导航栏
systemBars.hide(PlatformSystemBars.StatusBars)
systemBars.hide(PlatformSystemBars.NavigationBars)
// 退出沉浸模式 (全屏模式)
// 同时显示状态栏和导航栏、Home Indicator
systemBars.show(PlatformSystemBars.All)
// 单独控制状态栏和导航栏、Home Indicator
systemBars.show(PlatformSystemBars.StatusBars)
systemBars.show(PlatformSystemBars.NavigationBars)
```

设置系统栏的样式。

你可以自定义状态栏、导航栏、Home Indicator 的外观。

::: warning

在 Android 6.0 以下系统中，状态栏的内容不支持反色，如果你设置了亮色则会自动处理为半透明遮罩，但是对于 MIUI、Flyme 自行添加了反色功能的系统将使用其私有方案实现反色效果。

在 Android 8 以下系统中，导航栏的内容不支持反色，处理方式同上。

在 iOS 中，Home Indicator 不支持设置 `darkContent`，它的颜色是由系统控制的，但是你可以为其设置背景颜色。

:::

> 示例如下

```kotlin
// 设置状态栏的样式
systemBars.statusBarStyle = PlatformSystemBarStyle(
    // 设置背景颜色
    color = Color.White,
    // 设置内容颜色
    darkContent = true
)
// 设置导航栏、Home Indicator 的样式
systemBars.navigationBarStyle = PlatformSystemBarStyle(
    // 设置背景颜色
    color = Color.White,
    // 设置内容颜色
    darkContent = true
)
// 你可以一次性设置状态栏和导航栏、Home Indicator 的样式
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
// 你也可以同时设置状态栏和导航栏、Home Indicator 的样式
systemBars.setStyle(
    style = PlatformSystemBarStyle(
        color = Color.White,
        darkContent = true
    )
)
```

以下是 `PlatformSystemBarStyle` 中提供的预置样式，标有 `*` 的为默认样式。

| 样式               | 描述                                                                         |
| ------------------ | ---------------------------------------------------------------------------- |
| `Auto`             | 系统深色模式下为纯黑背景 + 浅色内容颜色，浅色模式下为纯白背景 + 深色内容颜色 |
| *`AutoTransparent` | 系统深色模式下为浅色内容颜色，浅色模式下为深色内容颜色，背景透明             |
| `Light`            | 纯白背景 + 深色内容颜色                                                      |
| `LightScrim`       | 半透明纯白背景 + 深色内容颜色                                                |
| `LightTransparent` | 透明背景 + 深色内容颜色                                                      |
| `Dark`             | 纯黑背景 + 浅色内容颜色                                                      |
| `DarkScrim`        | 半透明纯黑背景 + 浅色内容颜色                                                |
| `DarkTransparent`  | 透明背景 + 浅色内容颜色                                                      |

获取原生控制器。

你可以通过以下方式在对应的平台中获取原生控制器，在 `commonMain` 或不支持的平台中使用将始终返回 `null`。

> 示例如下

```kotlin
// 获取原生控制器
val nativeController = systemBars.nativeController
```

::: tip

`BetterAndroid` 同样为 iOS 提供了一个原生的 `SystemBarsController`，
它的用法与 [ui-component → 系统栏 (状态栏、导航栏等)](../library/ui-component.md#系统栏-状态栏、导航栏等) 除了初始化功能外基本保持一致，通常情况下你应该不需要直接使用它，这里也不再进行详细的介绍。

:::