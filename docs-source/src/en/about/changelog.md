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

### 1.0.5 | 2024.03.08 &ensp;<Badge type="tip" text="latest" vertical="middle" />

- Removed `Factory` suffix from all extension method `kt` files
- Removed deprecated files in the previous version
- Fixed the problem of incorrect subscripts and item positions returned by `onItemViewsClick` and `onItemViewsLongClick` in `CommonAdapterBuilder`
- Adjust the entry record object added in the adapter to `LinkedHashSet` to prevent repeated additions
- Open `DEFAULT_VIEW_TYPE` in `RecyclerAdapterBuilder`
- Added `onBindItemId` method in the adapter to customize the behavior of `getItemId`
- The adapter now allows direct use of `onBindViews` to bind item layouts without callbacks
- Added `onBindHeaderView` and `onBindFooterView` methods in `RecyclerAdapterBuilder`
- If the `trigger` method is called in `OnBackPressedCallback` and is not removed, the callback event will be re-enabled

### 1.0.4 | 2024.01.02 &ensp;<Badge type="warning" text="stale" vertical="middle" />

- `SystemBarsController` has been fully migrated to window insets API, please refer to the documentation to start using the new usage
- Reconstructed `SystemBarsController` and modified the initialization method, please refer to the documentation to get started using the new method
- Window insets has been completely separated from the system bars into `WindowInsetsWrapper`, `InsetsWrapper`, `InsetsFactory`
- Deprecated all related usages of window insets in the system bars, please start using the new window insets API now
- Reconstructed `BackPressedController` and connected it to `BackPressedDispatcher`, please refer to the documentation to start using the new usage
- `AppViewsFragment` adds a new loading layout method using the constructor method to pass in `layoutResId`
- Renamed `RecyclerCosmeticMaccker` to `RecyclerCosmetic` and made related functions generic
- Opened preset decorators for `RecyclerView`, now you can create them manually
- Refactored a lot of functions related to adapters. Now you can create them manually using the method of the same name for each adapter (`BaseAdapter` is `CommonAdapter`)
- Reconstructed notification related functions, please refer to the documentation to start using the new usage
- Renamed and modified some other functions

### 1.0.3 | 2023.12.03 &ensp;<Badge type="warning" text="stale" vertical="middle" />

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

### 1.0.4 | 2024.05.05 &ensp;<Badge type="tip" text="latest" vertical="middle" />

- Integrate the parameters of the entry and exit animation methods in the `Fragment` extension
- Remove the default transition animation in the `Fragment` extension and delete the related resource files
- Added the `Fragment.viewBinding` method in the `ViewBinding` extension
- Fix the problem that the layout inflate by `viewBinding` in the `ViewBinding` extension will not be automatically cached
- Fix the problem that the `inflate` method in the `ViewBinding` extension cannot be loaded correctly when encountering `<merge>` and `<include>` type layouts
- Adjust the `inflate` method in the `ViewBinding` extension to ignore the `attachToParent` parameter when encountering `<merge>` and `<include>` type layouts
- Remove all deprecated methods in the old version of the `ViewBinding` extension

### 1.0.3 | 2024.03.08 &ensp;<Badge type="warning" text="stale" vertical="middle" />

- Removed `Factory` suffix from all extension method `kt` files
- Removed deprecated files in the previous version
- The `ViewLayoutParams` method of `ViewFactory` now supports the `AbsListView.LayoutParams` type
- Added `updateCompoundDrawables` and `updateCompoundDrawablesWithIntrinsicBounds` methods in `ViewFactory`
- Added `parent` and `parentOrNull` methods in `ViewFactory`
- Added `walkToRoot`, `walkThroughChildren`, `indexOfInParent` methods in `ViewFactory`
- Added `child`, `childOrNull`, `firstChild`, `lastChild`, `firstChildOrNull`, `lastChildOrNull` methods in `ViewFactory`
- Merge the `inflate` method in `ViewFactory` into `LayoutInflaterFactory` and deprecated the original method
- Added `addToBackStack` parameter to the add methods in `FragmentFactory`
- The `commitTransaction` method is deprecated in `FragmentFactory` and is now migrate to the official `fragment-ktx` dependency
- Added `themeResId` method in `ResourcesFactory`
- Added new `ViewBinding` solution and deprecated the old solution

