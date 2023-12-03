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

### 1.0.3 | 2023.12.03

- `SystemBarsController` 的 `init` 方法新增 `defaultPaddings` 参数，可以配置初始化时是否自动添加对应的系统栏边距
- 在 `SystemBarsController` 中使用 `show` 或 `hide` 方法时将自动判断是否已经添加系统栏边距来决定是否添加对应的边距
- 新增 `AppComponentActivity`，继承于 `ComponentActivity`，可适用于 Jetpack Compose，无需设置 AppCompat 主题
- 使用 `AppBindingActivity`、`AppViewsActivity`、`AppComponentActivity` 时会自动将添加的布局背景颜色填充到父布局

## ui-extension

### 1.0.0 | 2023.11.02

- 首个版本提交至 Maven

### 1.0.1 | 2023.11.18

- 修复 ResourcesFactory 中 `getColor` 与 `getColorStateList` 方法返回值错误问题

## system-extension

### 1.0.0 | 2023.11.02

- 首个版本提交至 Maven