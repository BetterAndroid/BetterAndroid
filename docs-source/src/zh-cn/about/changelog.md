# 更新日志

> 这里记录了 `BetterAndroid` 的版本更新历史。

::: danger

我们只会对最新的依赖进行维护，若你正在使用过时的依赖则代表你自愿放弃一切维护的可能性。

:::

## ui-component

### 1.0.6 | 2025.01.25 &ensp;<Badge type="tip" text="最新" vertical="middle" />

- `AppBindingActivity` 中新增 `onPrepareContentView` 方法，可自定义装载布局前的操作
- 修复适配器构造器中可能出现数据集为空下标越界的问题
- `RecyclerAdapterBuilder` 中新增 `AdapterPosition` 实现动态下标功能
- 修复适配器构造器中使用布局 ID 装载时父布局提前装载造成的严重错误
- 新增自定义的 `RecyclerView` 布局管理器并修改默认的布局管理器以实现在添加了自定义的头部和末位布局时正确处理下标
- 新增 `RecyclerView.Adapter` 的 `wrapper` 扩展方法，通过包装实例实现在添加了自定义的头部和末位布局时正确处理下标
- 修复 `RecyclerAdapterBuilder` 中末位布局当做头部布局添加的严重错误

### 1.0.5 | 2024.03.08 &ensp;<Badge type="warning" text="过旧" vertical="middle" />

- 移除了所有扩展方法 `kt` 文件的 `Factory` 后缀
- 移除了上一个版本中已作废的文件
- 修复 `CommonAdapterBuilder` 中的 `onItemViewsClick`、`onItemViewsLongClick` 返回的下标和条目位置错误问题
- 调整适配器中添加的条目记录对象为 `LinkedHashSet` 以防止重复添加
- 开放 `RecyclerAdapterBuilder` 中的 `DEFAULT_VIEW_TYPE`
- 适配器中新增 `onBindItemId` 方法，可自定义 `getItemId` 的行为
- 适配器中现在允许无回调直接使用 `onBindViews` 绑定条目布局
- `RecyclerAdapterBuilder` 中新增 `onBindHeaderView`、`onBindFooterView` 方法
- 在 `OnBackPressedCallback` 中调用 `trigger` 方法后若未移除则将重新启用此回调事件

### 1.0.4 | 2024.01.02 &ensp;<Badge type="warning" text="过旧" vertical="middle" />

- `SystemBarsController` 已完全迁移到 Window Insets API，请参考文档开始使用新用法
- 重构了 `SystemBarsController` 并修改了初始化方法，请参考文档使用新的方式开始使用
- Window Insets 已从系统栏中完全分离为 `WindowInsetsWrapper`、`InsetsWrapper`、`InsetsFactory`
- 作废了全部有关系统栏中的 Window Insets 相关用法，现在请开始使用全新的 Window Insets API
- 重构了 `BackPressedController` 并对接到 `BackPressedDispatcher`，请参考文档开始使用新用法
- `AppViewsFragment` 新增使用构造方法传入 `layoutResId` 的装载布局方式
- 更名 `RecyclerCosmeticMacker` 为 `RecyclerCosmetic` 并对相关功能进行了泛型处理
- 开放了 `RecyclerView` 的预置装饰器，现在你可以手动创建它们
- 重构了大量有关适配器的功能，现在你可以使用每个适配器的同名方法手动进行创建 (`BaseAdapter` 为 `CommonAdapter`)
- 重构了通知相关功能，请参考文档开始使用新用法
- 进行了一些其它功能的更名和修改

### 1.0.3 | 2023.12.03 &ensp;<Badge type="warning" text="过旧" vertical="middle" />

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

### 1.0.6 | 2025.03.04 &ensp;<Badge type="tip" text="最新" vertical="middle" />

- 调整 `LayoutInflater` 扩展中的 `inflate` 方法和 `attachToParent` 参数的默认行为
- 修正 `View` 扩展中的 ~~`updateMargin`~~ 方法为 `updateMargins` 并添加 `setMargins` 方法
- 完全移除旧版 `ViewLayoutParams` 类，现在请使用 `ViewLayoutParams` 方法代替
- 修复 `ViewLayoutParams` 相关逻辑，现在它可以创建任意基于 `ViewGroup.LayoutParams` 的对象
- 重载 `ViewLayoutParams` 方法以支持直接传入 `lpClass` 参数

### 1.0.5 | 2025.01.25 &ensp;<Badge type="warning" text="过旧" vertical="middle" />

