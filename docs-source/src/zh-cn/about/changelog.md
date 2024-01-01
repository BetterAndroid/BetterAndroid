# 更新日志

> 这里记录了 `BetterAndroid` 的版本更新历史。

::: danger

我们只会对最新的依赖进行维护，若你正在使用过时的依赖则代表你自愿放弃一切维护的可能性。

:::

## ui-component

### 1.0.3 | 2023.12.03 &ensp;<Badge type="tip" text="最新" vertical="middle" />

- `SystemBarsController` 的 `init` 方法新增 `defaultPaddings` 参数，可以配置初始化时是否自动添加对应的系统栏边距
- 在 `SystemBarsController` 中使用 `show` 或 `hide` 方法时将自动判断是否已经添加系统栏边距来决定是否添加对应的边距
- 新增 `AppComponentActivity`，继承于 `ComponentActivity`，可适用于 Jetpack Compose，无需设置 AppCompat 主题
- 使用 `AppBindingActivity`、`AppViewsActivity`、`AppComponentActivity` 时会自动将添加的布局背景颜色填充到父布局

### 1.0.2 | 2023.11.24 &ensp;<Badge type="warning" text="过旧" vertical="middle" />

- 重构 `SystemBarsController` 中的 System Insets 功能修复在 Compose View 上无法计算布局最大尺寸问题
- `SystemBarsController` 中新增 `SystemInsets.Paddings` 和 `setBaseBackgroundResource`、`isVisible` 功能
- 作废了 `SystemBarsView`，请开始使用新的方式自定义 System Insets 功能
- 新增 `View.applySystemInsets`、`View.appendSystemInsets`、`View.removeSystemInsets` 方法

### 1.0.1 | 2023.11.18 &ensp;<Badge type="warning" text="过旧" vertical="middle" />

- 修复 `SystemBarsController` 中存在的装载异常捕获问题

### 1.0.0 | 2023.11.02 &ensp;<Badge type="warning" text="过旧" vertical="middle" />

- 首个版本提交至 Maven

## ui-extension

### 1.0.1 | 2023.11.18 &ensp;<Badge type="tip" text="最新" vertical="middle" />

- 修复 ResourcesFactory 中 `getColor` 与 `getColorStateList` 方法返回值错误问题

### 1.0.0 | 2023.11.02 &ensp;<Badge type="warning" text="过旧" vertical="middle" />

- 首个版本提交至 Maven

## system-extension

### 1.0.1 | 2024.01.02 &ensp;<Badge type="tip" text="最新" vertical="middle" />

- 更名 `SystemVersion` 中的部分命名不正确方法
- `SystemVersion` 中新增 `name` 属性
- 缓存 `SystemKind` 的检测结果防止重复创建反射对象
- 作废了 `SystemKind` 中的 `get`，现在请使用 `current` 属性获取当前系统类型
- 更名 `ApplicationFactoy` 中的部分命名不正确方法和类
- 移除了 `AplicationInfoFlagsWrapper` 中的 `FLAG` 前缀
- 修改了 `BroadcastFactory` 中的相关用法，现在请参考文档使用新用法创建运行时广播
- 修改了 `ClipboardFactory` 中的相关用法，现在请参考文档使用新用法使用剪贴板功能
- 对 `ServiceFactory` 中的方法进行了 `inline` 处理
- `ServiceFactory` 中新增 `startServiceOrElse`、`startForegroundServiceOrElse` 方法

### 1.0.0 | 2023.11.02 &ensp;<Badge type="warning" text="过旧" vertical="middle" />

- 首个版本提交至 Maven

## permission-extension

暂未发布。

## compose-extension

### 1.0.0 | 2024.01.02 &ensp;<Badge type="tip" text="最新" vertical="middle" />

- 首个版本提交至 Maven

## compose-multiplatform

### 0.1.0 | 2024.01.02 &ensp;<Badge type="tip" text="最新" vertical="middle" />

- 首个版本提交至 Maven