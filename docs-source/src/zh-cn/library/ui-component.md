# ui-component

![GitHub release](https://img.shields.io/github/v/release/BetterAndroid/BetterAndroid?display_name=release&logo=github&color=green&filter=ui-component-*)
<span style="margin-left: 5px"/>
![Android Min SDK](https://img.shields.io/badge/Min%20SDK-21-orange?logo=android)

这是针对 UI (用户界面) 相关组件的一个依赖。

## 配置依赖

你可以使用如下方式将此模块添加到你的项目中。

### SweetDependency (推荐)

在你的项目 `SweetDependency` 配置文件中添加依赖。

```yaml
libraries:
  com.highcapable.betterandroid:
    ui-component:
      version: +
```

在你的项目 `build.gradle.kts` 中配置依赖。

```kotlin
implementation(com.highcapable.betterandroid.ui.component)
```

### 传统方式

在你的项目 `build.gradle.kts` 中配置依赖。

```kotlin
implementation("com.highcapable.betterandroid:ui-component:<version>")
```

请将 `<version>` 修改为此文档顶部显示的版本。

## 功能介绍

你可以 [点击这里](kdoc://ui-component) 查看 KDoc。

### Activity

::: tip 本节内容

[AppBindingActivity](kdoc://ui-component/ui-component/com.highcapable.betterandroid.ui.component.activity/-app-binding-activity)

带有视图绑定的 `Activity` (继承于 `AppCompatActivtiy`)。

[AppViewsActivity](kdoc://ui-component/ui-component/com.highcapable.betterandroid.ui.component.activity/-app-views-activity)

基础视图组件 `Activity` (继承于 `AppCompatActivtiy`)。

[AppComponentActivity](kdoc://ui-component/ui-component/com.highcapable.betterandroid.ui.component.activity/-app-component-activity)

基础组件 `Activity` (继承于 `ComponentActivtiy`)。

可用于 Jetpack Compose 项目。

:::

::: tip

下方的预置组件都实现了 [IBackPressedController](kdoc://ui-component/ui-component/com.highcapable.betterandroid.ui.component.proxy/-i-back-pressed-controller)、
[ISystemBarsController](kdoc://ui-component/ui-component/com.highcapable.betterandroid.ui.component.proxy/-i-system-bars-controller) 接口。

你可以在下方的 [系统事件](#系统事件) 和 [系统栏 (状态栏、导航栏等)](#系统栏-状态栏、导航栏等) 中找到详细的使用方法。

:::

在使用 `ViewBinding` 的情况下，你可以使用 `AppBindingActivity` 来快速创建一个带有视图绑定的 `Activity`。

在 `AppBindingActivity` 中，你可以直接使用 `binding` 属性获取视图绑定对象而无需手动调用 `setContentView` 方法。

> 示例如下

```kotlin
class MainActivity : AppBindingActivity<ActivityMainBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.mainText.text = "Hello World!"
    }
}
```

你也可以使用 `AppViewsActivity` 来创建一个基本 `Activity`，使用 `findViewById` 方法来获取 `View`。

> 示例如下

```kotlin
class MainActivity : AppViewsActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<TextView>(R.id.main_text).text = "Hello World!"
    }
}
```

如果你的项目是一个 Jetpack Compose 项目，你可以使用 `AppComponentActivity` 来创建一个基本 `Activity`。

> 示例如下

```kotlin
class MainActivity : AppComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Text("Hello World!")
        }
    }
}
```

::: tip

更多功能你可以参考 [compose-extension](../library/compose-extension.md)。

:::

### Fragment

::: tip 本节内容

[AppBindingFragment](kdoc://ui-component/ui-component/com.highcapable.betterandroid.ui.component.fragment/-app-binding-fragment)

带有视图绑定的 `Fragment`。

[AppViewsFragment](kdoc://ui-component/ui-component/com.highcapable.betterandroid.ui.component.fragment/-app-views-fragment)

基础视图组件 `Fragment`。

:::

::: tip

下方的预置组件都实现了 [IBackPressedController](kdoc://ui-component/ui-component/com.highcapable.betterandroid.ui.component.proxy/-i-back-pressed-controller)、
[ISystemBarsController](kdoc://ui-component/ui-component/com.highcapable.betterandroid.ui.component.proxy/-i-system-bars-controller) 接口。

你可以在下方的 [系统事件](#系统事件) 和 [系统栏 (状态栏、导航栏等)](#系统栏-状态栏、导航栏等) 中找到详细的使用方法。

:::

在使用 `ViewBinding` 的情况下，你可以使用 `AppBindingFragment` 来快速创建一个带有视图绑定的 `Fragment`。

在 `AppBindingFragment` 中，你可以直接使用 `binding` 属性获取视图绑定对象而无需手动重写 `onCreateView` 方法。

你不需要考虑 `Fragment` 的生命周期对 `binding` 的影响，`BetterAndroid` 已经为你处理了这些问题。

> 示例如下

```kotlin
class MainFragment : AppBindingFragment<FragmentMainBinding>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mainText.text = "Hello World!"
    }
}
```

你也可以使用 `AppViewsFragment` 来创建一个基本 `Fragment`。

同样地，你无需重写 `onCreateView` 方法，直接将需要绑定的布局资源 ID 填入构造方法即可。

> 示例如下

```kotlin
class MainFragment : AppViewsFragment(R.layout.fragment_main) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<TextView>(R.id.main_text).text = "Hello World!"
    }
}
```

### 适配器 (Adapter)

::: tip 本节内容

[CommonAdapterBuilder](kdoc://ui-component/ui-component/com.highcapable.betterandroid.ui.component.adapter/-common-adapter-builder)

可用于构建一个 `BaseAdapter`。

[PagerAdapterBuilder](kdoc://ui-component/ui-component/com.highcapable.betterandroid.ui.component.adapter/-pager-adapter-builder)

可用于构建一个 `PagerAdapter`。

[RecyclerAdapterBuilder](kdoc://ui-component/ui-component/com.highcapable.betterandroid.ui.component.adapter/-recycler-adapter-builder)

可用于构建一个 `RecyclerView.Adapter`。

[PagerMediator](kdoc://ui-component/ui-component/com.highcapable.betterandroid.ui.component.adapter.mediator/-pager-mediator)

`ViewPager` 的页面调节器。

[RecyclerCosmetic](kdoc://ui-component/ui-component/com.highcapable.betterandroid.ui.component.adapter.recycler.cosmetic/-recycler-cosmetic)

`RecyclerView` 的 `LayoutManager` 与 `ItemDecoration` 的装饰器。

[LinearHorizontalItemDecoration](kdoc://ui-component/ui-component/com.highcapable.betterandroid.ui.component.adapter.recycler.decoration/-linear-horizontal-item-decoration)

`RecyclerView` 的线性横向列表装饰器。

[LinearVerticalItemDecoration](kdoc://ui-component/ui-component/com.highcapable.betterandroid.ui.component.adapter.recycler.decoration/-linear-vertical-item-decoration)

`RecyclerView` 的线性纵向列表装饰器。

[GridVerticalItemDecoration](kdoc://ui-component/ui-component/com.highcapable.betterandroid.ui.component.adapter.recycler.decoration/-grid-vertical-item-decoration)

`RecyclerView` 的九宫格纵向列表装饰器。

[CommonAdapterFactory](kdoc://ui-component/ui-component/com.highcapable.betterandroid.ui.component.adapter.factory)

适用于上述适配器构建的扩展方法。

:::

Android 中的适配器从 `ListView` 开始到 `RecyclerView` 的出现，一直是开发者最头疼的问题之一。

针对这个问题，`BetterAndroid` 对以下组件的适配器进行了封装：

`ListView`、`AutoCompleteTextView`、`ListPopupWindow`、`RecyclerView`、`ViewPager`、`ViewPager2`

在 Kotlin 中你能够更加方便地创建一个数据适配器。

现在，你只需要一个数据数组和一个自定义的适配器布局，就可以非常快速地创建一个适配器并绑定到上述这些组件上。

为 `ListView`、`AutoCompleteTextView`、`ListPopupWindow` 创建一个 `BaseAdapter`。

> 示例如下

```kotlin
// 假设这就是你的实体类
data class CustomBean(
    var iconRes: Int,
    var name: String
)
// 假设这就是你需要绑定的数据集
val listData = ArrayList<CustomBean>()
// 创建并绑定到自定义的 BaseAdapter
val adapter = listView.bindAdapter<CustomBean> {
    // 绑定数据集
    onBindData { listData }
    // 绑定自定义适配器布局 adapter_custom.xml
    onBindViews<AdapterCustomBinding> { binding, bean, position ->
        binding.iconView.setImageResource(bean.iconRes)
        binding.textView.text = bean.name
    }
    // 设置每项条目的点击事件
    onItemViewsClick { itemView, bean, position ->
        // Your code here.
    }
}
```

如果你希望手动创建一个适配器并绑定到上述组件上，请参考以下示例。

> 示例如下

```kotlin
// 假设这就是你当前的 Context
val context: Context
// 手动创建一个 BaseAdapter
val adapter = CommonAdapter<CustomBean>(context) {
    // 内容与上述相同
}
// 然后绑定到 listView
listView.adapter = adapter
```

为 `ViewPager` 创建一个 `PagerAdapter`。

> 示例如下

```kotlin
// 假设这就是你的实体类
data class CustomBean(
    var iconRes: Int,
    var name: String
)
// 假设这就是你需要绑定的数据集
val listData = ArrayList<CustomBean>()
// 创建并绑定到自定义的 PagerAdapter
val adapter = viewPager.bindAdapter<CustomBean> {
    // 绑定数据集
    onBindData { listData }
    // 绑定自定义适配器布局 adapter_custom.xml
    onBindViews<AdapterCustomBinding> { binding, bean, position ->
        binding.iconView.setImageResource(bean.iconRes)
        binding.textView.text = bean.name
    }
}
```

你也可以直接使用 `dataSetCount` 来不指定数据集，仅重复创建多个页面。

> 示例如下

```kotlin
// 创建并绑定到自定义的 PagerAdapter
val adapter = viewPager.bindAdapter {
    // 手动创建两个相同的页面
    dataSetCount = 2
    // 绑定自定义适配器布局 adapter_custom.xml
    onBindViews<AdapterCustomBinding> { binding, bean, position ->
        // 你可以通过 position 判断当前页面的位置
        binding.iconView.setImageResource(bean.iconRes)
        binding.textView.text = bean.name
    }
}
```

你也可以复用 `onBindViews` 方法来创建多个不同的页面，页面顺序按照创建顺序决定。

> 示例如下

```kotlin
// 创建并绑定到自定义的 PagerAdapter
val adapter = viewPager.bindAdapter {
    // 绑定自定义适配器布局 adapter_custom_1.xml
    onBindViews<AdapterCustom1Binding> { binding, bean, position ->
        binding.iconView.setImageResource(bean.iconRes)
        binding.textView.text = bean.name
    }
    // 绑定自定义适配器布局 adapter_custom_2.xml
    onBindViews<AdapterCustom2Binding> { binding, bean, position ->
        binding.iconView.setImageResource(bean.iconRes)
        binding.textView.text = bean.name
    }
}
```

创建的页面数量为复用 `onBindViews` 方法的次数。

::: danger

如果你复用 `onBindViews` 方法创建了多个不同页面，你不能再指定 `dataSetCount` 或绑定数据集。

:::

如果你需要处理 `PagerAdapter` 中的 `getPageTitle`、`getPageWidth`，你可以使用 `PagerMediator` 来完成。

> 示例如下

```kotlin
// 创建并绑定到自定义的 PagerAdapter
val adapter = viewPager.bindAdapter {
    // 绑定每项的 PagerMediator
    onBindMediators {
        // 处理页面的标题
        title =  when (position) {
            0 -> "主页"
            else -> "附加页"
        }
        // 处理页面的宽度 (比例)
        width = when (position) {
            0 -> 1f
            else -> 0.5f
        }
    }
    // ...
}
```

如果你希望手动创建一个 `PagerAdapter` 并绑定到 `ViewPager` 上，请参考以下示例。

> 示例如下

```kotlin
// 假设这就是你当前的 Context
val context: Context
// 手动创建一个 PagerAdapter
val adapter = PagerAdapter<CustomBean>(context) {
    // 内容与上述相同
}
// 然后绑定到 viewPager
viewPager.adapter = adapter
```

为 `RecyclerView`、`ViewPager2` 创建一个常规的 `RecyclerView.Adapter`。

> 示例如下

```kotlin
// 假设这就是你的实体类
data class CustomBean(
    var iconRes: Int,
    var name: String
)
// 假设这就是你需要绑定的数据集
val listData = ArrayList<CustomBean>()
// 创建并绑定到自定义的 RecyclerView.Adapter
val adapter = recyclerView.bindAdapter<CustomBean> {
    // 绑定数据集
    onBindData { listData }
    // 绑定自定义适配器布局 adapter_custom.xml
    onBindViews<AdapterCustomBinding> { binding, bean, position ->
        binding.iconView.setImageResource(bean.iconRes)
        binding.textView.text = bean.name
    }
    // 设置每项条目的点击事件
    onItemViewsClick { itemView, viewType, bean, position ->
        // Your code here.
    }
}
```

为 `RecyclerView`、`ViewPager2` 创建一个多 `View` 类型的 `RecyclerView.Adapter`。

> 示例如下

```kotlin
// 假设这就是你的实体类
data class CustomBean(
    var iconRes: Int,
    var name: String,
    var title: String,
    var dataType: Int
)
// 假设这就是你需要绑定的数据集
val listData = ArrayList<CustomBean>()
// 创建并绑定到自定义的 RecyclerView.Adapter
val adapter = recyclerView.bindAdapter<CustomBean> {
    // 绑定数据集
    onBindData { listData }
    // 绑定 View 类型
    onBindViewsType { bean, position -> bean.dataType }
    // 绑定自定义适配器布局 adapter_custom_1.xml
    onBindViews<AdapterCustom1Binding>(viewType = 1) { binding, bean, position ->
        binding.iconView.setImageResource(bean.iconRes)
        binding.textView.text = bean.name
    }
    // 绑定自定义适配器布局 adapter_custom_2.xml
    onBindViews<AdapterCustom2Binding>(viewType = 2) { binding, bean, position ->
        binding.iconView.setImageResource(bean.iconRes)
        binding.titleView.text = bean.title
    }
    // 设置每项条目的点击事件
    onItemViewsClick { itemView, viewType, bean, position ->
        // Your code here.
    }
}
```

如果你希望手动创建一个 `RecyclerView.Adapter` 并绑定到 `RecyclerView`、`ViewPager2` 上，请参考以下示例。

> 示例如下

```kotlin
// 假设这就是你当前的 Context
val context: Context
// 手动创建一个 RecyclerView.Adapter
val adapter = RecyclerAdapter<CustomBean>(context) {
    // 内容与上述相同
}
// 手动创建一个列表装饰器
val cosmetic = RecyclerCosmetic.fromLinearVertical(context)
// 然后绑定到 recyclerView
recyclerView.layoutManager = cosmetic.layoutManager
recyclerView.addItemDecoration(cosmetic.itemDecoration) 
recyclerView.adapter = adapter
// 绑定到 viewPager2 时你无需设置 layoutManager
viewPager2.addItemDecoration(cosmetic.itemDecoration) 
viewPager2.adapter = adapter
```

`BetterAndroid` 为开发者针对 `RecyclerView` 预置了几种常用的适配器布局类型以供你使用。

你可以在方法参数中指定一个 `RecyclerCosmetic` (列表装饰器)，默认为线性纵向列表装饰器。

> 示例如下

```kotlin
// 创建一个线性纵向列表，行间距为 10dp
val lvCosmetic = RecyclerCosmetic.fromLinearVertical(context, 10.toPx(context))
// 创建一个九宫格纵向列表，列间距为 10dp，行间距为 10dp
val gvCosmetic = RecyclerCosmetic.fromGridVertical(context, 10.toPx(context), 10.toPx(context))
// 以 lvCosmetic 举例
// 使用 bindAdapter 绑定到 recyclerView
recyclerView.bindAdapter<CustomBean>(lvCosmetic) {
    // ...
}
// 或者，手动绑定
val adapter = RecyclerAdapter<CustomBean>(context) {
    // ...
}
recyclerView.layoutManager = lvCosmetic.layoutManager
recyclerView.addItemDecoration(lvCosmetic.itemDecoration)
recyclerView.adapter = adapter
```

::: tip

如果你只需要一个 `ItemDecoration`，你可以通过预置的 `LinearHorizontalItemDecoration`、`LinearVerticalItemDecoration`、`GridVerticalItemDecoration` 来创建。

以下是一个简单的示例。

> 示例如下

```kotlin
// 创建一个线性纵向 ItemDecoration，行间距为 10dp
val itemDecoration = LinearVerticalItemDecoration(rowSpacing = 10.toPx(context))
// 设置到 recyclerView
recyclerView.addItemDecoration(itemDecoration)
// 如果你需要更新 ItemDecoration 的参数，你可以使用 update 方法
itemDecoration.update(rowSpacing = 15.toPx(context))
// 然后通知 recyclerView 更新
recyclerView.invalidateItemDecorations()
```

:::

除了上述示例中使用 `ViewBinding` 的方式之外，你还可以使用传统的 `View` 或一个布局资源 ID 来绑定其到适配器布局。

> 示例如下

```kotlin
// 绑定自定义适配器布局 adapter_custom.xml
onBindViews(R.layout.adapter_custom) { view, bean, position ->
    view.findViewById<ImageView>(R.id.icon_view).setImageResource(bean.iconRes)
    view.findViewById<TextView>(R.id.text_view).text = bean.name
}
// 假设这就是你的自定义 View
val adapterView: View
// 绑定自定义适配器布局到 adapterView
onBindViews(adapterView) { view, bean, position ->
    // Your code here.
}
```

为 `ViewPager` 创建一个 `FragmentPagerAdapter`。

::: warning

这种用法已被官方弃用，如果可能，请开始使用 `ViewPager2`。

:::

> 示例如下

```kotlin
// 假设这就是你当前的 FragmentActivity
val activity: FragmentActivity
// 创建并绑定到自定义的 FragmentPagerAdapter
val adapter = viewPager.bindFragments(activity) {
    // 设置需要显示的 Fragment 个数
    pageCount = 5
    // 绑定每个 Fragment
    onBindFragments { position ->
        when (position) {
            0 -> FirstFragment()
            1 -> SecondFragment()
            2 -> ThirdFragment()
            3 -> FourthFragment()
            else -> FifthFragment()
        }
    }
}
```

与 `FragmentPagerAdapter` 的构造方法使用方法相同，你还可以自定义其中的 `behavior` 参数。

如果你正在 `Fragment` 中使用，你可以在 `bindFragments` 的第一位参数中填入当前 `Fragment` 实例，将会自动绑定到 `getChildFragmentManager()`。

如果你希望手动创建一个 `FragmentPagerAdapter` 并绑定到 `ViewPager` 上，请参考以下示例。

> 示例如下

```kotlin
// 假设这就是你当前的 FragmentActivity
val activity: FragmentActivity
// 手动创建一个 FragmentPagerAdapter
val adapter = FragmentPagerAdapter(activity) {
    // 内容与上述相同
}
// 然后绑定到 viewPager
viewPager.adapter = adapter
```

为 `ViewPager2` 创建一个 `FragmentStateAdapter`。

> 示例如下

```kotlin
// 假设这就是你当前的 FragmentActivity
val activity: FragmentActivity
// 创建并绑定到自定义的 FragmentPagerAdapter
val adapter = viewPager2.bindFragments(activity) {
    // 设置需要显示的 Fragment 个数
    pageCount = 5
    // 绑定每个 Fragment
    onBindFragments { position ->
        when (position) {
            0 -> FirstFragment()
            1 -> SecondFragment()
            2 -> ThirdFragment()
            3 -> FourthFragment()
            else -> FifthFragment()
        }
    }
}
```

如果你正在 `Fragment` 中使用，你可以在 `bindFragments` 的第一位参数中填入当前 `Fragment` 实例，将会自动绑定到 `getChildFragmentManager()`。

如果你希望手动创建一个 `FragmentPagerAdapter` 并绑定到 `ViewPager2` 上，请参考以下示例。

> 示例如下

```kotlin
// 假设这就是你当前的 FragmentActivity
val activity: FragmentActivity
// 手动创建一个 FragmentStateAdapter
val adapter = FragmentStateAdapter(activity) {
    // 内容与上述相同
}
// 然后绑定到 viewPager2
viewPager2.adapter = adapter
```

### 系统事件

::: tip 本节内容

[BackPressedController](kdoc://ui-component/ui-component/com.highcapable.betterandroid.ui.component.backpress/-back-pressed-controller)

返回事件控制器。

[OnBackPressedCallback](kdoc://ui-component/ui-component/com.highcapable.betterandroid.ui.component.backpress.callback/-on-back-pressed-callback)

简单返回事件回调。

[IBackPressedController](kdoc://ui-component/ui-component/com.highcapable.betterandroid.ui.component.proxy/-i-back-pressed-controller)

返回事件控制器接口。

:::

在 `androidx` 的依赖 `androidx.activity:activity` 中已经为开发者提供了一个 `OnBackPressedDispatcher`。

但是出于对官方贸然作废重写 `onBackPressed` 方法的不满，`BetterAndroid` 对 `OnBackPressedDispatcher` 相关功能进行了封装，
支持了更适用于 Kotlin 写法的返回事件回调功能，同时添加了忽略全部回调事件直接释放返回事件的功能，使其变得更加灵活好用。

`AppBindingActivity`、`AppViewsActivity`、`AppComponentActivity`、`AppBindingFragment`、`AppViewsFragment`
已经默认实现了 `IBackPressedController` 接口，你可以直接使用 `backPressed` 获取 `BackPressedController`。

但是你依然可以在 `Activity` 中手动创建一个 `BackPressedController`。

> 示例如下

```kotlin
class YourActivity : AppCompatActivity() {

    // 创建一个懒加载对象
    val backPressed by lazy { BackPressedController.from(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // 在这里调用 backPressed 实现相关功能
        backPressed
    }
    
    override fun onDestroy() {
        super.onDestroy()
        // 销毁 backPressed，这会移除所有回调事件
        // 可选，防止内存泄漏
        backPressed.destroy()
    }
}
```

下面是 `BackPressedController` 的基本用法。

> 示例如下

```kotlin
// 添加一个返回回调
val callback = backPressed.addCallback {
    // 在回调内忽略当前回调并触发返回操作
    // 例如你可以在此处弹出一个对话框询问用户是否退出且此时选择了 “是”
    // 传入的对象需要为创建此回调的 backPressed
    trigger(backPressed)
    // 或者在触发后同时移除自身
    trigger(backPressed, removed = true)
    // 直接移除 (不推荐，你应该使用 backPressed.removeCallback)
    remove()
}
// 你也可以手动创建一个回调
// 注意：请确保引入 com.highcapable.betterandroid.ui.component.backpress.callback
//      包名下的 OnBackPressedCallback，而不是 androidx.activity.OnBackPressedCallback
val callback = OnBackPressedCallback {
    // Your code here.
}
// 然后添加到 backPressed
backPressed.addCallback(callback)
// 移除一个已知的回调
backPressed.removeCallback(callback)
// 触发系统的返回操作
backPressed.trigger()
// 你可以设置 ignored 为 true 来忽略所有已添加的回调直接返回
backPressed.trigger(ignored = true)
// 判断当前是否存在已启用的回调
val hasEnabledCallbacks = backPressed.hasEnabledCallbacks
// 销毁，这会移除所有回调事件
backPressed.destroy()
```

::: warning

在使用 `BackPressedController` 后，当前的 `OnBackPressedDispatcher` 已被其自动接管，
你不应该继续使用 `onBackPressedDispatcher.addCallback(...)`，这会造成存在未知的 (野生的) 回调导致无法干净地移除它们。

:::

### 通知

::: tip 本节内容

[NotificationBuilder](kdoc://ui-component/ui-component/com.highcapable.betterandroid.ui.component.notification/-notification-builder)

系统通知构建器。

[NotificationChannelBuilder](kdoc://ui-component/ui-component/com.highcapable.betterandroid.ui.component.notification/-notification-channel-builder)

系统通知渠道构建器。

[NotificationChannelGroupBuilder](kdoc://ui-component/ui-component/com.highcapable.betterandroid.ui.component.notification/-notification-channel-group-builder)

系统通知渠道组构建器。

[NotificationImportance](kdoc://ui-component/ui-component/com.highcapable.betterandroid.ui.component.notification.type/-notification-importance)

系统通知优先级。

[NotificationPoster](kdoc://ui-component/ui-component/com.highcapable.betterandroid.ui.component.notification/-notification-poster)

系统通知推送器。

[NotificationFactory](kdoc://ui-component/ui-component/com.highcapable.betterandroid.ui.component.notification.factory)

适用于通知构建的扩展方法。

:::

想要在 Android 中创建并发送一条通知并不容易，其中最大的问题就在于系统通知的创建复杂、管理混乱且 API 难以简单地兼容旧版本。

尤其是当开发者看到了 `NotificationCompat` 以及 `NotificationChannelCompat` 这两个类时，更是会感到无从下手。

于是 `BetterAndroid` 对系统通知相关 API 进行了整体性的封装，基本上覆盖了系统通知中能够用到的所有功能和调用。

所以你不需要再考虑类似通知渠道这样 Android 8 及以下系统的兼容性问题，`BetterAndroid` 已经为你处理了这些问题。

在 Kotlin 中你能够更加方便地创建一个系统通知。

> 示例如下

```kotlin
// 假设这就是你当前的 Context
val context: Context
// 创建需要推送的通知对象
val notification = context.createNotification(
    // 创建并设置通知渠道
    // 在 Android 8 及以上系统中必须存在一个通知渠道
    // 在低于 Android 8 的系统中，此功能会被自动兼容化处理
    channel = NotificationChannel("my_channel_id") {
        // 设置通知渠道名称 (这会显示在系统的通知设置中)
        name = "My Channel"
        // 设置通知渠道描述 (这会显示在系统的通知设置中)
        description = "My channel description."
        // 其余用法与 NotificationChannelCompat.Builder 保持一致
    }
) {
    // 设置通知小图标 (这将会显示在状态栏和通知栏中)
    // 通知小图标必须为单色图标 (建议为矢量图)
    smallIconResId = R.drawable.ic_my_notification
    // 设置通知标题
    contentTitle = "My Notification"
    // 设置通知内容
    contentText = "Hello World!"
    // 其余用法与 NotificationCompat.Builder 保持一致
}
// 使用默认通知 ID 推送通知
notification.post()
// 使用自定义通知 ID 推送通知
val notifyId = 1
notification.post(notifyId)
// 取消当前通知 (这会从系统通知栏中清除这条通知)
notification.cancel()
// 判断当前通知是否已经被取消
val isCanceled = notification.isCanceled
```

::: warning

在 Android 13 及以上系统中，你需要为通知定义并添加运行时权限。

当未正确定义此权限时，调用 `post` 方法时将自动要求你添加权限到 `AndroidManifest.xml` 中。

请参考 [Notification runtime permission](https://developer.android.com/develop/ui/views/notifications/notification-permission)。

:::

你可以使用以下方式在通知渠道中为通知设置优先级。

> 示例如下

```kotlin
// 假设这就是你当前的 Context
val context: Context
// 创建需要推送的通知对象
val notification = context.createNotification(
    // 创建并设置通知渠道
    // 在低于 Android 8 的系统中，此功能会被自动兼容化处理
    // 优先级决定了通知的重要性，这会影响通知的显示方式
    // BetterAndroid 将 NotificationManager 中的优先级静态变量
    // 封装到了 NotificationImportance 中，你可以更方便地设置通知的优先级
    // 这里我们设置了 NotificationImportance.HIGH (高优先级)，
    // 这将会在系统通知栏中以横幅的形式显示通知并伴随响铃提醒
    channel = NotificationChannel("my_channel_id", importance = NotificationImportance.HIGH) {
        name = "My Channel"
        description = "My channel description."
    }
) {
    smallIconResId = R.drawable.ic_my_notification
    contentTitle = "My Notification"
    contentText = "Hello World!"
}
// 使用默认通知 ID 推送通知
notification.post()
```

当遇到多组通知时，你可以使用以下方式创建一组通知渠道。

> 示例如下

```kotlin
// 假设这就是你当前的 Context
val context: Context
// 创建一个通知渠道组
// 在低于 Android 8 的系统中，此功能将无作用
val channelGroup = NotificationChannelGroup("my_channel_group_id") {
    // 设置通知渠道组名称 (这会显示在系统的通知设置中)
    name = "My Channel Group"
    // 设置通知渠道组描述 (这会显示在系统的通知设置中)
    description = "My channel group description."
}
// 创建第一个通知渠道并指定通知渠道组
val channel1 = NotificationChannel("my_channel_id_1", channelGroup) {
    name = "My Channel 1"
    description = "My channel description."
}
// 创建第二个通知渠道并指定通知渠道组
val channel2 = NotificationChannel("my_channel_id_2", channelGroup) {
    name = "My Channel 2"
    description = "My channel description."
}
// 使用 channel1 创建第一条通知并推送
context.createNotification(channel1) {
    smallIconResId = R.drawable.ic_my_notification
    contentTitle = "My Notification 1"
    contentText = "Hello World!"
}.post(1)
// 使用 channel2 创建第二条通知并推送
context.createNotification(channel2) {
    smallIconResId = R.drawable.ic_my_notification
    contentTitle = "My Notification 2"
    contentText = "Hello World!"
}.post(2)
```

上述内容将创建一个通知渠道组并在其中添加两个通知渠道。

通知推送后，系统将会自动为这两个通知渠道创建一个组分类。

::: warning

通知渠道中的设置仅会在首次创建这个通知渠道时生效，如果通知渠道的设置被用户修改过，那么这些设置将不会再被覆盖。

你无法修改已经创建的通知渠道设置，但是你可以重新为其分配一个新的通知渠道 ID，这样将会创建一个新的通知渠道。

请参考 [Create and manage notification channels](https://developer.android.com/develop/ui/views/notifications/channels)。

:::

上方的示例中，通知对象是被自动化管理的，如果你希望手动创建一个通知对象而并不依赖于 `context.createNotification` 方法，请参考以下示例。

> 示例如下

```kotlin
// 假设这就是你当前的 Context
val context: Context
// 创建需要推送的通知对象
val notification = Notification(
    // 设置 Context
    context = context,
    // 创建并设置通知渠道
    channel = NotificationChannel("my_channel_id") {
        name = "My Channel"
        description = "My channel description."
    }
) {
    smallIconResId = R.drawable.ic_my_notification
    contentTitle = "My Notification"
    contentText = "Hello World!"
}
// 将当前通知作为推送对象
val poster = notification.asPoster()
// 使用默认通知 ID 推送通知
poster.post()
// 使用自定义通知 ID 推送通知
val notifyId = 1
poster.post(notifyId)
// 取消当前通知 (这会从系统通知栏中清除这条通知)
poster.cancel()
// 判断当前通知是否已经被取消
val isCanceled = poster.isCanceled
```

::: tip

通过 `Notification`、`NotificationChannel`、`NotificationChannelGroup` 创建的对象是对
`NotificationCompat`、`NotificationChannelCompat`、`NotificationChannelGroupCompat` 的一个包装，你可以使用 `instance` 来得到其中的实际对象以进行一些你自己的操作。

你还可以通过 `Context.notificationManager` 来获取到 `NotificationManagerCompat` 对象以进行一些你自己的操作。

:::

### 边衬区 (Insets)

::: tip 本节内容

[WindowInsetsWrapper](kdoc://ui-component/ui-component/com.highcapable.betterandroid.ui.component.insets/-window-insets-wrapper)

`WindowInsets` 的包装器。

[WindowInsetsWrapper.Absolute](kdoc://ui-component/ui-component/com.highcapable.betterandroid.ui.component.insets/-window-insets-wrapper/-absolute)

`WindowInsetsWrapper` 的绝对 Insets 对象。

[InsetsWrapper](kdoc://ui-component/ui-component/com.highcapable.betterandroid.ui.component.insets/-insets-wrapper)

`Insets` 的包装器。

[InsetsFactory](kdoc://ui-component/ui-component/com.highcapable.betterandroid.ui.component.insets.factory)

适用于 `Insets`、`WindowInsets` 的扩展方法。

:::

::: tip

“边衬区” 是来自 [Android 开发者文档](https://developer.android.com/develop/ui/views/layout/insets?hl=zh-cn) 的简体中文翻译，请以英文原版的 Insets 为准。
:::

::: warning

在 `1.0.3` 及之前版本的依赖中，`BetterAndroid` 将 Insest、Window Insets 与 [系统栏 (状态栏、导航栏等)](#系统栏-状态栏、导航栏等) 封装在了一起，
这是曾经不正确的做法，目前对于 Insets、Window Insets 已被解耦合为独立的功能，正如你现在所看到的。

:::

Insets 和 Window Insets 在 Android 中是一个非常重要的概念，虽然这个 API 早在 Android 5.0 就已经存在了，但是在 Android 10 中才被官方正式推荐使用。(自 Android 9 开始，系统加入了异形屏处理的相关 API)

Insets 是一个特殊的空间，它代表 “附着” 在视图四周的占位区域，而诸如异形屏 (刘海屏) 遮挡的部分、状态栏、导航栏以及输入法等系统持有的 Insets 则称为 Window Insets。

`BetterAndroid` 所做的主要就是对这套 API 进行了封装，使其更加易用。

下面，你可以通过一个存在的 `WindowInsets` 对象来创建一个 `WindowInsetsWrapper` 对象。

::: tip

`WindowInsetsWrapper` 是参照 Jetpack Compose 官方提供的 [Window Insets API](https://developer.android.com/jetpack/compose/layouts/insets) 设计的，你能够更加有好的在原生层面上使用这套 API。

:::

出于对向下兼容的考虑，`WindowInsetsWrapper` 封装的对象为 `WindowInsetsCompat` 并建议使用它而不是 `WindowInsets`。

`WindowInsetsWrapper` 封装了 `WindowInsetsCompat.getInsets`、`WindowInsetsCompat.getInsetsIgnoringVisibility`、`WindowInsetsCompat.isVisible` 等方法，
你无需再为了获取一个 Insets 对象而写超级长的 `WindowInsetsCompat.getInsets(WindowInsetsCompat.Type.systemBars())` 等代码。

> 示例如下

```kotlin
// 假设这个就是你的 WindowInsets
val windowInsets: WindowInsetsCompat
// 创建一个 WindowInsetsWrapper
val insetsWrapper = windowInsets.createWrapper()
// 你还可以通过 from 方法来创建
val insetsWrapper = WindowInsetsWrapper.from(windowInsets)
// 获取系统栏的 Insets
val systemBars = insetsWrapper.systemBars
// 通常情况下，获取到的 Insets 会包含它的可见性，
// 在不可见的情况下，Insets 的值全部为 0
// 你可以通过参数 ignoringVisibility 来忽略可见性
val systemBars = insetsWrapper.systemBars(ignoreVisibility = true)
// 获取到 Insets 对象后，你可以使用 isVisible 来判断其是否可见
// 注意：Insets 的值是由系统提供的，isVisible 只是一个状态，
// 无论其值是否为 0，你都可以使用它来判断当前的 Insets 是否可见
val insetsIsVisible = systemBars.isVisible
```

`BetterAndroid` 针对 Android 9 以下异形屏设备的主流品牌的厂商各自的私有方案做了一个兼容处理，如果你需要兼容更旧的设备，你可以在方法参数中传入一个可选的 `Window` 对象。

如果你的应用程序只需要适配 Android 9 及以上的设备，你可以忽略此参数。

> 示例如下

```kotlin
// 假设这个就是你的 Activity
val activity: Activity
// 在通常情况下，你可以通过当前 Activity 获取 Window
val window = activity.window
// 创建一个 WindowInsetsWrapper
val insetsWrapper = windowInsets.createWrapper(window)
// 你还可以通过 from 方法来创建
val insetsWrapper = WindowInsetsWrapper.from(windowInsets, window)
// 获取异形屏的 Insets
val displayCutout = insetsWrapper.displayCutout
```

::: warning

如果你的应用程序需要在 Android 10 或以下设备上运行，我们建议始终传入一个 `Window` 对象以保证 `BetterAndroid` 能正确为你处理兼容问题。

目前已知兼容问题为 `androidx` 提供的兼容处理方法无法对 Android 11 以下设备的 `statusBars`、`navigationBars`、`systemBars` 的 `isVisible` 和其内容给出正确的值，`BetterAndroid` 为此进行了修复。

:::

你从 `WindowInsetsWrapper` 获取到的任何 Insets 对象即 `InsetsWrapper`，它封装了 `Insets` 对象并实现了可控的 `isVisible` 状态。

`InsetsWrapper` 可以轻松地转换为原始的 `Insets` 对象，同时也可以重新转换为 `InsetsWrapper`。

> 示例如下

```kotlin
// 获取系统栏的 Insets
val systemBars = insetsWrapper.systemBars
// 转换为 Insets
val insets = systemBars.toInsets()
// 转换为 InsetsWrapper
val wrapper = insets.toWrapper(systemBars.isVisible)
// 你还可以通过 of 方法来创建
val wrapper = InsetsWrapper.of(insets, systemBars.isVisible)
```

与 `Insets` 不同的是，`InsetsWrapper` 重载了运算符，你可以使用 `+`、`-` 以及 `or`、`and` 来对其进行运算或是对其进行比较。

> 示例如下

```kotlin
val insets1 = InsetsWrapper.of(10, 10, 10, 10)
val insets2 = InsetsWrapper.of(20, 20, 20, 20)
// 使用 "+" 运算符，等同于 Insets.add(insets1, insets2)
val insets3 = insets1 + insets2
// 使用 "-" 运算符，等同于 Insets.subtract(insets2, insets1)
val insets3 = insets2 - insets1
// 使用 "or" 运算符，等同于 Insets.max(insets1, insets2)
val insets3 = insets1 or insets2
// 使用 "and" 运算符，等同于 Insets.min(insets1, insets2)
val insets3 = insets1 and insets2
// 使用 ">" 运算符进行比较
val isUpperTo = insets1 > insets2
// 使用 "<" 运算符进行比较
val isLowerTo = insets1 < insets2
```

获取到 Insets 对象后，一般做法是设置为 `View` 的 `padding`，让其为系统占位置的地方 “让路”。

无论是 `InsetsWrapper` 还是 `Insets`，你都不需要使用诸如 `View.setPadding(insets.left, insets.top, insets.right, insets.bottom)` 这样的形式，这看起来及其不友好。

你可以使用以下方式来轻松地将它直接设置为 `View` 的 `padding`。

> 示例如下

```kotlin
// 假设这个就是你当前的 View
val view: View
// 获取系统栏的 Insets
val systemBars = insetsWrapper.systemBars
// 使用 Insets 设置 View 的 padding
view.setInsetsPadding(systemBars)
// 由于这里演示的对象是系统栏，你可以仅更新纵向 (上下) 的 padding
// 使用 updateInsetsPadding 方法的作用同 updatePadding
view.updateInsetsPadding(systemBars, vertical = true)
```

上面我们说到了，要创建一个 `WindowInsetsWrapper` 对象，你需要一个已存在的 `WindowInsetsCompat` 对象。

出于对向下兼容的考虑，你可以使用 `ViewCompat.setOnApplyWindowInsetsListener` 来为 `View` 设置一个改变监听。

它的实质作用是控制 Window Insets 的传递，Window Insets 通过 `View.onApplyWindowInsets` 方法由根视图向子视图传递，
直到你使用 `WindowInsetsCompat.CONSUMED` 来明确消费掉它才会停止传递。

> 示例如下

```kotlin
// 假设这个就是你当前的 View
val view: View
// 设置 View 的 Window Insets 改变监听
ViewCompat.setOnApplyWindowInsetsListener(view) { view, insets ->
    // insets 就是当前的 WindowInsetsCompat 对象
    // 你可以通过它创建 WindowInsetsWrapper
    val insetsWrapper = insets.createWrapper()
    // 在最后一位消费掉 Window Insets，将停止继续向下传递
    WindowInsetsCompat.CONSUMED // 或者填入当前的 insets 继续向下传递
}
```

这样的做法看起来会很麻烦，所以 `BetterAndroid` 同样为你提供了一个更加简单的方法。

例如，我们需要得知输入法所占的空间并为输入法布局设置来自 Window Insets 的 `padding`。

此时你可以使用 `View.handleOnWindowInsetsChanged` 直接得到一个 `WindowInsetsWrapper` 对象。

> 示例如下

```kotlin
// 假设这就是你的输入法布局
val imeSpaceLayout: FrameLayout
// 处理 View 的 Window Insets 改变监听
imeSpaceLayout.handleOnWindowInsetsChanged { imeSpaceLayout, insetsWrapper ->
    // 设置由 ime 提供的 padding
    imeSpaceLayout.setInsetsPadding(insetsWrapper.ime)
    // 或者使用 ime 更新底部的 padding
    imeSpaceLayout.updateInsetsPadding(insetsWrapper.ime, bottom = true)
}
```

如果你想对子视图消费掉 Window Insets 使其不再向下传递，你只需要在方法参数中设置 `consumed = true` 即可。

> 示例如下

```kotlin
// 处理 View 的 Window Insets 改变监听
imeSpaceLayout.handleOnWindowInsetsChanged(consumed = true) { imeSpaceLayout, insetsWrapper ->
    // 内容与上述相同
}
```

如果你想同时在 Window Insets 改变时使其拥有动画效果，你无需重新设置一个 `View.setWindowInsetsAnimationCallback`。

你只需要在方法参数中设置 `animated = true` 即可，这样回调就会在每次 Window Insets 改变中触发。

> 示例如下

```kotlin
// 处理 View 的 Window Insets 改变监听
imeSpaceLayout.handleOnWindowInsetsChanged(animated = true) { imeSpaceLayout, insetsWrapper ->
    // 内容与上述相同
}
```

::: warning

这个特性是从 Android 11 开始引入的，在这之前的系统中回调依然会被立即触发，所以将不会产生任何动画效果。

:::

另外，当你设置了 Window Insets 改变的监听后，你不需要关心监听是何时设置的，你可以在任何时候移除它们。

这个操作会移除所有 `View.setOnApplyWindowInsetsListener`、`View.setWindowInsetsAnimationCallback`。

> 示例如下

```kotlin
// 假设这个就是你当前的 View
val view: View
// 移除 View 的 Window Insets 改变监听
view.removeWindowInsetsListener()
```

::: warning

你只能为一个 `View` 设置一个 Window Insets 监听，重复设置的监听会被最后一次覆盖掉。

:::

如果你想直接从当前 `View` 中获取 Window Insets，那么你还可以使用以下方式创建一个 `WindowInsetsWrapper` 对象。

> 示例如下

```kotlin
// 假设这个就是你当前的 View
val view: View
// 创建一个 WindowInsetsWrapper
val insetsWrapper = view.createRootWindowInsetsWrapper()
// 你还可以通过 from 方法来创建
val insetsWrapper = WindowInsetsWrapper.from(view)
// 获取系统栏的 Insets
// 如果无法通过 View 获取到 Window Insets，将会返回 null
val systemBars = insetsWrapper?.systemBars
```

除了上述做法，`WindowInsetsWrapper` 还提供了一个 `WindowInsetsWrapper.Absolute` 对象，你可以无需通过任何监听即可直接通过
`Window.getDecorView` 获取到一个绝对的 Insets 对象。

> 示例如下

```kotlin
// 假设这个就是你的 Activity
val activity: Activity
// 在通常情况下，你可以通过当前 Activity 获取 Window
val window = activity.window
// 创建一个 WindowInsetsWrapper.Absolute
val absoluteWrapper = WindowInsetsWrapper.Absolute.from(window)
// 获取状态栏的 Insets
val statusBar = absoluteWrapper.statusBar
// 获取导航栏的 Insets
val navigationBar = absoluteWrapper.navigationBar
// 获取系统栏的 Insets
val systemBars = absoluteWrapper.systemBars
```

::: warning

这种方式获取到的值仅供参考，我们并不推荐这样去获取 Insets 对象，在当前为异形屏设备时，这些值可能会不准确。

:::

以下是 `WindowInsetsWrapper` 中提供的全部 Insets。

| Insets                    | 描述                                                                                      |
| ------------------------- | ----------------------------------------------------------------------------------------- |
| `statusBars`              | 状态栏                                                                                    |
| `navigationBars`          | 导航栏                                                                                    |
| `captionBar`              | 标题栏                                                                                    |
| `systemBars`              | 系统栏 (`captionBar` + `statusBars` + `navigationBars`)                                   |
| `ime`                     | 输入法                                                                                    |
| `tappableElement`         | 可点击元素                                                                                |
| `systemGestures`          | 系统手势                                                                                  |
| `mandatorySystemGestures` | 强制系统手势                                                                              |
| `displayCutout`           | 异形屏 (刘海屏)                                                                           |
| `waterFall`               | 瀑布屏 (曲面屏)                                                                           |
| `safeGestures`            | 安全手势 (`systemGestures` + `mandatorySystemGestures` + `waterFall` + `tappableElement`) |
| `safeDrawing`             | 安全绘制 (`displayCutout` + `systemBars` + `ime`)                                         |
| `safeDrawingIgnoringIme`  | 安全绘制 (不包括 `ime`) (`displayCutout` + `systemBars`)                                  |
| `safeContent`             | 安全内容 (`safeDrawing` + `safeGestures`)                                                 |

以下是 `WindowInsetsWrapper.Absolute` 中提供的全部 Insets。

| Insets           | 描述                                     |
| ---------------- | ---------------------------------------- |
| `statusBars`     | 状态栏                                   |
| `navigationBars` | 导航栏                                   |
| `systemBars`     | 系统栏 (`statusBars` + `navigationBars`) |

### 系统栏 (状态栏、导航栏等)

::: tip 本节内容

[SystemBarsController](kdoc://ui-component/ui-component/com.highcapable.betterandroid.ui.component.systembar/-system-bars-controller)

系统栏控制器。

[ISystemBarsController](kdoc://ui-component/ui-component/com.highcapable.betterandroid.ui.component.proxy/-i-system-bars-controller)

系统栏控制器接口。

[SystemBarStyle](kdoc://ui-component/ui-component/com.highcapable.betterandroid.ui.component.systembar.style/-system-bar-style)

系统栏的样式。

[SystemBars](kdoc://ui-component/ui-component/com.highcapable.betterandroid.ui.component.systembar.type/-system-bars)

系统栏的类型。

[SystemBarsBehavior](kdoc://ui-component/ui-component/com.highcapable.betterandroid.ui.component.systembar.type/-system-bars-behavior)

系统栏的行为。

:::

Android 开发的严重适配问题就在于终端设备没有统一开发规范的混乱性。

为了给用户带来更好的体验，状态栏、导航栏何时应该显示、隐藏，状态栏、导航栏的颜色、背景等等，这些都是开发者在开发过程中需要考虑的问题。

所以 `BetterAndroid` 对接并封装了 `androidx` 所提供的系统栏适配方案，并将其集成到了 `SystemBarsController` 中，现在，你可以非常方便地来调用它去轻松实现操作系统栏的一系列解决方案。

`SystemBarsController` 最低支持到 Android 5.0，并解决了部分厂商定制系统中的兼容性问题。

`AppBindingActivity`、`AppViewsActivity`、`AppComponentActivity`、`AppBindingFragment`、`AppViewsFragment`
已经默认实现了 `ISystemBarsController` 接口，你可以直接使用 `systemBars` 获取 `SystemBarsController`。

但是你依然可以在 `Activity` 中使用 `Activity.getWindow` 对象手动创建一个 `SystemBarsController`。

> 示例如下

```kotlin
class YourActivity : AppCompatActivity() {

    // 创建一个懒加载对象
    val systemBars by lazy { SystemBarsController.from(window) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 创建你的 binding
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // 使用当前根布局初始化 systemBars
        systemBars.init(binding.root)
    }

    override fun onDestroy() {
        super.onDestroy()
        // 销毁 systemBars，这会还原初始化之前的状态
        // 可选，防止内存泄漏
        systemBars.destroy()
    }
}
```

::: warning

在使用 `init` 方法时，推荐并建议传入你自己的根布局，否则将默认使用 `android.R.id.content` 作为根布局。

你<u>**应该避免使用它作为根布局**</u>，这是不可控的，你应该做到在 `Activity` 中能够随时维护一个自己的根布局。 

如果你并未使用 `ViewBinding`，`AppViewsActivity`、`AppComponentActivity` 已经默认为你重写了 `setContentView` 方法，
它会在你使用这个方法的时候自动装载你的根布局到 `SystemBarsController` 中。

你也可以手动重写 `setContentView` 方法来实现这个功能。

> 示例如下

```kotlin
override fun setContentView(layoutResID: Int) {
    super.setContentView(layoutResID)
    // 第一位子布局即你的根布局
    val rootView = findViewById<ViewGroup>(android.R.id.content).getChildAt(0)
    // 使用当前根布局初始化 systemBars
    systemBars.init(rootView)
}
```

:::

下面是 `SystemBarsController` 的详细用法介绍。

初始化 `SystemBarsController` 及处理根布局的 Window Insets `padding`。

> 示例如下

```kotlin
// 假设这就是你当前的根布局
val rootView: ViewGroup
// 初始化 SystemBarsController
// 你的根布局必须已经被设置到了一个父布局中，否则将会抛出异常
systemBars.init(rootView)
// 你可以自定义处理根布局的 Window Insets
systemBars.init(rootView, handleWindowInsets = { systemBars })
// 如果你不希望 SystemBarsController 自动为你处理根布局的 Window Insets，
// 你可以直接设置 handleWindowInsets 为 null
systemBars.init(rootView, handleWindowInsets = null)
```

::: warning

`SystemBarsController` 初始化时会自动设置 `Window.setDecorFitsSystemWindows(false)` (在异形屏设备上会同时设置 `layoutInDisplayCutoutMode` 为 `LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES`)，
你只要在 `init` 中设置了 `handleWindowInsets` (默认设置)，
那么你的根布局将会拥有一个 `safeDrawingIgnoringIme` 控制的 Window Insets `padding`，这也是为什么你应该做到在 `Activity` 中能够随时维护一个自己的根布局。 

如果你在 `init` 中将 `handleWindowInsets` 设为了 `null`，那么你的根布局将会完全扩展到全屏。

在不做出任何操作的情况下，你的布局就会被系统栏或系统的危险区域 (例如异形屏的挖空处) 遮挡，这会影响用户体验。

如果你想自己维护并管理当前根布局的 `padding`，你必须确保你的界面元素能够正确适应 Window Insets 提供的间距，你可以前往上一节的 [边衬区 (Insets)](#边衬区-insets) 了解更多关于 Window Insets 的内容。

:::

::: tip

在 Jetpack Compose 中，你可以使用 `AppComponentActivity` 来获得一个设置了 `handleWindowInsets = null` 初始化的 `SystemBarsController`，
然后使用 Jetpack Compose 的方式去设置 Window Insets，`BetterAndroid` 同样为其提供了扩展支持，更多功能你可以参考 [compose-extension](../library/compose-extension.md)。

:::

设置系统栏的行为。

这决定了显示或隐藏系统栏时由系统控制的行为。

> 示例如下

```kotlin
systemBars.behavior = SystemBarsBehavior.SHOW_TRANSIENT_BARS_BY_SWIPE
```

以下是 `SystemBarsBehavior` 中提供的全部行为，标有 `*` 的为默认行为。

| 行为                            | 描述                                                                   |
| ------------------------------- | ---------------------------------------------------------------------- |
| `DEFAULT`                       | 由系统控制的默认行为                                                   |
| *`SHOW_TRANSIENT_BARS_BY_SWIPE` | 在全屏时可由手势滑动弹出并显示为半透明的系统栏，并在一段时间后继续隐藏 |

显示、隐藏系统栏。

> 示例如下

```kotlin
// 进入沉浸模式 (全屏模式)
// 同时隐藏状态栏和导航栏
systemBars.hide(SystemBars.ALL)
// 单独控制状态栏和导航栏
systemBars.hide(SystemBars.STATUS_BARS)
systemBars.hide(SystemBars.NAVIGATION_BARS)
// 退出沉浸模式 (全屏模式)
// 同时显示状态栏和导航栏
systemBars.show(SystemBars.ALL)
// 单独控制状态栏和导航栏
systemBars.show(SystemBars.STATUS_BARS)
systemBars.show(SystemBars.NAVIGATION_BARS)
```

设置系统栏的样式。

你可以自定义状态栏、导航栏的外观。

::: warning

在 Android 6.0 以下系统中，状态栏的内容不支持反色，如果你设置了亮色则会自动处理为半透明遮罩，但是对于 MIUI、Flyme 自行添加了反色功能的系统将使用其私有方案实现反色效果。

在 Android 8 以下系统中，导航栏的内容不支持反色，处理方式同上。

:::

> 示例如下

```kotlin
// 设置状态栏的样式
// 注意：请确保引入 com.highcapable.betterandroid.ui.component.systembar.style
//      包名下的 SystemBarStyle，而不是 androidx.activity.SystemBarStyle
systemBars.statusBarStyle = SystemBarStyle(
    // 设置背景颜色
    color = Color.WHITE,
    // 设置内容颜色
    darkContent = true
)
// 设置导航栏的样式
systemBars.navigationBarStyle = SystemBarStyle(
    // 设置背景颜色
    color = Color.WHITE,
    // 设置内容颜色
    darkContent = true
)
// 你可以一次性设置状态栏和导航栏的样式
systemBars.setStyle(
    statusBar = SystemBarStyle(
        color = Color.WHITE,
        darkContent = true
    ),
    navigationBar = SystemBarStyle(
        color = Color.WHITE,
        darkContent = true
    )
)
// 你也可以同时设置状态栏和导航栏的样式
systemBars.setStyle(
    style = SystemBarStyle(
        color = Color.WHITE,
        darkContent = true
    )
)
```

以下是 `SystemBarStyle` 中提供的预置样式，标有 `*` 的为默认样式。

| 样式               | 描述                                                                         |
| ------------------ | ---------------------------------------------------------------------------- |
| `Auto`             | 系统深色模式下为纯黑背景 + 浅色内容颜色，浅色模式下为纯白背景 + 深色内容颜色 |
| *`AutoTransparent` | 系统深色模式下为浅色内容颜色，浅色模式下为深色内容颜色，背景透明             |
| `Light`            | 纯白背景 + 深色内容颜色                                                      |
| `LightScrim`       | 半透明纯白背景 + 深色内容颜色                                                |
| `LightTransparent` | 透明背景 + 深色内容颜色                                                      |
| `Dark`             | 纯黑背景 + 浅色内容颜色                                                      |
| `DarkScrim`        | 半透明纯黑背景 + 浅色内容颜色                                                |
| `DarkTransparent`  | 透明背景 + 浅色内容颜色                                                      |

::: tip

在应用程序首次冷启动时，系统栏的颜色将跟随你在 `styles.xml` 中设置的属性而决定。

为了能在冷启动时带来更好的用户体验，你可以参考以下示例。

> 示例如下

```xml
<style name="Theme.MyApp.Demo" parent="Theme.MaterialComponents.DayNight.NoActionBar">
    <!-- 设置状态栏颜色 -->
    <item name="android:statusBarColor">@color/colorPrimary</item>
    <!-- 设置导航栏颜色 -->
    <item name="android:navigationBarColor">@color/colorPrimary</item>
    <!-- 设置状态栏内容颜色 -->
    <item name="android:windowLightStatusBar">true</item>
    <!-- 设置导航栏内容颜色 -->
    <item name="android:windowLightNavigationBar">true</item>
</style>
```

:::

销毁 `SystemBarsController`。

这会还原初始化之前的状态，包括初始化之前的状态栏、导航栏颜色等。

> 示例如下

```kotlin
// 销毁 SystemBarsController，这会还原初始化之前的状态
systemBars.destroy()
// 你可以随时使用 isDestroyed 判断当前 SystemBarsController 是否已被销毁
val isDestroyed = systemBars.isDestroyed
```

::: warning

在使用 `SystemBarsController` 后，当前根布局 `rootView` 的 `WindowInsetsController` 已被其自动接管，
请不要手动设置 `WindowInsetsController` 中的 `isAppearanceLightStatusBars`、`isAppearanceLightNavigationBars` 等参数，
这可能会导致 `statusBarStyle`、`navigationBarStyle`、`setStyle` 等功能的实际效果显示异常。

:::