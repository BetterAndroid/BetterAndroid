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

## ui-extension

### 1.0.0 | 2023.11.02

- The first version is submitted to Maven

### 1.0.1 | 2023.11.18

- Fix the problem of incorrect return value of `getColor` and `getColorStateList` methods in ResourcesFactory

## system-extension

### 1.0.0 | 2023.11.02

- The first version is submitted to Maven