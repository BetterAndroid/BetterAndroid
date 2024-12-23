# ui-extension

![Maven Central](https://img.shields.io/maven-central/v/com.highcapable.betterandroid/ui-extension?logo=apachemaven&logoColor=orange)
<span style="margin-left: 5px"/>
![Android Min SDK](https://img.shields.io/badge/Min%20SDK-21-orange?logo=android)

这是针对 UI (用户界面) 相关功能扩展的一个依赖。

## 配置依赖

你可以使用如下方式将此模块添加到你的项目中。

### SweetDependency (推荐)

在你的项目 `SweetDependency` 配置文件中添加依赖。

```yaml
libraries:
  com.highcapable.betterandroid:
    ui-extension:
      version: +
```

在你的项目 `build.gradle.kts` 中配置依赖。

```kotlin
implementation(com.highcapable.betterandroid.ui.extension)
```

### 传统方式

在你的项目 `build.gradle.kts` 中配置依赖。

```kotlin
implementation("com.highcapable.betterandroid:ui-extension:<version>")
```

请将 `<version>` 修改为此文档顶部显示的版本。

## 功能介绍

你可以 [点击这里](kdoc://ui-extension) 查看 KDoc。

### Activity 扩展

::: tip 本节内容

[Activity / Fragment → startActivity](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component/start-activity)

[Activity / Fragment → startActivityOrElse](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component/start-activity-or-else)

[Activity → isInMultiWindowModeCompat](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component/is-in-multi-window-mode-compat)

适用于 `Activity` 的扩展。

:::

我们需要启动另一个 `Activity` 时，需要使用 `Intent` 创建一个 `Intent(this, AnotherActivity::class.java)`，然后调用 `startActivity(intent)` 来启动。

这样写起来大概不太友好，于是 `BetterAndroid` 为 `Activity` 提供了扩展，现在你可以直接使用以下方式来启动另一个 `Activity`。

> 示例如下

```kotlin
// 假设这就是你的 Context
val context: Context
// 假设 AnotherActivity 就是你的目标 Activity
context.startActivity<AnotherActivity>()
// 你可以使用以下方式创建一个 Intent 对象
context.startActivity<AnotherActivity> {
    // 在这里添加一些额外的参数
    putExtra("key", "value")
}
// 如果你需要使用 Intent.FLAG_ACTIVITY_NEW_TASK 来启动另一个 Activity，
// 你可以直接这样使用
context.startActivity<AnotherActivity>(newTask = true)
// 如果你需要添加启动参数，你可以向其中添加 "options" 选项
// 例如我们需要一个共享元素动画
val options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity).toBundle()
context.startActivity<AnotherActivity>(options = options)
// 同样地，你也可以在一个 Fragment 中直接使用
// 假设这就是你的 Fragment
val fragment: Fragment
// 假设 AnotherActivity 就是你的目标 Activity
fragment.startActivity<AnotherActivity>()
```

如果你需要启动一个外部应用程序的 `Activity`，你可以使用以下方式。

> 示例如下

```kotlin
// 假设这就是你的 Context
val context: Context
// 假设你需要启动的应用程序包名为 com.example.app
// 假设你需要启动的 Activity 类名为 com.example.app.MainActivity
context.startActivity("com.example.app", "com.example.app.MainActivity")
// 你依然可以使用以下方式创建一个 Intent 对象
context.startActivity("com.example.app", "com.example.app.MainActivity") {
    // 在这里添加一些额外的参数
    putExtra("key", "value")
}
// Intent.FLAG_ACTIVITY_NEW_TASK 将会在此方法中默认设置，这是为了避免启动新的应用程序后顶栈重叠，
// 如果你不需要考虑这个问题，你可以将 newTask 参数设置为 false
context.startActivity("com.example.app", "com.example.app.MainActivity", newTask = false)
```

如果你不知道需要启动的应用程序的入口 `Activity` 名称，你可以使用包名直接启动它。

此方法内部将会使用 `getLaunchIntentForPackage` 来获取入口 `Activity`。

> 示例如下

```kotlin
// 假设这就是你的 Context
val context: Context
// 假设你需要启动的应用程序包名为 com.example.app
context.startActivity("com.example.app")
```

::: warning

此操作在 Android 11 及之后需要 `QUERY_ALL_PACKAGES` 权限或明确配置一个 `queries` 属性列表。

请参考 [Package visibility filtering on Android](https://developer.android.com/training/package-visibility)。

:::

::: tip

你可以使用 `startActivityOrElse` 来代替 `startActivity` 以判断 `Activity` 是否能够启动成功，启动失败时此方法不会抛出异常，而是返回 `false`。

:::

针对 Android 7.0 及之后的版本新增的分屏模式，`BetterAndroid` 为其提供了一个兼容扩展。

对于 `isInMultiWindowMode`，你无需考虑版本兼容问题，你只需要在后方加入一个 `Compat` 即可。

> 示例如下

```kotlin
// 假设这就是你的 Activity
val activity: Activity
// 获取当前是否处于分屏模式
val isInMultiWindowMode = activity.isInMultiWindowModeCompat
```

### Fragment 扩展

::: tip 本节内容

[Fragment → fragmentManager](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component/fragment-manager)

[Fragment → parentFragment](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component/parent-fragment)

[Fragment → findFragment](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component/find-fragment)

[FragmentTransaction](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component/-fragment-transaction)

[Fragment → attach](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component/attach)

[Fragment → detach](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component/detach)

[Fragment → replace](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component/replace)

[Fragment → show](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component/show)

[Fragment → hide](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component/hide)

适用于 `Fragment` 的扩展。

:::

`Fragment` 是官方提供的针对 `Activity` 的一个高效片段，但是它的使用方式并不是很友好。

为了对 `Fragment` 相关操作进行简化，`BetterAndroid` 为 `Fragment` 提供了一些实用的扩展功能。

`BetterAndroid` 会自动帮你引入 `androidx.fragment:fragment-ktx` 依赖，你可以 [参考这里](https://developer.android.com/kotlin/ktx#fragment) 以开始使用。

::: warning

在 `1.0.2` 及之前版本中的 `commitTransaction` 方法已被弃用，本着 “不重复造轮子” 的原则，请迁移到 `fragment-ktx` 依赖中的 `commit`、`commitNow` 方法。

在 `1.0.5` 及之后的版本中，我们合并了 `...ToActivity`、`...ToFragment` 方法并改进了使用方式，请进行迁移。

:::

获取存在的 `FragmentManager`。

`BetterAndroid` 为 `FragmentActivity` 和 `Fragment` 提供了一个更加友好的方式来获取存在的 `FragmentManager`。

> 示例如下

```kotlin
// 假设这就是你的 FragmentActivity
val activity: FragmentActivity
// 假设这就是你的 Fragment
val fragment: Fragment
// 从 FragmentActivity 获取 FragmentManager
val fragmentManager = activity.fragmentManager()
// 从 Fragment 获取 FragmentManager
val fragmentManager = fragment.fragmentManager()
// 对于 Fragment，你可以设置参数 parent 为 true 来获取父 FragmentManager
val parentFragmentManager = fragment.fragmentManager(parent = true)
```

使用泛型的方式来获取父级 `Fragment`。

`BetterAndroid` 为 `Fragment` 提供了一个更加友好的方式来获取父级 `Fragment`。

它可以自动帮你把找到的 `Fragment` 转换为当前类型而不需要再使用 `as` 的形式进行强制转换。

你完全不需要担心找不到或者类型错误的情况，遇到这种情况将会返回 `null`。

> 示例如下

```kotlin
// 假设这就是你的 Fragment
val fragment: Fragment
// 通过泛型的方式获取父 Fragment
val parentFragment = fragment.parentFragment<YourParentFragment>()
```

使用泛型的方式来查找一个存在的 `Fragment`。

同样地，它可以自动帮你把找到的 `Fragment` 转换为当前类型而不需要再使用 `as` 的形式进行强制转换。

你完全不需要担心找不到或者类型错误的情况，遇到这种情况将会返回 `null`。

> 示例如下

```kotlin
// 假设这就是你的 FragmentActivity
val activity: FragmentActivity
// 通过 ID 查找一个 Fragment
val fragment = activity.fragmentManager().findFragment<YourFragment>(R.id.container)
// 通过 TAG 查找一个 Fragment
val fragment = activity.fragmentManager().findFragment<YourFragment>("your_fragment_tag")
```

绑定 `Fragment` 到宿主 (`FragmentActivity` 或 `Fragment`)。

你可以不需要使用类似 `FragmentManger.beginTransaction`...`commit` 的方式进行，现在完成这个操作将会更加简单。

> 示例如下

```kotlin
// 假设这就是你的宿主
val activity: FragmentActivity
// 假设这就是你的 Fragment
val fragment = YourFragment()
// 绑定 Fragment
fragment.attach(activity)
```

是的，这样你就完成了全部的绑定操作，在默认情况下，如果宿主的类型为 `Activity`，它会将 `Fragment` 绑定到 `Activity` 通过 `setContentView` 设置的布局中，
如果为 `Fragment`，它会将 `Fragment` 绑定到 `Fragment.getView` 的布局中。

如果你需要将其绑定到一个自定义的布局中，你可以使用以下方式。

> 示例如下

```kotlin
// 假设这就是你的宿主
val activity: FragmentActivity
// 假设这就是你的 Fragment
val fragment = YourFragment()
// 绑定 Fragment 到 ID 为 R.id.container 的布局中
fragment.attach(activity, R.id.container)
```

如果这个自定义的布局不是通过 ID 得到的，而是一个 `View` 对象，你可以使用以下方式。

::: warning

这个 `View` 对象必须已经添加到正在显示的布局中，并建议为其设置一个 ID，未设置 ID 的 `View` 将会使用 `View.generateViewId` 生成一个 ID。

:::

> 示例如下

```kotlin
// 假设这就是你的宿主
val activity: FragmentActivity
// 假设这就是你的 View
val container: View
// 假设这就是你的 Fragment
val fragment = YourFragment()
// 绑定 Fragment 到 container
fragment.attach(activity, container)
```

你还可以方便地为 `Fragment` 设置绑定时的入场动画。

> 示例如下

```kotlin
// 假设这就是你的宿主
val activity: FragmentActivity
// 假设这就是你的 Fragment
val fragment = YourFragment()
// 绑定 Fragment 并设置入场动画
fragment.attach(
    host = activity,
    container = R.id.container,
    customAnimId = R.anim.slide_in_right // 入场动画
)
```

从宿主解绑 `Fragment`。

你同样可以非常方便地解绑 `Fragment`。

> 示例如下

```kotlin
// 假设这就是你的宿主
val activity: FragmentActivity
// 假设这就是你的 Fragment
val fragment = YourFragment()
// 从宿主解绑 Fragment
fragment.detach(activity)
// 如果你不填写参数，则默认获取当前 Fragment 所持有的宿主
fragment.detach()
```

除了绑定，你还能够在同一个绑定的布局中替换掉一个 `Fragment`。

> 示例如下

```kotlin
// 假设这就是你的宿主
val activity: FragmentActivity
// 假设这就是你的 Fragment
val fragment = YourFragment()
// 替换 Fragment 到 ID 为 R.id.container 的布局中
fragment.replace(activity, R.id.container)
```

显示、隐藏当前 `Fragment`。

> 示例如下

```kotlin
// 假设这就是你的宿主
val activity: FragmentActivity
// 假设这就是你的 Fragment
val fragment = YourFragment()
// 从宿主显示、隐藏 Fragment
fragment.show(activity)
fragment.hide(activity)
// 如果你不填写参数，则默认获取当前 Fragment 所持有的宿主
fragment.show()
fragment.hide()
```

::: tip

任何一个绑定、解绑、替换、显示、隐藏操作都可以被设置过渡动画，你可以在这些方法中找到 `customAnimId`、`customEnterAnimId` 和 `customExitAnimId` 参数，默认将不设置动画效果。

在所有事务事件中，这些方法都保留了 `body` 参数，你可以继续在其中执行自己的自定义事务。

`BetterAndroid` 还为你提供了 `FragmentTransaction`，你可以使用此方法创建一个模版，并应用在自己的 `body` 中。

> 示例如下

```kotlin
// 假设这就是你的宿主
val activity: FragmentActivity
// 假设这就是你的 Fragment
val fragment = YourFragment()
// 绑定 Fragment
fragment.attach(activity) {
    // 在这里添加一些额外的事务
    addSharedElement(view, "shared_element")
}
// 创建一个事务模版
val myTransaction = FragmentTransaction {
    // 在这里添加一些额外的事务
    addSharedElement(view, "shared_element")
}
// 绑定 Fragment
fragment.attach(activity, body = myTransaction)
```

:::

::: warning

从 `1.0.4` 版本开始，`BetterAndroid` 移除了默认的过渡动画并移除了相关资源文件，我们认为过渡动画应该是每个开发者自行决定的事情，并非工具库所为之。

:::

### LifecycleOwner 扩展

::: tip 本节内容

[LifecycleOwner → context](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component/context)

[LifecycleOwner → activity](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component/activity)

[LifecycleOwner → requireContext](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component/require-context)

[LifecycleOwner → requireActivity](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component/require-activity)

[View → lifecycleOwner](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component/lifecycle-owner)

[View → requireLifecycleOwner](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component/require-lifecycle-owner)

适用于 `LifecycleOwner` 的扩展。

:::

`LifecycleOwner` 是 Android Jetpack 中的一个重要组件，它为 `Activity`、`Fragment` 等提供了生命周期的管理。

`BetterAndroid` 为 `LifecycleOwner` 提供了通过其获取上下文的职能，你可以在常用继承于 `LifecycleOwner` 的实例中使用。

> 示例如下

```kotlin
// 假设这就是你的 LifecycleOwner
val lcOwner: LifecycleOwner
// 获取上下文
val context = lcOwner.context
// 获取 Activity
val activity = lcOwner.activity
// 获取非空上下文 (获取失败会抛出异常)
val context = lcOwner.requireContext()
// 获取非空 Activity (获取失败会抛出异常)
val activity = lcOwner.requireActivity()
// 你也可以将获取到的 Activity 转换为指定的类型
val yourActivity = lcOwner.activity<YourActivity>()
// 或者
val yourActivity = lcOwner.requireActivity<YourActivity>()
```

你还可以从一个 `View` 中获取其所在的 `LifecycleOwner`。

> 示例如下

```kotlin
// 假设这就是你的 View
val view: View
// 获取 View 所在的 LifecycleOwner
val lcOwner = view.lifecycleOwner
// 获取非空 LifecycleOwner (获取失败会抛出异常)
val lcOwner = view.requireLifecycleOwner()
```

::: danger

这本质上是一个实验性功能，从 `View` 获取的 `LifecycleOwner` 是不确定的，
它本质上是通过获取 `View` 中的上下文来确定并优先选择 `Activity` 中绑定的首位 `Fragment`，如果没有绑定 `Fragment` 则会选择 `Activity`。

我们建议优先对自定义 `View` 采用传递当前 `LifecycleOwner` 的方式来获取，或者在 `View` 装载后传递。

> 示例如下

```kotlin
// 你可以在构造函数中传递 LifecycleOwner
class YourView(
    context: Context,
    attrs: AttributeSet?,
    lcOwner: LifecycleOwner
) : View(context, attrs) {

    // 或者在装载后设置 LifecycleOwner
    var lifecycleOwner: LifecycleOwner = lcOwner

    init {
        // Your code here.
    }
}
// 在你的 LifecycleOwner 中设置
// 假设这就是你的 View
val yourView: YourView
// 假设 this 就是当前的 LifecycleOwner
yourView.lifecycleOwner = this
```

:::

### 协程 (Coroutines) 扩展

::: tip 本节内容

[LifecycleOwner → launch](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component/launch)

[LifecycleOwner → async](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component/async)

[CoroutinesScope / LifecycleOwner → runDelayed](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component/run-delayed)

[CoroutinesScope / LifecycleOwner → repeatWithDelay](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component/repeat-with-delay)

适用于协程的扩展。

:::

协程 (Coroutines) 是 Kotlin 中的一个重要特性，它为异步编程提供了一种更加优雅的解决方案。

协程本身作为 Kotlin 的一个标准库，它并不会直接与 Android 的生命周期进行绑定，为此 `BetterAndroid` 为其提供了一些实用的扩展功能并将其作用于 UI 重要的交互桥梁。

::: warning

这些扩展功能仅支持使用 Kotlin 实现，如果你正在使用 Java，你将无法使用这些扩展功能。

:::

在 Android 生命周期中使用协程每次都需要使用 `lifecycleScope`，试图在证明 “我是我”，虽然调用域规范，但是显得很臃肿。

现在，你不再需要使用 `lifecycleScope.launch` 来启动一个协程，你可以直接在任何继承于 `LifecycleOwner` 的实例中使用。

> 示例如下

```kotlin
// 假设这就是你的 LifecycleOwner
val lcOwner: LifecycleOwner
// 启动一个协程
lcOwner.launch {
    // Your code here.
}
// 启动一个异步协程
val deferred = lcOwner.async {
    // Your code here.
}
```

除此之外，`BetterAndroid` 还为协程提供了更多适用于 Android 主线程与非主线程互相切换的扩展功能。

> 示例如下

```kotlin
// 假设这就是你的 LifecycleOwner
val lcOwner: LifecycleOwner
// 延迟 1s 后执行
lcOwner.runDelayed(1000) {
    // Your code here.
}
// 重复执行 10 次，每次默认延迟 1s
lcOwner.repeatWithDelay(10) { index ->
    Log.d("Repeat", "Index: $index")
}
```

### 尺寸 (Dimension) 扩展

::: tip 本节内容

[DisplayDensity](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component.base/-display-density)

对 Density 的友好处理接口。

[Dimension → toPx](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component.base/to-px)

[Dimension → toDp](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component.base/to-dp)

适用于 Dimension 的扩展。

:::

在 Android 的 UI 中无处不存在着尺寸的问题，例如你需要将一个 `View` 的 `padding` 设置为 10dp，在 XML 布局中能够很方便地设置，
但是在代码中你需要使用 `TypedValue.applyDimension` 来进行转换，这样的代码看起来并不是很友好。

于是，大家都开始去封装一个形如 `dp2px` 的方法，但是这样的做法依然不是很优雅且存在问题。

`BetterAndroid` 为此提供了一个更加优雅的解决方案。

一般情况下，你只需要传入一个存在的 `Context` 或 `Resources` 即可完成转换。

> 示例如下

```kotlin
// 假设这就是你的 Context
val context: Context
// 假设这就是你的 Resources
val resources: Resources
// 将 10dp 转换为 px
val px = 10.toPx(context)
// 将 10dp 转换为 px
val px = 10.toPx(resources)
// 将 36px 转换为 dp
val dp = 36.toDp(context)
// 将 36px 转换为 dp
val dp = 36.toDp(resources)
```

如果你当前已经处于一个存在 `Context` 或 `Resources` 的环境中，你可以像下面这样写得更加简便一点。

这个时候你只需要为当前的实例实现一个 `DisplayDensity` 接口即可，你无需重写任何方法。

> 示例如下

```kotlin
class YourActivity : AppCompatActivity(), DisplayDensity {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 将 10dp 转换为 px
        val px = 10.dp
        // 将 10dp 转换为 px
        val px = 10.toPx()
        // 将 36px 转换为 dp
        val dp = 36.px
        // 将 36px 转换为 dp
        val dp = 36.toDp()
    }
}
```

::: warning

`DisplayDensity` 接口仅支持被实现到以下实例或是被其继承的实例中：

`Context`、`Window`、`View`、`Resources`、`Fragment`、`Dialog`

如果在不支持的实例中使用将会抛出异常，如果你愿意，你也可以重写 `DisplayDensity` 接口中的方法手动进行支持。

`dp`、`px`、`toPx`、`toDp` 的命名方式可能会与 Jetpack Compose 中的命名方式冲突，所以不建议在这样的项目中使用。

:::

### 资源 (Resources) 扩展

::: tip 本节内容

[Resources → themeResId](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component.base/theme-res-id)

[Resources → isUiInNightMode](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component.base/is-ui-in-night-mode)

[Resources → toHexResourceId](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component.base/to-hex-resource-id)

[Resources → getDrawableCompat](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component.base/get-drawable-compat)

[Resources → getColorCompat](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component.base/get-color-compat)

[Resources → getColorStateListCompat](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component.base/get-color-state-list-compat)

[Resources → getFloatCompat](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component.base/get-float-compat)

[Resources → getFontCompat](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component.base/get-font-compat)

[Resources → getThemeAttrsBoolean](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component.base/get-theme-attrs-boolean)

[Resources → getThemeAttrsFloat](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component.base/get-theme-attrs-float)

[Resources → getThemeAttrsInteger](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component.base/get-theme-attrs-integer)

[Resources → getThemeAttrsIntArray](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component.base/get-theme-attrs-int-array)

[Resources → getThemeAttrsStringArray](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component.base/get-theme-attrs-string-array)

[Resources → getThemeAttrsString](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component.base/get-theme-attrs-string)

[Resources → getThemeAttrsDimension](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component.base/get-theme-attrs-dimension)

[Resources → getThemeAttrsDrawable](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component.base/get-theme-attrs-drawable)

[Resources → getThemeAttrsColorStateList](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component.base/get-theme-attrs-color-state-list)

[Resources → getThemeAttrsColor](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component.base/get-theme-attrs-color)

[Resources → areThemeAttrsIdsValueEquals](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component.base/are-theme-attrs-ids-value-equals)

[Resources → hasThemeAttrsId](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component.base/has-theme-attrs-id)

[Resources → getThemeAttrsId](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component.base/get-theme-attrs-id)

[Resources → getMenuFromResource](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component.base/get-menu-from-resource)

[Resources → getStringArray](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component.base/get-string-array)

[Resources → getIntArray](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component.base/get-int-array)

[Resources → getColorOrNull](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component.base/get-color-or-null)

[Resources → getColorStateListOrNull](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component.base/get-color-state-list-or-null)

[Resources → getIntegerOrNull](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component.base/get-integer-or-null)

[Resources → getIntOrNull](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component.base/get-int-or-null)

[Resources → getTextOrNull](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component.base/get-text-or-null)

[Resources → getStringOrNull](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component.base/get-string-or-null)

[Resources → getBooleanOrNull](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component.base/get-boolean-or-null)

[Resources → getFloatOrNull](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component.base/get-float-or-null)

[Resources → getDimensionOrNull](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component.base/get-dimension-or-null)

[Resources → getDimensionPixelSizeOrNull](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component.base/get-dimension-pixel-size-or-null)

[Resources → getDimensionPixelOffsetOrNull](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component.base/get-dimension-pixel-offset-or-null)

[Resources → getLayoutDimensionOrNull](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component.base/get-layout-dimension-or-null)

[Resources → getDrawableOrNull](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component.base/get-drawable-or-null)

[Resources → getFontOrNull](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component.base/get-font-or-null)

[Resources → obtainStyledAttributes](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component.base/obtain-styled-attributes)

适用于 `Resources` 的扩展。

:::

资源 (Resources) 是 Android 中非常重要的一个部分，它包含了应用程序中需要的布局、图片、字符串等等。

为了能更加方便地使用 `Resources`，`BetterAndroid` 为其提供了一些实用的扩展功能。

获取 `ContextThemeWrapper` 中的主题资源 ID。

通常情况下使用 `setTheme` 设置的主题资源 ID 是无法直接获取到的，为此 `BetterAndroid` 通过反射的方式为你提供了一个获取方式。

::: warning

此方法可能不会适用于所有设备或兼容所有 Android 版本，请自行测试。

:::

> 示例如下

```kotlin
// 假设这就是你的 ContextThemeWrapper
val context: ContextThemeWrapper
// 获取已设置的主题资源 ID
val themeResId = context.themeResId
```

针对 `Resources` 中的 attr 属性，通常情况下我们需要创建一个 `TypedValue` 对象，然后使用 `Context.getTheme().resolveAttribute` 来对它填充相应的 ID 以获取到其本身的值内容。

例如我们需要获取 `android.R.attr.windowBackground` 的值，我们需要这样去做。

> 示例如下

```kotlin
// 假设这就是你的 Context
val context: Context
// 创建一个 TypedValue 对象
val typedValue = TypedValue()
// 使用 Context.getTheme().resolveAttribute 来填充相应的 ID
context.theme.resolveAttribute(android.R.attr.windowBackground, typedValue, true)
// 获取所在资源 ID
val windowBackgroundId = typedValue.resourceId
// 获取其本身的值内容
val windowBackground = context.getDrawable(windowBackgroundId)
```

整个过程下来可谓是非常繁琐，于是 `BetterAndroid` 为此提供了一个更加简单的方式。

现在，你只需要使用以下方式即可获取其本身的值内容。

> 示例如下

```kotlin
// 假设这就是你的 Context
val context: Context
// 获取其本身的值内容
val windowBackground = context.getThemeAttrsDrawable(android.R.attr.windowBackground)
```

如果你只需要获取 attr 所对应的资源 ID，你可以使用以下方式。

> 示例如下

```kotlin
// 假设这就是你的 Context
val context: Context
// 获取所在资源 ID
val windowBackgroundId = context.getThemeAttrsId(android.R.attr.windowBackground)
```

你还可以仅判断 attr 所对应的资源 ID 是否存在。

> 示例如下

```kotlin
// 假设这就是你的 Context
val context: Context
// 判断 attr 所对应的资源 ID 是否存在
val hasWindowBackgroundId = context.hasThemeAttrsId(android.R.attr.windowBackground)
```

你还能够比较两个 attr 的值内容是否相等。

> 示例如下

```kotlin
// 假设这就是你的 Context
val context: Context
// 比较两个 attr 的值内容是否相等
val isEquals = context.areThemeAttrsIdsValueEquals(R.attr.first_attr, R.attr.second_attr)
```

::: tip

`Context.getThemeAttrs*` 系列方法支持 Android 中能够被转换到实际资源对象的常用实例，例如 `getThemeAttrsColor`、`getThemeAttrsInteger`、`getThemeAttrsString` 等。

目前 `Context.getThemeAttrs*`、`Context.areThemeAttrsIdsValueEquals` 支持以下常用资源 ID 的值内容：

`Color`、`ColorStateList`、`Drawable`、`Dimension`、`String`、`StringArray`、`IntArray`、`Float`、`Boolean`

:::

在 Android 中没有提供直接获取 `Menu` 资源 ID 并解析为 `Menu` 对象的方式，为此 `BetterAndroid` 提供了一个可能的获取方式。

`BetterAndroid` 封装了使用 `PopupMenu` 并通过 `MenuInflater` 的形式将获取到的内容转换为一个 `List<MenuItem>` 对象的方法。

现在，你可以非常方便地使用以下方式来获取 `Menu` 资源 ID 的值内容。

> 示例如下

```kotlin
// 假设这就是你的 Context
val context: Context
// 获取 Menu 资源 ID 的值内容
// 获取到的内容即为一个 List<MenuItem> 对象
val menuItems = context.getMenuFromResource(R.menu.my_menu)
```

针对历史版本系统的兼容性处理，`BetterAndroid` 封装了由 `ResourcesCompat` 提供的方法，现在，你不需要考虑一些方法被作废的问题，
你只需要在每个方法后加上 `Compat` 即可自动为其兼容化处理，并能够像原方法一样进行调用，功能完全一致。

下面是一个针对 `Drawable` 的兼容性处理示例。

> 示例如下

```kotlin
// 假设这就是你的 Context
val context: Context
// 假设这就是你的 Resources
val resources: Resources
// 通过 Context 获取 Drawable 对象
val drawable = context.getDrawableCompat(R.drawable.my_drawable)
// 通过 Resources 获取 Drawable 对象
val drawable = resources.getDrawableCompat(R.drawable.my_drawable, context.theme)
// 你还可以使用泛型的方式将其转换为对应的类型
val drawable = context.getDrawableCompat<ColorDrawable>(R.drawable.my_background)
```

以下是原始方法与兼容性处理方法的对照表。

| 原始方法                      | 兼容性处理方法                      |
| ----------------------------- | ----------------------------------- |
| `Context.getDrawable`         | `Context.getDrawableCompat`         |
| `Context.getColor`            | `Context.getColorCompat`            |
| `Context.getColorStateList`   | `Context.getColorStateListCompat`   |
| `Context.getFloat`            | `Context.getFloatCompat`            |
| `Resources.getDrawable`       | `Resources.getDrawableCompat`       |
| `Resources.getColor`          | `Resources.getColorCompat`          |
| `Resources.getColorStateList` | `Resources.getColorStateListCompat` |
| `Resources.getFloat`          | `Resources.getFloatCompat`          |
| `Resources.getFont`           | `Context.getFontCompat`             |

针对自定义 `View` 的属性相关功能，`BetterAndroid` 提供了一个可自动回收处理的 `TypedArray` 扩展。

你可以直接使用 `obtainStyledAttributes` 方法创建一个 `TypedArray` 对象并无需考虑何时应该调用 `recycle`。

> 示例如下

```kotlin
class MyView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    init {
        obtainStyledAttributes(attrs, R.styleable.MyView) {
            val myType = it.getInteger(R.styleable.MyView_myType, 0)
        }
    }
}
```

::: tip

`BetterAndroid` 还为 `TypedArray` 提供了 `getStringArray`、`getIntArray` 以及 `getColorOrNull` 等实用方法，你可以在上方的 **本节内容** 中找到它们。

`TypedArray.get...OrNull` 的作用不同于原始方法的功能，它会首先判断是否存在此属性资源，然后才会返回相关结果，如果不存在则返回默认的 `defValue`，默认为 `null`。

例如 Color 这种属性资源，有的时候我们需要明确确定用户是否设置了此属性，原始的方法只能设置一个非 `null` 的默认值，但是颜色在任何数值上都有作用，此时 **首先判断是否存在此属性资源** 的作用就应运而生。

具体实现方案就是 `val myType = if (value.hasValue(index)) value.get...(..., ...) else ...`，在实际应用中，我们需要自己封装一个这样的做法去实现对应的属性资源调用，显得很繁琐，
所以现在 `BetterAndroid` 为你提供了这种封装，你可以直接使用如下方式进行操作。

> 示例如下

```xml
<declare-styleable name="MyView">
    <attr name="myType" format="color" />
</declare-styleable>
```

```xml
<com.example.MyView
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:myType="#FF000000" />
```

```kotlin
obtainStyledAttributes(attrs, R.styleable.MyView) {
    // 如果 "app:myType" 已被声明于 XML 中，则 myType 的值为 0xFF000000，否则为 null
    val myType = it.getColorOrNull(R.styleable.MyView_myType)
    // 如果 "app:myType" 已被声明于 XML 中，则 myType 的值为 0xFF000000，否则为 0xFF232323
    // myType 的值在任何情况下都可能为 null，不建议使用 !! 来绝对确认其不为 null
    val myType = it.getColorOrNull(R.styleable.MyView_myType, 0xFF232323.toInt())
    // (推荐) 你可以这样来绝对获得 myType 的值不为 null，保持 defValue 为 null
    val myType = it.getColorOrNull(R.styleable.MyView_myType) ?: 0xFF232323.toInt()
    // 或者，你可以在保持 defValue 为 null 时对 myType 为 null 时做出判断
    // 不为 null 即用户设置了 "app:myType" 属性
    if (myType != null) {
        // Your code here.
    }
}
```

:::

系统夜间模式 (Dark Mode) 的功能扩展。

系统的夜间模式是使用 `Configuration.UI_MODE_NIGHT_MASK` 进行判断的，为了能让这个功能更加通俗易懂，你无需使用位运算的方式进行判断，
因为通常情况下我们不需要关心当前系统的夜间模式到底处于哪种托管状态，我们只需要知道当前系统外观是否为深色即可。

所以 `BetterAndroid` 在 `Configuration` 中提供了一个直接使用 `Boolean` 类型进行判断的扩展。

> 示例如下

```kotlin
// 假设这就是你的 Context
val context: Context
// 判断当前系统是否为深色模式
val isDarkMode = context.resources.configuration.isUiInNightMode
```

### 系统颜色 (System Colors)

::: tip 本节内容

[SystemColors](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.component.feature/-system-colors)

适用于系统颜色 (动态主题色) 的扩展。

:::

在 Android 12 中，官方为我们提供了一个新的功能，即动态主题色 (Dynamic Colors)，不同于传统的壁纸取色，Material 3 中的动态主题色是基于壁纸的层级色调进行计算的。

`BetterAndroid` 将系统提供的主题色封装为了 `SystemColors`，你可以在代码层面动态地获取到系统的主题色。

以下是一个创建 `SystemColors` 并使用的示例。

> 示例如下

```kotlin
// 假设这就是你的 Context
val context: Context
// 创建 SystemColors 对象
val systemColors = SystemColors.from(context)
// 获取系统提供的 android.R.color.system_accent1_100 的颜色
val accentColor = systemColors.systemAccentPrimary(100)
// 获取 Material 提供的 com.google.android.material.R.color.material_dynamic_primary50 的颜色
val primaryColor = systemColors.materialDynamicPrimary(50)
```

不是每个搭载了 Android 12 的设备都支持动态主题色，你可以使用以下方式进行判断。

> 示例如下

```kotlin
// 你可以直接判断当前系统是否支持动态主题色
val isAvailable = SystemColors.isAvailable
```

在不支持的情况下在获取颜色时将返回系统提供的默认颜色，但是如果目标设备低于 Android 12，任何从 `SystemColors` 中获取到的颜色都将会是 `Color.TRANSPARENT`。

以下是完整的 `SystemColors` 支持的颜色方法及其可用参数列表。

| 方法名称                        | 可用参数                                                     |
| ------------------------------- | ------------------------------------------------------------ |
| `systemAccentPrimary`           | 0、10、50、100、200、300、400、500、600、700、800、900、1000 |
| `systemAccentSecondary`         | 0、10、50、100、200、300、400、500、600、700、800、900、1000 |
| `systemAccentTertiary`          | 0、10、50、100、200、300、400、500、600、700、800、900、1000 |
| `systemNeutralPrimary`          | 0、10、50、100、200、300、400、500、600、700、800、900、1000 |
| `systemNeutralSecondary`        | 0、10、50、100、200、300、400、500、600、700、800、900、1000 |
| `materialDynamicPrimary`        | 0、10、20、30、40、50、60、70、80、90、95、99、100           |
| `materialDynamicSecondary`      | 0、10、20、30、40、50、60、70、80、90、95、99、100           |
| `materialDynamicTertiary`       | 0、10、20、30、40、50、60、70、80、90、95、99、100           |
| `materialDynamicNeutral`        | 0、10、20、30、40、50、60、70、80、90、95、99、100           |
| `materialDynamicNeutralVariant` | 0、10、20、30、40、50、60、70、80、90、95、99、100           |

::: danger

你仅能在给出的方法中传入上述所列出可用参数中的数值，其它数值均不支持，否则将会抛出异常。

:::

### 颜色 (Color) 扩展

::: tip 本节内容

[AttrState](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.graphics/-attr-state)

基于 `ColorStateList` 的颜色状态。

[Color → isBrightColor](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.graphics/is-bright-color)

[Color → toHexColor](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.graphics/to-hex-color)

[Color → toAlphaColor](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.graphics/to-alpha-color)

[Color → mixColorOf](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.graphics/mix-color-of)

[Color → toNormalColorStateList](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.graphics/to-normal-color-state-list)

[Color → toNullableColorStateList](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.graphics/to-nullable-color-state-list)

[Color → ColorStateList](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.graphics/-color-state-list)

适用于颜色的扩展。

:::

颜色在 Android 中是以 `Integer` (整型) 的形式存在的，虽然存在一个名为 `Color` 的类对其进行封装，但是很多方法都是在高版本的系统中添加的且 `androidx` 也没有针对其特定的兼容实现，
在代码上下文中传递的对象通常也是直接使用 `Integer`。

`BetterAndroid` 没有理由且无任何必要重新设计一个封装类来管理颜色，于是 `BetterAndroid` 仅针对 `Integer` 类型提供了相关扩展。

所有在方法中传递的颜色对象都会被标注 `@ColorInt` 注解，请你也遵守 `androidx` 提供的规范。

下面是一些对颜色扩展的相关示例用法。

判断颜色的明亮程度。

这在你需要根据颜色的明亮程度来决定是否使用深色文字时非常有用。

> 示例如下

```kotlin
// 假设我们有以下颜色
val color = Color.WHITE
// 要判断其明亮程度，你只需要使用以下方法
// 你肯定会得到一个 true，因为这是一个白色
val isBright = color.isBrightColor
```

将颜色转换为 HEX 字符串。

> 示例如下

```kotlin
// 假设我们有以下颜色
val color = Color.WHITE
// 要将其转换为 HEX 字符串，你只需要使用以下方法
// 将会得到一个包含透明度的 "#FFFFFFFF"
val hexString = color.toHexColor()
```

设置当前颜色的透明度。

> 示例如下

```kotlin
// 假设我们有以下颜色
val color = Color.WHITE
// 你可以使用 0f-1f 的浮点数来设置透明度
val alphaColor = color.toAlphaColor(0.5f)
// 你还可以使用 0-255 的整数来设置透明度
val alphaColor = color.toAlphaColor(127)
```

混合两个颜色。

> 示例如下

```kotlin
// 假设我们有以下颜色
val color1 = Color.WHITE
val color2 = Color.BLACK
// 你可以使用以下方法非常简单地将它们进行混合
val mixColor = mixColorOf(color1, color2)
// 你还可以设置混合的比率，默认为 0.5f
val mixColor = mixColorOf(color1, color2, 0.2f)
```

`BetterAndroid` 也同样为 `ColorStateList` 提供了一些扩展功能。

你可以使用以下方式快速地将现有颜色转换为一个拥有默认颜色的 `ColorStateList`。

> 示例如下

```kotlin
// 假设我们有以下颜色
val color = Color.WHITE
// 使用以下方法将其转换为一个拥有默认颜色的 ColorStateList
val colorStateList = color.toNormalColorStateList()
// 你还可以转换为一个在颜色为透明时返回 null 的 ColorStateList
val colorStateList = color.toNullableColorStateList()
```

你还可以通过 `AttrState` 手动创建一个 `ColorStateList`。

> 示例如下

```kotlin
val colorStateList = ColorStateList(
    AttrState.CHECKED to Color.WHITE,
    AttrState.PRESSED to Color.BLACK,
    AttrState.NORMAL to Color.TRANSPARENT
)
```

### 位图 (Bitmap) 扩展

::: tip 本节内容

[Bitmap → decodeToBitmap](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.graphics/decode-to-bitmap)

[Bitmap → decodeToBitmapOrNull](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.graphics/decode-to-bitmap-or-null)

[Bitmap → createBitmap](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.graphics/create-bitmap)

[Bitmap → createBitmapOrNull](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.graphics/create-bitmap-or-null)

[Bitmap → writeBitmap](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.graphics/write-bitmap)

[Bitmap → compressBitmap](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.graphics/compress-bitmap)

[Bitmap → blur](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.graphics/blur)

[Bitmap → round](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.graphics/round)

[Bitmap → compress](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.graphics/compress)

[Bitmap → reduce](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.graphics/reduce)

[Bitmap → zoom](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.graphics/zoom)

适用于位图的扩展。

[BitmapBlurFactory](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.graphics.base/-bitmap-blur-factory)

适用于位图模糊的扩展。

:::

在 Android 中，位图能够用到各种地方，它是用来显示图片的重要对象。

`BetterAndroid` 为位图提供了从装载到对其进行转换、缩放、压缩以及模糊等一系列扩展功能。

在装载位图时，你可以不再需要使用 `BitmapFactory`，现在有以下方式可以帮助你更加方便地完成这个操作。

通过文件 `File` 对象或输入流 `InputStream` 对象装载位图。

> 示例如下

```kotlin
// 假设你的图片位于这个路径
// 注意这个路径仅用于演示，实际情况下需要访问内置存储中的文件
// 你需要申请各种各样不同的权限且应当使用 Environment.getExternalStorageDirectory() 方法来获取路径
val imageFile = File("/storage/emulated/0/DCIM/Camera/IMG_20210901_000000.jpg")
// 通过文件路径创建文件输入流
val inputStream = FileInputStream("/storage/emulated/0/DCIM/Camera/IMG_20210901_000000.jpg")
// 通过文件对象装载位图
val bitmap = imageFile.decodeToBitmap()
// 你可以在方法参数中传入 BitmapFactory.Options 对象来对其进行配置
val bitmap = imageFile.decodeToBitmap(BitmapFactory.Options().apply {
    // ...
})
// 通过输入流装载位图
val bitmap = inputStream.decodeToBitmap()
```

通过字节数组 `ByteArray` 装载位图。

如果你有一个通过 Base64 加密的图片，你可以将其转换为字节数组并使用以下方式装载位图。

> 示例如下

```kotlin
// 假设这就是你的 Base64 字符串
val base64String = "iVBORw0KGgoAAAANSUhEUgAAADIAAAAyCAYAAAA..."
// 将 Base64 字符串转换为字节数组
val byteArray = Base64.decode(base64String, Base64.DEFAULT)
// 通过字节数组装载位图
val bitmap = byteArray.decodeToBitmap()
```

通过 `Resources` 对象装载位图。

此操作实际上是通过已知的 `Drawable` 资源 ID 创建一个新的 `Bitmap` 对象。

> 示例如下

```kotlin
// 假设这就是你的 Resources
val resources: Resources
// 通过资源 ID 装载位图
val bitmap = resources.createBitmap(R.drawable.my_image)
```

::: tip

在不确定位图能否装载成功时，你可以将装载的方法替换为 `decodeToBitmapOrNull` 及 `createBitmapOrNull`，这样在装载失败时将会返回 `null` 而不是抛出异常。

:::

位图装载到内存后或是内存中存在的位图对象，你可以将其重新保存到文件。

针对 `File` 对象 Kotlin 的 stdlib 已经给出了一个 `File.writeText` 的方法，所以 `BetterAndroid` 仿照其提供了一个 `File.writeBitmap` 方法。

> 示例如下

```kotlin
// 假设这就是你的位图对象
val bitmap: Bitmap
// 假设这就是你要保存到的文件
// 注意这个路径仅用于演示，实际情况下需要访问内置存储中的文件
// 你需要申请各种各样不同的权限且应当使用 Environment.getExternalStorageDirectory() 方法来获取路径
val imageFile = File("/storage/emulated/0/DCIM/Camera/IMG_20210901_000000_modified.jpg")
// 将位图保存到文件
imageFile.writeBitmap(bitmap)
// 你可以调整 format 和 quality 参数来对其进行配置
// 默认为 PNG 格式，质量为 100
imageFile.writeBitmap(bitmap, format = Bitmap.CompressFormat.JPEG, quality = 80)
```

如果你能实际得到一个 `OutputStream` 对象，你可以使用以下方式将位图写入到输出流中。

> 示例如下

```kotlin
// 假设这就是你的位图对象
val bitmap: Bitmap
// 假设这就是你的输出流对象
val outputStream: OutputStream
// 使用 use 方法来自动关闭输出流
outputStream.use {
    // 将位图写入到输出流中
    it.compressBitmap(bitmap)
    // 你可以调整 format 和 quality 参数来对其进行配置
    // 默认为 PNG 格式，质量为 100
    it.compressBitmap(bitmap, format = Bitmap.CompressFormat.JPEG, quality = 80)
}
```

下面是针对位图的缩放、压缩以及模糊等扩展功能。

缩放位图。

> 示例如下

```kotlin
// 假设这就是你的位图对象
val bitmap: Bitmap
// 将位图缩放为 100x100 的大小
val zoomBitmap = bitmap.zoom(100, 100)
// 以倍数缩放，默认为 2 倍
val zoomBitmap = bitmap.reduce(3)
```

压缩位图。

> 示例如下

```kotlin
// 假设这就是你的位图对象
val bitmap: Bitmap
// 将位图压缩为 100 KB 的大小
val compressBitmap = bitmap.compress(maxSize = 100)
// 你可以调整 format 和 quality 参数来对其进行配置
// 默认为 PNG 格式，质量为 100
val compressBitmap = bitmap.compress(maxSize = 100, format = Bitmap.CompressFormat.JPEG, quality = 80)
```

圆角化位图。

> 示例如下

```kotlin
// 假设这就是你的位图对象
val bitmap: Bitmap
// 将位图设置以 10dp 为半径的圆角
val roundBitmap = bitmap.round(10.toPx(context))
// 你还可以设置每个角的圆角半径
val roundBitmap = bitmap.round(
    topLeft = 12.toPx(context),
    topRight = 15.toPx(context),
    bottomLeft = 10.toPx(context),
    bottomRight = 9.toPx(context)
)
```

模糊位图，你也可以使用 `BitmapBlurFactory` 来完成。

> 示例如下

```kotlin
// 假设这就是你的位图对象
val bitmap: Bitmap
// 将位图以 25 模糊度进行模糊
val blurBitmap = bitmap.blur(25)
```

::: warning

这里提供的模糊只是一个通用算法，它能够快速实现模糊效果，但在速度和性能上可能会存在问题，且无法用于动态模糊效果。

在 Android 中的位图模糊效果你可以参考并使用其它可能的第三方库，目前并没有一个通用且完善的解决方案，这是 Android 中的历史遗留问题，`BetterAndroid` 没有理由且无任何必要特意封装针对位图模糊的相关功能。

如果你的应用程序目标为 Android 12 及以上版本，我们建议使用官方提供的 `RenderEffect` 来进行模糊操作，在以下版本中使用 `RenderScript` 的替代品 [renderscript-intrinsics-replacement-toolkit](https://github.com/android/renderscript-intrinsics-replacement-toolkit) (但是已经超过两年没有维护)。

:::

### 可绘制 (Drawable) 扩展

::: tip 本节内容

[GradientDrawableCompat](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.graphics/-gradient-drawable-compat)

`GradientDrawable` 的兼容处理类。

[Drawable → setPaddingCompat](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.graphics/set-padding-compat)

[Drawable → setPadding](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.graphics/set-padding)

[Drawable → updatePadding](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.graphics/update-padding)

适用于 `Drawable` 的扩展。

:::

在一些情况下，我们可能需要用到直接操作 `Drawable` 对象的场景，`BetterAndroid` 为此提供了一些可能用到的扩展功能。

为支持的 `Drawable` 设置 `padding`。

你可以在任何类型的 `Drawable` 上使用以下方式设置 `padding`，但是仅对以下类型的 `Drawable` 有效：

`RippleDrawable`、`LayerDrawable`、`ShapeDrawable`、`GradientDrawable`

> 示例如下

```kotlin
// 假设这就是你的 Drawable 对象
val drawable: Drawable
// 为支持的 Drawable 设置 padding
drawable.setPadding(10.toPx(context))
// 或者更新指定边的 padding
drawable.updatePadding(horizontal = 10.toPx(context))
```

为 `GradientDrawable` 设置 `padding` 并进行兼容处理。

支持在代码层面设置 `padding` 的 `GradientDrawable` 仅在 Android 10 及以上版本中可用。

你可以使用以下兼容方式为 `GradientDrawable` 设置 `padding`。

> 示例如下

```kotlin
// 假设这就是你的 GradientDrawable 对象
val drawable: GradientDrawable
// 设置 padding
drawable.setPaddingCompat(10.toPx(context), 10.toPx(context), 10.toPx(context), 10.toPx(context))
// 或者使用上述提到的设置方法一次性设置所有边的 padding
// 这个方法会自动为你对接到 setPaddingCompat
drawable.setPadding(10.toPx(context))
```

但是请注意，在 Android 10 及以下版本中，你需要使用 `GradientDrawableCompat` 来创建 `GradientDrawable` 才能使 `padding` 生效。

你还可以继承 `GradientDrawableCompat` 来实现自己的 `GradientDrawable`。

### Toast 扩展

::: tip 本节内容

[Toast → toast](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.view/toast)

适用于 `Toast` 的扩展。

:::

Android 中的 `Toast` 使用场景非常广泛，但是其存在各种各样的使用问题。

在早些时候 `androidx` 的 `ktx` 为 `Toast` 提供了一个 `Context.toast` 的扩展方法，但是后期因为通知权限的问题被移除，
有关此问题的讨论可以参考 [Toast extensions on Context](https://github.com/android/android-ktx/issues/143)。

在 Kotlin 中简化一个 `Toast` 的使用方式应该是非常有必要的，因为有时候显示信息最快的方式还是弹出一个 `Toast`。

所以 `BetterAndroid` 重新为此提供了一个 `toast` 的扩展方法，你可以在以下实例或是被其继承的实例中使用它：

`Context`、`Window`、`Fragment`、`View`、`Dialog`

你可以非常简单地使用以下方式弹出一个 `Toast`。

> 示例如下

```kotlin
// 假设这就是你的 Context
val context: Context
// 弹出一个 Toast
context.toast("Hello World!")
// 你可以在方法参数中传入 Toast.LENGTH_SHORT 或 Toast.LENGTH_LONG 来设置显示时长
// 默认为 Toast.LENGTH_SHORT
context.toast("Hello World!", Toast.LENGTH_SHORT)
```

以上就是它的全部使用方式，`BetterAndroid` 没有继续对其做出自定义操作，因为自定义功能在后期也被 Android 进行了限制，
请参考 [Custom toasts from the background are blocked](https://developer.android.com/about/versions/11/behavior-changes-11#custom-toasts-bg-blocked)。

`Toast` 仅能在主线程中使用，如果要在任何线程中弹出一个 `Toast`，你只需要像下面这样简单地对其进行配置。

> 示例如下

```kotlin
// 假设这就是你的 Context
val context: Context
// 创建一个新的线程
Thread {
    // 延迟 1 秒
    Thread.sleep(1000)
    // 弹出一个 Toast 并设置 allowBackground 为 true
    context.toast("Hello World!", allowBackground = true)
}.start() // 启动它
```

这样，你就能在任何线程的情况下弹出一个 `Toast`，需要注意的是，这个参数在默认情况下是 `false`，你需要手动设置它。

你应该尽量避免在非主线程中弹出 `Toast`，因为这样可能会导致一些可能的 “黑盒问题” 以及未知的隐患。

::: warning

正如上面所说，在 Android 13 及以上系统中，你需要为 `Toast` 与通知一样定义并添加运行时权限。

类似 MIUI 等一些第三方 ROM 可能会允许在没有权限的情况下在应用程序内部弹出 `Toast`。

如果没有悬浮窗需求，一个好的建议是使用一些其它类似行为的方式来替代 `Toast`，例如使用 Material 组件提供的 `Snackbar`。

请参考 [Notification runtime permission](https://developer.android.com/develop/ui/views/notifications/notification-permission)。

:::

### Window 扩展

::: tip 本节内容

[Window → updateLayoutParams](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.view/update-layout-params)

[Window → updateScreenBrightness](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.view/update-screen-brightness)

[Window → clearScreenBrightness](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.view/clear-screen-brightness)

适用于 `Window` 的扩展。

:::

`BetterAndroid` 为 `Window` 提供了一些可能用到的扩展功能。

你可以像使用 `androidx` 中的 `View.updateLayoutParams` 一样使用以下方式修改 `Window.attributes`。

> 示例如下

```kotlin
// 假设这就是你的 Window 对象
val window: Window
// 修改 Window.attributes 中的参数
window.updateLayoutParams {
    gravity = Gravity.CENTER
    // ...
}
```

`BetterAndroid` 还封装了为 `Window` 单独设置屏幕亮度的方法。

你可以使用以下方式更方便地修改 `Window.attributes.screenBrightness`。

> 示例如下

```kotlin
// 假设这就是你的 Window 对象
val window: Window
// 为当前 Window 设置屏幕亮度 (0-100)
window.updateScreenBrightness(50)
// 你也可以使用 Float 类型的参数来设置屏幕亮度
window.updateScreenBrightness(0.5f)
```

如果你需要还原系统提供的默认屏幕亮度，你可以使用以下方式。

> 示例如下

```kotlin
// 假设这就是你的 Window 对象
val window: Window
// 清除已设置的屏幕亮度
window.clearScreenBrightness()
```

### View 扩展

::: tip 本节内容

[View → location](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.view/location)

[View → tag](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.view/tag)

[View → getTag](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.view/get-tag)

[View → parent](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.view/parent)

[View → parentOrNull](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.view/parent-or-null)

[View → child](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.view/child)

[View → childOrNull](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.view/child-or-null)

[View → firstChild](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.view/first-child)

[View → lastChild](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.view/last-child)

[View → firstChildOrNull](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.view/first-child-or-null)

[View → lastChildOrNull](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.view/last-child-or-null)

[View → removeSelf](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.view/remove-self)

[View → removeSelfInLayout](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.view/remove-self-in-layout)

[View → animate](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.view/animate)

[View → showIme](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.view/show-ime)

[View → hideIme](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.view/hide-ime)

[View → performKeyPressed](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.view/perform-key-pressed)

[View → performTouch](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.view/perform-touch)

[View → setIntervalOnClickListener](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.view/set-interval-on-click-listener)

[View → updatePadding](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.view/update-padding)

[View → updateMargin](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.view/update-margin)

[View → walkToRoot](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.view/walk-to-root)

[View → walkThroughChildren](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.view/walk-through-children)

[View → indexOfInParent](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.view/index-of-in-parent)

[View → outlineProvider](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.view/outline-provider)

[View → ViewLayoutParams](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.view/-view-layout-params.html)

[View → LayoutParamsMatchParent](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.view/-layout-params-match-parent)

[View → LayoutParamsWrapContent](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.view/-layout-params-wrap-content)

适用于 `View` 的扩展。

:::

`View` 是用户界面中的重要组成部分，在使用 Kotlin 时，`androidx` 为我们提供了关于 `View` 的扩展功能，但是依然不够完善。

`BetterAndroid` 在 `androidx` 的相关扩展功能基础上进行了完善和丰富，下面是你能够用到的一些扩展功能。

获取 `View` 在屏幕中的坐标。

你可以通过以下方式获取到一个 `Point` 对象，它包含了 `View` 在屏幕中的坐标。

> 示例如下

```kotlin
// 假设这就是你的 View 对象
val view: View
// 获取 View 在屏幕中的坐标
val location = view.location
// X 坐标
val x = location.x
// Y 坐标
val y = location.y
```

获取当前 `View` 的标签 (Tag)。

在传统写法中，我们需要使用 `View.getTag` 获取到标签对象，然后使用 `as` 转换为我们需要的类型。

这种写法看起来非常麻烦，于是 `BetterAndroid` 为此提供了一个更加简单的方式。

> 示例如下

```kotlin
// 假设这就是你的 View 对象
val view: View
// 指定标签的类型 (如果类型是已知且确定的)
// 返回结果可为 null
val tag = view.tag<String>()
// 指定标签的类型和 ID (如果类型是已知且确定的)
// 返回结果可为 null
val tag = view.getTag<String>(R.id.my_tag)
// 指定标签的类型和 ID 并设置默认值 (如果类型是已知且确定的)
// 返回结果不会为 null，如果类型错误或标签不存在，将会返回默认值
val tag = view.getTag<String>(R.id.my_tag, "Hello World!")
```

获取当前 `View` 的父布局。

在传统写法中，我们需要使用 `View.parent` 获取到 `ViewParent` 对象，然后使用 `as` 转换为 `ViewGroup` 以获取到父布局对象。

这种写法看起来非常麻烦，于是 `BetterAndroid` 为此提供了一个更加简单的方式。

> 示例如下

```kotlin
// 假设这就是你的 View 对象
val view: View
// 获取当前 View 的父布局
val parent: ViewGroup = view.parent()
// 指定父布局的类型 (如果类型是已知且确定的)
val parent = view.parent<LinearLayout>()
// 在不确定父布局是否存在的情况下，你还可以使用以下方式
val parent = view.parentOrNull()
```

获取当前 `ViewGroup` 的子布局。

在传统写法中，我们需要使用 `ViewGroup.getChildAt` 获取到 `View` 对象，然后使用 `as` 转换为 `View` 以获取到子布局对象。

这种写法看起来同样非常麻烦，于是 `BetterAndroid` 也此提供了一个更加简单的方式。

> 示例如下

```kotlin
// 假设这就是你的 ViewGroup 对象
val viewGroup: ViewGroup
// 获取当前 ViewGroup 的子布局
val child: View = viewGroup.child(index = 0)
// 指定子布局的类型 (如果类型是已知且确定的)
val child = viewGroup.child<Button>(index = 0)
// 获取当前 ViewGroup 的第一个子布局
val firstChild: View = viewGroup.firstChild
// 获取当前 ViewGroup 的最后一个子布局
val lastChild: View = viewGroup.lastChild
// 在不确定子布局是否存在的情况下，你还可以使用以下方式
val child = viewGroup.childOrNull(index = 0)
val firstChild = viewGroup.firstChildOrNull
val lastChild = viewGroup.lastChildOrNull
```

从父布局 (容器) 中移除自身。

通常情况下，我们从父布局中移除 `View` 自身需要使用 `View.getParent` 获取到父布局对象，然后转换为 `ViewGroup`，最后调用 `ViewGroup.removeView` 方法。

现在，你可以使用以下方式更加简单地从父布局中移除其自身，如果不存在父布局或是父布局不是 `ViewGroup` (这种情况一般是不存在的)，此方法将无任何作用且不需要担心会产生任何负面效果。

> 示例如下

```kotlin
// 假设这就是你的 View 对象
val view: View
// 从父布局中移除自身
view.removeSelf()
// 使用 ViewGroup.removeViewInLayout 方法移除自身
view.removeSelfInLayout()
```

创建动画。

`BetterAndroid` 为 `View` 提供了 `animate` 方法的 lambda 实现，它将自动调用 `start` 方法，你可以使用它来创建一些简单的动画效果。

> 示例如下

```kotlin
// 假设这就是你的 View 对象
val view: View
// 创建动画
view.animate {
    // Your code here.
}
```

显示或隐藏输入法 (IME)。

通常情况下控制输入法的显示和隐藏可以通过输入框的焦点来自动完成，但是一些情况下我们希望能够手动控制输入法的显示和隐藏。

如果当前 `View` 在 `Activity` 中，你可以使用以下方式显示或隐藏输入法。

> 示例如下

```kotlin
// 假设这就是你的 View 对象
val view: View
// 显示输入法
view.showIme()
// 隐藏输入法
view.hideIme()
```

在 Android 11 及以上版本中，以上方法会使用 `WindowInsetsController` 来控制输入法的显示和隐藏，
在 `View` 不处于 `Activity` 中或当前 Android 版本低于 Android 11 时，将会使用 `InputMethodManager` 来控制输入法的显示和隐藏。

::: tip

如果你需要使用拖拽来控制输入法的显示或隐藏 (Window Insets 动画效果)，目前 `BetterAndroid` 暂时没有提供相关的扩展功能，
你可以参考 [WindowInsetsAnimationController](https://developer.android.com/reference/android/view/WindowInsetsAnimationController) 自己实现。

:::

::: warning

在 Android 11 及以上版本中，建议将 `Activity` 的 `android:windowSoftInputMode` 参数设置为 `adjustResize` 以便于更好地控制输入法的显示和隐藏。

如果你的 `View` 不处于 `Activity` 中或当前 Android 版本低于 11，以上方案可能在一定条件下会无效。

在 `Activity` 首次启动时，建议为显示或隐藏输入法事件进行延迟处理，否则可能会无效，且显示和隐藏事件的间隔不应过短。

:::

模拟触摸事件 (Touch)。

`View` 提供的 `performClick` 方法仅能模拟点击事件，如果你需要模拟触摸事件，你可以使用 `BetterAndroid` 提供的 `View.performTouch` 方法。

这个方法所接受的参数如下表所示。

| 参数名称   | 参数类型 | 参数说明                                          |
| ---------- | -------- | ------------------------------------------------- |
| `downX`    | `Float`  | 模拟触摸事件的按下 X 坐标                         |
| `downY`    | `Float`  | 模拟触摸事件的按下 Y 坐标                         |
| `upX`      | `Float`  | 模拟触摸事件的抬起 X 坐标                         |
| `upY`      | `Float`  | 模拟触摸事件的抬起 Y 坐标                         |
| `duration` | `Long`   | 模拟触摸事件的持续时间 (从按下到抬起)，单位为毫秒 |

> 示例如下

```kotlin
// 假设这就是你的 View 对象
val view: View
// 模拟从 (15, 30) 到 (105, 130) 的触摸事件，持续 500 毫秒
view.performTouch(15f, 30f, 105f, 130f, 500)
```

模拟按键事件。

你可以模拟物理键盘或输入法 (IME) 发送给当前 `View` 的按键事件，例如在 `EditText` 中，你可以模拟按下退格键来删除文本。

> 示例如下

```kotlin
// 假设这就是你的 View 对象
val view: EditText
// 模拟按下退格键
view.performKeyPressed(KeyEvent.KEYCODE_DEL)
// 你可以在后方加入 duration 参数来设置持续时间 (从按下到抬起)，单位为毫秒
// 默认为 150 毫秒
view.performKeyPressed(KeyEvent.KEYCODE_DEL, duration = 500)
```

设置间隔点击事件。

`View` 提供的 `setOnClickListener` 方法设置的点击事件可能在短时间内被多次触发造成误操作，针对这种情况，`BetterAndroid` 为你提供了 `setIntervalOnClickListener` 方法。

在指定的间隔时间内，点击事件重复触发将被忽略。

> 示例如下

```kotlin
// 假设这就是你的 View 对象
val view: View
// 设置间隔点击事件
view.setIntervalOnClickListener {
    // Your code here.
}
// 你可以在方法参数中传入 timeMillis 参数来设置点击事件的间隔时间，单位为毫秒
// 默认为 300 毫秒
view.setIntervalOnClickListener(1000) {
    // Your code here.
}
```

更新 `View` 的 `padding` 和 `margin`。

`androidx` 提供了一个 `View.updatePadding` 方法，`BetterAndroid` 在此基础上提供了可更新横向和纵向方向的方法，如果你只需要更新这两个方向的 `padding`，你可以无需写两遍重复数值。

> 示例如下

```kotlin
// 假设这就是你的 View 对象
val view: View
// 更新横向方向的 padding
view.updatePadding(horizontal = 10.toPx(context))
// 更新纵向方向的 padding
view.updatePadding(vertical = 10.toPx(context))
```

`BetterAndroid` 同样提供了一个 `View.updateMargin` 方法，它的使用方式与 `View.updatePadding` 相同。

这个方法仅会在 `View` 的 `LayoutParams` 为 `MarginLayoutParams` 时生效，其它情况下均不会有任何作用。

> 示例如下

```kotlin
// 假设这就是你的 View 对象
val view: View
// 更新横向方向的 margin
view.updateMargin(horizontal = 10.toPx(context))
// 更新纵向方向的 margin
view.updateMargin(vertical = 10.toPx(context))
// 更新左侧的 margin
view.updateMargin(left = 10.toPx(context))
```

遍历父布局以及所有子布局。

通常情况下，我们需要使用 `View.parent` 方法递归遍历父布局，使用 `ViewGroup.children` 方法递归遍历子布局。

`BetterAndroid` 为此提供了一个更加简单的方式，它的设计灵感来源于 Kotlin 提供的 `File` 中的 `walk` 扩展方法。

> 示例如下

```kotlin
// 假设这就是你的 View 对象
val view: View
// 假设这就是你的 ViewGroup 对象
val viewGroup: ViewGroup
// 获取所有父布局
val parents = view.walkToRoot()
// 获取所有子布局
val children = viewGroup.walkThroughChildren()
```

获取 `View` 在父布局中的索引。

在传统写法中，我们需要使用 `ViewGroup.indexOfChild` 获取到 `View` 在父布局中的索引。

这种写法看起来不是很友好，于是 `BetterAndroid` 为此提供了一个更加简单的方式。

> 示例如下

```kotlin
// 假设这就是你的 View 对象
val view: View
// 获取 View 在父布局中的索引
// 如果不存在父布局将会返回 -1
val index = view.indexOfInParent()
```

设置 `View` 的 `outlineProvider` (外轮廓提供者)。

`View` 的 `outlineProvider` 用于设置 `View` 的外轮廓，它的类型为 `ViewOutlineProvider`。

在 Kotlin 中，我们需要使用 `view.outlineProvider = object : ViewOutlineProvider()` 的方式进行设置，这看起来并不友好。

于是 `BetterAndroid` 为此提供了一个更加简单的方式。

> 示例如下

```kotlin
// 假设这就是你的 View 对象
val view: View
// 设置 View 的 outlineProvider
view.outlineProvider { view, outline ->
    // 例如，你可以在此处设置圆形的外轮廓
    outline.setOval(0, 0, view.width, view.height)
    // 或者你可以在此处设置圆角矩形的外轮廓
    outline.setRoundRect(0, 0, view.width, view.height, 10f.toPx(context))
}
// 记得在设置 outlineProvider 之后设置 clipToOutline 为 true
view.clipToOutline = true
```

手动创建一个 `LayoutParams` 对象。

`LayoutParams` 是 `View` 的布局参数，它的类型取决于 `View` 的父布局，例如 `LinearLayout` 的 `LayoutParams` 为 `LinearLayout.LayoutParams`。

有时候，你可能需要有手动创建这个对象并设置到 `View` 中的需求，此时 `BetterAndroid` 提供了 `ViewLayoutParams` 方法，
现在，你可以省去使用超级长的 `ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)` 创建对象的步骤。

> 示例如下

```kotlin
// 假设这就是你的 View 对象
val view: View
// 创建一个 LinearLayout.LayoutParams 对象，默认情况下 width 和 height 均为 WRAP_CONTENT
val layoutParams = ViewLayoutParams<LinearLayout.LayoutParams>()
// 同时设置 width 和 height 为 MATCH_PARENT
val layoutParams = ViewLayoutParams<LinearLayout.LayoutParams>(marchParent = true)
// 仅设置 width 为 MATCH_PARENT
val layoutParams = ViewLayoutParams<LinearLayout.LayoutParams>(widthMatchParent = true)
// 手动设置 width 和 height
val layoutParams = ViewLayoutParams<LinearLayout.LayoutParams>(50.toPx(context), 30.toPx(context))
// 设置到 View 中
view.layoutParams = layoutParams
```

### LayoutInflater 扩展

::: tip 本节内容

[LayoutInflater → layoutInflater](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.view/layout-inflater)

[LayoutInflater → inflate](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.view/inflate)

适用于 `LayoutInflater` 的扩展。

:::

通常情况下我们可以在 `Activity` 中使用 `getLayoutInflater` 或使用 `LayoutInflater.from(context)` 来创建一个 `LayoutInflater` 对象，然后使用 `inflate` 方法来装载布局。

`BetterAndroid` 为你简化了这一步骤，现在，你能够在 `Context` 中使用 `layoutInflater` 获取到 `LayoutInflater` 对象，然后使用 `inflate` 方法来装载布局。

> 示例如下

```kotlin
// 假设这就是你的 ViewGroup 对象
val view: ViewGroup
// 假设这就是你的 Context 对象
val context: Context
// 你可以在 ViewGroup 中使用以下方式装载布局
val myView = context.layoutInflater.inflate(R.layout.my_layout, root = view)
// 你可以设置 attachToRoot 参数来决定是否同时将其添加到 ViewGroup 中
val myView = context.layoutInflater.inflate(R.layout.my_layout, root = view, attachToRoot = true)
```

### TextView 扩展

::: tip 本节内容

[TextView → isEllipsize](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.view/is-ellipsize)

[TextView → isUnderline](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.view/is-underline)

[TextView → isStrikeThrough](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.view/is-strike-through)

[TextView → textColor](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.view/text-color)

[TextView → textToString](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.view/text-to-String)

[TextView → hintToString](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.view/hint-to-String)

[EditText → updateText](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.view/update-text)

[TextView → clear](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.view/clear)

[TextView → updateTypeface](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.view/update-typeface)

[TextView → updateCompoundDrawables](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.view/update-compound-drawables)

[TextView → updateCompoundDrawablesWithIntrinsicBounds](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.view/update-compound-drawables-with-intrinsic-bounds)

[TextView → setDigits](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.view/set-digits)

适用于 `TextView` 的扩展。

:::

`TextView` 是 Android 中最常用的组件之一，`BetterAndroid` 为 `TextView` 提供了一些能在 Kotlin 中使用更加方便的扩展功能。

判断 `TextView` 是否存在省略号。

> 示例如下

```kotlin
// 假设这就是你的 TextView 对象
val textView: TextView
// 判断 TextView 是否存在省略号
val isEllipsize = textView.isEllipsize
```

获取、设置 `TextView` 的下划线效果。

> 示例如下

```kotlin
// 假设这就是你的 TextView 对象
val textView: TextView
// 获取 TextView 是否存在下划线效果
val isUnderline = textView.isUnderline
// 设置 TextView 的下划线效果
textView.isUnderline = true
```

获取、设置 `TextView` 的删除线效果。

> 示例如下

```kotlin
// 假设这就是你的 TextView 对象
val textView: TextView
// 获取 TextView 是否存在删除线效果
val isStrikeThrough = textView.isStrikeThrough
// 设置 TextView 的删除线效果
textView.isStrikeThrough = true
```

::: warning

在设置下划线或删除线效果时，你可能需要手动对 `TextView` 使用 `paint.isAntiAlias = true` 启用抗锯齿以达到更好的效果，这是 Android 中对于绘制性能的一个历史遗留问题。

:::

获取、设置 `TextView` 的文本颜色。

虽然说，你能够使用 `TextView.setTextColor` 方法来设置文本颜色，但是它不能很好地被识别为 Kotlin 中的 Getter、Setter 方法，因为对应的 `TextView.getTextColors` 是一个 `ColorStateList` 对象。

所以 `BetterAndroid` 添加了针对这个功能的扩展，现在，你可以使用以下方式获取、设置 `TextView` 的文本颜色。

> 示例如下

```kotlin
// 假设这就是你的 TextView 对象
val textView: TextView
// 获取 TextView 的文本颜色
val textColor = textView.textColor
// 设置 TextView 的文本颜色
textView.textColor = Color.RED
```

获取 `TextView` 的文本并将其转换为 `String`。

通常情况下，直接使用 `TextView.getText` 获取到的文本是一个 `CharSequence` 对象，如果你需要将其转换为 `String`，需要 `getText().toString()`，这看起来比较繁琐。

`BetterAndroid` 为此提供了一个更加简单的方式，现在，你可以使用以下方式获取 `TextView` 的文本并将其转换为 `String`。

> 示例如下

```kotlin
// 假设这就是你的 TextView 对象
val textView: TextView
// 获取 TextView 的文本并转换为 String
val text = textView.textToString()
```

使用 `TextView.getHint` 也可以使用同样类似的方式。

> 示例如下

```kotlin
// 假设这就是你的 TextView 对象
val textView: TextView
// 获取 TextView 的提示文本并转换为 String
val hint = textView.hintToString()
```

更新 `EditText` 的文本。

`EditText` 继承自 `TextView`，直接使用 `setText(...)` 更新的文本会导致光标位置依然处于第一位。

`BetterAndroid` 为此提供了一个更加方便的方式，它会根据文本长度自动帮你设置 `setSelection` 以保持光标位置始终在文本的最后。

> 示例如下

```kotlin
// 假设这就是你的 EditText 对象
val editText: EditText
// 更新 EditText 的文本
editText.updateText("Hello World!")
```

清空 `TextView` 的文本。

使用 `setText("")` 或 `text = ""` 看起来不是很友好，于是 `BetterAndroid` 为此提供了一个更加简单的方式。

> 示例如下

```kotlin
// 假设这就是你的 TextView 对象
val textView: TextView
// 清空 TextView 的文本
textView.clear()
```

更新 `TextView` 的 `Typeface` (字体)。

`Typeface` 是 Android 中的字体，有时候我们只需要关心字体的粗细和斜体，而不需要设置具体的字体。

`BetterAndroid` 为此提供了一个更加简单的方式，现在，你可以使用以下方式更新 `TextView` 的 `Typeface`。

> 示例如下

```kotlin
// 假设这就是你的 TextView 对象
val textView: TextView
// 仅将 TextView 的字体设置为粗体
textView.updateTypeface(Typeface.BOLD)
```

更新 `TextView` 的 `CompoundDrawables` (复合绘图)。

`CompoundDrawables` 是 `TextView` 中的复合绘图，它包含了 `Drawable` 对象。

使用传统的 `setCompoundDrawables` 方法需要传入四个参数且不需要的参数还需要填写 `null`，这看起来非常麻烦。

`BetterAndroid` 为此提供了一个更加简单的方式，现在，你可以使用以下方式直接更新 `TextView` 的 `CompoundDrawables`。

> 示例如下

```kotlin
// 假设这就是你的 TextView 对象
val textView: TextView
// 更新 TextView 的 CompoundDrawables
// 我们只需要填写需要方向的参数，其它方向的参数将会自动重新设置为原有的 CompoundDrawables
textView.updateCompoundDrawables(
    left = R.drawable.ic_left,
    top = R.drawable.ic_top,
    right = R.drawable.ic_right,
    bottom = R.drawable.ic_bottom
)
// 使用 setCompoundDrawablesWithIntrinsicBounds 的方式更新 CompoundDrawables
textView.updateCompoundDrawablesWithIntrinsicBounds(
    left = R.drawable.ic_left,
    top = R.drawable.ic_top,
    right = R.drawable.ic_right,
    bottom = R.drawable.ic_bottom
)
```

设置 `TextView` 的输入限制 (Digits)。

这个属性仅能在 XML 中方便地使用 `digits` 来设置，但是如果你需要动态修改它，就需要 `TextView.setKeyListener` 方法。

为此，`BetterAndroid` 为 `TextView` 提供了一个 `setDigits` 方法，你可以使用以下方式设置 `TextView` 的输入限制。

由于输入功能是由 `EditText` 完成的，而 `EditText` 继承自 `TextView`，所以一般情况下此方法仅在 `EditText` 中使用。

> 示例如下

```kotlin
// 假设这就是你的 EditText 对象
val editText: EditText
// 设置输入框仅能输入数字
editText.setDigits("0123456789")
// 设置输入框仅能输入小写字母
editText.setDigits("abcdefghijklmnopqrstuvwxyz")
// 第二位参数为 inputType，你可以在此处限制输入框的显示行为
// 例如仅能输入数字且显示为密码
editText.setDigits("0123456789", InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD)
// 第三位参数为 locale，你可以在此处设置输入框的国家区域 (仅在 Android 8 及以上版本中可用)
// 例如设置为中国大陆地区
editText.setDigits("0123456789", locale = Locale.CHINA)
```

### RecyclerView 扩展

::: tip 本节内容

[RecyclerView → layoutManager](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.view/layout-manager)

适用于 `RecyclerView` 的扩展。

:::

`RecyclerView` 是 Android 中最常用的列表组件之一，`BetterAndroid` 为 `RecyclerView` 提供了一些能在 Kotlin 中使用更加方便的扩展功能。

获取 `RecyclerView` 的 `LayoutManager`。

`BetterAndroid` 为你提供了一个方便获取 `LayoutManager` 的方式，现在你不再需要获取到 `RecyclerView` 的 `layoutManager` 后再使用 `as` 转换为对应类型，你可以直接使用以下方式获取 `LayoutManager`。

> 示例如下

```kotlin
// 假设这就是你的 RecyclerView 对象
val recyclerView: RecyclerView
// 获取 RecyclerView 的 LayoutManager，如果类型错误或不存在将会返回 null
val layoutManager = recyclerView.layoutManager<LinearLayoutManager>()
```

### 布局绑定 (ViewBinding) 扩展

::: tip 本节内容

[ViewBinding → ViewBinding](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.binding/-view-binding)

[ViewBinding → viewBinding](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.binding/view-binding)

[ViewBinding → ViewBindingBuilder](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.binding/-view-binding-builder)

[ViewBinding → ViewBindingDelegate](kdoc://ui-extension/ui-extension/com.highcapable.betterandroid.ui.extension.binding/-view-binding-delegate)

适用于 `ViewBinding` 的扩展。

:::

`ViewBinding` 是 Android 中的一个新特性，它能够帮助我们更加方便地操作布局中的组件。

但是显然官方没有开放对它的使用方法，能够获取到生成类的接口 `ViewBinding` 也不存在 `inflate` 等方法的实现，你仅能使用类似 `ActivityMainBinding.inflate(layoutInflater)` 的方式来装载布局。

于是 `BetterAndroid` 对其进行了反射处理来获得其中的 `inflate` 方法并通过泛型来提取对象的类型。

这些设计的灵感部分来源于 [ViewBindingKTX](https://github.com/DylanCaiCoding/ViewBindingKTX) 项目，非常感谢这个项目的作者。

现在你可以使用以下方式创建一个 `ViewBindingBuilder` 并可以将其传递到任何需要的地方进行操作。

> 示例如下

```kotlin
// 创建一个 ViewBindingBuilder
val builder = ViewBinding<ActivityMainBinding>()
// 在需要时装载布局
val binding = builder.inflate(layoutInflater)
// 假设这就是你的 View
val view: View
// 你也可以绑定到一个存在的 View 上
val binding = builder.bind(view)
```

这样我们就实现了 `ViewBinding` 的传递，你可以在任何地方使用它。

你还可以使用委托的方式来实现在 `Activity` 等地方使用 `ViewBinding`。

> 示例如下

```kotlin
class YourActvity : Activity() {

    val binding: ActivityMainBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}
```

如果你需要实现将 `ViewBinding` 封装到父类并使用泛型的方式绑定到子类，你可以使用以下方式。

首先我们需要创建一个父类。

> 示例如下

```kotlin
open class YourBaseActvity<VB : ViewBinding> : Activity() {

    lateinit var binding: VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 在这里装载 ViewBinding
        binding = ViewBindingBuilder.fromGeneric<VB>(this).inflate(layoutInflater)
        setContentView(binding.root)
    }
}
```

然后将这个父类作为全局对象继承到子类中。

> 示例如下

```kotlin
class YourActivity : YourBaseActivity<ActivityMainBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.mainText.text = "Hello World!"
    }
}
```

::: tip

你还可以参考 [ui-component → Activity](../library/ui-component.md#activity) 直接使用已经封装好的 `AppBindingActivity` 或参考
[ui-component → Fragment](../library/ui-component.md#fragment) 直接使用已经封装好的 `AppBindingFragment`。

:::

::: danger

如果你的应用程序在编译时启用了混淆功能，你需要参考 [R8 与 ProGuard 混淆](../config/r8-proguard) 来正确配置混淆规则。

:::