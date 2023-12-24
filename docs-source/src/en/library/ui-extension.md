# ui-extension

![GitHub release](https://img.shields.io/github/v/release/BetterAndroid/BetterAndroid?display_name=release&logo=github&color=green&filter=ui-extension-*)
<span style="margin-left: 5px"/>
![Android Min SDK](https://img.shields.io/badge/Min%20SDK-21-orange?logo=android)

This is a dependency for UI (user interface) related extensions.

## Configure Dependency

You can add this module to your project using the following method.

### SweetDependency (Recommended)

Add dependency in your project's `SweetDependency` configuration file.

```yaml
libraries:
  com.highcapable.betterandroid:
    ui-extension:
      version: +
```

Configure dependency in your project `build.gradle.kts`.

```kotlin
implementation(com.highcapable.betterandroid.ui.extension)
```

### Traditional Method

Configure dependency in your project `build.gradle.kts`.

```kotlin
implementation("com.highcapable.betterandroid:ui-extension:<version>")
```

Please change `<version>` to the version displayed at the top of this document.

## Function Introduction

You can view the KDoc [click here](kdoc://ui-extension).

### Activity Extension

::: tip Contents of This Section

[ActivityFactory → startActivity](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component/start-activity)

[ActivityFactory → startActivityOrElse](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component/start-activity-or-else)

[ActivityFactory → isInMultiWindowModeCompat](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component/is-in-multi-window-mode-compat)

Extensions for `Activity`.

:::

When we need to start another `Activity`, we need to use `Intent` to create an `Intent(this, AnotherActivity::class.java)`, and then call `startActivity(intent)` to start it.

This may not be very friendly to write, so `BetterAndroid` provides an extension for `Activity`, now you can directly use the following method to start another `Activity`.

> The following example

```kotlin
// Assume this is your context.
val context: Context
// Assume AnotherActivity is your target activity.
context.startActivity<AnotherActivity>()
// You can create an intent object using the following method.
context.startActivity<AnotherActivity> {
    // Add some extra parameters here.
    putExtra("key", "value")
}
// If you need to use Intent.FLAG_ACTIVITY_NEW_TASK to start another activity,
// you can use it directly like this.
context.startActivity<AnotherActivity>(newTask = true)
```

If you need to start an `Activity` from an external app, you can use the following method.

> The following example

```kotlin
// Assume this is your context.
val context: Context
// Assume that the app package you need to start is named com.example.app.
// Assume that the activity class you need to start is named com.example.app.MainActivity.
context.startActivity("com.example.app", "com.example.app.MainActivity")
// You can still create an intent object using the following method.
context.startActivity("com.example.app", "com.example.app.MainActivity") {
    // Add some extra parameters here.
    putExtra("key", "value")
}
// Intent.FLAG_ACTIVITY_NEW_TASK will be set by default in this method,
// this is to avoid stack overlap after starting a new app.
// If you don't need to consider this issue, you can set the newTask parameter to false.
context.startActivity("com.example.app", "com.example.app.MainActivity", newTask = false)
```

If you don't know the entry `Activity` name of the app you want to start, you can use the package name to start it directly.

This method will use `getLaunchIntentForPackage` internally to obtain the entry `Activity`.

> The following example

```kotlin
// Assume this is your context.
val context: Context
// Assume that the app package you need to start is named com.example.app.
context.startActivity("com.example.app")
```

::: warning

If the app you need to start does not have an entry `Activity`, or the app you need to start is not installed, an exception will be thrown.

At this time you can use `startActivityOrElse` to determine whether it can be started successfully, if it fails, this method returns `false`.

This operation requires the `QUERY_ALL_PACKAGES` permission or explicitly configuring a `queries` property list on Android 11 and later.

Please refer to [Package visibility filtering on Android](https://developer.android.com/training/package-visibility).

:::

For the new multi-window mode in Android 7.0 and later versions, `BetterAndroid` provides a compatible extension for it.

For `isInMultiWindowMode`, you do not need to consider version compatibility issues, you only need to add a `Compat` at the end.

> The following example

```kotlin
// Assume this is your activity.
val activity: Activity
// Get whether it is currently in multi-window mode.
val isInMultiWindowMode = activity.isInMultiWindowModeCompat
```

### Fragment Extension

::: tip Contents of This Section

[FragmentFactory → commitTransaction](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component/commit-transaction)

[FragmentFactory → findFragment](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component/find-fragment)

[FragmentFactory → attachToActivity](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component/attach-to-activity)

[FragmentFactory → attachToFragment](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component/attach-to-fragment)

[FragmentFactory → detachFromActivity](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component/detach-from-activity)

[FragmentFactory → detachFromFragment](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component/detach-from-fragment)

[FragmentFactory → replaceFromActivity](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component/replace-from-activity)

[FragmentFactory → replaceFromFragment](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component/replace-from-fragment)

[FragmentFactory → hide](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component/hide)

[FragmentFactory → show](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component/show)

Extensions for `Fragment`.

:::

`Fragment` is an officially provided efficient fragment for `Activity`, but its usage is not very user-friendly.

In order to simplify `Fragment` related operations, `BetterAndroid` provides some practical extension functions for `Fragment`.

You can use `commitTransaction` to complete the operation from `beginTransaction` to `commit`.

This method helps you perform the operations from `beginTransaction` to `commit` or `commitAllowingStateLoss`, you only need to add the transactions therein.

> The following example

```kotlin
// Assume this is your FragmentActivity.
val activity: FragmentActivity
// Submit a transaction.
actvity.supportFragmentManager.commitTransaction {
    // Add some transaction operations here.
    // For example, add a fragment.
    add(R.id.container, YourFragment())
}
// You can set the allowingStateLoss parameter to decide whether to allow state loss.
// Defaults to true if not set.
actvity.supportFragmentManager.commitTransaction(allowingStateLoss = true) {
    // ...
    add(R.id.container, YourFragment())
}
```

Use generics to find an existing `Fragment`.

It can automatically help you convert the found `Fragment` to the current type without using the `as` form for coercion.

You don't need to worry about not being found or type errors at all, in which case `null` will be returned.

> The following example

```kotlin
// Assume this is your FragmentActivity.
val activity: FragmentActivity
// Find a fragment by ID.
val fragment = activity.supportFragmentManager.findFragment<YourFragment>(R.id.container)
// Find a fragment by TAG.
val fragment = activity.supportFragmentManager.findFragment<YourFragment>("your_fragment_tag")
```

Bind `Fragment` to `FragmentActivity`.

You don't need to use something like `FragmentManger.beginTransaction`...`commit`, it will be easier to complete this operation now.

> The following example

```kotlin
// Assume this is your FragmentActivity.
val activity: FragmentActivity
// Assume this is your fragment.
val fragment = YourFragment()
// Bind fragment.
fragment.attachToActivity(activity)
```

Yes, you have completed all binding operations, by default, it will bind `Fragment` to the layout set by `Activity` through `setContentView`.

If you need to bind it to a custom layout, you can use the following method.

> The following example

```kotlin
// Assume this is your FragmentActivity.
val activity: FragmentActivity
// Assume this is your fragment.
val fragment = YourFragment()
// Bind fragment to the layout with ID R.id.container.
fragment.attachToActivity(activity, R.id.container)
```

If this custom layout is not obtained by ID, but is a `View` object, you can use the following method.

> The following example

```kotlin
// Assume this is your FragmentActivity.
val activity: FragmentActivity
// Assume this is your view.
val container: View
// Assume this is your fragment.
val fragment = YourFragment()
// Bind fragment to container. (this View must have an ID)
fragment.attachToActivity(activity, view = container)
```

You can also easily set the entry animation for `Fragment` when binding.

> The following example

```kotlin
// Assume this is your FragmentActivity.
val activity: FragmentActivity
// Assume this is your fragment.
val fragment = YourFragment()
// Bind fragment and set entry animation.
fragment.attachToActivity(
    activity = activity,
    viewId = R.id.container,
    beginAnimId = R.anim.slide_in_right, // Start entering.
    finishAnimId = R.anim.slide_out_left // Finish entry.
)
```

Bind `Fragment` to `Fragment`.

A `Fragment` can also be bound to another `Fragment` to form a nested relationship.

At this point you only need to replace `attachToActivity` with `attachToFragment`.

> The following example

```kotlin
// Assume this is your parent fragment.
val parentFragment = YourParentFragment()
// Assume this is your fragment.
val fragment = YourFragment()
// Bind fragment.
fragment.attachToFragment(parentFragment)
```

Unbind `Fragment` from `FragmentActivity`.

You can also unbind `Fragment` very easily.

> The following example

```kotlin
// Assume this is your FragmentActivity.
val activity: FragmentActivity
// Assume this is your fragment.
val fragment = YourFragment()
// Unbind fragment from FragmentActivity.
fragment.detachFromActivity(activity)
// If you do not fill in the parameters,
// the activity where the current fragment is located will be obtained by default.
fragment.detachFromActivity()
```

Unbind `Fragment` from `Fragment`.

At this point you only need to replace `detachFromActivity` with `detachFromFragment`.

> The following example

```kotlin
// Assume this is your parent fragment.
val parentFragment = YourParentFragment()
// Assume this is your fragment.
val fragment = YourFragment()
// Unbind dragment from parent fragment.
fragment.detachFromFragment(parentFragment)
// If you do not fill in the parameters,
// the parent fragment where the current fragment is located will be obtained by default.
fragment.detachFromFragment()
```

In addition to bindings, you can also replace a `Fragment` within the same bound layout.

> The following example

```kotlin
// Assume this is your FragmentActivity.
val activity: FragmentActivity
// Assume this is your fragment.
val fragment = YourFragment()
// Replace fragment into the layout with ID R.id.container.
fragment.replaceFromActivity(activity, R.id.container)
```

Replace `Fragment` in `Fragment`.

At this point you only need to replace `replaceFromActivity` with `replaceFromFragment`.

> The following example

```kotlin
// Assume this is your parent fragment.
val parentFragment = YourParentFragment()
// Assume this is your fragment.
val fragment = YourFragment()
// Replace fragment into the layout with ID R.id.container.
fragment.replaceFromFragment(parentFragment, R.id.container)
```

Hide the current `Fragment`.

> The following example

```kotlin
// Assume this is your FragmentActivity.
val activity: FragmentActivity
// Assume this is your parent fragment.
val parentFragment = YourParentFragment()
// Assume this is your fragment.
val fragment = YourFragment()
// Hide dragment from FragmentActivity.
fragment.hide(activity)
// Hide fragment from parent fragment.
fragment.hide(fragment = parentFragment)
// If you do not fill in the parameters,
// the parent fragment or activity where the current fragment is located will be obtained by default.
fragment.hide()
```

Show the current `Fragment`.

> The following example

```kotlin
// Assume this is your FragmentActivity.
val activity: FragmentActivity
// Assume this is your parent fragment.
val parentFragment = YourParentFragment()
// Assume this is your fragment.
val fragment = YourFragment()
// Show fragment from FragmentActivity.
fragment.show(activity)
// Show fragment from parent fragment.
fragment.show(fragment = parentFragment)
// If you do not fill in the parameters,
// the parent fragment or activity where the current fragment is located will be obtained by default.
fragment.show()
```

::: tip

Any binding, unbinding, replacement, showing, or hiding operation can be set up with a transition animation.

You can find the `beginAnimId` and `finishAnimId` parameters in these methods, the default animation has been built in by `BetterAndroid`, and you do not need to do it.

Any operation can have animation effects while performing those operations.

:::

### Dimension Extension

::: tip Contents of This Section

[DisplayDensity](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component.base/-display-density)

Friendly interface to density.

[DimensionFactory → toPx](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component.base/to-px)

[DimensionFactory → toDp](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component.base/to-dp)

Extension for Dimension.

:::

There are size issues everywhere in the Android UI, for example, you need to set the `padding` of a `View` to 10dp, which can be easily set in the XML layout.

But in the code you need to use `TypedValue.applyDimension` to convert, such code does not look very friendly.

As a result, everyone began to encapsulate a method in the form of `dp2px`, but this approach was still not very elegant and had problems.

`BetterAndroid` provides a more elegant solution for this.

Normally, you only need to pass in an existing `Context` or `Resources` to complete the conversion.

> The following example

```kotlin
// Assume this is your context.
val context: Context
// Assume this is your resources.
val resources: Resources
// Convert 10dp to px.
valpx = 10.toPx(context)
// Convert 10dp to px.
valpx = 10.toPx(resources)
// Convert 36px to dp.
val dp = 36.toDp(context)
// Convert 36px to dp.
val dp = 36.toDp(resources)
```

If you are already in an environment where a `Context` or `Resources` exists, you can write it more simply as follows.

At this time, you only need to implement a `DisplayDensity` interface for the current instance, and you do not need to override any methods.

> The following example

```kotlin
class YourActivity : AppCompatActivity(), DisplayDensity {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Convert 10dp to px.
        valpx = 10.dp
        // Convert 10dp to px.
        valpx = 10.toPx()
        // Convert 36px to dp.
        valdp = 36.px
        // Convert 36px to dp.
        val dp = 36.toDp()
    }
}
```

::: warning

The `DisplayDensity` interface only supports being implemented into the following instances or instances inherited from them:

`Context`, `Window`, `View`, `Resources`, `Fragment`, `Dialog`

An exception will be thrown if used in an unsupported instance, if you want, you can also override the methods in the `DisplayDensity` interface to support it manually.

The naming method of `dp`, `px`, `toPx`, `toDp` may conflict with the naming method in Jetpack Compose, so it is not recommended to use it in such a project.

:::

### Resources Extension

::: tip Contents of This Section

[ResourcesFactory → isUiInNightMode](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component.base/is-ui-in-night-mode)

[ResourcesFactory → toHexResourceId](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component.base/to-hex-resource-id)

[ResourcesFactory → getDrawableCompat](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component.base/get-drawable-compat)

[ResourcesFactory → getColorCompat](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component.base/get-color-compat)

[ResourcesFactory → getColorStateListCompat](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component.base/get-color-state-list-compat)

[ResourcesFactory → getFloatCompat](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component.base/get-float-compat)

[ResourcesFactory → getFontCompat](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component.base/get-font-compat)

[ResourcesFactory → getThemeAttrsBoolean](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component.base/get-theme-attrs-boolean)

[ResourcesFactory → getThemeAttrsFloat](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component.base/get-theme-attrs-float)

[ResourcesFactory → getThemeAttrsInteger](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component.base/get-theme-attrs-integer)

[ResourcesFactory → getThemeAttrsIntArray](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component.base/get-theme-attrs-int-array)

[ResourcesFactory → getThemeAttrsStringArray](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component.base/get-theme-attrs-string-array)

[ResourcesFactory → getThemeAttrsString](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component.base/get-theme-attrs-string)

[ResourcesFactory → getThemeAttrsDimension](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component.base/get-theme-attrs-dimension)

[ResourcesFactory → getThemeAttrsDrawable](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component.base/get-theme-attrs-drawable)

[ResourcesFactory → getThemeAttrsColorStateList](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component.base/get-theme-attrs-color-state-list)

[ResourcesFactory → getThemeAttrsColor](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component.base/get-theme-attrs-color)

[ResourcesFactory → isThemeAttrsIdsValueEquals](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component.base/is-theme-attrs-ids-value-equals)

[ResourcesFactory → hasThemeAttrsId](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component.base/has-theme-attrs-id)

[ResourcesFactory → getThemeAttrsId](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component.base/get-theme-attrs-id)

[ResourcesFactory → getMenuFromResource](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component.base/get-menu-from-resource)

[ResourcesFactory → getStringArray](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component.base/get-string-array)

[ResourcesFactory → getIntArray](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component.base/get-int-array)

[ResourcesFactory → getColorOrNull](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component.base/get-color-or-null)

[ResourcesFactory → obtainStyledAttributes](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component.base/obtain-styled-attributes)

Extensions for `Resources`.

:::

Resources are a very important part of Android, they contain layouts, images, strings, etc, needed in the apps.

In order to use `Resources` more conveniently, `BetterAndroid` provides some practical extension functions for it.

For the attr in `Resources`, usually we need to create a `TypedValue` object, and then use `Context.getTheme().resolveAttribute`
to fill it with the corresponding ID to obtain its own value.

For example, if we need to get the value of `android.R.attr.windowBackground`, we need to do this.

> The following example

```kotlin
// Assume this is your context.
val context: Context
// Create a TypedValue object.
val typedValue = TypedValue()
// Use Context.getTheme().resolveAttribute to fill in the corresponding ID.
context.theme.resolveAttribute(android.R.attr.windowBackground, typedValue, true)
// Get the resource ID.
val windowBackgroundId = typedValue.resourceId
// Get its own value.
val windowBackground = context.getDrawable(windowBackgroundId)
```

The whole process can be said to be very cumbersome, so `BetterAndroid` provides a simpler way for this.

Now, you just need to use the following method to get the value of itself.

> The following example

```kotlin
// Assume this is your context.
val context: Context
// Get its own value.
val windowBackground = context.getThemeAttrsDrawable(android.R.attr.windowBackground)
```

If you only need to get the resource ID corresponding to attr, you can use the following method.

> The following example

```kotlin
// Assume this is your context.
val context: Context
// Get the resource ID.
val windowBackgroundId = context.getThemeAttrsId(android.R.attr.windowBackground)
```

You can also only determine whether the resource ID corresponding to attr exists.

> The following example

```kotlin
// Assume this is your context.
val context: Context
// Determine whether the resource ID corresponding to attr exists.
val hasWindowBackgroundId = context.hasThemeAttrsId(android.R.attr.windowBackground)
```

You can also compare two attr values to see if they are equals.

> The following example

```kotlin
// Assume this is your context.
val context: Context
// Compare the values ​​of the two attr to see if they are equals.
val isEquals = context.isThemeAttrsIdsValueEquals(R.attr.first_attr, R.attr.second_attr)
```

::: tip

The `Context.getThemeAttrs*` series of methods support common instances in Android that can be converted to actual resource objects, 
such as `getThemeAttrsColor`, `getThemeAttrsInteger`, `getThemeAttrsString`, etc.

Currently `Context.getThemeAttrs*` and `Context.isThemeAttrsIdsValueEquals` support the value contents of the following common resource IDs:

`Color`, `ColorStateList`, `Drawable`, `Dimension`, `String`, `StringArray`, `IntArray`, `Float`, `Boolean`

:::

There is no way to directly obtain the `Menu` resource ID and parse it into a `Menu` object in Android.

For this reason, `BetterAndroid` provides a possible way to obtain it.

`BetterAndroid` encapsulates the method of using `PopupMenu` and converting the obtained content into a `List<MenuItem>` object through `MenuInflater`.

Now, you can easily use the following method to get the value of the `Menu` resource ID.

> The following example

```kotlin
// Assume this is your context.
val context: Context
// Get the value of Menu resource ID.
// The obtained content is a List<MenuItem> object.
val menuItems = context.getMenuFromResource(R.menu.my_menu)
```

For compatibility processing of historical version systems,
`BetterAndroid` encapsulates the methods provided by `ResourcesCompat`, now, you do not need to consider the issue of some methods being deprecated.

You only need to add `Compat` after each method to automatically make it compatible and call it like the original method, with exactly the same function.

Below is an example of compatibility handling for `Drawable`.

> The following example

```kotlin
// Assume this is your context.
val context: Context
// Assume this is your resources.
val resources: Resources
// Get the drawable through context.
val drawable = context.getDrawableCompat(R.drawable.my_drawable)
// Get the drawable through resources.
val drawable = resources.getDrawableCompat(R.drawable.my_drawable, context.theme)
// You can also use generics to convert it to the corresponding type.
val drawable = context.getDrawableCompat<ColorDrawable>(R.drawable.my_background)
```

The following is a comparison table of the original method and the compatibility method.

| Original Method               | Compatibility Method                |
| ----------------------------- | ----------------------------------- |
| `Context.getDrawable`         | `Context.getDrawableCompat`         |
| `Context.getColor`            | `Context.getColorCompat`            |
| `Context.getColorStateList`   | `Context.getColorStateListCompat`   |
| `Context.getFloat`            | `Context.getFloatCompat`            |
| `Resources.getDrawable`       | `Resources.getDrawableCompat`       |
| `Resources.getColor`          | `Resources.getColorCompat`          |
| `Resources.getColorStateList` | `Resources.getColorStateListCompat` |
| `Resources.getFloat`          | `Resources.getFloatCompat`          |
| `Resources.getFont`           | `Context.getFontCompat`             |

For the property-related functions of custom `View`, `BetterAndroid` provides a `TypedArray` extension that can be automatically recycled.

You can directly use the `obtainStyledAttributes` method to create a `TypedArray` object without having to think about when `recycle` should be called.

> The following example

```kotlin
class MyView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    init {
        obtainStyledAttributes(attrs, R.styleable.MyView) {
            val myType = it.getInteger(R.styleable.MyView_myType, 0)
        }
    }
}
```

::: tip

`BetterAndroid` also provides `getStringArray`, `getIntArray` and `getColorOrNull` utility methods for `TypedArray`, which you can find them in **Contents of This Section** above.

:::

Function expansion of system night mode (dark mode).

The system's night mode is determined using `Configuration.UI_MODE_NIGHT_MASK`, in order to make this function more understandable, you do not need to use bit operations to determine.

Because usually we don't need to care about which hosting state the current system's night mode is in, we only need to know whether the current system appearance is dark.

So `BetterAndroid` provides an extension in `Configuration` that directly uses the `Boolean` type for judgment.

> The following example

```kotlin
// Assume this is your context.
val context: Context
// Determine whether the current system is in dark mode.
val isDarkMode = context.resources.configuration.isUiInNightMode
```

### System Colors

::: tip Contents of This Section

[SystemColors](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component.feature/-system-colors)

Extension for system colors (dynamic colors).

:::

In Android 12, the official provides us with a new feature, namely dynamic colors.

Different from the traditional wallpaper color selection, the dynamic colors in Material 3 are calculated based on the hierarchical tones of the wallpaper.

`BetterAndroid` encapsulates the theme colors provided by the system into `SystemColors`, and you can dynamically obtain the system's theme colors at the code level.

Here is an example of creating and using `SystemColors`.

> The following example

```kotlin
// Assume this is your context.
val context: Context
// Create SystemColors object.
val systemColors = SystemColors.from(context)
// Get the color of android.R.color.system_accent1_100 provided by the system.
val accentColor = systemColors.systemAccentPrimary(100)
// Get the color of com.google.android.material.R.color.material_dynamic_primary50 provided by Material.
val primaryColor = systemColors.materialDynamicPrimary(50)
```

Not every device running Android 12 supports dynamic colors, you can use the following methods to determine.

> The following example

```kotlin
// You can directly determine whether the current system supports dynamic colors.
val isAvailable = SystemColors.isAvailable
```

Retrieving colors will return the system-provided default color when not supported, but if the target device is older than Android 12,
any color retrieved from `SystemColors` will be `Color.TRANSPARENT`.

The following is a complete list of the color methods supported by `SystemColors` and their available parameters.

| Method Name                     | Available Parameters                                         |
| ------------------------------- | ------------------------------------------------------------ |
| `systemAccentPrimary`           | 0, 10, 50, 100, 200, 300, 400, 500, 600, 700, 800, 900, 1000 |
| `systemAccentSecondary`         | 0, 10, 50, 100, 200, 300, 400, 500, 600, 700, 800, 900, 1000 |
| `systemAccentTertiary`          | 0, 10, 50, 100, 200, 300, 400, 500, 600, 700, 800, 900, 1000 |
| `systemNeutralPrimary`          | 0, 10, 50, 100, 200, 300, 400, 500, 600, 700, 800, 900, 1000 |
| `systemNeutralSecondary`        | 0, 10, 50, 100, 200, 300, 400, 500, 600, 700, 800, 900, 1000 |
| `materialDynamicPrimary`        | 0, 10, 20, 30, 40, 50, 60, 70, 80, 90, 95, 99, 100           |
| `materialDynamicSecondary`      | 0, 10, 20, 30, 40, 50, 60, 70, 80, 90, 95, 99, 100           |
| `materialDynamicTertiary`       | 0, 10, 20, 30, 40, 50, 60, 70, 80, 90, 95, 99, 100           |
| `materialDynamicNeutral`        | 0, 10, 20, 30, 40, 50, 60, 70, 80, 90, 95, 99, 100           |
| `materialDynamicNeutralVariant` | 0, 10, 20, 30, 40, 50, 60, 70, 80, 90, 95, 99, 100           |

::: danger

You can only pass in the values among the available parameters listed above in the given method.

Other values are not supported, otherwise an exception will be thrown.

:::

### Color Extension

::: tip Contents of This Section

[AttrState](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.graphics/-attr-state)

Color state based on `ColorStateList`.

[ColorFactory → isBrightColor](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.graphics/is-bright-color)

[ColorFactory → toHexColor](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.graphics/to-hex-color)

[ColorFactory → toAlphaColor](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.graphics/to-alpha-color)

[ColorFactory → mixColorOf](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.graphics/mix-color-of)

[ColorFactory → toNormalColorStateList](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.graphics/to-normal-color-state-list)

[ColorFactory → toNullableColorStateList](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.graphics/to-nullable-color-state-list)

[ColorFactory → ColorStateList](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.graphics/-color-state-list)

Extensions for color.

:::

Color exists in the form of `Integer` in Android.

Although there is a class named `Color` to encapsulate it, many methods are added in higher versions of the system and `androidx` is no specific compatible implementation for it,
objects passed in code context are also usually passed directly using `Integer`.

`BetterAndroid` has no reason and no need to redesign a wrapper class to manage colors, so `BetterAndroid` only provides relevant extensions for the `Integer` type.

All color objects passed in the method will be marked with the `@ColorInt` annotation. please also comply with the specifications provided by `androidx`.

Here are some relevant example uses of color extensions.

Determine how bright the color is.

This is useful when you need to decide whether to use dark text based on how bright the color is.

> The following example

```kotlin
// Assume we have the following colors.
val color = Color.WHITE
// To determine how bright it is, you just need to use the following method.
// You will definitely get a true because this is a white color.
val isBright = color.isBrightColor
```

Convert color to HEX string.

> The following example

```kotlin
// Assume we have the following colors.
val color = Color.WHITE
// To convert it to a HEX string you just need to use the following method.
// You will get a "#FFFFFFFF" with transparency.
val hexString = color.toHexColor()
```

Set the transparency of the current color.

> The following example

```kotlin
// Assume we have the following colors.
val color = Color.WHITE
// You can use a floating point number from 0f-1f to set transparency.
val alphaColor = color.toAlphaColor(0.5f)
// You can also use an integer from 0-255 to set transparency.
val alphaColor = color.toAlphaColor(127)
```

Mix two colors.

> The following example

```kotlin
// Assume we have the following colors.
val color1 = Color.WHITE
val color2 = Color.BLACK
// You can mix them very easily using.
val mixColor = mixColorOf(color1, color2)
// You can also set the mixing ratio, the default is 0.5f.
val mixColor = mixColorOf(color1, color2, 0.2f)
```

`BetterAndroid` also provides some extended functions for `ColorStateList`.

You can quickly convert existing colors to a `ColorStateList` with default color using the following method.

> The following example

```kotlin
// Assume we have the following colors.
val color = Color.WHITE
// Convert it to a ColorStateList with default colors using the following method.
val colorStateList = color.toNormalColorStateList()
// You can also convert to a ColorStateList that returns null when the color is transparent.
val colorStateList = color.toNullableColorStateList()
```

You can also manually create a `ColorStateList` via `AttrState`.

> The following example

```kotlin
val colorStateList = ColorStateList(
    AttrState.CHECKED to Color.WHITE,
    AttrState.PRESSED to Color.BLACK,
    AttrState.NORMAL to Color.TRANSPARENT
)
```

### Bitmap Extension

::: tip Contents of This Section

[BitmapFactory → decodeToBitmap](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.graphics/decode-to-bitmap)

[BitmapFactory → decodeToBitmapOrNull](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.graphics/decode-to-bitmap-or-null)

[BitmapFactory → createBitmap](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.graphics/create-bitmap)

[BitmapFactory → createBitmapOrNull](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.graphics/create-bitmap-or-null)

[BitmapFactory → writeBitmap](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.graphics/write-bitmap)

[BitmapFactory → compressBitmap](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.graphics/compress-bitmap)

[BitmapFactory → blur](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.graphics/blur)

[BitmapFactory → round](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.graphics/round)

[BitmapFactory → compress](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.graphics/compress)

[BitmapFactory → reduce](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.graphics/reduce)

[BitmapFactory → zoom](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.graphics/zoom)

Extensions for bitmap.

[BitmapBlurFactory](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.graphics.base/-bitmap-blur-factory)

Extension suitable for bitmap blurring.

:::

In Android, bitmaps can be used in various places, and they are an important object used to display images.

`BetterAndroid` provides a series of extended functions for bitmaps, from loading to transforming, scaling, compressing and blurring them.

When loading bitmaps, you no longer need to use `BitmapFactory`, now there are the following methods to help you complete this operation more conveniently.

Load bitmap via a `File` or an `InputStream`.

> The following example

```kotlin
// Assume your image is located at this path.
// Note that this path is only for demonstration, in actual situations, 
// you need to access files in the external storage.
// You need to apply for various different permissions and should use the
// Environment.getExternalStorageDirectory() method to get the path.
val imageFile = File("/storage/emulated/0/DCIM/Camera/IMG_20210901_000000.jpg")
// Create a file input stream through the file path.
val inputStream = FileInputStream("/storage/emulated/0/DCIM/Camera/IMG_20210901_000000.jpg")
// Load bitmap through file object.
val bitmap = imageFile.decodeToBitmap()
// You can configure it by passing the BitmapFactory.Options object in the method parameter.
val bitmap = imageFile.decodeToBitmap(BitmapFactory.Options().apply {
    // ...
})
// Load the bitmap through the input stream.
val bitmap = inputStream.decodeToBitmap()
```

Load bitmap via `ByteArray`.

If you have an image encrypted via Base64, you can convert it to a byte array and load the bitmap.

> The following example

```kotlin
// Assume this is your Base64 string.
val base64String = "iVBORw0KGgoAAAANSUhEUgAAADIAAAAyCAYAAAA..."
// Convert Base64 string to byte array.
val byteArray = Base64.decode(base64String, Base64.DEFAULT)
// Load bitmap via byte array.
val bitmap = byteArray.decodeToBitmap()
```

Load bitmap via `Resources`.

This operation actually creates a new `Bitmap` object with a known `Drawable` resource ID.

> The following example

```kotlin
// Assume this is your resources.
val resources: Resources
// Load bitmap via resource ID.
val bitmap = resources.createBitmap(R.drawable.my_image)
```

::: tip

When you are not sure whether the bitmap can be loaded successfully, you can replace the loading method with `decodeToBitmapOrNull` and `createBitmapOrNull`,
so that if the loading fails, it will return `null` instead of throwing an exception.

:::

After a bitmap is loaded into memory or a bitmap object exists in memory, you can resave it to a file.

Kotlin's stdlib has already provided a `File.writeText` method for the `File` object, so `BetterAndroid` follows its example and provides a `File.writeBitmap` method.

> The following example

```kotlin
// Assume this is your bitmap object.
val bitmap: Bitmap
// Assume this is the file you want to save to.
// Note that this path is only for demonstration, in actual situations,
// you need to access files in the external storage.
// You need to apply for various different permissions and should use the
// Environment.getExternalStorageDirectory() method to get the path.
val imageFile = File("/storage/emulated/0/DCIM/Camera/IMG_20210901_000000_modified.jpg")
// Save bitmap to file.
imageFile.writeBitmap(bitmap)
// You can configure it by adjusting the format and quality parameters.
// Default is PNG format, quality is 100.
imageFile.writeBitmap(bitmap, format = Bitmap.CompressFormat.JPEG, quality = 80)
```

If you can actually get an `OutputStream` object, you can write the bitmap to the output stream using the following.

> The following example

```kotlin
// Assume this is your bitmap object.
val bitmap: Bitmap
// Assume this is your output stream object.
val outputStream: OutputStream
// Use the use method to automatically close the output stream.
outputStream.use {
    // Write the bitmap to the output stream.
    it.compressBitmap(bitmap)
    // You can configure it by adjusting the format and quality parameters.
    // Default is PNG format, quality is 100.
    it.compressBitmap(bitmap, format = Bitmap.CompressFormat.JPEG, quality = 80)
}
```

The following are extended functions for bitmap scaling, compression, and blur.

Scale bitmap.

> The following example

```kotlin
// Assume this is your bitmap object.
val bitmap: Bitmap
// Scale the bitmap to 100x100 size.
val zoomBitmap = bitmap.zoom(100, 100)
// Scale by multiple, default is 2 times.
val zoomBitmap = bitmap.reduce(3)
```

Compressed bitmap.

> The following example

```kotlin
// Assume this is your bitmap object.
val bitmap: Bitmap
// Compress bitmap to 100 KB size.
val compressBitmap = bitmap.compress(maxSize = 100)
// You can configure it by adjusting the format and quality parameters.
// Default is PNG format, quality is 100.
val compressBitmap = bitmap.compress(maxSize = 100, format = Bitmap.CompressFormat.JPEG, quality = 80)
```

Rounded bitmap.

> The following example

```kotlin
// Assume this is your bitmap object.
val bitmap: Bitmap
// Set the bitmap to a rounded corner with a radius of 10dp.
val roundBitmap = bitmap.round(10.toPx(context))
```

To blur bitmaps, you can also use `BitmapBlurFactory` to accomplish this.

> The following example

```kotlin
// Assume this is your bitmap object.
val bitmap: Bitmap
// Blur the bitmap with a blur level of 25.
val blurBitmap = bitmap.blur(25)
```

::: warning

The blur provided here is just a general algorithm, which can quickly achieve blur effects, but may have problems with speed and performance, and cannot be used for motion blur effects.

For the bitmap blur effect in Android, you can refer to and use other possible third-party libraries.

Currently, there is no universal and complete solution, this is a historical problem in Android.

There is no reason and no need for `BetterAndroid` to be special, encapsulates related functions for bitmap blur.

If your app targets Android 12 and above, we recommend using the officially provided `RenderEffect` for blur operations, and using the `RenderScript` replacement [renderscript-intrinsics-replacement-toolkit](https://github.com/android/renderscript-intrinsics-replacement-toolkit) (but has not been maintained for more than two years).

:::

### Drawable Extension

::: tip Contents of This Section

[GradientDrawableCompat](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.graphics/-gradient-drawable-compat)

Compatible handler class for `GradientDrawable`.

[DrawableFactory → setPaddingCompat](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.graphics/set-padding-compat)

[DrawableFactory → setPadding](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.graphics/set-padding)

[DrawableFactory → updatePadding](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.graphics/update-padding)

Extensions for `Drawable`.

:::

In some cases, we may need to use scenarios that directly manipulate `Drawable` objects.

`BetterAndroid` provides some extended functions that may be used for this purpose.

Sets `padding` for supported `Drawable`.

You can set `padding` on any type of `Drawable` using the following method, but it only works on the following types of `Drawable`:

`RippleDrawable`, `LayerDrawable`, `ShapeDrawable`, `GradientDrawable`

> The following example

```kotlin
// Assume this is your drawable.
val drawable: Drawable
// Set padding for supported drawables.
drawable.setPadding(10.toPx(context))
// Or update the padding of the specified edge.
drawable.updatePadding(horizontal = 10.toPx(context))
```

Set `padding` for `GradientDrawable` and handle it for compatibility.

`GradientDrawable` that supports setting `padding` at the code level is only available in Android 10 and above.

You can set `padding` for `GradientDrawable` using the following compatible methods.

> The following example

```kotlin
// Assume this is your GradientDrawable object.
val drawable: GradientDrawable
// Set padding.
drawable.setPaddingCompat(10.toPx(context), 10.toPx(context), 10.toPx(context), 10.toPx(context))
// Or use the setting method mentioned above to set the padding of all sides at once.
// This method will automatically call to setPaddingCompat for you.
drawable.setPadding(10.toPx(context))
```

But please note that in Android 10 and below, you need to use `GradientDrawableCompat` to create `GradientDrawable` for `padding` to take effect.

You can also inherit `GradientDrawableCompat` to implement your own `GradientDrawable`.

### Toast Extension

::: tip Contents of This Section

[ToastFactory → toast](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.view/toast)

Extensions for `Toast`.

:::

`Toast` in Android has a wide range of usage scenarios, but there are various usage problems.

Earlier, `ktx` of `androidx` provided an extension method of `Context.toast` for `Toast`, but it was later removed due to notification permission issues.

Discussions about this issue can be found in [Toast extensions on Context](https://github.com/android/android-ktx/issues/143).

It is necessary to simplify the use of `Toast` in Kotlin, because sometimes the fastest way to display information is to show a `Toast`.

So `BetterAndroid` provides a `toast` extension method for this purpose, you can use it in the following instances or instances inherited from them:

`Context`, `Window`, `Fragment`, `View`, `Dialog`

You can show a `Toast` very simply using the following method.

> The following example

```kotlin
// Assume this is your context.
val context: Context
// Show a toast
context.toast("Hello World!")
// You can pass in Toast.LENGTH_SHORT or Toast.LENGTH_LONG in the method parameters to set the duration.
// Default is Toast.LENGTH_SHORT.
context.toast("Hello World!", Toast.LENGTH_SHORT)
```

The above are all the ways to use it, `BetterAndroid` did not continue to customize it because the custom functions were also restricted by Android in the later period.

Please refer to [Custom toasts from the background are blocked](https://developer.android.com/about/versions/11/behavior-changes-11#custom-toasts-bg-blocked).

`Toast` can only be used in the main thread, if you want to show a `Toast` in any thread, you only need to configure it simply like below.

> The following example

```kotlin
// Assume this is your context.
val context: Context
// Create a new thread.
Thread {
    // Delay 1 second.
    Thread.sleep(1000)
    // Show a toast and set allowBackground to true.
    context.toast("Hello World!", allowBackground = true)
}.start() // Start it.
```

In this way, you can show a `Toast` in any thread, it should be noted that this parameter is `false` by default and you need to set it manually.

You should try to avoid showing `Toast` in non-main threads, because this may lead to some possible "black box problems" and unknown hidden dangers.

::: warning

As mentioned above, in Android 13 and above, you need to define and add runtime permissions for `Toast` just like notifications.

Some third-party ROMs like MIUI may allow `Toast` to show inside the app without permission.

If there is no need for floating windows, a good suggestion is to use some other similar behavior instead of `Toast`, such as using the `Snackbar` provided by the material component.

Please refer to [Notification runtime permission](https://developer.android.com/develop/ui/views/notifications/notification-permission).

:::

### Window Extension

::: tip Contents of This Section

[WindowFactory → updateLayoutParams](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.view/update-layout-params)

[WindowFactory → updateScreenBrightness](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.view/update-screen-brightness)

[WindowFactory → clearScreenBrightness](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.view/clear-screen-brightness)

Extensions for `Window`.

:::

`BetterAndroid` provides some possible extension functions for `Window`.

You can modify `Window.attributes` in the same way as `View.updateLayoutParams` in `androidx`.

> The following example

```kotlin
// Assume this is your window.
val window: Window
// Modify parameters in Window.attributes.
window.updateLayoutParams {
    gravity = Gravity.CENTER
    // ...
}
```

`BetterAndroid` also encapsulates the method of setting the screen brightness separately for `Window`.

You can modify `Window.attributes.screenBrightness` more conveniently using the following methods.

> The following example

```kotlin
// Assume this is your window.
val window: Window
// Set screen brightness (0-100) for the current window.
window.updateScreenBrightness(50)
// You can also use Float type parameters to set the screen brightness.
window.updateScreenBrightness(0.5f)
```

If you need to restore the default screen brightness provided by the system, you can use the following method.

> The following example

```kotlin
// Assume this is your window.
val window: Window
// Clear the set screen brightness.
window.clearScreenBrightness()
```

### View Extension

::: tip Contents of This Section

[ViewFactory → location](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.view/location)

[ViewFactory → removeSelf](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.view/remove-self)

[ViewFactory → removeSelfInLayout](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.view/remove-self-in-layout)

[ViewFactory → showIme](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.view/show-ime)

[ViewFactory → hideIme](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.view/hide-ime)

[ViewFactory → performKeyPressed](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.view/perform-key-pressed)

[ViewFactory → performTouch](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.view/perform-touch)

[ViewFactory → updatePadding](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.view/update-padding)

[ViewFactory → updateMargin](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.view/update-margin)

[ViewFactory → inflate](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.view/inflate)

[ViewFactory → ViewLayoutParams](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.view/-view-layout-params.html)

[ViewFactory → LayoutParamsMatchParent](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.view/-layout-params-match-parent)

[ViewFactory → LayoutParamsWrapContent](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.view/-layout-params-wrap-content)

Extensions for  `View`.

:::

`View` is an important part of the user interface, when using Kotlin, `androidx` provides us with extended functions for `View`, but it is still not perfect enough.

`BetterAndroid` has been improved and enriched based on the related extension functions of `androidx`, here are some extension functions you can use.

Get the location of `View` on the screen.

You can get a `Point` object in the following way, which contains the location of the `View` on the screen.

> The following example

```kotlin
// Assume this is your view.
val view: View
// Get the location of the View on the screen.
val location = view.location
// X coordinate.
val x = location.x
// Y coordinate.
val y = location.y
```

Removes itself from the parent layout (container).

Normally, when we remove `View` from the parent layout, we need to use `View.getParent` to obtain the parent layout object, then convert it to `ViewGroup`,
and finally call the `ViewGroup.removeView` method.

Now, you can more easily remove itself from the parent layout using the following method, if the parent layout does not exist or the parent layout is not a `ViewGroup` (which usually does not exist),
this method will have no effect and no need to worry about any negative effects.

> The following example

```kotlin
// Assume this is your view.
val view: View
// Remove itself from parent layout.
view.removeSelf()
// Use the ViewGroup.removeViewInLayout method to remove itself.
view.removeSelfInLayout()
```

Show or hide the input method (IME).

Normally, controlling the showing and hiding of the input method can be done automatically through the focus of the input box,
but in some cases we want to be able to manually control the showing and hiding of the input method.

If the current `View` is in `Activity`, you can use the following methods to show or hide the input method.

> The following example

```kotlin
// Assume this is your view.
val view: View
// Show input method.
view.showIme()
// Hide input method.
view.hideIme()
```

In Android 10 and above, the above method will use `WindowInsetsController` to control the showing and hiding of the input method.

When `View` is not in `Activity` or the current Android version is lower than Android 10, `InputMethodManager` will be used to control the showing and hiding of the input method.

::: tip

If you need to use drag and drop to control the showing or hiding of input methods (window insets animation), currently `BetterAndroid` does not provide related extension functions.

You can refer to [WindowInsetsAnimationController](https://developer.android.com/reference/android/view/WindowInsetsAnimationController) to implement it yourself.

:::

::: warning

In Android 10 and above, it is recommended to set the `android:windowSoftInputMode` parameter of `Activity` to `adjustResize` to better control the showing and hiding of the input method.

If your `View` is not in `Activity` or the current Android version is lower than Android 10, the above solution may not be effective under certain conditions.

When `Activity` is first started, it is recommended to delay processing of show or hide input method events, otherwise it may be ineffective,
and the interval between show and hide events should not be too short.

:::

Simulate touch events.

The `performClick` method provided by `View` can only simulate click events, if you need to simulate touch events, you can use the `View.performTouch` method provided by `BetterAndroid`.

The parameters accepted by this method are shown in the table below.

| Parameter Name | Parameter Type | Parameter Description                                                            |
| -------------- | -------------- | -------------------------------------------------------------------------------- |
| `downX`        | `Float`        | Simulates the down X coordinate of a touch event.                                |
| `downY`        | `Float`        | Simulates the down Y coordinate of a touch event.                                |
| `upX`          | `Float`        | Simulate the lift X coordinate of a touch event.                                 |
| `upY`          | `Float`        | Simulate the lift Y coordinate of a touch event.                                 |
| `duration`     | `Long`         | The duration of the simulated touch event. (from press to lift), in milliseconds |

> The following example

```kotlin
// Assume this is your view.
val view: View
// Simulate touch events from (15, 30) to (105, 130) for 500 milliseconds.
view.performTouch(15f, 30f, 105f, 130f, 500)
```

Simulate key events.

You can simulate physical keyboard or input method (IME) key events sent to the current `View`, for example in `EditText` you can simulate pressing the backspace key to delete text.

> The following example

```kotlin
// Assume this is your view.
val view: EditText
// Simulate pressing the backspace key.
view.performKeyPressed(KeyEvent.KEYCODE_DEL)
// You can add the duration parameter at the end to set the duration (from pressing to lifting),
// the unit is milliseconds.
// Default is 150 milliseconds.
view.performKeyPressed(KeyEvent.KEYCODE_DEL, duration = 500)
```

Update `View`’s `padding` and `margin`.

`androidx` provides a `View.updatePadding` method, and `BetterAndroid` provides a method that can update the horizontal and vertical directions,
if you only need to update the `padding` in these two directions, you don't need to write two repeat the value.

> The following example

```kotlin
// Assume this is your view.
val view: View
// Update the padding in the horizontal direction.
view.updatePadding(horizontal = 10.toPx(context))
// Update the padding in the vertical direction.
view.updatePadding(vertical = 10.toPx(context))
```

`BetterAndroid` also provides a `View.updateMargin` method, which is used in the same way as `View.updatePadding`.

This method will only take effect when `LayoutParams` of `View` is `MarginLayoutParams`, and will have no effect in other cases.

> The following example

```kotlin
// Assume this is your view.
val view: View
// Update the margin in the horizontal direction.
view.updateMargin(horizontal = 10.toPx(context))
// Update the margin in the vertical direction.
view.updateMargin(vertical = 10.toPx(context))
// Update left margin.
view.updateMargin(left = 10.toPx(context))
```

Inflate layout.

Normally we can use `getLayoutInflater` or `LayoutInflater.from(context)` in `Activity` to create a `LayoutInflater` object, and then use the `inflate` method to inflate the layout.

`BetterAndroid` simplifies this step for you, now, you can use the `inflate` method in `ViewGroup` and `Context` to inflate the layout.

> The following example

```kotlin
// Assume this is your ViewGroup.
val view: ViewGroup
// Assume this is your context.
val context: Context
// You can inflate the layout in ViewGroup using the following method.
val myView = view.inflate(R.layout.my_layout)
// You can set the attachToRoot parameter to decide whether to add it to the ViewGroup at the same time.
val myView = view.inflate(R.layout.my_layout, attachToRoot = true)
// When context exists, you can inflate the layout using the following method.
val myView = context.inflate(R.layout.my_layout)
// You can specify a parent layout for it,
// if you need to add it to the parent layout immediately, you can set the attachToRoot parameter to true.
val myView = context.inflate(R.layout.my_layout, view, attachToRoot = true)
```

Manually create a `LayoutParams` object.

`LayoutParams` is the layout parameter of `View`, and its type depends on the parent layout of `View`, for example, the `LayoutParams` of `LinearLayout` is `LinearLayout.LayoutParams`.

Sometimes, you may need to manually create this object and set it into `View`, in this case, `BetterAndroid` provides the `ViewLayoutParams` method,
now, you can save yourself the step of creating an object using the super long `ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)`.

> The following example

```kotlin
// Assume this is your view.
Val view: View
// Create a LinearLayout.LayoutParams object, by default, width and height are both WRAP_CONTENT.
val layoutParams = ViewLayoutParams<LinearLayout.LayoutParams>()
// Set both width and height to MATCH_PARENT.
val layoutParams = ViewLayoutParams<LinearLayout.LayoutParams>(marchParent = true)
// Just set width to MATCH_PARENT.
val layoutParams = ViewLayoutParams<LinearLayout.LayoutParams>(widthMatchParent = true)
// Manually set width and height.
val layoutParams = ViewLayoutParams<LinearLayout.LayoutParams>(50.toPx(context), 30.toPx(context))
// Set to view.
view.layoutParams = layoutParams
```

### TextView Extension

::: tip Contents of This Section

[TextViewFactory → isEllipsize](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.view/is-ellipsize)

[TextViewFactory → isUnderline](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.view/is-underline)

[TextViewFactory → isStrikeThrough](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.view/is-strike-through)

[TextViewFactory → textColor](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.view/text-color)

[TextViewFactory → setDigits](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.view/set-digits)

Extensions for `TextView`.

:::

`TextView` is one of the most commonly used components in Android, `BetterAndroid` provides `TextView` with some extended functions that are more convenient to use in Kotlin.

Determine whether there is an ellipsis in `TextView`.

> The following example

```kotlin
// Assume this is your TextView.
val textView: TextView
// Determine whether there is an ellipsis in TextView.
val isEllipsize = textView.isEllipsize
```

Get and set the underline effect of `TextView`.

> The following example

```kotlin
// Assume this is your TextView.
val textView: TextView
// Get whether the TextView has an underline effect.
val isUnderline = textView.isUnderline
// Set the underline effect of TextView.
textView.isUnderline = true
```

Get and set the strike through effect of `TextView`.

> The following example

```kotlin
// Assume this is your TextView.
val textView: TextView
// Get whether the TextView has a strike through effect.
val isStrikeThrough = textView.isStrikeThrough
// Set the strikethrough effect of TextView.
textView.isStrikeThrough = true
```

::: warning

When setting the underline or strikethrough effect, you may need to manually enable anti-aliasing for `TextView` using `paint.isAntiAlias = true` to achieve better results,
this is a historical issue with drawing performance in Android.

:::

Get and set the text color of `TextView`.

Although you can use the `TextView.setTextColor` method to set the text color, it is not well recognized as the Getter and Setter methods in Kotlin
because the corresponding `TextView.getTextColors` is a `ColorStateList` object.

So `BetterAndroid` has added an extension for this function, now, you can use the following methods to get and set the text color of `TextView`.

> The following example

```kotlin
// Assume this is your TextView.
val textView: TextView
// Get the text color of TextView.
val textColor = textView.textColor
// Set the text color of TextView.
textView.textColor = Color.RED
```

Set the input limits (digits) of the `TextView`.

This property is conveniently set in XML only using the `digits` attribute, but if you need to modify it dynamically, you will need the `TextView.setKeyListener` method.

For this purpose, `BetterAndroid` provides a `setDigits` method for `TextView`, you can use the following method to set the input limit of `TextView`.

Since the input function is completed by `EditText`, and `EditText` inherits from `TextView`, this method is generally only used in `EditText`.

> The following example

```kotlin
// Assume this is your EditText.
val editText: EditText
// Set the input box to only input numbers.
editText.setDigits("0123456789")
// Set the input box to only input lowercase letters.
editText.setDigits("abcdefghijklmnopqrstuvwxyz")
// The second parameter is inputType, you can limit the display behavior of the input box here.
// For example, only numbers can be entered and displayed as passwords.
editText.setDigits("0123456789", InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD)
// The third parameter is locale,
// where you can set the country area of the input box. (only available in Android 8 and above)
// For example, set to mainland China.
editText.setDigits("0123456789", locale = Locale.CHINA)
```

### ViewBinding Extension

::: tip Contents of This Section

[ViewBindingFactory → inflateViewBinding](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.view/inflate-view-binding)

Extensions for `ViewBinding`.

:::

`ViewBinding` is a new feature in Android, which can help us operate components in the layout more conveniently.

But obviously the official method of using it is not open, you can get the interface `ViewBinding` of the generated class,
and there is no implementation of `inflate` and other methods, you can only use a method like `ActivityMainBinding.inflate(layoutInflater)` to inflate the layout.

So `BetterAndroid` performed reflection processing on it to obtain the `inflate` method and extract the object type through generics.

::: warning

We do not recommend that you manually use this method to operate `ViewBinding` yourself.

This set of APIs may not work stably in any special scenarios, they are currently only used to provide [ui-component](../library/ui-component.md), and only tested limited usage scenarios.

Therefore, detailed usage methods will not be provided here, if you still want to use this API to load `ViewBinding`,
you can find the annotated usage in the `inflateViewBinding` method in **Contents of This Section** to read.

:::