### 1.0.2 | 2024.01.02 &ensp;<Badge type="warning" text="stale" vertical="middle" />

- Some package names that are not classified according to the standard have been moved, if you encounter an error, please re-import these calling methods
- Modified related names for Java calls
- Added `updatePadding` method in `ViewFactory` that can set horizontal and vertical directions
- The `inflate` method in `ViewFactory` will automatically use `Activity.getLayoutInflater`
- Renamed `ColorsFactory` to `ColorFactory`
- Renamed `toMixColor` in `ColorFactory` to `mixColorOf`
- Some methods in `ColorFactory` will now no longer throw exceptions
- Added `GradientDrawableCompat`, you can use it to create `GradientDrawable` for versions below Android 10
- Added `updatePadding` method in `DrawableFactory` that can set horizontal and vertical directions
- `asDp`, `asPx` and its usage in `DimensionFactory` are obsolete because of its design error, and are now connected to the new usage `toDp`, `toPx`
- Added `DisplayDensity`, you can easily use `dp`, `px` to convert units
- Deprecated `getDimensionPx` in `ResourcesFactory`, please use `toPx` to calculate manually
- Deprecated `isSpecialWindowingMode` in `ResourcesFactory` and `ActivityFactory`, please do not use it again
- Added `getFontCompat` method in `ResourcesFactory`
- Fix the usage error of `commt` related functions in `FragmentFactory`
- A large number of related usages in `FragmentFactory` have been modified, now please refer to the documentation to start using the new usages
- Added `compressBitmap` method in `BitmapFactory`
- Added `Window.toast` method in `ToastFactory`
- The `toast` method in `ToastFactory` has a new `allowBackgroud` parameter, you can now allow `Toast` to show in non-main threads
- Some methods in `WindowFactory` will now no longer throw exceptions
- A new method in `WindowFactory` can be used to set the screen brightness using the `Float` type
- Use `WindowInsetsController` in `ViewFactory` to show or hide the input method and rename the methods to `showIme`, `hideIme`
- Deprecated `ViewLayoutParam` class, please use the `ViewLayoutParams` method instead
- Added `duration` parameter to `performKeyPressed` in `ViewFactory`
- Added `updateMargin` method in `ViewFactory` that can set horizontal and vertical directions
- Methods in `ActivityFactory` were `inline` processed
- Added each `startActivityOrElse` method in `ActivityFactory`
- The `round` method in `BimapFactory` now supports setting the corner radius in each direction

### 1.0.1 | 2023.11.18 &ensp;<Badge type="warning" text="stale" vertical="middle" />

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

### 1.0.2 | 2024.01.16 &ensp;<Badge type="tip" text="latest" vertical="middle" />

- Added `HapticFeedback` method, you can set it to any click or touch event
- `ComponentPadding` adds a `None` method, you can use it to directly get `ComponentPadding(0.dp)`
- Modified `onDismissRequest` in `Dialog` to make it mandatory to exist, matching the usage in the official `foundation`
- Optimize some code styles

### 1.0.1 | 2024.01.08 &ensp;<Badge type="warning" text="stale" vertical="middle" />

- Removed some useless `@Stable` annotations
- Added `borderOrElse` method with the same name and new usage
- Added `AdaptiveRow`, `AdaptiveColumn`

### 1.0.0 | 2024.01.02 &ensp;<Badge type="warning" text="stale" vertical="middle" />

- The first version is submitted to Maven

## compose-multiplatform

### 0.1.0 | 2024.01.02 &ensp;<Badge type="tip" text="latest" vertical="middle" />

- The first version is submitted to Maven