- 修复 `TextView` 扩展中的 `updateText` 方法可能在重写了 `setText` 方法后设置的文本光标下标越界的问题
- 修改 `TextView` 扩展中 `setDigits` 方法的 `inputType` 参数默认从 `TextView` 自身读取
- 修改 `TextView` 扩展中 `updateTypeface` 方法的 `tf` 参数默认为 `null`，防止设置非预期的字体样式
- `TextView` 扩展中新增 `textToString`、`hintToString` 方法
- 修复 `Toast` 扩展中非主线程可能导致无法弹出 `Toast` 的问题
- `Fragment` 扩展中新增 `parentFragment` 泛型方法
- `Activity` 扩展中 `startActivity` 方法新增 `options` 参数
- 新增 `LifecycleOwner` 扩展
- 新增 `Coroutines` 协程功能相关扩展
- `View` 扩展中新增 `setIntervalOnClickListener` 方法
- `View` 扩展中新增 `child`、`childOrNull`、`firstChild`、`firstChildOrNull`、`lastChild`、`lastChildOrNull` 非泛型实现方法
- 修正 `Resources` 中的方法命名 ~~`isThemeAttrsIdsValueEquals`~~ 为 `areThemeAttrsIdsValueEquals`
- `Resources` 扩展中新增大量针对 `TypedArray` 的扩展方法
- **<u>⚠️ 破坏性更新</u>**，大量重构 `Fragment` 扩展中的方法，并独立出 `FragmentTransaction` 方法，**<u>迁移可能需要成本</u>**
- `Bitmap` 扩展中新增 `FileDescriptor.decodeToBitmap` 和 `FileDescriptor.decodeToBitmapOrNull` 方法
- 新增 `RecyclerView` 扩展
- `View` 扩展中新增 `tag`、`setTag`、`animate` 方法
- `View` 扩展中新增 `tooltipTextCompat` 方法
- `ViewBindingBuilder` 中的 `toString` 方法现在将返回 `ViewBinding` 所持有的完整对象名称
- 新增 `Adapter` 扩展

### 1.0.4 | 2024.05.05 &ensp;<Badge type="warning" text="过旧" vertical="middle" />

- 整合 `Fragment` 扩展中的进入、退出动画方法参数
- 移除默认的 `Fragment` 扩展中的过渡动画，并删除相关资源文件
- `ViewBinding` 扩展中新增 `Fragment.viewBinding` 方法
- 修复 `ViewBinding` 扩展中 `viewBinding` 装载的布局不会自动缓存的问题
- 修复 `ViewBinding` 扩展中 `inflate` 方法遇到 `<merge>` 和 `<include>` 类型布局不能正确装载的问题
- 调整 `ViewBinding` 扩展中 `inflate` 方法遇到 `<merge>` 和 `<include>` 类型布局将忽略 `attachToParent` 参数
- 移除旧版 `ViewBinding` 扩展中已弃用的全部方法

### 1.0.3 | 2024.03.08 &ensp;<Badge type="warning" text="过旧" vertical="middle" />

- 移除了所有扩展方法 `kt` 文件的 `Factory` 后缀
- 移除了上一个版本中已作废的文件
- `ViewFactory` 的 `ViewLayoutParams` 方法新增支持 `AbsListView.LayoutParams` 类型
- `ViewFactory` 中新增 `updateCompoundDrawables`、`updateCompoundDrawablesWithIntrinsicBounds` 方法
- `ViewFactory` 中新增 `parent`、`parentOrNull` 方法
- `ViewFactory` 中新增 `walkToRoot`、`walkThroughChildren`、`indexOfInParent` 方法
- `ViewFactory` 中新增 `child`、`childOrNull`、`firstChild`、`lastChild`、`firstChildOrNull`、`lastChildOrNull` 方法
- 合并 `ViewFactory` 中的 `inflate` 方法到 `LayoutInflaterFactory` 中并作废原方法
- `FragmentFactory` 中的添加方法新增 `addToBackStack` 参数
- `FragmentFactory` 中作废了 `commitTransaction` 方法，现已迁移到官方提供的 `fragment-ktx` 依赖
- `ResourcesFactory` 中新增 `themeResId` 方法
- 新增全新的 `ViewBinding` 解决方案并弃用旧版方案

### 1.0.2 | 2024.01.02 &ensp;<Badge type="warning" text="过旧" vertical="middle" />

