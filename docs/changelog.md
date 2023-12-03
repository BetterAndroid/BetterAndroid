# Changelog

## ui-component

### 1.0.0 | 2023.11.02

- The first version is submitted to Maven

### 1.0.1 | 2023.11.18

- Fix the loading exception catching problem in SystemBarsController

### 1.0.2 | 2023.11.24

- Refactor the System Insets function in `SystemBarsController` to fix the problem that the maximum layout size cannot be calculated on Compose View
- Added `SystemInsets.Paddings` and `setBaseBackgroundResource`, `isVisible` functions to `SystemBarsController`
- `SystemBarsView` is obsolete, please start using the new way to customize the System Insets function
- Added `View.applySystemInsets`, `View.appendSystemInsets`, `View.removeSystemInsets` methods

### 1.0.3 | 2023.12.03

- The `init` method of `SystemBarsController` adds the `defaultPaddings` parameter, which can configure whether to automatically add the corresponding
  system bars padding during initialization
- When using the `show` or `hide` method in `SystemBarsController`, it will automatically determine whether the system bars padding has been added to
  determine whether to add the corresponding padding
- Added `AppComponentActivity`, inherited from `ComponentActivity`, applicable to Jetpack Compose without setting AppCompat theme
- When using `AppBindingActivity`, `AppViewsActivity`, `AppComponentActivity`, the added layout background color will be automatically filled into the
  parent layout

## ui-extension

### 1.0.0 | 2023.11.02

- The first version is submitted to Maven

### 1.0.1 | 2023.11.18

- Fix the problem of incorrect return value of `getColor` and `getColorStateList` methods in ResourcesFactory

## system-extension

### 1.0.0 | 2023.11.02

- The first version is submitted to Maven