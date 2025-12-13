# Changelog

> The version update history of `BetterAndroid` is recorded here.

::: danger

We will only maintain the latest dependencies. If you are using outdated dependencies, you voluntarily renounce any possibility of maintenance.

:::

::: warning

To avoid translation time consumption, Changelog will use **Google Translation** from **Chinese** to **English**, please refer to the original text for actual reference.

Time zone of version release date: **UTC+8**

:::

## ui-component

### 1.0.9 | 2025.12.14 &ensp;<Badge type="tip" text="latest" vertical="middle" />

- Breaking change: Migrate Insets related functions to [ui-extension](#ui-extension)

### 1.0.8 | 2025.08.03 &ensp;<Badge type="warning" text="stale" vertical="middle" />

- Remove adapter-related functions and merge them into [ui-component-adapter](#ui-component-adapter)
- Migrate Java reflection-related behavior from [YukiReflection](https://github.com/HighCapable/YukiReflection) to [KavaRef](https://github.com/HighCapable/KavaRef)

### 1.0.7 | 2025.03.08 &ensp;<Badge type="warning" text="stale" vertical="middle" />

- Removed Material dependencies and refactored into corresponding dependencies
- Extensively refactored adapter (Adapter) underlying APIs
- Removed unreasonable `View` instance loading methods in adapters
- Added `ViewHolderDelegate` to adapters to support custom layout inflaters
- `AdapterPosition` now supports `layout` and `absolute` indexes
- Other known adapter-related issues fixed

### 1.0.6 | 2025.01.25 &ensp;<Badge type="warning" text="stale" vertical="middle" />

- Added `onPrepareContentView` method in `AppBindingActivity` to customize operations before loading the layout
- Fixed the issue of out-of-bounds index when the data set is empty in the adapter builder
- Added `AdapterPosition` in `RecyclerAdapterBuilder` to implement dynamic index functionality
- Fixed a critical error caused by the parent layout being loaded prematurely when using layout ID in the adapter builder
- Added a custom `RecyclerView` layout manager and modified the default layout manager to correctly handle indexes when custom header and footer layouts are added
- Added `wrapper` extension method for `RecyclerView.Adapter` to correctly handle indexes when custom header and footer layouts are added by wrapping instances
- Fixed a critical error where the footer layout was added as a header layout in `RecyclerAdapterBuilder`

### 1.0.5 | 2024.03.08 &ensp;<Badge type="warning" text="stale" vertical="middle" />

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

## ui-component-adapter

### 1.0.0 | 2025.08.03 &ensp;<Badge type="tip" text="latest" vertical="middle" />

- The first version is submitted to Maven

## ui-extension

### 1.0.8 | 2025.12.14 &ensp;<Badge type="tip" text="latest" vertical="middle" />

- Added Insets related functions from [ui-component](#ui-component)
- Insets extension `handleOnWindowInsetsChanged` method adds `requestApplyOnLayout` parameter
- Added Lint functionality (experimental)

### 1.0.7 | 2025.08.03 &ensp;<Badge type="warning" text="stale" vertical="middle" />

- Remove the `Adapter` related extension and merge it into [ui-component-adapter](#ui-component-adapter)
- Migrate Java reflection-related behavior from [YukiReflection](https://github.com/HighCapable/YukiReflection) to [KavaRef](https://github.com/HighCapable/KavaRef)
- `Resources` adjustment in the extension `isUiInNightMode` the underlying logic is connected to the `isNightModeActive` method
- ~~`obtainStyledAttributes`~~ method in the `Resources` extension is now recommended to migrate to Jetpack's `withStyledAttributes`
- `View` extension adds `updateMarginsRelative` method
- The ~~`zoom`~~ method in the `Bitmap` extension is invalid. It is now recommended to migrate to Jetpack's `scale` method
- Added `backgroundColor` parameter to `round` in the `Bitmap` extension
- ~~`compress`~~ in the `Bitmap` extension, please use the `shrink` method instead

### 1.0.6 | 2025.03.04 &ensp;<Badge type="warning" text="stale" vertical="middle" />

- Adjusted the default behavior of the `inflate` method and `attachToParent` parameter in the `LayoutInflater` extension
- Corrected the `updateMargin` method in the `View` extension to `updateMargins` and added the `setMargins` method
- Completely removed the old `ViewLayoutParams` class, now please use the `ViewLayoutParams` method instead
- Fixed the logic related to `ViewLayoutParams`, now it can create any object based on `ViewGroup.LayoutParams`
- Overloaded the `ViewLayoutParams` method to support directly passing in the `lpClass` parameter

### 1.0.5 | 2025.01.25 &ensp;<Badge type="warning" text="stale" vertical="middle" />

- Fixed the issue where the `updateText` method in the `TextView` extension could cause an out-of-bounds cursor index after overriding the `setText` method
- Modified the `setDigits` method in the `TextView` extension to read the `inputType` parameter from the `TextView` itself by default
- Modified the `updateTypeface` method in the `TextView` extension to set the `tf` parameter to `null` by default to prevent setting unexpected font styles
- Added `textToString` and `hintToString` methods in the `TextView` extension
- Fixed the issue where `Toast` might not pop up in non-main threads in the `Toast` extension
- Added a generic `parentFragment` method in the `Fragment` extension
- Added an `options` parameter to the `startActivity` method in the `Activity` extension
- Added `LifecycleOwner` extension
- Added `Coroutines` coroutine-related extensions
- Added `setIntervalOnClickListener` method in the `View` extension
- Added non-generic implementations of `child`, `childOrNull`, `firstChild`, `firstChildOrNull`, `lastChild`, `lastChildOrNull` methods in the `View` extension
- Corrected the method name `isThemeAttrsIdsValueEquals` to `areThemeAttrsIdsValueEquals` in `Resources`
- Added a large number of extension methods for `TypedArray` in the `Resources` extension
- **<u>⚠️ Breaking Change</u>**, extensively refactored methods in the `Fragment` extension and separated `FragmentTransaction` methods, **<u>migration may require effort</u>**
- Added `FileDescriptor.decodeToBitmap` and `FileDescriptor.decodeToBitmapOrNull` methods in the `Bitmap` extension
- Added `RecyclerView` extension
- Added `tag`, `setTag`, and `animate` methods in the `View` extension
- Added `tooltipTextCompat` method in the `View` extension
- The `toString` method in `ViewBindingBuilder` now returns the full object name held by `ViewBinding`
- Added `Adapter` extension

### 1.0.4 | 2024.05.05 &ensp;<Badge type="warning" text="stale" vertical="middle" />

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

### 1.0.3 | 2025.08.03 &ensp;<Badge type="tip" text="latest" vertical="middle" />

- Migrate Java reflection-related behavior from [YukiReflection](https://github.com/HighCapable/YukiReflection) to [KavaRef](https://github.com/HighCapable/KavaRef)
- ~~`SystemVersion`~~ renamed to `AndroidVersion` and added Android 15, 16 and 1-4.3 version constants, and functional adjustments are made at the same time
- ~~`SystemKind`~~ rename it to `RomType` and make functional adjustments
- Added the `options` parameter of the `sendBroadcast` method to the `options` extension

### 1.0.2 | 2025.01.25 &ensp;<Badge type="warning" text="stale" vertical="middle" />

- Added `BroadcastReceiver` method in `Broadcast` extension and modified the return value of `registerReceiver` method to itself

### 1.0.1 | 2024.01.02 &ensp;<Badge type="warning" text="stale" vertical="middle" />

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