- 移动了部分不规范分类的包名，如遇到报错，请重新对这些调用的方法进行 `import` 操作
- 修改了针对 Java 调用的相关名称
- `ViewFactory` 中新增可设置横向纵向的 `updatePadding` 方法
- `ViewFactory` 中的 `inflate` 方法将自动使用 `Activity.getLayoutInflater`
- 更名 `ColorsFactory` 为 `ColorFactory`
- 更名 `ColorFactory` 中的 `toMixColor` 为 `mixColorOf`
- `ColorFactory` 中的部分方法现在将不会再抛出异常
- 新增 `GradientDrawableCompat`，你可以使用其创建 Android 10 以下版本的 `GradientDrawable`
- `DrawableFactory` 中新增可设置横向纵向的 `updatePadding` 方法
- 作废了 `DimensionFactory` 中的 `asDp`、`asPx` 及其用法因为它设计错误，现已对接到新用法 `toDp`、`toPx`
- 新增 `DisplayDensity`，你可以方便地使用 `dp`、`px` 来转换单位
- 作废了 `ResourcesFactory` 中的 `getDimensionPx`，请手动使用 `toPx` 进行计算
- 作废了 `ResourcesFactory` 及 `ActivityFactory` 中的 `isSpecialWindowingMode`，请不要再使用
- `ResourcesFactory` 中新增 `getFontCompat` 方法
- 修复了 `FragmentFactory` 中的 `commt` 相关功能用法错误
- 大量修改了 `FragmentFactory` 中的相关用法，现在请参考文档开始使用新用法
- `BitmapFactory` 中新增 `compressBitmap` 方法
- `ToastFactory` 中新增 `Window.toast` 方法
- `ToastFactory` 中的 `toast` 方法新增 `allowBackgroud` 参数，你现在可以允许在非主线程中弹出 `Toast`
- `WindowFactory` 中的部分方法现在将不会再抛出异常
- `WindowFactory` 中新增可使用 `Float` 类型设置屏幕亮度的方法
- `ViewFactory` 中使用 `WindowInsetsController` 来显示或隐藏输入法并更名方法为 `showIme`、`hideIme`
- 作废了 `ViewLayoutParam` 类，现在请使用 `ViewLayoutParams` 方法代替
- `ViewFactory` 中的 `performKeyPressed` 新增 `duration` 参数
- `ViewFactory` 中新增可设置横向纵向的 `updateMargin` 方法
- 对 `ActivityFactory` 中的方法进行了 `inline` 处理
- `ActivityFactory` 中新增每个 `startActivityOrElse` 方法
- `BimapFactory` 中的 `round` 方法新增支持设置每个方向的圆角半径大小

### 1.0.1 | 2023.11.18 &ensp;<Badge type="warning" text="过旧" vertical="middle" />

- 修复 ResourcesFactory 中 `getColor` 与 `getColorStateList` 方法返回值错误问题

### 1.0.0 | 2023.11.02 &ensp;<Badge type="warning" text="过旧" vertical="middle" />

- 首个版本提交至 Maven

## system-extension

### 1.0.2 | 2025.01.25 &ensp;<Badge type="tip" text="最新" vertical="middle" />

- `Broadcast` 扩展中新增 `BroadcastReceiver` 方法并修改 `registerReceiver` 方法的返回值为其自身

### 1.0.1 | 2024.01.02 &ensp;<Badge type="warning" text="过旧" vertical="middle" />

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

### 1.0.2 | 2024.01.16 &ensp;<Badge type="tip" text="最新" vertical="middle" />

- 新增 `HapticFeedback` 方法，你可以将其设置到任意的点击、触摸事件上
- `ComponentPadding` 新增 `None` 方法，你可以使用其直接得到 `ComponentPadding(0.dp)`
- 修改了 `Dialog` 中的 `onDismissRequest` 为强制要求存在，匹配官方提供的 `foundation` 中的用法
- 部分代码风格优化

### 1.0.1 | 2024.01.08 &ensp;<Badge type="warning" text="过旧" vertical="middle" />

- 移除部分无用的 `@Stable` 注解
- 新增 `borderOrElse` 的同名方法和新用法
- 新增 `AdaptiveRow`、`AdaptiveColumn`

### 1.0.0 | 2024.01.02 &ensp;<Badge type="warning" text="过旧" vertical="middle" />

- 首个版本提交至 Maven

## compose-multiplatform

### 0.1.0 | 2024.01.02 &ensp;<Badge type="tip" text="最新" vertical="middle" />

- 首个版本提交至 Maven