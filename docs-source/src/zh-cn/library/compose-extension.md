# compose-extension

![GitHub release](https://img.shields.io/github/v/release/BetterAndroid/BetterAndroid?display_name=release&logo=github&color=green&filter=compose-extension-*)
<span style="margin-left: 5px"/>
![Android Min SDK](https://img.shields.io/badge/Min%20SDK-21-orange?logo=android)

这是针对 Jetpack Compose 相关功能扩展的一个依赖，同时支持多平台。

目前支持的平台：Android、iOS、Desktop (JVM)。

::: warning

此模块尚在开发与测试，在 `1.0.0` 版本正式发布前 API 可能会发生变化，欢迎前往 [GitHub Issues](repo://issues) 向我们提出建议。

:::

## 配置依赖

你可以使用如下方式将此模块添加到你的项目中。

这是一个 Kotlin Multiplatform 依赖，你需要 `org.jetbrains.kotlin.multiplatform` 插件来应用相关依赖。

### SweetDependency (推荐)

在你的项目 `SweetDependency` 配置文件中添加依赖。

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

在你的项目 `build.gradle.kts` 中配置依赖。

如果你在普通的项目中使用多平台依赖，你只需要按需部署对应平台后缀的依赖即可。

```kotlin
implementation(com.highcapable.betterandroid.compose.extension.android)
implementation(com.highcapable.betterandroid.compose.extension.iosarm64)
implementation(com.highcapable.betterandroid.compose.extension.iosx64)
implementation(com.highcapable.betterandroid.compose.extension.iossimulatorarm64)
implementation(com.highcapable.betterandroid.compose.extension.desktop)
```

如果你在多平台项目中使用多平台依赖，你需要在 `commonMain` 中添加 `compose-extension` 依赖。

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

### 传统方式

在你的项目 `build.gradle.kts` 中配置依赖。

如果你在普通的项目中使用多平台依赖，你只需要按需部署对应平台后缀的依赖即可。

```kotlin
implementation("com.highcapable.betterandroid:compose-extension-android:<version>")
implementation("com.highcapable.betterandroid:compose-extension-iosarm64:<version>")
implementation("com.highcapable.betterandroid:compose-extension-iosx64:<version>")
implementation("com.highcapable.betterandroid:compose-extension-iossimulatorarm64:<version>")
implementation("com.highcapable.betterandroid:compose-extension-desktop:<version>")
```

如果你在多平台项目中使用多平台依赖，你需要在 `commonMain` 中添加 `compose-extension` 依赖。

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

请将 `<version>` 修改为此文档顶部显示的版本。

## 功能介绍

你可以 [点击这里](kdoc://compose-extension) 查看 KDoc。

### 颜色 (Color) 扩展

::: tip 本节内容

[Color → isBrightColor](kdoc://compose-extension/compose-extension/com.highcapable.betterandroid.compose.extension.ui/is-bright-color)

[Color → toHexColor](kdoc://compose-extension/compose-extension/com.highcapable.betterandroid.compose.extension.ui/to-hex-color)

[Color → mixColorOf](kdoc://compose-extension/compose-extension/com.highcapable.betterandroid.compose.extension.ui/mix-color-of)

[Color → orNull](kdoc://compose-extension/compose-extension/com.highcapable.betterandroid.compose.extension.ui/or-null)

[Color → toPlatformColor (desktop)](kdoc://compose-extension/compose-extension/com.highcapable.betterandroid.compose.extension.ui/[desktop]to-platform-color)

[Color → toPlatformColor (ios)](kdoc://compose-extension/compose-extension/com.highcapable.betterandroid.compose.extension.ui/[ios]to-platform-color)

[Color → toComposeColor](kdoc://compose-extension/compose-extension/com.highcapable.betterandroid.compose.extension.ui/to-compose-color)

适用于 `Color` 的扩展。

:::

Jetpack Compose 中的 `Color` 封装了颜色相关的实现，它解决了原生 Android 中使用 `Integer` 类型传递颜色的问题。

`BetterAndroid` 同样为 `Color` 提供了与在 [ui-extension → 颜色 (Color) 扩展](../library/ui-extension.md#颜色-color-扩展) 中类似的扩展，使其更加易用。

由于 `Color` 提供了 `alpha` 属性，所以原生中提供的 `toAlphaColor` 扩展在 `Color` 中已不再需要。

下面是一些对颜色扩展的相关示例用法。

判断颜色的明亮程度。

这在你需要根据颜色的明亮程度来决定是否使用深色文字时非常有用。

> 示例如下

```kotlin
// 假设我们有以下颜色
val color = Color.White
// 要判断其明亮程度，你只需要使用以下方法
// 你肯定会得到一个 true，因为这是一个白色
val isBright = color.isBrightColor
```

将颜色转换为 HEX 字符串。

> 示例如下

```kotlin
// 假设我们有以下颜色
val color = Color.White
// 要将其转换为 HEX 字符串，你只需要使用以下方法
// 将会得到一个包含透明度的 "#FFFFFFFF"
val hexString = color.toHexColor()
```

混合两个颜色。

> 示例如下

```kotlin
// 假设我们有以下颜色
val color1 = Color.White
val color2 = Color.Black
// 你可以使用以下方法非常简单地将它们进行混合
val mixColor = mixColorOf(color1, color2)
// 你还可以设置混合的比率，默认为 0.5f
val mixColor = mixColorOf(color1, color2, 0.2f)
```

判断 `Color` 是否未定义。

在 `Color` 为 `Color.Unspecified` 时，你可以使用以下方式得到一个可在此状态下为 `null` 的对象。

> 示例如下

```kotlin
// 假设我们有以下颜色
val color = Color.Unspecified
// 在此状态下将会返回 null，然后你就可以使用 ?: 传递在未定义情况下的默认值
val myColor = color.orNull() ?: Color.White
```

在非 Android 平台中，你可以通过以下方式将对应的 `Color` 对象转换为平台的颜色对象。

> 示例如下

```kotlin
// 假设我们有以下颜色
val color = Color.White
// 例如，你可以将其转换为 JavaFX 中的颜色对象
val awtColor = color.toPlatformColor()
// 在 iOS 中，你可以将其转换为 UIColor 对象
val uiColor = color.toPlatformColor()
```

::: tip

在 Android 平台中，你可以使用 `Color.toArgb()` 方法将 `Color` 对象转换为 `Integer` 类型。

:::

同样地，你可以通过以下方式将平台的颜色对象转换为 Compose 的 `Color` 对象。

> 示例如下

```kotlin
// 假设这是 JavaFX 中的颜色对象
val awtColor = Color.WHITE
// 假设这是 iOS 中的颜色对象
val uiColor = UIColor.whiteColor()
// 将其转换为 Compose 的 Color 对象
val composeColor = awtColor.toComposeColor()
val composeColor = uiColor.toComposeColor()
```

::: tip

在 Android 平台中，你可以使用类似 `Color(0xFFFFFFFF)` 的形式将 `Long` 类型的颜色转换为 `Color` 对象。

:::

### 边框 (Border) 扩展

::: tip 本节内容

[Border → borderOrElse](kdoc://compose-extension/compose-extension/com.highcapable.betterandroid.compose.extension.ui/border-or-else)

[BorderStroke → solidColor](kdoc://compose-extension/compose-extension/com.highcapable.betterandroid.compose.extension.ui/solid-color)

适用于 `Border`、`BorderStroke` 的扩展。

:::

你可以通过 `Modifier.border(...)` 方法为组件添加边框，但是当边框大小为 `0.dp` 时，边框依然会存在。

为了解决这个问题，`BetterAndroid` 提供了 `borderOrElse` 扩展，它会在边框大小为 `0.dp` 时不再为组件添加边框。

> 示例如下

```kotlin
Box(
    modifier = Modifier
        .size(50.dp)
        // 在这里，边框的大小为 0.dp，所以组件将不会有边框
        .borderOrElse(0.dp, RectangleShape)
) {
    // 一些内容
}
```

从一个已存在的 `BorderStroke` 对象中获取 `brush` (`SolidColor`) 中的颜色。

`SolidColor` 是 `Brush` 的一种，它可以用于填充颜色，一个通过 `BorderStroke(10.dp, Color.White)` 创建的边框将无法方便地从 `brush` 中获取颜色。

`BetterAndroid` 为此提供了扩展，现在你可以使用以下方式获取 `brush` 中的颜色。

> 示例如下

```kotlin
// 假设这就是你的 BorderStroke 对象
val border = BorderStroke(10.dp, Color.White)
// 在确定 brush 是 SolidColor 的情况下，你可以使用以下方式获取其中的颜色
// 如果无法获取，将返回 Color.Unspecified
val color = border.solidColor
// 你也可以在获取不到的时候设置其默认值
val color = border.solidColor(Color.Black)
```

### 内边距 (Padding) 扩展

::: tip 本节内容

[ComponentPadding](kdoc://compose-extension/compose-extension/com.highcapable.betterandroid.compose.extension.ui/-component-padding)

组件化 `padding`，继承于 `PaddingValues`。

:::

`PaddingValues` 是 Jetpack Compose 中提供的 `padding` 用法，它可以直接设置到 `Modifier.padding(...)` 中。

但是 `PaddingValues` 并没有提供 `copy` 等功能，一旦设置就不可修改，非常的不方便。

于是 `BetterAndroid` 继承于 `PaddingValues` 重新写了一个 `ComponentPadding`，使其更加好用。

在用法上，`ComponentPadding` 与 `PaddingValues` 完全相同，你还能够将其直接将其设置到 `Modifier.padding(...)` 中。

> 示例如下

```kotlin
// 创建一个 ComponentPadding
val padding = ComponentPadding(10.dp)
// 设置每个方向的 padding
val padding = ComponentPadding(
    start = 15.dp,
    top = 10.dp,
    bottom = 12.dp,
    end = 16.dp
)
// 设置水平、垂直方向的 padding
val padding = ComponentPadding(
    horizontal = 10.dp,
    vertical = 15.dp
)
// 通过 copy 方式创建一个新的 ComponentPadding
val paddingCopy = padding.copy(top = 15.dp)
// 设置到组件上
Box(
    modifier = Modifier.padding(padding)
) {
    // 一些内容
}
```

`BetterAndroid` 同时将 `calculateLeftPadding`、`calculateRightPadding` 封装为了 Composeable 方法，你无需再使用 `LayoutDirection` 来计算 LTR、RTL 方向的 `padding`。

> 示例如下

```kotlin
@Composable
fun MyComponent() {
    // 假设这就是你的 ComponentPadding
    val padding: ComponentPadding
    // 获取 left
    val left = padding.left
    // 获取 right
    val right = padding.right
}
```

你还能够使用以下方式将其转换为 `PaddingValues`。

> 示例如下

```kotlin
// 假设这就是你的 ComponentPadding
val padding: ComponentPadding
// 转换为 PaddingValues
val paddingValues = padding.toPaddingValues()
```

### 基础 (Foundation) 扩展

::: tip 本节内容

[Foundation → componentState](kdoc://compose-extension/compose-extension/com.highcapable.betterandroid.compose.extension.ui/component-state)

[Foundation → clickable](kdoc://compose-extension/compose-extension/com.highcapable.betterandroid.compose.extension.ui/clickable)

[Foundation → combinedClickable](kdoc://compose-extension/compose-extension/com.highcapable.betterandroid.compose.extension.ui/combined-clickable)

[Foundation → toggleable](kdoc://compose-extension/compose-extension/com.highcapable.betterandroid.compose.extension.ui/toggleable)

[Foundation → selectable](kdoc://compose-extension/compose-extension/com.highcapable.betterandroid.compose.extension.ui/selectable)

基础功能相关扩展。

:::

在 Jetpack Compose 的组件中缺少一种 “禁用” 的状态，`BetterAndroid` 提供了以下方式来通过调整组件的透明度以实现这个效果。

它的实际作用其实就是一个视觉上的启用或禁用的透明度效果，并不会对组件设置任何状态。

> 示例如下

```kotlin
Box(
    modifier = Modifier
        .size(50.dp)
        // 通过 componentState 来设置组件的状态
        // 你还可以设置 disabledAlpha 来调整禁用状态下的组件透明度
        .componentState(enabled = false)
) {
    // 一些内容
}
```

在原始的 `clickable`、`combinedClickable`、`toggleable`、`selectable` 中，你需要手动去为其设置默认值，如果仅在一个简单的场景去使用这些功能，那么会显得更加繁琐。

`BetterAndroid` 为此提供了以上方法的同名扩展，现在你能更方便地使用这些方法而不必考虑使用 `remember` 等方法为其设置状态。

> 示例如下

```kotlin
Box(
    modifier = Modifier
        .size(50.dp)
        // 通过 clickable 来设置组件的点击事件
        .clickable {
            // 处理点击事件
        }
) {
    // 一些内容
}
```

::: warning

你需要将这些方法引用的包名由 `androidx.compose.foundation` 更换为 `com.highcapable.betterandroid.compose.extension.ui`。

:::

### 矢量图 (ImageVector) 扩展

::: tip 本节内容

[ImageVector → ImageVector](kdoc://compose-extension/compose-extension/com.highcapable.betterandroid.compose.extension.ui/-image-vector)

适用于 `ImageVector` 的扩展。

:::

Jetpack Compose 原生提供了一个能够创建矢量图的 `ImageVector`，但是你要使用 `ImageVector.Builder` 来创建它。

这样的方式看起来不是很友好，于是 `BetterAndroid` 为此提供了一个 `ImageVector` 的同名方法。

> 示例如下

```kotlin
// 通过 ImageVector 创建一个矢量图
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

### 单位 (Unit) 扩展

::: tip 本节内容

[Unit → orNull](kdoc://compose-extension/compose-extension/com.highcapable.betterandroid.compose.extension.ui/or-null)

适用于 `Unit` 的扩展。

:::

在所有能够使用 `isSpecified` 进行判断的单位中，Jetpack Compose 都提供了 `takeOrElse` 方法，但是它并不简洁且不容易理解。

于是 `BetterAndroid` 为此提供了 `orNull` 方法，你可以使用它来得到一个可在此状态下为 `null` 的对象。

下面是使用 `takeOrElse` 与 `orNull` 的对比。

> 示例如下

```kotlin
// 假设我们有以下单位
val dp: Dp
val sp: TextUnit
// 使用 takeOrElse
val dpValue = dp.takeOrElse { 10.dp }
val spValue = sp.takeOrElse { 10.sp }
// 使用 orNull
val dpValue = dp.orNull() ?: 10.dp
val spValue = sp.orNull() ?: 10.sp
```

你会发现 `orNull` 的写法更加简洁，而且更容易理解。

### Dialog、Popup 组件扩展

::: tip 本节内容

[DialogPropertiesWrapper](kdoc://compose-extension/compose-extension/com.highcapable.betterandroid.compose.extension.ui.window/-dialog-properties-wrapper)

Android 平台中 `DialogProperties` 的包装类。

[DialogPropertiesWrapper.AndroidProperties](kdoc://compose-extension/compose-extension/com.highcapable.betterandroid.compose.extension.ui.window/-dialog-properties-wrapper/-android-properties)

Android 平台中 `DialogProperties` 中仅限于 Android 平台的属性。

[SecureFlagPolicyWrapper](kdoc://compose-extension/compose-extension/com.highcapable.betterandroid.compose.extension.ui.window/-secure-flag-policy-wrapper)

Android 平台中 `SecureFlagPolicy` 的包装类。

[Dialog → Dialog](kdoc://compose-extension/compose-extension/com.highcapable.betterandroid.compose.extension.ui.window/-dialog)

适用于 `Dialog` 的扩展。

[Popup → Popup](kdoc://compose-extension/compose-extension/com.highcapable.betterandroid.compose.extension.ui.window/-popup)

适用于 `Popup` 的扩展。

:::

本节中介绍的扩展功能可用于在 Kotlin Multiplatform 中使用。

在创建 `Dialog`、`Popup` 时，`commonMain` 中并没有很好地处理 Android 平台中的特殊功能。

`BetterAndroid` 为此提供了与 Android 平台相关的功能，你可以在 `commonMain` 中开箱即用，不需要单独为 Android 进行适配。

`DialogPropertiesWrapper` 镜像了全部来自 `DialogProperties` 的属性，并将 Android 平台特殊的属性封装为了 `DialogPropertiesWrapper.AndroidProperties`。

在 `BetterAndroid` 为你提供的 `Dialog` 方法中，你可以直接传入 `DialogPropertiesWrapper` 对象。

> 示例如下

```kotlin
// 创建 Dialog
Dialog(
    onDismissRequest = {
        // 处理相关事件
    },
    properties = DialogPropertiesWrapper(
        dismissOnBackPress = true,
        dismissOnClickOutside = true,
        usePlatformDefaultWidth = true,
        usePlatformInsets = true,
        scrimColor = Color.Black.copy(alpha = 0.6f),
        // 为 Android 平台设置特殊属性
        androidProperties = DialogPropertiesWrapper.AndroidProperties(
            secureFlagPolicy = SecureFlagPolicyWrapper.Inherit,
            decorFitsSystemWindows = true
        )
    )
) {
    // 一些内容
}
```

::: warning

目前 `DialogPropertiesWrapper` 中的 `scrimColor` 在 Android 平台只有 `alpha` 能够生效。

:::

对于 `Popup`，Android 平台不存在 `onPreviewKeyEvent`、`onKeyEvent` 参数，这会导致 `commonMain` 分发到 Android 时编译失败。

为了修复这个问题，`BetterAndroid` 为 Android 平台模拟了 `onPreviewKeyEvent`、`onKeyEvent`，现在你无需考虑兼容性问题。

> 示例如下

```kotlin
// 创建 Popup
Popup(
    onDismissRequest = {
        // 处理相关事件
    },
    properties = PopupProperties(),
    onPreviewKeyEvent = {
        // 处理相关事件
        false
    },
    onKeyEvent = {
        // 处理相关事件
        false
    }
) {
    // 一些内容
}
```

::: warning

你需要将上述 `Dialog`、`Popup` 引用的包名由 `androidx.compose.ui.window` 更换为 `com.highcapable.betterandroid.compose.extension.ui.window`。

:::

## 多平台支持

为了能让适应了原生 Android 开发的开发者们能够更快地对各种平台进行适配，借助于 Kotlin Multiplatform 与 Jetpack Compose 的跨平台特性，`BetterAndroid` 无缝地提供了多平台支持。

对于不同平台差异性的扩展支持虽然同样属于 [compose-extension](#compose-extension)，但是它们将独立于 [功能介绍](#功能介绍) 中的内容单独进行介绍。

多平台支持的相关功能将继续在 [compose-extension](#compose-extension) 中开发，以下是目前正在开发、开发完成的功能。

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

### 系统事件

::: tip 本节内容

[BackHandler → BackHandler](kdoc://compose-extension/compose-extension/com.highcapable.betterandroid.compose.extension.platform.backpress/-back-handler)

适用于系统返回事件的扩展。

:::

虽然 `androidx.activity:activity-compose` 提供了 `BackHandler`，但是它只能在 Android 中使用。

`BetterAndroid` 提供了 `BackHandler` 的多平台分发支持，你可以直接在 `commonMain` 中使用它，但是它仅会在 Android 平台中生效。

`BackHandler` 在 Android 中使用 [ui-component → 系统事件](../library/ui-component.md#系统事件) 中的 `BackPressedController` 实现，
你可以参考 [ui-component → Activity](../library/ui-component.md#activity) 使用 `AppComponentActivty` 作为 Compose 的绘制对象以获得更好的体验。

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

[PlatformSystemBarsController](kdoc://compose-extension/compose-extension/com.highcapable.betterandroid.compose.extension.platform.systembar/-platform-system-bars-controller)

Android、iOS 的系统栏控制器。

[NativeSystemBarsController](kdoc://compose-extension/compose-extension/com.highcapable.betterandroid.compose.extension.platform.systembar/-native-system-bars-controller)

Android、iOS 对应的原生系统栏控制器。

[PlatformSystemBarStyle](kdoc://compose-extension/compose-extension/com.highcapable.betterandroid.compose.extension.platform.systembar/-platform-system-bar-style)

系统栏的样式。

[PlatformSystemBars](kdoc://compose-extension/compose-extension/com.highcapable.betterandroid.compose.extension.platform.systembar/-platform-system-bars)

系统栏的类型。

[PlatformSystemBarBehavior](kdoc://compose-extension/compose-extension/com.highcapable.betterandroid.compose.extension.platform.systembar/-platform-system-bar-behavior)

系统栏的行为。

[SystemBars → rememberSystemBarsController](kdoc://compose-extension/compose-extension/com.highcapable.betterandroid.compose.extension.platform.systembar/remember-system-bars-controller)

[SystemBars → nativeController](kdoc://compose-extension/compose-extension/com.highcapable.betterandroid.compose.extension.platform.systembar/native-controller)

适用于系统栏的扩展。

> iOS 平台特殊组件

[SystemBarsController](kdoc://compose-extension/compose-extension/com.highcapable.betterandroid.compose.extension.platform.component.systembar/-system-bars-controller)

系统栏控制器。

[SystemBarStyle](kdoc://compose-extension/compose-extension/com.highcapable.betterandroid.compose.extension.platform.component.systembar.style/-system-bar-style)

系统栏的样式。

[SystemBars](kdoc://compose-extension/compose-extension/com.highcapable.betterandroid.compose.extension.platform.component.systembar.type/-system-bars)

系统栏的类型。

[SystemBarBehavior](kdoc://compose-extension/compose-extension/com.highcapable.betterandroid.compose.extension.platform.component.systembar.type/-system-bar-behavior)

系统栏的行为。

[AppComponentUIViewController](kdoc://compose-extension/compose-extension/com.highcapable.betterandroid.compose.extension.platform.component.uiviewcontroller/-app-component-u-i-view-controller)

实现了系统栏控制器的 `UIViewController`。

[UIViewController → AppComponentUIViewController](kdoc://compose-extension/compose-extension/com.highcapable.betterandroid.compose.extension.platform.uiviewcontroller/-app-component-u-i-view-controller)

适用于创建 Compose 绘制入口的 `UIViewController` 扩展。

:::

在 Android、iOS 中，你都需要对系统提供的状态栏、导航栏 (Home Indicator) 进行控制和调整以便给用户带来更好的体验。

为此 `BetterAndroid` 为你提供了 `PlatformSystemBarsController`，它能够通过通用 API 来轻松实现对每个平台系统栏的控制。

`PlatformSystemBarsController` 的工作原理是通过 `NativeSystemBarsController` 根据平台的不同而分发到对应的原生控制器。

在首次使用时，你需要对 `PlatformSystemBarsController` 进行平台特殊化配置。

**Android 平台**

**1. 自动配置**

请参考 [ui-component → Activity](../library/ui-component.md#activity) 使用 `AppComponentActivty` 作为 Compose 的绘制对象即可。

**2. 手动配置**

首先你需要在对应的 Android 项目中导入 [ui-component](../library/ui-component.md) 模块，然后你需要使用 `ComponentActivity` 作为你的 `Activity`，并对其实现 `ISystemBarsController` 接口。

> 示例如下

```kotlin
class MainActivity : ComponentActivity(), ISystemBarsController {

    override val systemBars by lazy { SystemBarsController.from(window) }
}
```

上述步骤完成后，你可以参考 [ui-component → 系统栏 (状态栏、导航栏等)](../library/ui-component.md#系统栏-状态栏、导航栏等) 中的初始化方式手动对其进行初始化。

`PlatformSystemBarsController` 将会自动识别使用了 `ISystemBarsController` 接口的 `Activity`。

**3. 不进行配置**

你可以不进行任何配置直接使用 `PlatformSystemBarsController`，它会在被调用时等待 Composition 结束后为当前 `Activity` 创建并初始化新的 `SystemBarsController`。

::: warning

这种行为是实验性的且不可控，它可能会影响 Compose 的生命周期，我们不建议你使用这种方式。

:::

**iOS 平台**

在 iOS 项目中，你必须手动对 Compose 的绘制入口进行配置。

首先，请在 `iosMain` 中的 `App.ios.kt` 文件中使用以下方式创建一个 `AppComponentUIViewController`。

> 示例如下

```kotlin
fun createUIViewController() = AppComponentUIViewController {
    // 你的 Compose 绘制入口
}
```

然后，请使用 UIKit 创建 iOS 项目并对 `AppDelegate.swift` 文件进行以下配置。

> 示例如下

```swift
import UIKit
import shared // 这里为你的 Kotlin Multiplatform 共享模块名称

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate {

    var window: UIWindow?

    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions:
        [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        // 创建一个新的 UIWindow
        window = UIWindow(frame: UIScreen.main.bounds)
        // 设置根视图的控制器
        window?.rootViewController = App_iosKt.createUIViewController()
        // 使其可见
        window?.makeKeyAndVisible()
        return true
    }
}
```

::: warning

不要使用 `UIViewControllerRepresentable` 为 Swift UI 创建 `UIViewController`，这会导致系统栏控制器相关功能失效，
因为 Swift UI 会接管整个视图的状态，此时，你只能在 Swift UI 中控制系统栏。

:::

::: tip

如果你觉得上述配置过程过于繁琐，你可以参考 [快速开始 → 项目模版](../guide/quick-start.md#项目模版) 来创建项目。

:::

上述配置完成后，要在 Compose 中使用 `PlatformSystemBarsController`，你可以非常方便地使用以下方式来创建、获取它。

> 示例如下

```kotlin
// 通过状态管理获取 PlatformSystemBarsController
val systemBars = rememberSystemBarsController()
```

::: warning

在使用 `rememberSystemBarsController` 时，如果原生的 `SystemBarsController` 未被初始化，它将会自动调用 `init` 进行初始化，
为了避免界面效果出现问题，你应该确保在平台特殊化配置阶段就已经完成了初始化操作，否则你应该确保 `rememberSystemBarsController` 在所有内容开始绘制前进行调用。

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