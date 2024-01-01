# Changelog

> The version update history of `BetterAndroid` is recorded here.

::: danger

We will only maintain the latest dependencies, if you are using outdate dependencies, you voluntarily renounce any possibility of maintenance.

:::

::: warning

To avoid translation time consumption, Changelog will use **Google Translation** from **Chinese** to **English**, please refer to the original text for actual reference.

Time zone of version release date: **UTC+8**

:::

## ui-component

### 1.0.3 | 2023.12.03 &ensp;<Badge type="tip" text="latest" vertical="middle" />

- The `init` method of `SystemBarsController` adds the `defaultPaddings` parameter, which can configure whether to automatically add the corresponding
  system bars padding during initialization
- When using the `show` or `hide` method in `SystemBarsController`, it will automatically determine whether the system bars padding has been added to
  determine whether to add the corresponding padding
- Added `AppComponentActivity`, inherited from `ComponentActivity`, applicable to Jetpack Compose without setting AppCompat theme
- When using `AppBindingActivity`, `AppViewsActivity`, `AppComponentActivity`, the added layout background color will be automatically filled into the
  parent layout

### 1.0.2 | 2023.11.24 &ensp;<Badge type="warning" text="stale" vertical="middle" />

- Refactor the System Insets function in `SystemBarsController` to fix the problem that the maximum layout size cannot be calculated on Compose View
- Added `SystemInsets.Paddings` and `setBaseBackgroundResource`, `isVisible` functions to `SystemBarsController`
- `SystemBarsView` is obsolete, please start using the new way to customize the System Insets function
- Added `View.applySystemInsets`, `View.appendSystemInsets`, `View.removeSystemInsets` methods

### 1.0.1 | 2023.11.18 &ensp;<Badge type="warning" text="stale" vertical="middle" />

- Fix the loading exception catching problem in `SystemBarsController`

### 1.0.0 | 2023.11.02 &ensp;<Badge type="warning" text="stale" vertical="middle" />

- The first version is submitted to Maven

## ui-extension

### 1.0.1 | 2023.11.18 &ensp;<Badge type="tip" text="latest" vertical="middle" />

- Fix the problem of incorrect return value of `getColor` and `getColorStateList` methods in ResourcesFactory

### 1.0.0 | 2023.11.02 &ensp;<Badge type="warning" text="stale" vertical="middle" />

- The first version is submitted to Maven

## system-extension

### 1.0.1 | 2024.01.02 &ensp;<Badge type="tip" text="latest" vertical="middle" />

- Renamed some incorrectly named methods in `SystemVersion`
- Added `name` attribute in `SystemVersion`
- Caching `SystemKind` detection results to prevent repeated creation of reflection objects
- Deprecated `get` in `SystemKind`, now please use the `current` property to get the current system type.
- Renamed some incorrectly named methods and classes in `ApplicationFactoy`
- Removed `FLAG` prefix in `AplicationInfoFlagsWrapper`
- Modified the related usage in `BroadcastFactory`, now please refer to the documentation to use the new usage to create runtime broadcasts
- Modified the relevant usage in `ClipboardFactory`, now please refer to the document to use the new usage to use the clipboard function
- Methods in `ServiceFactory` were `inline` processed
- Added `startServiceOrElse` and `startForegroundServiceOrElse` methods in `ServiceFactory`

### 1.0.0 | 2023.11.02 &ensp;<Badge type="warning" text="stale" vertical="middle" />

- The first version is submitted to Maven

## permission-extension

Not yet released.

## compose-extension

### 1.0.0 | 2024.01.02 &ensp;<Badge type="tip" text="latest" vertical="middle" />

- The first version is submitted to Maven

## compose-multiplatform

### 0.1.0 | 2024.01.02 &ensp;<Badge type="tip" text="latest" vertical="middle" />

- The first version is submitted to Maven