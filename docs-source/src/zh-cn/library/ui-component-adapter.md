# ui-component-adapter

![Maven Central](https://img.shields.io/maven-central/v/com.highcapable.betterandroid/ui-component-adapter?logo=apachemaven&logoColor=orange&style=flat-square)
<span style="margin-left: 5px"/>
![Maven metadata URL](https://img.shields.io/maven-metadata/v?metadataUrl=https%3A%2F%2Fraw.githubusercontent.com%2FHighCapable%2Fmaven-repository%2Frefs%2Fheads%2Fmain%2Frepository%2Freleases%2Fcom%2Fhighcapable%2Fbetterandroid%2Fui-component-adapter%2Fmaven-metadata.xml&logo=apachemaven&logoColor=orange&label=highcapable-maven-releases&style=flat-square)
<span style="margin-left: 5px"/>
![Android Min SDK](https://img.shields.io/badge/Min%20SDK-21-orange?logo=android&style=flat-square)

这是针对 UI (用户界面) 适配器组件的一个依赖。

## 配置依赖

你可以使用如下方式将此模块添加到你的项目中。

### Version Catalog (推荐)

在你的项目 `gradle/libs.versions.toml` 中添加依赖。

```toml
[versions]
betterandroid-ui-component-adapter = "<version>"

[libraries]
betterandroid-ui-component-adapter = { module = "com.highcapable.betterandroid:ui-component-adapter", version.ref = "betterandroid-ui-component-adapter" }
```

在你的项目 `build.gradle.kts` 中配置依赖。

```kotlin
implementation(libs.betterandroid.ui.component.adapter)
```

请将 `<version>` 修改为此文档顶部显示的版本。

### 传统方式

在你的项目 `build.gradle.kts` 中配置依赖。

```kotlin
implementation("com.highcapable.betterandroid:ui-component-adapter:<version>")
```

请将 `<version>` 修改为此文档顶部显示的版本。

## 功能介绍

你可以 [点击这里](kdoc://ui-component-adapter) 查看 KDoc。

::: tip 本节内容

[BaseAdapterBuilder](kdoc://ui-component-adapter/ui-component-adapter/com.highcapable.betterandroid.ui.component.adapter/-base-adapter-builder)

可用于构建一个 `BaseAdapter`。

[PagerAdapterBuilder](kdoc://ui-component-adapter/ui-component-adapter/com.highcapable.betterandroid.ui.component.adapter/-pager-adapter-builder)

可用于构建一个 `PagerAdapter`。

[RecyclerAdapterBuilder](kdoc://ui-component-adapter/ui-component-adapter/com.highcapable.betterandroid.ui.component.adapter/-recycler-adapter-builder)

可用于构建一个 `RecyclerView.Adapter`。

[PagerMediator](kdoc://ui-component-adapter/ui-component-adapter/com.highcapable.betterandroid.ui.component.adapter.mediator/-pager-mediator)

`ViewPager` 的页面调节器。

[RecyclerCosmetic](kdoc://ui-component-adapter/ui-component-adapter/com.highcapable.betterandroid.ui.component.adapter.recycler.cosmetic/-recycler-cosmetic)

`RecyclerView` 的 `LayoutManager` 与 `ItemDecoration` 的装饰器。

[LinearHorizontalItemDecoration](kdoc://ui-component-adapter/ui-component-adapter/com.highcapable.betterandroid.ui.component.adapter.recycler.decoration/-linear-horizontal-item-decoration)

`RecyclerView` 的线性横向列表装饰器。

[LinearVerticalItemDecoration](kdoc://ui-component-adapter/ui-component-adapter/com.highcapable.betterandroid.ui.component.adapter.recycler.decoration/-linear-vertical-item-decoration)

`RecyclerView` 的线性纵向列表装饰器。

[GridVerticalItemDecoration](kdoc://ui-component-adapter/ui-component-adapter/com.highcapable.betterandroid.ui.component.adapter.recycler.decoration/-grid-vertical-item-decoration)

`RecyclerView` 的九宫格纵向列表装饰器。

[LinearLayoutManager](kdoc://ui-component-adapter/ui-component-adapter/com.highcapable.betterandroid.ui.component.adapter.recycler.layoutmanager/-linear-layout-manager)

`RecyclerView` 的增强型线性列表布局管理器。

[GridLayoutManager](kdoc://ui-component-adapter/ui-component-adapter/com.highcapable.betterandroid.ui.component.adapter.recycler.layoutmanager/-grid-layout-manager)

`RecyclerView` 的增强型九宫格布局管理器。

[RecyclerLayoutManager](kdoc://ui-component-adapter/ui-component-adapter/com.highcapable.betterandroid.ui.component.adapter.recycler.layoutmanager.base/-recycler-layout-manager)

`RecyclerView` 的增强型布局管理器基类。

[RecyclerAdapterWrapper](kdoc://ui-component-adapter/ui-component-adapter/com.highcapable.betterandroid.ui.component.adapter.recycler.wrapper/-recycler-adapter-wrapper)

`RecyclerView` 的自定义适配器包装类。

[RecyclerView、RecyclerAdapter](kdoc://ui-component-adapter/ui-component-adapter/com.highcapable.betterandroid.ui.component.adapter.recycler.factory)

适用于 `RecyclerView` 和其适配器构建的扩展方法。

[CommonAdapter](kdoc://ui-component-adapter/ui-component-adapter/com.highcapable.betterandroid.ui.component.adapter.factory)

适用于上述适配器构建的扩展方法。

[ViewHolderDelegate](kdoc://ui-component-adapter/ui-component-adapter/com.highcapable.betterandroid.ui.component.adapter.viewholder.delegate.base/-view-holder-delegate)

自定义 `ViewHolder` 代理类。

[AdapterPosition](kdoc://ui-component-adapter/ui-component-adapter/com.highcapable.betterandroid.ui.component.adapter.entity/-adapter-position)

动态适配器下标实例。

:::

Android 中的适配器从 `ListView` 开始到 `RecyclerView` 的出现，一直是开发者最头疼的问题之一。

针对这个问题，`BetterAndroid` 对以下组件的适配器进行了封装：

`ListView`、`AutoCompleteTextView`、`ListPopupWindow`、`RecyclerView`、`ViewPager`、`ViewPager2`

在 Kotlin 中你能够更加方便地创建一个数据适配器。

现在，你只需要一个数据数组和一个自定义的适配器布局，就可以非常快速地创建一个适配器并绑定到上述这些组件上。

### 基本适配器

为 `ListView`、`AutoCompleteTextView`、`ListPopupWindow` 创建一个 `BaseAdapter`。

> 示例如下

```kotlin
// 假设这就是你的实体类
data class MyEntity(
    var iconRes: Int,
    var name: String
)
// 假设这就是你需要绑定的数据集
val listData = ArrayList<MyEntity>()
// 创建并绑定到自定义的 BaseAdapter
val adapter = listView.bindAdapter<MyEntity> {
    // 绑定数据集
    onBindData { listData }
    // 绑定自定义适配器布局 adapter_my_layout.xml
    onBindItemView<AdapterMyLayoutBinding> { binding, entity, position ->
        binding.iconView.setImageResource(entity.iconRes)
        binding.textView.text = entity.name
    }
    // 设置每项条目的点击事件
    onItemViewClick { itemView, entity, position ->
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
val adapter = BaseAdapter<MyEntity>(context) {
    // 内容与上述相同
}
// 然后绑定到 listView
listView.adapter = adapter
```

为 `ViewPager` 创建一个 `PagerAdapter`。

> 示例如下

```kotlin
// 假设这就是你的实体类
data class MyEntity(
    var iconRes: Int,
    var name: String
)
// 假设这就是你需要绑定的数据集
val listData = ArrayList<MyEntity>()
// 创建并绑定到自定义的 PagerAdapter
val adapter = viewPager.bindAdapter<MyEntity> {
    // 绑定数据集
    onBindData { listData }
    // 绑定自定义适配器布局 adapter_my_layout.xml
    onBindPageView<AdapterMyLayoutBinding> { binding, entity, position ->
        binding.iconView.setImageResource(entity.iconRes)
        binding.textView.text = entity.name
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
    // 绑定自定义适配器布局 adapter_my_layout.xml
    onBindPageView<AdapterMyLayoutBinding> { binding, _, position ->
        // 你可以通过 position 判断当前页面的位置
    }
}
```

你也可以复用 `onBindPageView` 方法来创建多个不同的页面，页面顺序按照创建顺序决定。

> 示例如下

```kotlin
// 创建并绑定到自定义的 PagerAdapter
val adapter = viewPager.bindAdapter {
    // 绑定自定义适配器布局 adapter_my_layout_1.xml
    onBindPageView<AdapterMyLayout1Binding> { binding, _, position ->
        // 你可以通过 position 判断当前页面的位置
    }
    // 绑定自定义适配器布局 adapter_my_layout_2.xml
    onBindPageView<AdapterMyLayout2Binding> { binding, _, position ->
        // 你可以通过 position 判断当前页面的位置
    }
}
```

创建的页面数量为复用 `onBindPageView` 方法的次数。

::: danger

如果你复用 `onBindPageView` 方法创建了多个不同页面，你不能再指定 `dataSetCount` 或绑定数据集。

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
val adapter = PagerAdapter<MyEntity>(context) {
    // 内容与上述相同
}
// 然后绑定到 viewPager
viewPager.adapter = adapter
```

### RecyclerView 适配器

Android Jetpack 为开发者带来了更现代化、功能更加丰富的适配器组件 `RecyclerView.Adapter`。

为 `RecyclerView`、`ViewPager2` 创建一个常规的 `RecyclerView.Adapter`。

> 示例如下

```kotlin
// 假设这就是你的实体类
data class MyEntity(
    var iconRes: Int,
    var name: String
)
// 假设这就是你需要绑定的数据集
val listData = ArrayList<MyEntity>()
// 创建并绑定到自定义的 RecyclerView.Adapter
val adapter = recyclerView.bindAdapter<MyEntity> {
    // 绑定数据集
    onBindData { listData }
    // 绑定自定义适配器布局 adapter_my_layout.xml
    onBindItemView<AdapterMyLayoutBinding> { binding, entity, position ->
        binding.iconView.setImageResource(entity.iconRes)
        binding.textView.text = entity.name
    }
    // 设置每项条目的点击事件
    onItemViewClick { itemView, viewType, entity, position ->
        // Your code here.
    }
}
```

为 `RecyclerView`、`ViewPager2` 创建一个多 `View` 类型的 `RecyclerView.Adapter`。

> 示例如下

```kotlin
// 假设这就是你的实体类
data class MyEntity(
    var iconRes: Int,
    var name: String,
    var title: String,
    var dataType: Int
)
// 假设这就是你需要绑定的数据集
val listData = ArrayList<MyEntity>()
// 创建并绑定到自定义的 RecyclerView.Adapter
val adapter = recyclerView.bindAdapter<MyEntity> {
    // 绑定数据集
    onBindData { listData }
    // 绑定 View 类型
    onBindViewType { entity, position -> entity.dataType }
    // 绑定自定义适配器布局 adapter_my_layout_1.xml
    onBindItemView<AdapterMyLayout1Binding>(viewType = 1) { binding, entity, position ->
        binding.iconView.setImageResource(entity.iconRes)
        binding.textView.text = entity.name
    }
    // 绑定自定义适配器布局 adapter_my_layout_2.xml
    onBindItemView<AdapterMyLayout2Binding>(viewType = 2) { binding, entity, position ->
        binding.iconView.setImageResource(entity.iconRes)
        binding.titleView.text = entity.title
    }
    // 设置每项条目的点击事件
    onItemViewClick { itemView, viewType, entity, position ->
        // Your code here.
    }
}
```

::: tip

在 `RecyclerView.Adapter` 中，`onBindItemView` 中的 `position` 类型为 `AdapterPosition` 而非 [基本适配器](#基本适配器) 中的 `Int`。

由于 `RecyclerView.Adapter` 可以局部更新，所以在动态添加或删除条目后，现有条目的 `onBindItemView` 并不会重新回调，此时你就需要 `AdapterPosition` 这样的动态下标实例，通过 `position.value` 获取到当前条目的正确下标。

`AdapterPosition` 合入了 `RecyclerView.ViewHolder` 中的 `getLayoutPosition`、`getBindingAdapterPosition`、`getAbsoluteAdapterPosition` 方法，它们对应为 `position.layout`、`position.value` 和 `position.absolute`。

:::

为 `RecyclerView` 创建头部 `View` 和末位 `View`。

你可以使用 `onBindHeaderView` 和 `onBindFooterView` 方法来添加一个头部 `View` 和末位 `View`，这是两个特殊的条目布局，它们不会被计算入绑定的数据中，且通过 `onBindItemView` 等方法回调的下标 `position` 不受影响。

::: warning

你只能同时添加一个头部 `View` 和一个末位 `View`，并且这些添加后的布局不支持动态移除。

:::

> 示例如下

```kotlin
// 假设这就是你的实体类
data class MyEntity(
    var iconRes: Int,
    var name: String
)
// 假设这就是你需要绑定的数据集
val listData = ArrayList<MyEntity>()
// 创建并绑定到自定义的 RecyclerView.Adapter
val adapter = recyclerView.bindAdapter<MyEntity> {
    // 绑定数据集
    onBindData { listData }
    // 绑定头部 View
    onBindHeaderView<AdapterHeaderBinding> { binding ->
        binding.someText.text = "Header"
    }
    // 绑定末位 View
    onBindFooterView<AdapterFooterBinding> { binding ->
        binding.someText.text = "Footer"
    }
    // 绑定自定义适配器布局 adapter_my_layout.xml
    onBindItemView<AdapterMyLayoutBinding> { binding, entity, position ->
        binding.iconView.setImageResource(entity.iconRes)
        binding.textView.text = entity.name
    }
}
```

除了上述示例中使用 `ViewBinding` 的方式之外，你还可以使用传统的布局资源 ID 来绑定其到适配器布局。

> 示例如下

```kotlin
// 绑定自定义适配器布局 adapter_my_layout.xml
onBindItemView(R.layout.adapter_my_layout) { itemView, entity, position ->
    itemView.findViewById<ImageView>(R.id.icon_view).setImageResource(entity.iconRes)
    itemView.findViewById<TextView>(R.id.text_view).text = entity.name
}
```

如果所有布局装载方式都不满足你的需求，你还可以基于 `ViewHolderDelegate` 来创建一个自定义的 `ViewHolder` 代理类。

> 示例如下

```kotlin
// 创建一个代理类，实现自己的布局装载方案
// 这里我们假设 MyLayoutBinder 就是你的布局装载器
class MyViewHolderDelegate(@LayoutRes private val resId: Int) : ViewHolderDelegate<MyLayoutBinder>() {

    override fun create(context: Context, parent: ViewGroup?): MyLayoutBinder {
        // 假设这是你自定义的布局装载器的运作方式
        // 记得传入并实现 parent 参数，因为我们需要 parent 的 LayoutParams
        // 注意：一定不要现在就绑定到 parent 上！适配器不允许子布局提前持有父布局
        val binder = MyLayoutBinder.inflate(context, resId, parent, attachToParent = false)
        return binder
    }

    override fun getView(instance: MyLayoutBinder): View {
        // 从你的布局装载器中获取需要的 View
        return instance.root
    }
}
```

然后，使用你的自定义 `ViewHolderDelegate`。

> 示例如下

```kotlin
// 绑定你的自定义 ViewHolderDelegate
onBindItemView(MyViewHolderDelegate(R.layout.adapter_my_layout)) { delegate, entity, position ->
    // 这里的 delegate 即 MyLayoutBinder 对象，假设下面的方法都是你自己实现的
    delegate.get<ImageView>(R.id.icon_view).setImageResource(entity.iconRes)
    delegate.get<TextView>(R.id.text_view).text = entity.name
}
```

::: warning

当你设置了头部或末位 `View` 时，在使用 `RecyclerView.Adapter` 的 `notifyItemInserted`、`notifyItemRemoved`、`notifyItemChanged`、`notifyItemMoved` 等方法时，下标的位置将会出现问题，因为默认情况下 `onBindItemView` 计算出的 `position` 将不包含头部与末位布局，以及 `RecyclerView.scrollToPosition`、`RecyclerView.smoothScrollToPosition` 等方法也会受到影响。

由于这些方法在 `RecyclerView.Adapter` 中均为 `final`，无法重写它们，在这种情况下，`BetterAndroid` 为你提供了一个解决方案，在使用 `RecyclerView.Adapter` 时，你可以调用 `wrapper` 方法来获取包装实例，它将会为你自动处理这些问题。

> 示例如下

```kotlin
// 假设你已将使用 RecyclerAdapterBuilder 创建的 adapter 绑定到 RecyclerView
val recyclerView: RecyclerView
// 获取包装实例，如果目标适配器不是 RecyclerAdapterBuilder 创建的，将会返回 null
val wrapper = recyclerView.adapter?.wrapper
// 正常使用 RecyclerView.Adapter 的通知更新方法
wrapper?.notifyItemInserted(0)
wrapper?.notifyItemRemoved(0)
// 头部或末位布局需要单独使用以下方法更新
wrapper?.notifyHeaderItemChanged()
wrapper?.notifyFooterItemChanged()
// 更进一步，你可以手动使用以下方式判断是否存在头部、末位布局
val hasHeaderView = wrapper?.hasHeaderView == true
val hasFooterView = wrapper?.hasFooterView == true
```

回到我们前面说到的问题，`RecyclerView.scrollToPosition`、`RecyclerView.smoothScrollToPosition` 等方法也会受到影响，这种情况你可以使用 `com.highcapable.betterandroid.ui.component.adapter.recycler.layoutmanager` 包名下提供的 `LinearLayoutManager`、`GridLayoutManager` 以及 `RecyclerLayoutManager` 来解决。

这些封装后的增强型布局管理器将会通过默认的 `RecyclerCosmetic` 自动集成 (参考下方的 [Recycler 装饰器](#recycler-装饰器))，你无需任何手动操作，当你需要手动创建 `RecyclerView.LayoutManager` 时，我们均建议你继承于这个包名下提供的实例来完成。

当你使用了 `BetterAndroid` 提供的 `RecyclerView.LayoutManager` 时，由于头部或末位布局会自动处理 `position`，因此在使用 `RecyclerView.scrollToPosition`、`RecyclerView.smoothScrollToPosition` 滚动到顶部、底部时，你需要像这样 `scrollToPosition(-1)` (顶部) 或 `scrollToPosition(lastIndex + 1)` (底部)。

所以我们始终建议你在有滚动到顶部、底部需求的情况下，使用 `scrollToFirstPosition`、`scrollToLastPosition`、`smoothScrollToFirstPosition`、`smoothScrollToLastPosition` 方法取代，它们会自动处理这类问题 (无论你是否使用了 `BetterAndroid` 提供的 `RecyclerView.LayoutManager`)。

:::

### Fragment 适配器

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

### Recycler 装饰器

如果你希望手动创建一个 `RecyclerView.Adapter` 并绑定到 `RecyclerView`、`ViewPager2` 上，请参考以下示例。

> 示例如下

```kotlin
// 假设这就是你当前的 Context
val context: Context
// 手动创建一个 RecyclerView.Adapter
val adapter = RecyclerAdapter<CustomBean>(context) {
    // 内容与上述相同
}
// 手动创建一个装饰器
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

你可以在方法参数中指定一个 `RecyclerCosmetic`，默认为线性纵向列表装饰器。

> 示例如下

```kotlin
// 创建一个线性纵向列表，行间距为 10dp
val lvCosmetic = RecyclerCosmetic.fromLinearVertical(context, 10.toPx(context))
// 创建一个九宫格纵向列表，列间距为 10dp，行间距为 10dp
val gvCosmetic = RecyclerCosmetic.fromGridVertical(context, 10.toPx(context), 10.toPx(context))
// 以 lvCosmetic 举例
// 使用 bindAdapter 绑定到 recyclerView
recyclerView.bindAdapter<MyEntity>(lvCosmetic) {
    // ...
}
// 或者，手动绑定
val adapter = RecyclerAdapter<MyEntity>(context) {
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

### 适配器扩展

::: tip 本节内容

[RecyclerAdapter → notifyAllItemsInserted](kdoc://ui-component-adapter/ui-component-adapter/com.highcapable.betterandroid.ui.component.adapter.recycler.factory/notify-all-items-inserted)

[RecyclerAdapter → notifyAllItemsChanged](kdoc://ui-component-adapter/ui-component-adapter/com.highcapable.betterandroid.ui.component.adapter.recycler.factory/notify-all-items-changed)

[RecyclerAdapter → clearAndNotify](kdoc://ui-component-adapter/ui-component-adapter/com.highcapable.betterandroid.ui.component.adapter.recycler.factory/clear-and-notify)

[RecyclerAdapter → notifyDataSetChangedIgnore](kdoc://ui-component-adapter/ui-component-adapter/com.highcapable.betterandroid.ui.component.adapter.recycler.factory/notify-data-set-changed-ignore)

适用于 `RecyclerView` 适配器的扩展。

:::

本节所介绍的适配器扩展基本上都属于 `RecyclerView.Adapter` 的扩展。

通常情况下，我们需要使用 `notifyItemInserted`、`notifyItemChanged` 等方法来通知适配器数据发生了变化。

当我们一次性向数据集中添加数据后，通常需要使用 `notifyItemRangeInserted` 来通知适配器数据发生了变化。

> 示例如下

```kotlin
// 假设这就是你的 RecyclerView.Adapter 对象
val adapter: RecyclerView.Adapter<*>
// 假设这就是你的数据集，在绑定时为空
val dataSet: MutableList<MyEntity>
// 添加一些数据到数据集中
dataSet.addAll(...)
// 通知适配器数据发生了变化
adapter.notifyItemRangeInserted(0, dataSet.size)
```

当数据确定从 0 开始添加的时候，`BetterAndroid` 为你提供了一个更加简单的方式来完成，现在你可以使用以下方式通知适配器数据发生了变化。

> 示例如下

```kotlin
// 假设这就是你的 RecyclerView.Adapter 对象
val adapter: RecyclerView.Adapter<*>
// 假设这就是你的数据集，在绑定时为空
val dataSet: MutableList<MyEntity>
// 添加一些数据到数据集中
dataSet.addAll(...)
// 通知适配器数据发生了变化
adapter.notifyAllItemsInserted()
```

以上方法将默认使用 `adapter.itemCount` 来获取数据集的大小，无需再手动指定需要更新的范围。

此时请确保你的适配器返回了正确的 `itemCount`，否则请手动传入 `dataSet`。

> 示例如下

```kotlin
// 通知适配器数据发生了变化
adapter.notifyAllItemsInserted(dataSet)
```

同样地，当数据确定全部发生了变化的时候 (例如在一个多选状态列表中，更新选中与非选中的复选框状态)，你可以使用以下方式通知适配器数据发生了变化。

> 示例如下

```kotlin
// 假设这就是你的 RecyclerView.Adapter 对象
val adapter: RecyclerView.Adapter<*>
// 假设这就是你的数据集
val dataSet: MutableList<MyEntity>
// 模拟操作数据集 (例如全选动作)
dataSet.forEach { it.isSelected = true }
// 通知适配器数据发生了变化
adapter.notifyAllItemsChanged()
```

同样地，请确保你的适配器返回了正确的 `itemCount`，否则请手动传入 `dataSet`。

> 示例如下

```kotlin
// 通知适配器数据发生了变化
adapter.notifyAllItemsChanged(dataSet)
```

当我们需要清空数据集并通知适配器数据发生了变化时，通常需要使用 `notifyItemRangeRemoved` 来通知适配器数据发生了变化。

> 示例如下

```kotlin
// 假设这就是你的 RecyclerView.Adapter 对象
val adapter: RecyclerView.Adapter<*>
// 假设这就是你的数据集
val dataSet: MutableList<MyEntity>
// 保存当前数据的大小
val count = dataSet.size
// 清空数据集
dataSet.clear()
// 通知适配器数据发生了变化
adapter.notifyItemRangeRemoved(0, count)
```

这一过程依然繁琐，`BetterAndroid` 为此提供了一个更加简单的方式，现在你可以使用以下方式清空数据集并通知适配器数据发生了变化，此方法将自动计算数据集的大小。

> 示例如下

```kotlin
// 假设这就是你的 RecyclerView.Adapter 对象
val adapter: RecyclerView.Adapter<*>
// 假设这就是你的数据集
val dataSet: MutableList<MyEntity>
// 清空数据集并通知适配器数据发生了变化
adapter.clearAndNotify(dataSet)
```

::: tip

还有一些其它可被使用的扩展方法，`notifyDataSetChangedIgnore` 将忽略 Lint 在编码过程中给出的警告直接提供给你进行使用，其内部会直接调用 `notifyDataSetChanged`。

但是这个方法依然不推荐使用，因为它会导致整个列表的刷新，这在大数据量的列表中将会导致性能问题。

:::