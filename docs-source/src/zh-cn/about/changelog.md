# 更新日志

> 这里记录了 `BetterAndroid` 的版本更新历史。

::: danger

我们只会对最新的依赖进行维护，若你正在使用过时的依赖则代表你自愿放弃一切维护的可能性。

:::

## Android

### 1.1.4 | 2026.07.02 &ensp;<Badge type="tip" text="最新" vertical="middle" />

- 修复部分模块 Lint 自动修复功能的 BUG

#### ui-extension

- 添加 `AxisPadding` 支持，现在其可以支持横向、纵向内边距场景
- 添加 `ViewPadding` 的 `horizontal`、`relativeHorizontal`、`vertical` 属性，支持横向、纵向内边距场景

### 1.1.3 | 2026.06.06 &ensp;<Badge type="warning" text="过旧" vertical="middle" />

- 更新 [KavaRef](https://github.com/HighCapable/KavaRef) 依赖支持以实现完整适配 Min SDK 21
- 优化部分模块的 `@JvmOverloads` 注解使用
- 将部分模块的函数进行内联优化，减少调用开销和可能的冗余包装
- 限定 Lint 功能作用范围为 Kotlin，Java 代码将被排除

### 1.1.2 | 2026.06.03 &ensp;<Badge type="warning" text="过旧" vertical="middle" />

- 修复部分模块 Lint 自动修复功能的 BUG

#### system-extension

- 修复 `AndroidVersion` 中可能对华为设备产生 `Class` 抛出异常的严重问题

### 1.1.1 | 2026.05.30 &ensp;<Badge type="warning" text="过旧" vertical="middle" />

- 修复部分模块 Lint 自动修复功能的 BUG

#### ui-component

- 移除 `AndroidManifest.xml` 中的额外 `meta-data` 以防止配置污染

#### system-extension

- 优化 `AndroidVersion`，移除无用的 `lazy` 并对条件进行 `inline` 处理

### 1.1.0 | 2026.05.27 &ensp;<Badge type="warning" text="过旧" vertical="middle" />

- 更新 Target SDK 至 37 (Android 17)
- 移除所有模块中弃用已经超过半年左右的 API
- 完善所有模块的 Lint 功能

#### ui-component

- **<u>⚠️ 破坏性更新</u>**，移除 `BackPressedController`、`IBackPressedController` 相关系统事件能力，请迁移到 [ui-extension → BackPressed 扩展](../library/ui-extension.md#backpressed-扩展) 或参考 [配置 → 迁移指南 → 迁移 BackPressedController](../config/migration.md#迁移-backpressedcontroller)
- 补全 `NotificationBuilder` 中更多与 `NotificationCompat.Builder` 对齐的功能
- 优化 `SystemBarsCompat` 中旧版系统状态栏深色模式适配相关的反射查找缓存策略，减少重复解析开销
- 优化 `SystemBarsController` 中系统栏样式设置逻辑，避免重复应用相同样式带来的额外开销
- 优化 `NotificationPoster` 中通知通道与分组的创建策略，减少重复系统调用开销
- 优化 `NotificationPoster` 中通知超时任务的主线程 `Handler` 复用策略，减少重复对象创建开销
- `AppBindingActivity`、`AppBindingFragment` 新增可重写的 `onInflateBinding` 方法
- 调整 `AppBindingFragment` 的 `binding` 生命周期管理，现仅在 `onCreateView` 到 `onDestroyView` 之间可用，并会在 `onDestroyView` 时自动释放
- `BaseFragment` 中新增 `systemBarsOrNull`，可在宿主未实现 `ISystemBarsController` 时安全判断当前系统栏控制器是否可用
- 调整 `NotificationPoster.cancel` 为幂等行为，并为通知通道和通知分组缓存增加线程安全与按包名隔离的键策略
- 修复 `NotificationChannelWrapper` 中 `isLightsEnabled` 错误映射到 `setShowBadge` 的问题

#### ui-component-adapter

- 优化 `RecyclerAdapterBuilder`、`BaseAdapterBuilder`、`PagerAdapterBuilder` 中数据源快照与条目绑定逻辑，减少一次绑定链路中的重复数据读取与状态漂移风险
- 优化 `PagerAdapterBuilder` 中页面 `ViewHolder` 的缓存回收策略，销毁页面后会同步释放缓存并在缓存视图状态异常时自动重建
- `RecyclerView.bindAdapter` 新增通过 `applyCosmetic` 管理 `RecyclerCosmetic` 的行为，重复绑定时将默认替换上一次注入的装饰器，避免间距叠加
- `RecyclerAdapterBuilder` 在配置 `onBindItemId` 后现会自动启用 Stable IDs
- `CommonAdapter` 中 `AutoCompleteTextView` 绑定适配器现已直接调用公开 `setAdapter`，移除反射兜底依赖
- `FragmentPagerAdapter`、`FragmentStateAdapter` 新增直接接收 `FragmentActivity`、`Fragment` 的强类型构造入口
- 修复 `RecyclerAdapterWrapper` 中区间通知方法错误换算 `itemCount` 的问题
- 修复 `BaseRecyclerItemDecoration` 在 `NO_POSITION` 与空列表阶段仍继续计算 `offset` 的问题
- `RecyclerAdapterBuilder` 中新增 `onBindDiffer`，支持通过 `RecyclerAsyncDiffer` 将差分提交流程直接接入适配器构建
- 新增可独立使用的 `RecyclerAsyncDiffer`，支持手动创建差分提交实例并复用 `RecyclerView.Adapter` 现有更新链路
- `RecyclerAdapterWrapper` 中新增 `differ` 与 `submitList`，可在配置了 `onBindDiffer` 后直接提交新列表
- 调整 `CommonAdapter` 中 `BaseAdapter`、`PagerAdapter`、`RecyclerAdapter` 相关构造与绑定入口的返回类型及注释，对齐各 Builder 实际返回的 `Instance`
- `RecyclerAdapter` 扩展中新增 `notifyByDiff` 方法，可通过 `DiffUtil` 根据前后数据集差异自动通知更新
- `RecyclerAdapter` 扩展中新增 `DiffUtilCallback` 与 `ListUpdateCallback` 方法，支持手动构建 `DiffUtil.Callback` 与 `ListUpdateCallback`

#### ui-extension

- 新增 `BackPressed` 扩展，提供 `Fragment`、`View` 的 `onBackPressedDispatcher`、`OnBackPressedCallback` 以及 `trigger` 用法
- **<u>⚠️ 破坏性更新</u>**，移除 `SystemColors` 相关系统颜色功能，请参考 [配置 → 迁移指南 → 迁移 SystemColors](../config/migration.md#迁移-systemcolors)
- 移除 `ui-extension` 中对 `material` 的依赖
- 调整 `TextView` 扩展中的部分扩展功能，对齐官方 `TextView` 的行为并修复一些潜在的错误使用问题
- `View` 扩展中的 `walkToRoot`、`walkThroughChildren` 现已对齐官方 `ancestors`、`descendants` 的遍历语义
- **<u>⚠️ 破坏性更新</u>**，`walkToRoot`、`walkThroughChildren` 的返回值已由 `List` 调整为 `Sequence`，如需立即求值结果请补充 `toList()`
- 新增 `Lifecycle` 扩展，支持使用 lambda 快速创建 `DefaultLifecycleObserver`、`LifecycleEventObserver`，并直接通过 `Lifecycle.addObserver` 完成注册
- `LifecycleOwner` 扩展中新增 `Context.lifecycleOwner`、`Context.requireLifecycleOwner` 方法
- 新增 `Context` 扩展，提供 `Context.hostActivity`、`Context.requireHostActivity` 及对应泛型用法
- 调整 `View.lifecycleOwner` 的查找逻辑，现会优先对接 `findViewTreeLifecycleOwner`，并在无法获取时回退到 `Context`
- 调整 `View.tooltipTextCompat` 在低版本系统中的兼容逻辑，现会真实调用 `TooltipCompat` 模拟完整行为，同时继续保留工具提示文本的 tag 存储能力
- `View` 扩展中新增 `View.padding`、`ViewPadding`、`AbsolutePadding`、`RelativePadding`、`PaddingValues`
- `View` 扩展中的 `ViewLayoutParams` 新增 `KClass` 重载，可直接使用 `LinearLayout.LayoutParams::class` 这类写法创建目标 `LayoutParams`
- 为 `Insets` 与 `InsetsWrapper` 新增 `toPaddingValues`，可直接转换为 `AbsolutePadding`
- 调整 `setInsetsPadding`、`updateInsetsPadding` 的内部计算模型，新增 `syncInsetsPadding`，现会基于用户原始 `padding` 基线与当前 Insets `padding` 叠加计算结果
- 优化 `Resources` 扩展中主题属性解析的对象复用方式，调整为基于 `ThreadLocal` 的 `TypedValue` 实现
- 优化 `Resources` 扩展中的 `areThemeAttrsIdsValueEquals` 比较逻辑，减少重复的主题属性解析与类型探测行为
- 移除 `ColorStateList` 的全局缓存实现，避免不同 `Theme` 与 `Configuration` 下的潜在结果复用问题
- 新增 `WindowInsetsLayout`，可通过 XML 配置自动监听 Window Insets 变化并将目标 Insets 应用到布局 `padding`
- 优化 `ViewBinding` 构建过程中的反射方法查找逻辑，新增 `bind` 与 `inflate` 方法缓存以减少重复解析开销
- 优化 `WindowInsetsWrapperCompat` 中旧版异形屏适配相关的反射查找缓存策略，减少重复解析开销
- 调整 `Fragment.viewBinding` 委托的生命周期管理，缓存的 binding 现会跟随 Fragment View 生命周期自动清空
- 调整 `toast(..., allowBackground = true)`，现会投递回主线程执行，不再创建后台 `Looper`
- 调整 `View.showIme` 的兼容行为，改为通过焦点与 `InputMethodManager.showSoftInput` 发起隐式显示请求
- 修复 `View.performTouch` 未回收生成的 `MotionEvent` 实例的问题
- Fragment `attach` 与 `replace` 新增 `generateViewId` 参数，并默认保持自动生成容器 ID 的行为
- 为 `Bitmap.shrink` 与 `Bitmap.reduce` 增加边界保护，避免产生非法目标尺寸

#### system-extension

- `AndroidVersion` 新增 Android 17 的版本常量
- 补全 `RomType` 中更多 ROM 类型常量与识别逻辑，现已支持 `RealmeUI`、`MagicOS`、`OxygenOS`、`H2OS`、`ZUXOS`、`NebulaAIOS`、`MyOS`、`MifavorUI`、`SmartisanOS`、`EUI`、`360OS`、`HarmonyOS NEXT` 等系统判断
- 优化 `RomType.current` 的 ROM 类型匹配结果缓存策略，减少重复判断开销
- 修复 `SystemProperties` 中 `Boolean` 方法绑定错误的问题
- 修复 `startServiceOrElse`、`startForegroundServiceOrElse` 成功判定的假阳性问题
- `Intent` 扩展中新增针对指定组件快速创建 `Intent` 的泛型方法
- 重构 `Clipboard` 扩展，独立 `ClipDataBuilder` 并支持链式调用与 `build` 方法
- `Clipboard` 扩展中新增 `Uri.resolveMimeTypes` 方法，可通过 `ContentResolver` 解析 `Uri` 的 MIME 类型
- `Clipboard` 扩展中的 `addUri` 与 `copy` 方法新增支持传入多个 MIME 类型
- `ClipboardManager` 新增 `primaryClipItems`、`primaryClipItemsOrNull`、`firstPrimaryClipItem`、`firstPrimaryClipItemOrNull` 方法
- 优化 `Clipboard` 扩展中的剪贴板条目读取逻辑，减少不必要的列表创建开销
- 优化 `ApplicationInfo` 中 CPU ABI 相关反射字段的查找缓存策略，减少重复解析开销
- 移除了旧包名 `tool` 下的所有内容

### 历史版本

#### ui-component

##### 1.0.10 | 2025.12.16 &ensp;<Badge type="warning" text="过旧" vertical="middle" />

- 适配 Kotlin 2.2+

##### 1.0.9 | 2025.12.14 &ensp;<Badge type="warning" text="过旧" vertical="middle" />

- 破坏性更改：迁移 Insets 相关功能至 [ui-extension](#ui-extension)

##### 1.0.8 | 2025.08.03 &ensp;<Badge type="warning" text="过旧" vertical="middle" />

- 移除适配器相关功能，统一合并至 [ui-component-adapter](#ui-component-adapter)
- 将 Java 反射相关行为由 [YukiReflection](https://github.com/HighCapable/YukiReflection) 迁移至 [KavaRef](https://github.com/HighCapable/KavaRef)

##### 1.0.7 | 2025.03.08 &ensp;<Badge type="warning" text="过旧" vertical="middle" />

- 移除 Material 依赖并解构为对应依赖
- 大量重构适配器 (Adapter) 底层相关的 API
- 移除适配器不合理的 `View` 实例装载条目方法
- 适配器加入 `ViewHolderDelegate` 以支持自定义布局装载器
- `AdapterPosition` 现已支持 `layout` 和 `absolute` 下标
- 其它适配器相关的已知问题修复

##### 1.0.6 | 2025.01.25 &ensp;<Badge type="warning" text="过旧" vertical="middle" />

- `AppBindingActivity` 中新增 `onPrepareContentView` 方法，可自定义装载布局前的操作
- 修复适配器构造器中可能出现数据集为空下标越界的问题
- `RecyclerAdapterBuilder` 中新增 `AdapterPosition` 实现动态下标功能
- 修复适配器构造器中使用布局 ID 装载时父布局提前装载造成的严重错误
- 新增自定义的 `RecyclerView` 布局管理器并修改默认的布局管理器以实现在添加了自定义的头部和末位布局时正确处理下标
- 新增 `RecyclerView.Adapter` 的 `wrapper` 扩展方法，通过包装实例实现在添加了自定义的头部和末位布局时正确处理下标
- 修复 `RecyclerAdapterBuilder` 中末位布局当做头部布局添加的严重错误

##### 1.0.5 | 2024.03.08 &ensp;<Badge type="warning" text="过旧" vertical="middle" />

- 移除了所有扩展方法 `kt` 文件的 `Factory` 后缀
- 移除了上一个版本中已作废的文件
- 修复 `CommonAdapterBuilder` 中的 `onItemViewsClick`、`onItemViewsLongClick` 返回的下标和条目位置错误问题
- 调整适配器中添加的条目记录对象为 `LinkedHashSet` 以防止重复添加
- 开放 `RecyclerAdapterBuilder` 中的 `DEFAULT_VIEW_TYPE`
- 适配器中新增 `onBindItemId` 方法，可自定义 `getItemId` 的行为
- 适配器中现在允许无回调直接使用 `onBindViews` 绑定条目布局
- `RecyclerAdapterBuilder` 中新增 `onBindHeaderView`、`onBindFooterView` 方法
- 在 `OnBackPressedCallback` 中调用 `trigger` 方法后若未移除则将重新启用此回调事件

##### 1.0.4 | 2024.01.02 &ensp;<Badge type="warning" text="过旧" vertical="middle" />

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

##### 1.0.3 | 2023.12.03 &ensp;<Badge type="warning" text="过旧" vertical="middle" />

- `SystemBarsController` 的 `init` 方法新增 `defaultPaddings` 参数，可以配置初始化时是否自动添加对应的系统栏边距
- 在 `SystemBarsController` 中使用 `show` 或 `hide` 方法时将自动判断是否已经添加系统栏边距来决定是否添加对应的边距
- 新增 `AppComponentActivity`，继承于 `ComponentActivity`，可适用于 Jetpack Compose，无需设置 AppCompat 主题
- 使用 `AppBindingActivity`、`AppViewsActivity`、`AppComponentActivity` 时会自动将添加的布局背景颜色填充到父布局

##### 1.0.2 | 2023.11.24 &ensp;<Badge type="warning" text="过旧" vertical="middle" />

- 重构 `SystemBarsController` 中的 System Insets 功能修复在 Compose View 上无法计算布局最大尺寸问题
- `SystemBarsController` 中新增 `SystemInsets.Paddings` 和 `setBaseBackgroundResource`、`isVisible` 功能
- 作废了 `SystemBarsView`，请开始使用新的方式自定义 System Insets 功能
- 新增 `View.applySystemInsets`、`View.appendSystemInsets`、`View.removeSystemInsets` 方法

##### 1.0.1 | 2023.11.18 &ensp;<Badge type="warning" text="过旧" vertical="middle" />

- 修复 `SystemBarsController` 中存在的装载异常捕获问题

##### 1.0.0 | 2023.11.02 &ensp;<Badge type="warning" text="过旧" vertical="middle" />

- 首个版本提交至 Maven

#### ui-component-adapter

##### 1.0.1 | 2025.12.16 &ensp;<Badge type="warning" text="过旧" vertical="middle" />

- 适配 Kotlin 2.2+
- 移除了部分内联方法不必要的 `reified` 关键字

##### 1.0.0 | 2025.08.03 &ensp;<Badge type="warning" text="过旧" vertical="middle" />

- 首个版本提交至 Maven

#### ui-extension

##### 1.0.9 | 2025.12.16 &ensp;<Badge type="warning" text="过旧" vertical="middle" />

- 适配 Kotlin 2.2+
- `View` 扩展中的 `ViewLayoutParams` 调整 `width` 和 `height` 参数的默认值为 `null`，不再采用 `-2` 作为不设置宽高的默认值

##### 1.0.8 | 2025.12.14 &ensp;<Badge type="warning" text="过旧" vertical="middle" />

- 新增 [ui-component](#ui-component) Insets 相关功能
- Insets 扩展 `handleOnWindowInsetsChanged` 方法新增 `requestApplyOnLayout` 参数
- 新增 Lint 功能 (实验性)

##### 1.0.7 | 2025.08.03 &ensp;<Badge type="warning" text="过旧" vertical="middle" />

- 移除 `Adapter` 相关扩展，统一合并至 [ui-component-adapter](#ui-component-adapter)
- 将 Java 反射相关行为由 [YukiReflection](https://github.com/HighCapable/YukiReflection) 迁移至 [KavaRef](https://github.com/HighCapable/KavaRef)
- `Resources` 扩展中调整 `isUiInNightMode` 底层逻辑对接到 `isNightModeActive` 方法
- 作废 `Resources` 扩展中的 ~~`obtainStyledAttributes`~~ 方法，现在推荐迁移到 Jetpack 的 `withStyledAttributes`
- `View` 扩展新增 `updateMarginsRelative` 方法
- 作废 `Bitmap` 扩展中的 ~~`zoom`~~ 方法，现在推荐迁移到 Jetpack 的 `scale` 方法
- `Bitmap` 扩展中对 `round` 新增 `backgroundColor` 参数
- 作废 `Bitmap` 扩展中的 ~~`compress`~~ 请使用 `shrink` 方法代替

##### 1.0.6 | 2025.03.04 &ensp;<Badge type="warning" text="过旧" vertical="middle" />

- 调整 `LayoutInflater` 扩展中的 `inflate` 方法和 `attachToParent` 参数的默认行为
- 修正 `View` 扩展中的 ~~`updateMargin`~~ 方法为 `updateMargins` 并添加 `setMargins` 方法
- 完全移除旧版 `ViewLayoutParams` 类，现在请使用 `ViewLayoutParams` 方法代替
- 修复 `ViewLayoutParams` 相关逻辑，现在它可以创建任意基于 `ViewGroup.LayoutParams` 的对象
- 重载 `ViewLayoutParams` 方法以支持直接传入 `lpClass` 参数

##### 1.0.5 | 2025.01.25 &ensp;<Badge type="warning" text="过旧" vertical="middle" />

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

##### 1.0.4 | 2024.05.05 &ensp;<Badge type="warning" text="过旧" vertical="middle" />

- 整合 `Fragment` 扩展中的进入、退出动画方法参数
- 移除默认的 `Fragment` 扩展中的过渡动画，并删除相关资源文件
- `ViewBinding` 扩展中新增 `Fragment.viewBinding` 方法
- 修复 `ViewBinding` 扩展中 `viewBinding` 装载的布局不会自动缓存的问题
- 修复 `ViewBinding` 扩展中 `inflate` 方法遇到 `<merge>` 和 `<include>` 类型布局不能正确装载的问题
- 调整 `ViewBinding` 扩展中 `inflate` 方法遇到 `<merge>` 和 `<include>` 类型布局将忽略 `attachToParent` 参数
- 移除旧版 `ViewBinding` 扩展中已弃用的全部方法

##### 1.0.3 | 2024.03.08 &ensp;<Badge type="warning" text="过旧" vertical="middle" />

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

##### 1.0.2 | 2024.01.02 &ensp;<Badge type="warning" text="过旧" vertical="middle" />

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

##### 1.0.1 | 2023.11.18 &ensp;<Badge type="warning" text="过旧" vertical="middle" />

- 修复 ResourcesFactory 中 `getColor` 与 `getColorStateList` 方法返回值错误问题

##### 1.0.0 | 2023.11.02 &ensp;<Badge type="warning" text="过旧" vertical="middle" />

- 首个版本提交至 Maven

#### system-extension

##### 1.0.4 | 2025.12.16 &ensp;<Badge type="warning" text="过旧" vertical="middle" />

- 适配 Kotlin 2.2+
- 修复 `Clipboard` 扩展中在创建 `ClipData` 时非空判断错误的问题
- 调整并新增 `Clipboard` 扩展中的 `copy` 直接创建 DSL 功能
- 修正 `ClipDataItemBuilder` 中的方法命名 ~~`addText`~~ 为 `addPlainText`
- 移动包名 `tool` 到 `utils`
- 新增 Lint 功能 (实验性)

##### 1.0.3 | 2025.08.03 &ensp;<Badge type="warning" text="过旧" vertical="middle" />

- 将 Java 反射相关行为由 [YukiReflection](https://github.com/HighCapable/YukiReflection) 迁移至 [KavaRef](https://github.com/HighCapable/KavaRef)
- ~~`SystemVersion`~~ 重命名为 `AndroidVersion` 并新增 Android 15、16 以及 1-4.3 的版本常量，同时进行功能性调整
- ~~`SystemKind`~~ 重命名为 `RomType` 并进行功能性调整
- `Broadcast` 扩展中新增 `sendBroadcast` 方法的 `options` 参数

##### 1.0.2 | 2025.01.25 &ensp;<Badge type="warning" text="过旧" vertical="middle" />

- `Broadcast` 扩展中新增 `BroadcastReceiver` 方法并修改 `registerReceiver` 方法的返回值为其自身

##### 1.0.1 | 2024.01.02 &ensp;<Badge type="warning" text="过旧" vertical="middle" />

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

##### 1.0.0 | 2023.11.02 &ensp;<Badge type="warning" text="过旧" vertical="middle" />

- 首个版本提交至 Maven

## Jetpack Compose

### compose-extension

#### 1.0.3 | 2026.05.27 &ensp;<Badge type="tip" text="最新" vertical="middle" />

- 作废 `ComponentPadding` 中的 ~~`None`~~，请迁移到 `Zero`
- 作废 `Foundation` 中的 ~~`HapticFeedback`~~，请迁移到 `hapticFeedback`

#### 1.0.2 | 2024.01.16 &ensp;<Badge type="warning" text="过旧" vertical="middle" />

- 新增 `HapticFeedback` 方法，你可以将其设置到任意的点击、触摸事件上
- `ComponentPadding` 新增 `None` 方法，你可以使用其直接得到 `ComponentPadding(0.dp)`
- 修改了 `Dialog` 中的 `onDismissRequest` 为强制要求存在，匹配官方提供的 `foundation` 中的用法
- 部分代码风格优化

#### 1.0.1 | 2024.01.08 &ensp;<Badge type="warning" text="过旧" vertical="middle" />

- 移除部分无用的 `@Stable` 注解
- 新增 `borderOrElse` 的同名方法和新用法
- 新增 `AdaptiveRow`、`AdaptiveColumn`

#### 1.0.0 | 2024.01.02 &ensp;<Badge type="warning" text="过旧" vertical="middle" />

- 首个版本提交至 Maven

### compose-multiplatform

#### 0.1.1 | 2026.05.27 &ensp;<Badge type="tip" text="最新" vertical="middle" />

- Android 平台中的 `BackHandler` 现已直接对接官方 `androidx.activity:activity-compose` 提供的实现

#### 0.1.0 | 2024.01.02 &ensp;<Badge type="warning" text="过旧" vertical="middle" />

- 首个版本提交至 Maven