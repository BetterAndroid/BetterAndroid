# compose-extension

![Maven Central](https://img.shields.io/maven-central/v/com.highcapable.betterandroid/compose-extension?logo=apachemaven&logoColor=orange&style=flat-square)
<span style="margin-left: 5px"/>
![Maven metadata URL](https://img.shields.io/maven-metadata/v?metadataUrl=https%3A%2F%2Fraw.githubusercontent.com%2FHighCapable%2Fmaven-repository%2Frefs%2Fheads%2Fmain%2Frepository%2Freleases%2Fcom%2Fhighcapable%2Fbetterandroid%2Fcompose-extension%2Fmaven-metadata.xml&logo=apachemaven&logoColor=orange&label=highcapable-maven-releases&style=flat-square)
<span style="margin-left: 5px"/>
![Android Min SDK](https://img.shields.io/badge/Min%20SDK-21-orange?logo=android&style=flat-square)

这是针对 Jetpack Compose 相关功能扩展的一个依赖，同时支持多平台。

目前支持的平台：Android、iOS、Desktop (JVM)。

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

### Version Catalog

在你的项目 `gradle/libs.versions.toml` 中添加依赖。

```toml
[versions]
betterandroid-compose-extension = "<version>"

[libraries]
# commonMain
betterandroid-compose-extension = { module = "com.highcapable.betterandroid:compose-extension", version.ref = "betterandroid-compose-extension" }
# androidMain
betterandroid-compose-extension-android = { module = "com.highcapable.betterandroid:compose-extension-android", version.ref = "betterandroid-compose-extension" }
# iosArm64Main
betterandroid-compose-extension-iosarm64 = { module = "com.highcapable.betterandroid:compose-extension-iosarm64", version.ref = "betterandroid-compose-extension" }
# iosX64Main
betterandroid-compose-extension-iosx64 = { module = "com.highcapable.betterandroid:compose-extension-iosx64", version.ref = "betterandroid-compose-extension" }
# iosSimulatorArm64Main
betterandroid-compose-extension-iossimulatorarm64 = { module = "com.highcapable.betterandroid:compose-extension-iossimulatorarm64", version.ref = "betterandroid-compose-extension" }
# desktopMain
betterandroid-compose-extension-desktop = { module = "com.highcapable.betterandroid:compose-extension-desktop", version.ref = "betterandroid-compose-extension" }
```

在你的项目 `build.gradle.kts` 中配置依赖。

如果你在普通的项目中使用多平台依赖，你只需要按需部署对应平台后缀的依赖即可。

```kotlin
implementation(libs.betterandroid.compose.extension.android)
implementation(libs.betterandroid.compose.extension.iosarm64)
implementation(libs.betterandroid.compose.extension.iosx64)
implementation(libs.betterandroid.compose.extension.iossimulatorarm64)
implementation(libs.betterandroid.compose.extension.desktop)
```

如果你在多平台项目中使用多平台依赖，你需要在 `commonMain` 中添加 `compose-extension` 依赖。

```kotlin
kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.betterandroid.compose.extension)
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

> commonMain

[Color → isBrightColor](kdoc://compose-extension/compose-extension/com.highcapable.betterandroid.compose.extension.ui/is-bright-color)

[Color → toHexColor](kdoc://compose-extension/compose-extension/com.highcapable.betterandroid.compose.extension.ui/to-hex-color)

[Color → mixColorOf](kdoc://compose-extension/compose-extension/com.highcapable.betterandroid.compose.extension.ui/mix-color-of)

[Color → orNull](kdoc://compose-extension/compose-extension/com.highcapable.betterandroid.compose.extension.ui/or-null)

[Color → toComposeColor](kdoc://compose-extension/compose-extension/com.highcapable.betterandroid.compose.extension.ui/to-compose-color)

> desktopMain

[Color → toPlatformColor](kdoc://compose-extension/compose-extension/com.highcapable.betterandroid.compose.extension.ui/[desktop]to-platform-color)

> iosMain

[Color → toPlatformColor](kdoc://compose-extension/compose-extension/com.highcapable.betterandroid.compose.extension.ui/[ios]to-platform-color)

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

这个问题的起因出自 [这里](https://stackoverflow.com/questions/72514987/unexpected-border-in-composables-border-shows-even-if-border-width-is-zero)，
其中提到的将边框设置为透明颜色的方案并不友好，因为它依然进行了一次绘制操作。

于是 `BetterAndroid` 提供了 `borderOrElse` 扩展，它会在边框大小为 `0.dp` 时不再为组件添加边框。

> 示例如下

```kotlin
Box(
    modifier = Modifier
        .size(50.dp)
        // 在这里，边框的大小为 0.dp，所以组件将不会有边框
        .borderOrElse(0.dp, Color.Black, RectangleShape)
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

> commonMain

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
// 得到一个四边均为 0.dp 的 ComponentPadding
val padding = ComponentPadding.None
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

> commonMain

[Foundation → componentState](kdoc://compose-extension/compose-extension/com.highcapable.betterandroid.compose.extension.ui/component-state)

[Foundation → clickable](kdoc://compose-extension/compose-extension/com.highcapable.betterandroid.compose.extension.ui/clickable)

[Foundation → combinedClickable](kdoc://compose-extension/compose-extension/com.highcapable.betterandroid.compose.extension.ui/combined-clickable)

[Foundation → toggleable](kdoc://compose-extension/compose-extension/com.highcapable.betterandroid.compose.extension.ui/toggleable)

[Foundation → selectable](kdoc://compose-extension/compose-extension/com.highcapable.betterandroid.compose.extension.ui/selectable)

[Foundation → HapticFeedback](kdoc://compose-extension/compose-extension/com.highcapable.betterandroid.compose.extension.ui/-haptic-feedback)

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

在原生 Android 中，要实现触感反馈需要使用 `View.performHapticFeedback` 方法或在类似 `setOnLongClickListener` 的方法最后一位传入 `true`。

在 Jetpack Compose 中，你需要每次都引用 `LocalHapticFeedback`，然后使用 `performHapticFeedback` 方法，这看起来并不友好。

`BetterAndroid` 为此提供了 `HapticFeedback` 方法，你现在可以更加简单地使用以下方式来实现触感反馈。

> 示例如下

```kotlin
Box(
    modifier = Modifier.combinedClickable(
        onLongClick = HapticFeedback {
            // Do something.
        }
    )
) {
    Text("Long Click Me")
}
```

### 矢量图 (ImageVector) 扩展

::: tip 本节内容

> commonMain

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

> commonMain

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

### 自适应布局 (Adaptive Layout)

::: tip 本节内容

> commonMain

[AdaptiveLayout → AdaptiveRow](kdoc://compose-extension/compose-extension/com.highcapable.betterandroid.compose.extension.ui.layout/-adaptive-row)

[AdaptiveLayout → AdaptiveColumn](kdoc://compose-extension/compose-extension/com.highcapable.betterandroid.compose.extension.ui.layout/-adaptive-column)

自适应布局相关扩展。

:::

自适应布局提供了可在不知道子组件大小的情况下对每个子组件进行测量并水平或垂直以父布局的大小对其进行平均分配的解决方案。

例如，你可以使用以下方式对两个按钮进行水平排列，并以父组件的大小对它们的宽度进行平均分配。

> 示例如下

```kotlin
// 创建一个 AdaptiveRow
AdaptiveRow(
    modifier = Modifier.width(150.dp),
    // 你可以设置每个组件的间距
    spacingBetween = 10.dp
) {
    Button(onClick = { /* ... */ }) {
        Text("Button 1")
    }
    Button(onClick = { /* ... */ }) {
        Text("Button 2")
    }
}
```

此时你不需要对 `Button` 设置任何宽度，它们将会自动进行测量并平均分配。

同样地，你也可以使用 `AdaptiveColumn` 对组件进行垂直排列。

> 示例如下

```kotlin
// 创建一个 AdaptiveColumn
AdaptiveColumn(
    modifier = Modifier.height(150.dp),
    // 你可以设置每个组件的间距
    spacingBetween = 10.dp
) {
    Button(onClick = { /* ... */ }) {
        Text("Button 1")
    }
    Button(onClick = { /* ... */ }) {
        Text("Button 2")
    }
}
```

### Dialog、Popup 组件扩展

::: tip 本节内容

> commonMain

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