# compose-extension

![Maven Central](https://img.shields.io/maven-central/v/com.highcapable.betterandroid/compose-extension?logo=apachemaven&logoColor=orange&color=green)
<span style="margin-left: 5px"/>
![Android Min SDK](https://img.shields.io/badge/Min%20SDK-21-orange?logo=android)

This is a dependency for Jetpack Compose related functional extensions and supports multi-platform.

Currently supported platforms: Android, iOS, Desktop (JVM).

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

> commonMain

[Border → borderOrElse](kdoc://compose-extension/compose-extension/com.highcapable.betterandroid.compose.extension.ui/border-or-else)

[BorderStroke → solidColor](kdoc://compose-extension/compose-extension/com.highcapable.betterandroid.compose.extension.ui/solid-color)

Extensions for `Border` and `BorderStroke`.

:::

You can add a border to a component through the `Modifier.border(...)` method, but when the border size is `0.dp`, the border will still exist.

The origin of this question comes from [here](https://stackoverflow.com/questions/72514987/unexpected-border-in-composables-border-shows-even-if-border-width-is-zero),
the solution mentioned in setting the border to a transparent color is not friendly because it still performs a drawing operation.

So `BetterAndroid` provides the `borderOrElse` extension, which will no longer add borders to components when the border size is `0.dp`.

> The following example

```kotlin
Box(
    modifier = Modifier
        .size(50.dp)
        // Here, the size of the border is 0.dp, so the component will not have a border.
        .borderOrElse(0.dp, Color.Black, RectangleShape)
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

> commonMain

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
// Create a 0.dp ComponentPadding of all directions.
val padding = ComponentPadding.None
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

> commonMain

[Foundation → componentState](kdoc://compose-extension/compose-extension/com.highcapable.betterandroid.compose.extension.ui/component-state)

[Foundation → clickable](kdoc://compose-extension/compose-extension/com.highcapable.betterandroid.compose.extension.ui/clickable)

[Foundation → combinedClickable](kdoc://compose-extension/compose-extension/com.highcapable.betterandroid.compose.extension.ui/combined-clickable)

[Foundation → toggleable](kdoc://compose-extension/compose-extension/com.highcapable.betterandroid.compose.extension.ui/toggleable)

[Foundation → selectable](kdoc://compose-extension/compose-extension/com.highcapable.betterandroid.compose.extension.ui/selectable)

[Foundation → HapticFeedback](kdoc://compose-extension/compose-extension/com.highcapable.betterandroid.compose.extension.ui/-haptic-feedback)

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

In native Android, to implement haptic feedback, you need to use the `View.performHapticFeedback` method or pass `true` in the last bit of a method like `setOnLongClickListener`.

In Jetpack Compose, you need to reference `LocalHapticFeedback` every time and then use the `performHapticFeedback` method, which seems unfriendly.

`BetterAndroid` provides the `HapticFeedback` method for this purpose, you can now use the following methods to implement haptic feedback more simply.

> The following example

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

### ImageVector Extension

::: tip Contents of This Section

> commonMain

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

> commonMain

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

### Adaptive Layout

::: tip Contents of This Section

> commonMain

[AdaptiveLayout → AdaptiveRow](kdoc://compose-extension/compose-extension/com.highcapable.betterandroid.compose.extension.ui.layout/-adaptive-row)

[AdaptiveLayout → AdaptiveColumn](kdoc://compose-extension/compose-extension/com.highcapable.betterandroid.compose.extension.ui.layout/-adaptive-column)

Extensions for adaptive layout.

:::

Adaptive layout provides a solution to measure each child component and distribute it evenly with the size of the parent layout horizontally or vertically without knowing the size of the child component.

For example, you can use the following method to arrange two buttons horizontally and evenly distribute their widths based on the size of the parent component.

> The following example

```kotlin
// Create an AdaptiveRow.
AdaptiveRow(
    modifier = Modifier.width(150.dp),
    // You can set the spacing of each component.
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

You don't need to set any width for the `Button` at this point, they will be automatically measured and evenly distributed.

Likewise, you can use `AdaptiveColumn` to arrange components vertically.

> The following example

```kotlin
// Create an AdaptiveColumn.
AdaptiveColumn(
    modifier = Modifier.height(150.dp),
    // You can set the spacing of each component.
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

### Dialog, Popup Component Extension

::: tip Contents of This Section

> commonMain

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