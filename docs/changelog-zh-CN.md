# 更新日志

## ui-component

### 1.0.0 | 2023.11.02

- 首个版本提交至 Maven

### 1.0.1 | 2023.11.18

- 修复 SystemBarsController 中存在的装载异常捕获问题

### 1.0.2 | 2023.11.24

- 重构 `SystemBarsController` 中的 System Insets 功能修复在 Compose View 上无法计算布局最大尺寸问题
- `SystemBarsController` 中新增 `SystemInsets.Paddings` 和 `setBaseBackgroundResource`、`isVisible` 功能
- 作废了 `SystemBarsView`，请开始使用新的方式自定义 System Insets 功能
- 新增 `View.applySystemInsets`、`View.appendSystemInsets`、`View.removeSystemInsets` 方法

## ui-extension

### 1.0.0 | 2023.11.02

- 首个版本提交至 Maven

### 1.0.1 | 2023.11.18

- 修复 ResourcesFactory 中 `getColor` 与 `getColorStateList` 方法返回值错误问题

## system-extension

### 1.0.0 | 2023.11.02

- 首个版本提交至 Maven