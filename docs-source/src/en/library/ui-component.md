# ui-component

![Maven Central](https://img.shields.io/maven-central/v/com.highcapable.betterandroid/ui-component?logo=apachemaven&logoColor=orange)
<span style="margin-left: 5px"/>
![Maven metadata URL](https://img.shields.io/maven-metadata/v?metadataUrl=https%3A%2F%2Fraw.githubusercontent.com%2FHighCapable%2Fmaven-repository%2Frefs%2Fheads%2Fmain%2Frepository%2Freleases%2Fcom%2Fhighcapable%2Fbetterandroid%2Fui-component%2Fmaven-metadata.xml&logo=apachemaven&logoColor=orange&label=highcapable-maven-releases)
<span style="margin-left: 5px"/>
![Android Min SDK](https://img.shields.io/badge/Min%20SDK-21-orange?logo=android)

This is a dependency for UI (user interface) related components.

## Configure Dependency

You can add this module to your project using the following method.

### SweetDependency (Recommended)

Add dependency in your project's `SweetDependency` configuration file.

```yaml
libraries:
  com.highcapable.betterandroid:
    ui-component:
      version: +
```

Configure dependency in your project `build.gradle.kts`.

```kotlin
implementation(com.highcapable.betterandroid.ui.component)
```

### Traditional Method

Configure dependency in your project `build.gradle.kts`.

```kotlin
implementation("com.highcapable.betterandroid:ui-component:<version>")
```

Please change `<version>` to the version displayed at the top of this document.

## Function Introduction

You can view the KDoc [click here](kdoc://ui-component).

### Activity

::: tip Contents of This Section

[AppBindingActivity](kdoc://ui-component/ui-component/com.highcapable.betterandroid.ui.component.activity/-app-binding-activity)

`Activity` with view binding (inherited from `AppCompatActivtiy`).

[AppViewsActivity](kdoc://ui-component/ui-component/com.highcapable.betterandroid.ui.component.activity/-app-views-activity)

Base view component `Activity` (inherited from `AppCompatActivtiy`).

[AppComponentActivity](kdoc://ui-component/ui-component/com.highcapable.betterandroid.ui.component.activity/-app-component-activity)

Basic component `Activity` (inherited from `ComponentActivtiy`).

Available for Jetpack Compose projects.

:::

::: tip

The preset components below all implement [IBackPressedController](kdoc://ui-component/ui-component/com.highcapable.betterandroid.ui.component.proxy/-i-back-pressed-controller),
[ISystemBarsController](kdoc://ui-component/ui-component/com.highcapable.betterandroid.ui.component.proxy/-i-system-bars-controller) interface.

You can find detailed usage methods in [System Event](#system-event) and [System Bars (Status Bars, Navigation Bars, etc)](#system-bars-status-bars-navigation-bars-etc) below.

:::

When using `ViewBinding`, you can use `AppBindingActivity` to quickly create an `Activity` with view binding.

In `AppBindingActivity`, you can directly use the `binding` property to obtain the view binding object without manually calling the `setContentView` method.

> The following example

```kotlin
class MainActivity : AppBindingActivity<ActivityMainBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.mainText.text = "Hello World!"
    }
}
```

::: tip

If you need to perform some custom operations before the view is loaded, in general, you might do this.

> The following example

```kotlin
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        doSomething()
        setContentView(R.layout.activity_main)
        findViewById<TextView>(R.id.main_text).text = "Hello World!"
    }

    private fun doSomething() {
        // Your code here.
    }
}
```

When using `AppBindingActivity`, you need to do this.

> The following example

```kotlin
class MainActivity : AppBindingActivity<ActivityMainBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.mainText.text = "Hello World!"
    }

    override fun onPrepareContentView(savedInstanceState: Bundle?): LayoutInflater {
        doSomething()
        // You can return the processed LayoutInflater instance,
        // which will be used to initialize the layout.
        return super.onPrepareContentView(savedInstanceState)
    }

    private fun doSomething() {
        // Your code here.
    }
}
```

:::

You can also use `AppViewsActivity` to create a basic `Activity` and use the `findViewById` method to get the `View`.

> The following example

```kotlin
class MainActivity : AppViewsActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<TextView>(R.id.main_text).text = "Hello World!"
    }
}
```

If your project is a Jetpack Compose project, you can use `AppComponentActivity` to create a basic `Activity`.

> The following example

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

For related extensions of Jetpack Compose, you can refer to [compose-extension](../library/compose-extension.md), [compose-multiplatform](../library/compose-multiplatform.md).

`BetterAndroid` also provides related extensions for `Activity`, you can refer to [ui-extension → Activity Extension](../library/ui-extension.md#activity-extension).

:::

### Fragment

::: tip Contents of This Section

[AppBindingFragment](kdoc://ui-component/ui-component/com.highcapable.betterandroid.ui.component.fragment/-app-binding-fragment)

`Fragment` with view binding.

[AppViewsFragment](kdoc://ui-component/ui-component/com.highcapable.betterandroid.ui.component.fragment/-app-views-fragment)

Base view component `Fragment`.

:::

::: tip

The preset components below all implement [IBackPressedController](kdoc://ui-component/ui-component/com.highcapable.betterandroid.ui.component.proxy/-i-back-pressed-controller),
[ISystemBarsController](kdoc://ui-component/ui-component/com.highcapable.betterandroid.ui.component.proxy/-i-system-bars-controller) interface.

You can find detailed usage methods in [System Event](#system-event) and [System Bars (Status Bars, Navigation Bars, etc)](#system-bars-status-bars-navigation-bars-etc) below.

:::

When using `ViewBinding`, you can use `AppBindingFragment` to quickly create a `Fragment` with view binding.

In `AppBindingFragment`, you can directly use the `binding` property to obtain the view binding object without manually overriding the `onCreateView` method.

You don’t need to consider the impact of `Fragment`’s lifecycle on `binding`, `BetterAndroid` has already handled these issues for you.

> The following example

```kotlin
class MainFragment : AppBindingFragment<FragmentMainBinding>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mainText.text = "Hello World!"
    }
}
```

You can also use `AppViewsFragment` to create a basic `Fragment`.

Similarly, you don't need to overriding the `onCreateView` method, just fill in the layout resource ID that needs to be bound directly into the constructor.

> The following example

```kotlin
class MainFragment : AppViewsFragment(R.layout.fragment_main) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<TextView>(R.id.main_text).text = "Hello World!"
    }
}
```

::: tip

`BetterAndroid` also provides related extensions for `Fragment`, you can refer to [ui-extension → Fragment Extension](../library/ui-extension.md#fragment-extension).

:::

### Adapter

::: tip Contents of This Section

[CommonAdapterBuilder](kdoc://ui-component/ui-component/com.highcapable.betterandroid.ui.component.adapter/-common-adapter-builder)

Can be used to build a `BaseAdapter`.

[PagerAdapterBuilder](kdoc://ui-component/ui-component/com.highcapable.betterandroid.ui.component.adapter/-pager-adapter-builder)

Can be used to build a `PagerAdapter`.

[RecyclerAdapterBuilder](kdoc://ui-component/ui-component/com.highcapable.betterandroid.ui.component.adapter/-recycler-adapter-builder)

Can be used to build a `RecyclerView.Adapter`.

[PagerMediator](kdoc://ui-component/ui-component/com.highcapable.betterandroid.ui.component.adapter.mediator/-pager-mediator)

Pager mediator for `ViewPager`.

[RecyclerCosmetic](kdoc://ui-component/ui-component/com.highcapable.betterandroid.ui.component.adapter.recycler.cosmetic/-recycler-cosmetic)

Cosmetic of `LayoutManager` and `ItemDecoration` of `RecyclerView`.

[LinearHorizontalItemDecoration](kdoc://ui-component/ui-component/com.highcapable.betterandroid.ui.component.adapter.recycler.decoration/-linear-horizontal-item-decoration)

Linear horizontal list decoration for `RecyclerView`.

[LinearVerticalItemDecoration](kdoc://ui-component/ui-component/com.highcapable.betterandroid.ui.component.adapter.recycler.decoration/-linear-vertical-item-decoration)

Linear vertical list decoration for `RecyclerView`.

[GridVerticalItemDecoration](kdoc://ui-component/ui-component/com.highcapable.betterandroid.ui.component.adapter.recycler.decoration/-grid-vertical-item-decoration)

Grid vertical list decoration for `RecyclerView`.

[LinearLayoutManager](kdoc://ui-component/ui-component/com.highcapable.betterandroid.ui.component.adapter.recycler.layoutmanager/-linear-layout-manager)

Enhanced linear layout manager for `RecyclerView`.

[GridLayoutManager](kdoc://ui-component/ui-component/com.highcapable.betterandroid.ui.component.adapter.recycler.layoutmanager/-grid-layout-manager)

Enhanced grid layout manager for `RecyclerView`.

[RecyclerLayoutManager](kdoc://ui-component/ui-component/com.highcapable.betterandroid.ui.component.adapter.recycler.layoutmanager.base/-recycler-layout-manager)

Enhanced layout manager base class for `RecyclerView`.

[RecyclerAdapter](kdoc://ui-component/ui-component/com.highcapable.betterandroid.ui.component.adapter.recycler.factory)

Extension methods for the adapter build of `RecyclerView`.

[CommonAdapter](kdoc://ui-component/ui-component/com.highcapable.betterandroid.ui.component.adapter.factory)

Extension methods for the adapter build above.

[AdapterPosition](kdoc://ui-component/ui-component/com.highcapable.betterandroid.ui.component.adapter.entity/-adapter-position)

Adapter position entity.

:::

From the beginning of `ListView` to the emergence of `RecyclerView`, adapters in Android have always been one of the most troublesome problems for developers.

To address this problem, `BetterAndroid` encapsulates the adapters of the following components:

`ListView`, `AutoCompleteTextView`, `ListPopupWindow`, `RecyclerView`, `ViewPager`, `ViewPager2`

In Kotlin you can create a data adapter more easily.

Now, all you need is a data array and a custom adapter layout to create an adapter very quickly and bind to these components.

Create a `BaseAdapter` for `ListView`, `AutoCompleteTextView`, `ListPopupWindow`.

> The following example

```kotlin
// Assume this is your entity class.
data class CustomBean(
    var iconRes: Int,
    var name: String
)
// Assume this is the dataset you need to bind to.
val listData = ArrayList<CustomBean>()
// Create and bind to a custom BaseAdapter.
val adapter = listView.bindAdapter<CustomBean> {
    // Bind dataset.
    onBindData { listData }
    // Bind custom adapter layout adapter_custom.xml.
    onBindViews<AdapterCustomBinding> { binding, bean, position ->
        binding.iconView.setImageResource(bean.iconRes)
        binding.textView.text = bean.name
    }
    // Set the click event for each item.
    onItemViewsClick { itemView, bean, position ->
        // Your code here.
    }
}
```

If you want to manually create an adapter and bind it to the above components, please refer to the following example.

> The following example

```kotlin
// Assume this is your current Context.
val context: Context
// Create a BaseAdapter manually.
val adapter = CommonAdapter<CustomBean>(context) {
    // The content is the same as above.
}
// Then bind to listView.
listView.adapter = adapter
```

Create a `PagerAdapter` for `ViewPager`.

> The following example

```kotlin
// Assume this is your entity class.
data class CustomBean(
    var iconRes: Int,
    var name: String
)
// Assume this is the dataset you need to bind to.
val listData = ArrayList<CustomBean>()
// Create and bind to a custom PagerAdapter.
val adapter = viewPager.bindAdapter<CustomBean> {
    // Bind dataset.
    onBindData { listData }
    // Bind custom adapter layout adapter_custom.xml.
    onBindViews<AdapterCustomBinding> { binding, bean, position ->
        binding.iconView.setImageResource(bean.iconRes)
        binding.textView.text = bean.name
    }
}
```

You can also use `dataSetCount` directly without specifying a dataset, just repeatedly creating multiple pages.

> The following example

```kotlin
// Create and bind to a custom PagerAdapter.
val adapter = viewPager.bindAdapter {
    // Manually create two identical pages.
    dataSetCount = 2
    // Bind custom adapter layout adapter_custom.xml.
    onBindViews<AdapterCustomBinding> { binding, bean, position ->
        // You can judge the position of the current page by position.
        binding.iconView.setImageResource(bean.iconRes)
        binding.textView.text = bean.name
    }
}
```

You can also reuse the `onBindViews` method to create multiple different pages.

The order of the pages is determined by the order in which they were created.

> The following example

```kotlin
// Create and bind to a custom PagerAdapter.
val adapter = viewPager.bindAdapter {
    // Bind custom adapter layout adapter_custom_1.xml.
    onBindViews<AdapterCustom1Binding> { binding, bean, position ->
        binding.iconView.setImageResource(bean.iconRes)
        binding.textView.text = bean.name
    }
    // Bind custom adapter layout adapter_custom_2.xml.
    onBindViews<AdapterCustom2Binding> { binding, bean, position ->
        binding.iconView.setImageResource(bean.iconRes)
        binding.textView.text = bean.name
    }
}
```

The number of pages created is the number of times the `onBindViews` method is reused.

::: danger

If you reuse the `onBindViews` method to create multiple different pages, you can no longer specify the `dataSetCount` or bind the dataset.

:::

If you need to handle `getPageTitle` and `getPageWidth` in `PagerAdapter`, you can use `PagerMediator` to complete it.

> The following example

```kotlin
// Create and bind to custom PagerAdapter.
val adapter = viewPager.bindAdapter {
    // Bind the PagerMediator for each item.
    onBindMediators {
        // Handle the title of the page.
        title = when (position) {
            0 -> "Home"
            else -> "Additional"
        }
        // Handle the width of the page (scale).
        width = when (position) {
            0 -> 1f
            else -> 0.5f
        }
    }
    // ...
}
```

If you want to manually create a `PagerAdapter` and bind it to `ViewPager`, please refer to the following example.

> The following example

```kotlin
// Assume this is your current Context.
val context: Context
// Create a PagerAdapter manually.
val adapter = PagerAdapter<CustomBean>(context) {
    // The content is the same as above.
}
// Then bind to viewPager.
viewPager.adapter = adapter
```

Create a regular `RecyclerView.Adapter` for `RecyclerView`, `ViewPager2`.

> The following example

```kotlin
// Assume this is your entity class.
data class CustomBean(
    var iconRes: Int,
    var name: String
)
// Assume this is the dataset you need to bind to.
val listData = ArrayList<CustomBean>()
// Create and bind to custom RecyclerView.Adapter.
val adapter = recyclerView.bindAdapter<CustomBean> {
    // Bind dataset
    onBindData { listData }
    // Bind custom adapter layout adapter_custom.xml.
    onBindViews<AdapterCustomBinding> { binding, bean, position ->
        binding.iconView.setImageResource(bean.iconRes)
        binding.textView.text = bean.name
    }
    // Set the click event for each item.
    onItemViewsClick { itemView, viewType, bean, position ->
        // Your code here.
    }
}
```

Create a `RecyclerView.Adapter` of multiple `View` types for `RecyclerView`, `ViewPager2`.

> The following example

```kotlin
// Assume this is your entity class.
data class CustomBean(
    var iconRes: Int,
    var name: String,
    var title: String,
    var dataType: Int
)
// Assume this is the dataset you need to bind to.
val listData = ArrayList<CustomBean>()
// Create and bind to custom RecyclerView.Adapter.
val adapter = recyclerView.bindAdapter<CustomBean> {
    // Bind dataset.
    onBindData { listData }
    // Bind the View type.
    onBindViewsType { bean, position -> bean.dataType }
    // Bind custom adapter layout adapter_custom_1.xml.
    onBindViews<AdapterCustom1Binding>(viewType = 1) { binding, bean, position ->
        binding.iconView.setImageResource(bean.iconRes)
        binding.textView.text = bean.name
    }
    // Bind custom adapter layout adapter_custom_2.xml.
    onBindViews<AdapterCustom2Binding>(viewType = 2) { binding, bean, position ->
        binding.iconView.setImageResource(bean.iconRes)
        binding.titleView.text = bean.title
    }
    // Set the click event for each item.
    onItemViewsClick { itemView, viewType, bean, position ->
        // Your code here.
    }
}
```

::: tip

In `RecyclerView.Adapter`, the `position` type in `onBindViews` is `AdapterPosition` instead of `Int`, which is a new feature introduced in version `1.0.6`.

Since `RecyclerView.Adapter` can perform partial updates, the `onBindViews` of existing items will not be recalled after dynamically adding or deleting items.

In this case, you need a dynamic index instance like `AdapterPosition` to get the correct index of the current item through `position.value`.

`AdapterPosition` overloads operators, so it can directly participate in comparisons and basic arithmetic operations without using `position.value`.

:::

Create the header `View` and the footer `View` for `RecyclerView`.

You can use the `onBindHeaderView` and `onBindFooterView` methods to add a header `View` and a footer `View`.

These are two special item layouts, they will not be calculated into the bound data and are passed through the subscript `position` of method callbacks such as `onBindViews` is not affected.

::: warning

You can only add one header `View` and one footer `View` at the same time.

:::

> The following example

```kotlin
// Assume this is your entity class.
data class CustomBean(
    var iconRes: Int,
    var name: String
)
// Assume this is the data set you need to bind to.
val listData = ArrayList<CustomBean>()
// Create and bind to custom RecyclerView.Adapter.
val adapter = recyclerView.bindAdapter<CustomBean> {
    // Bind data set.
    onBindData { listData }
    // Bind the header View.
    onBindHeaderView<AdapterHeaderBinding> { binding ->
        binding.someText.text = "Header"
    }
    // Bind the footer View.
    onBindFooterView<AdapterFooterBinding> { binding ->
        binding.someText.text = "Footer"
    }
    // Bind custom adapter layout adapter_custom.xml.
    onBindViews<AdapterCustomBinding> { binding, bean, position ->
        binding.iconView.setImageResource(bean.iconRes)
        binding.textView.text = bean.name
    }
}
```

If you want to manually create a `RecyclerView.Adapter` and bind it to `RecyclerView`, `ViewPager2`, please refer to the following example.

> The following example

```kotlin
// Assume this is your current Context.
val context: Context
// Create a RecyclerView.Adapter manually.
val adapter = RecyclerAdapter<CustomBean>(context) {
    // The content is the same as above.
}
// Create a cosmetic manually.
val cosmetic = RecyclerCosmetic.fromLinearVertical(context)
// Then bind to recyclerView.
recyclerView.layoutManager = cosmetic.layoutManager
recyclerView.addItemDecoration(cosmetic.itemDecoration) 
recyclerView.adapter = adapter
// You don't need to set layoutManager when binding to viewPager2
viewPager2.addItemDecoration(cosmetic.itemDecoration) 
viewPager2.adapter = adapter
```

`BetterAndroid` presets several common adapter layout types for `RecyclerView` for developers to use.

You can specify a `RecyclerCosmetic` in the method parameter, which defaults to a linear vertical cosmetic.

> The following example

```kotlin
// Create a linear vertical cosmetic with 10dp row spacing.
val lvCosmetic = RecyclerCosmetic.fromLinearVertical(context, 10.toPx(context))
// Create a nine-square grid vertical cosmetic with a column spacing of 10dp and a row spacing of 10dp.
val gvCosmetic = RecyclerCosmetic.fromGridVertical(context, 10.toPx(context), 10.toPx(context))
// Take lvCosmetic as an example.
// Use bindAdapter to bind to recyclerView.
recyclerView.bindAdapter<CustomBean>(lvCosmetic) {
    // ...
}
// Or, bind manually.
val adapter = RecyclerAdapter<CustomBean>(context) {
    // ...
}
recyclerView.layoutManager = lvCosmetic.layoutManager
recyclerView.addItemDecoration(lvCosmetic.itemDecoration)
recyclerView.adapter = adapter
```

::: tip

If you only need a `ItemDecoration`, you can create it through the preset
`LinearHorizontalItemDecoration`, `LinearVerticalItemDecoration`, `GridVerticalItemDecoration`.

Here's a simple example.

> The following example

```kotlin
// Create a linear vertical item decoration with a line spacing of 10dp.
val itemDecoration = LinearVerticalItemDecoration(rowSpacing = 10.toPx(context))
// Set to recyclerView.
recyclerView.addItemDecoration(itemDecoration)
// If you need to update the parameters of item decoration, you can use the update method.
itemDecoration.update(rowSpacing = 15.toPx(context))
// Then notify recyclerView to update.
recyclerView.invalidateItemDecorations()
```

:::

::: warning

When you set a header or footer `View`, using methods such as `notifyItemInserted`, `notifyItemRemoved`, `notifyItemChanged`,
`notifyItemMoved` in `RecyclerView.Adapter` will cause issues with the position index.

This is because, by default, the `position` calculated in `onBindViews` will not include the header and footer layouts.
Methods like `RecyclerView.scrollToPosition` and `RecyclerView.smoothScrollToPosition` will also be affected.

Since these methods in `RecyclerView.Adapter` are `final` and cannot be overridden, `BetterAndroid` provides a solution.
When using `RecyclerView.Adapter`, you can call the `wrapper` method to get a wrapper instance, which will automatically handle these issues for you.

> The following example

```kotlin
// Assume you have bound the adapter created using RecyclerAdapterBuilder to RecyclerView.
val recyclerView: RecyclerView
// Get the wrapper instance, if the target adapter is not created
// by RecyclerAdapterBuilder, it will return null.
val wrapper = recyclerView.adapter?.wrapper
// Use the notification update methods of RecyclerView.Adapter normally.
wrapper?.notifyItemInserted(0)
wrapper?.notifyItemRemoved(0)
// Use the following methods to update the header or footer item separately.
wrapper?.notifyHeaderItemChanged()
wrapper?.notifyFooterItemChanged()
```

Returning to the issue we mentioned earlier, methods like `RecyclerView.scrollToPosition` and `RecyclerView.smoothScrollToPosition` will also be affected.

In this case, you can use the `LinearLayoutManager`, `GridLayoutManager`, and `RecyclerLayoutManager` provided under the
package name `com.highcapable.betterandroid.ui.component.adapter.recycler.layoutmanager` to solve the problem.

These enhanced layout managers will be automatically integrated through the default `RecyclerCosmetic`, and you do not need any manual operations.
When you need to manually create a `RecyclerView.LayoutManager`, we recommend inheriting from the instances provided under this package name.

:::

In addition to using `ViewBinding` in the above example, you can also use a traditional `View`
or a layout resource ID to bind it to the adapter layout.

> The following example

```kotlin
// Bind custom adapter layout adapter_custom.xml.
onBindViews(R.layout.adapter_custom) { view, bean, position ->
    view.findViewById<ImageView>(R.id.icon_view).setImageResource(bean.iconRes)
    view.findViewById<TextView>(R.id.text_view).text = bean.name
}
// Assume this is your custom view.
val adapterView: View
// Bind custom adapter layout with adapterView.
onBindViews(adapterView) { view, bean, position ->
    // Your code here.
}
```

Create a `FragmentPagerAdapter` for `ViewPager`.

::: warning

This usage is officially deprecated, start using `ViewPager2` if possible.

:::

> The following example

```kotlin
// Assume this is your current FragmentActivity.
val activity: FragmentActivity
// Create and bind to a custom FragmentPagerAdapter.
val adapter = viewPager.bindFragments(activity) {
    // Set the number of fragments to display.
    pageCount = 5
    // Bind each fragment.
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

You can also customize the `behavior` parameter in the same way as the constructor of `FragmentPagerAdapter`.

If you are using `Fragment`, you can fill in the current `Fragment` instance in the first parameter of `bindFragments` and it will be
automatically bound to `getChildFragmentManager()`.

If you want to manually create a `FragmentPagerAdapter` and bind it to `ViewPager`, please refer to the following example.

> The following example

```kotlin
// Assume this is your current FragmentActivity.
val activity: FragmentActivity
// Create a FragmentPagerAdapter manually.
val adapter = FragmentPagerAdapter(activity) {
    // The content is the same as above.
}
// Then bind to viewPager.
viewPager.adapter = adapter
```

Create a `FragmentStateAdapter` for `ViewPager2`.

> The following example

```kotlin
// Assume this is your current FragmentActivity.
val activity: FragmentActivity
// Create and bind to a custom FragmentPagerAdapter.
val adapter = viewPager2.bindFragments(activity) {
    // Set the number of fragments to display.
    pageCount = 5
    // Bind each fragment.
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

If you are using `Fragment`, you can fill in the current `Fragment` instance in the first parameter of `bindFragments` and it will be
automatically bound to `getChildFragmentManager()`.

If you want to manually create a `FragmentStateAdapter` and bind it to `ViewPager2`, please refer to the following example.

> The following example

```kotlin
// Assume this is your current FragmentActivity.
val activity: FragmentActivity
// Create a FragmentStateAdapter manually.
val adapter = FragmentStateAdapter(activity) {
    // The content is the same as above.
}
// Then bind to viewPager2.
viewPager2.adapter = adapter
```

### System Event

::: tip Contents of This Section

[BackPressedController](kdoc://ui-component/ui-component/com.highcapable.betterandroid.ui.component.backpress/-back-pressed-controller)

Back pressed event controller.

[OnBackPressedCallback](kdoc://ui-component/ui-component/com.highcapable.betterandroid.ui.component.backpress.callback/-on-back-pressed-callback)

Simple back pressed event callback.

[IBackPressedController](kdoc://ui-component/ui-component/com.highcapable.betterandroid.ui.component.proxy/-i-back-pressed-controller)

Back pressed event controller interface.

:::

An `OnBackPressedDispatcher` has been provided for developers in the `androidx` dependency `androidx.activity:activity`.

However, out of dissatisfaction with the official rash deprecated overwriting of the `onBackPressed` method, `BetterAndroid` encapsulated the `OnBackPressedDispatcher` related functions.

It supports the back pressed event callback function that is more suitable for Kotlin writing, and also adds the function of ignoring all callback events and directly releasing
the back pressed event, making it more flexible and easy to use.

`AppBindingActivity`, `AppViewsActivity`, `AppComponentActivity`, `AppBindingFragment`, `AppViewsFragment`
have implemented the `IBackPressedController` interface by default, you can directly use `backPressed` to obtain `BackPressedController`.

But you can still manually create a `BackPressedController` in `Activity`.

> The following example

```kotlin
class YourActivity : AppCompatActivity() {

    // Create a lazy object.
    val backPressed by lazy { BackPressedController.from(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Call backPressed here to implement related functions.
        backPressed
    }

    override fun onDestroy() {
        super.onDestroy()
        // Destroy backPressed, this will remove all callbacks.
        // Optional, prevent memory leaks.
        backPressed.destroy()
    }
}
```

The following is the basic usage of `BackPressedController`.

> The following example

```kotlin
// Add a back pressed callback
val callback = backPressed.addCallback {
    // Ignore the current callback within the callback and trigger the back operation.
    // For example, you can pop up a dialog here to ask the user whether to exit
    // and select "Yes" at this time.
    // The object passed in needs to be the backPressed that created this callback.
    trigger(backPressed)
    // Or remove itself at the same time after triggering.
    trigger(backPressed, removed = true)
    // Remove directly. (Not recommended, you should use backPressed.removeCallback)
    remove()
}
// You can also create a callback manually.
// Note: Please make sure to import com.highcapable.betterandroid.ui.component.backpress.callback
//       OnBackPressedCallback under the package name, NOT androidx.activity.OnBackPressedCallback
val callback = OnBackPressedCallback {
    // Your code here.
}
// Then add to backPressed.
backPressed.addCallback(callback)
// Remove a known callback.
backPressed.removeCallback(callback)
// Trigger the back operation of the system.
backPressed.trigger()
// You can set ignored to true to ignore all added callbacks and back directly.
backPressed.trigger(ignored = true)
// Determine whether there is currently an enabled callback
val hasEnabledCallbacks = backPressed.hasEnabledCallbacks
// Destroy, this will remove all callbacks.
backPressed.destroy()
```

::: warning

After using `BackPressedController`, the current `OnBackPressedDispatcher` has been automatically taken over by it.

You should not continue to use `onBackPressedDispatcher.addCallback(...)` as this will expose unknown (wild)
callbacks and prevent them from being cleanly removed.

:::

### Notification

::: tip Contents of This Section

[NotificationBuilder](kdoc://ui-component/ui-component/com.highcapable.betterandroid.ui.component.notification/-notification-builder)

System notification builder.

[NotificationChannelBuilder](kdoc://ui-component/ui-component/com.highcapable.betterandroid.ui.component.notification/-notification-channel-builder)

System notification channel builder.

[NotificationChannelGroupBuilder](kdoc://ui-component/ui-component/com.highcapable.betterandroid.ui.component.notification/-notification-channel-group-builder)

System notification channel group builder.

[NotificationImportance](kdoc://ui-component/ui-component/com.highcapable.betterandroid.ui.component.notification.type/-notification-importance)

System notification importance.

[NotificationPoster](kdoc://ui-component/ui-component/com.highcapable.betterandroid.ui.component.notification/-notification-poster)

System notification poster.

[Notification](kdoc://ui-component/ui-component/com.highcapable.betterandroid.ui.component.notification.factory)

Extension methods for the notification builds.

:::

It is not easy to create and send a notification in Android.

The biggest problem is that the creation of system notifications is complicated, management is confusing, and the API is difficult to be easily compatible with older versions.

Especially when developers see the two classes `NotificationCompat` and `NotificationChannelCompat`, they will feel at a loss to start.

So `BetterAndroid` comprehensively encapsulates the system notification-related APIs, basically covering all functions and calls that can be used in system notifications.

So you no longer need to consider compatibility issues with Android 8 and below systems like notification channels, `BetterAndroid` has already taken care of these issues for you.

In Kotlin you can create a system notification more easily.

> The following example

```kotlin
// Assume this is your current Context.
val context: Context
// Create the notification object that needs to be posted.
val notification = context.createNotification(
    // Create and set up notification channels.
    // A notification channel must exist in Android 8 and above systems.
    // In systems lower than Android 8, this feature will be automatically compatible.
    channel = NotificationChannel("my_channel_id") {
        // Set the notification channel name.
        // (this will be displayed in the system's notification settings)
        name = "My Channel"
        // Set notification channel description.
        // (this will be displayed in the system's notification settings)
        description = "My channel description."
        // The rest of the usage is consistent with NotificationChannelCompat.Builder.
    }
) {
    // Set the notification icon. (this will be displayed in the status bar and notification bar)
    // The notification icon must be a monochrome icon. (a vector image is recommended)
    smallIconResId = R.drawable.ic_my_notification
    // Set notification title.
    contentTitle = "My Notification"
    // Set notification content.
    contentText = "Hello World!"
    // The rest of the usage is consistent with NotificationCompat.Builder.
}
// Post notification using default notification ID.
notification.post()
// Post notification using custom notification ID.
val notifyId = 1
notification.post(notifyId)
// Cancel the current notification. (this will clear the notification from the system notification bar)
notification.cancel()
// Determine whether the current notification has been canceled.
val isCanceled = notification.isCanceled
```

::: warning

In Android 13 and above, you need to define and add runtime permission for notifications.

When this permission is not defined correctly, calling the `post` method will automatically ask you to add the permission to `AndroidManifest.xml`.

Please refer to [Notification runtime permission](https://developer.android.com/develop/ui/views/notifications/notification-permission).

:::

You can set importance for notifications in notification channels using the following methods.

> The following example

```kotlin
// Assume this is your current Context.
val context: Context
// Create the notification object that needs to be posted.
val notification = context.createNotification(
    // Create and set up notification channels.
    // In systems lower than Android 8, this feature will be automatically compatible.
    // Importance determines the importance of the notification,
    // which affects how the notification is displayed.
    // BetterAndroid sets the importance static variable in NotificationManager.
    // Encapsulated into NotificationImportance, you can set the
    // importance of notifications more conveniently.
    // Here we set NotificationImportance.HIGH (high importance),
    // this will display the notification as a banner in the system notification with a ring reminder.
    channel = NotificationChannel("my_channel_id", importance = NotificationImportance.HIGH) {
        name = "My Channel"
        description = "My channel description."
    }
) {
    smallIconResId = R.drawable.ic_my_notification
    contentTitle = "My Notification"
    contentText = "Hello World!"
}
// Post notification using default notification ID.
notification.post()
```

When encountering multiple groups of notifications, you can use the following methods to create a group of notification channels.

> The following example

```kotlin
// Assume this is your current Context.
val context: Context
// Create a notification channel group.
// In systems lower than Android 8, this feature will have no effect.
val channelGroup = NotificationChannelGroup("my_channel_group_id") {
    //Set the notification channel group name.
    // (this will be displayed in the system's notification settings)
    name = "My Channel Group"
    // Set notification channel group description.
    // (this will be displayed in the system's notification settings)
    description = "My channel group description."
}
// Create the first notification channel and specify the notification channel group.
val channel1 = NotificationChannel("my_channel_id_1", channelGroup) {
    name = "My Channel 1"
    description = "My channel description."
}
// Create the second notification channel and specify the notification channel group.
val channel2 = NotificationChannel("my_channel_id_2", channelGroup) {
    name = "My Channel 2"
    description = "My channel description."
}
// Use channel1 to create the first notification and post it.
context.createNotification(channel1) {
    smallIconResId = R.drawable.ic_my_notification
    contentTitle = "My Notification 1"
    contentText = "Hello World!"
}.post(1)
// Use channel2 to create the second notification and post it.
context.createNotification(channel2) {
    smallIconResId = R.drawable.ic_my_notification
    contentTitle = "My Notification 2"
    contentText = "Hello World!"
}.post(2)
```

The above will create a notification channel group and add two notification channels to it.

After the notification is posted, the system will automatically create a group classification for these two notification channels.

::: warning

The settings in the notification channel will only take effect when the notification channel is first created.

If the settings of the notification channel are modified by the user, these settings will not be overwritten again.

You cannot modify the settings of a notification channel that has already been created, but you can reassign it a new notification channel ID,
which will create a new notification channel.

Please refer to [Create and manage notification channels](https://developer.android.com/develop/ui/views/notifications/channels).

:::

In the above example, the notification object is managed automatically, if you want to manually create a notification object
without relying on the `context.createNotification` method, please refer to the following example.

> The following example

```kotlin
// Assume this is your current Context.
val context: Context
// Create the notification object that needs to be posted.
val notification = Notification(
    // Set Context.
    context = context,
    // Create and set up notification channels.
    channel = NotificationChannel("my_channel_id") {
        name = "My Channel"
        description = "My channel description."
    }
) {
    smallIconResId = R.drawable.ic_my_notification
    contentTitle = "My Notification"
    contentText = "Hello World!"
}
// Use the current notification as a post object.
val poster = notification.asPoster()
// Post notification using default notification ID.
poster.post()
// Post notification using custom notification ID.
val notifyId = 1
poster.post(notifyId)
// Cancel the current notification. (this will clear the notification from the system notification bar)
poster.cancel()
// Determine whether the current notification has been canceled.
val isCanceled = poster.isCanceled
```

::: tip

Objects created through `Notification`, `NotificationChannel`, `NotificationChannelGroup` are a wrapper for `NotificationCompat`, `NotificationChannelCompat`, and `NotificationChannelGroupCompat`.

You can use `instance` to get the actual object to perform some of your own operations.

You can also get the `NotificationManagerCompat` object through `Context.notificationManager` to perform some of your own operations.

:::

### Insets

::: tip Contents of This Section

[WindowInsetsWrapper](kdoc://ui-component/ui-component/com.highcapable.betterandroid.ui.component.insets/-window-insets-wrapper)

A wrapper for `WindowInsets`.

[WindowInsetsWrapper.Absolute](kdoc://ui-component/ui-component/com.highcapable.betterandroid.ui.component.insets/-window-insets-wrapper/-absolute)

An absolute insets for `WindowInsetsWrapper`.

[InsetsWrapper](kdoc://ui-component/ui-component/com.highcapable.betterandroid.ui.component.insets/-insets-wrapper)

A wrapper for `Insets`.

[Insets](kdoc://ui-component/ui-component/com.highcapable.betterandroid.ui.component.insets.factory)

Extension methods for `Insets`, `WindowInsets`.

:::

::: warning

Among the library of `1.0.3` and previous versions, `BetterAndroid` encapsulates insest, window insets and [System Bars (Status Bars, Navigation Bars, etc)](#system-bars-status-bars-navigation-bars-etc), this was once incorrect, and now insets and window insets have been decoupled into separate functions, as you can see now.

:::

Insets and window insets are a very important concept in Android.

Although this API has existed as early as Android 5.0, it was only officially recommended in Android 10. 
(Since Android 9, the system has added related APIs for cutout displays processing)

Insets is a special space, which represents the placeholder area "attached" around the view,
insets held by the system such as the part blocked by the cutout displays (notch screens), status bars, navigation bars,
and input method are called window insets.

What `BetterAndroid` mainly does is wrapped this set of APIs to make them easier to use.

Next, you can create a `WindowInsetsWrapper` from an existing `WindowInsets` object.

::: tip

`WindowInsetsWrapper` is designed with reference to the [Window Insets API](https://developer.android.com/jetpack/compose/layouts/insets) officially provided by Jetpack Compose.

You can better use this set at the native level API.

:::

For backward compatibility reasons, the object wrapped by `WindowInsetsWrapper` is `WindowInsetsCompat` and it is recommended to use it instead of `WindowInsets`.

`WindowInsetsWrapper` wrapped `WindowInsetsCompat.getInsets`, `WindowInsetsCompat.getInsetsIgnoringVisibility`, `WindowInsetsCompat.isVisible` and other methods,
you no longer need to write super long code such as `WindowInsetsCompat.getInsets(WindowInsetsCompat.Type.systemBars())` to get an insets.

> The following example

```kotlin
// Assume this is your WindowInsets.
val windowInsets: WindowInsetsCompat
// Create a WindowInsetsWrapper.
val insetsWrapper = windowInsets.createWrapper()
// You can also create it through the from method.
val insetsWrapper = WindowInsetsWrapper.from(windowInsets)
// Get the insets of the system bars.
val systemBars = insetsWrapper.systemBars
// Normally, the obtained insets will include its visibility,
// when invisible, the values of insets are all 0.
// You can ignore visibility by passing parameter ignoringVisibility.
val systemBars = insetsWrapper.systemBars(ignoreVisibility = true)
// After obtaining the insets, you can use isVisible to determine whether it is visible.
// Note: The value of insets is provided by the system, isVisible is just a state,
// regardless of whether its value is 0,
// you can use it to determine whether the current insets are visible.
val insetsIsVisible = systemBars.isVisible
```

`BetterAndroid` has made a compatibility process for the respective private solutions of manufacturers of
mainstream brands of cutout display devices below Android 9.

If you need to be compatible with older devices, you can pass in an optional `Window` object in the method parameter.

If your apps only needs to adapt to Android 9 and above devices, you can ignore this parameter.

> The following example

```kotlin
// Assume this is your activity.
val activity: Activity
// Under normal circumstances, you can get the window through the current activity.
val window = activity.window
// Create a WindowInsetsWrapper.
val insetsWrapper = windowInsets.createWrapper(window)
// You can also create it through the from method.
val insetsWrapper = WindowInsetsWrapper.from(windowInsets, window)
// Get the insets of the cutout displays.
val displayCutout = insetsWrapper.displayCutout
```

::: warning

If your app needs to run on Android 10 or below devices, we recommend always passing in a `Window` object to ensure that `BetterAndroid` can correctly handle compatibility issues for you.

Currently known compatibility issues are that the compatibility processing method provided by `androidx` cannot give correct values ​​to the `isVisible` and their contents of
`statusBars`, `navigationBars`, `systemBars` of devices below Android 11, for this reason, `BetterAndroid` repairs were made.

:::

Any insets you get from `WindowInsetsWrapper` is `InsetsWrapper`, which wrapped `Insets` and implements controllable `isVisible` state.

`InsetsWrapper` can be easily converted to an original `Insets` object, and can also be converted back to an `InsetsWrapper`.

> The following example

```kotlin
// Get the insets of the system bars.
val systemBars = insetsWrapper.systemBars
// Convert to Insets.
val insets = systemBars.toInsets()
// Convert to InsetsWrapper.
val wrapper = insets.toWrapper(systemBars.isVisible)
// You can also create it through the of method.
val wrapper = InsetsWrapper.of(insets, systemBars.isVisible)
```

Unlike `Insets`, `InsetsWrapper` has overloaded operators, and you can use `+`, `-` and `or`, `and` to operate or compare on it.

> The following example

```kotlin
val insets1 = InsetsWrapper.of(10, 10, 10, 10)
val insets2 = InsetsWrapper.of(20, 20, 20, 20)
// Use "+" operator, equivalent to Insets.add(insets1, insets2).
val insets3 = insets1 + insets2
// Use "-" operator, equivalent to Insets.subtract(insets2, insets1).
val insets3 = insets2 - insets1
// Use "or" operator, equivalent to Insets.max(insets1, insets2).
val insets3 = insets1 or insets2
// Use "and" operator, equivalent to Insets.min(insets1, insets2).
val insets3 = insets1 and insets2
// Use the ">" operator to compare
val isUpperTo = insets1 > insets2
// Use the "<" operator to compare
val isLowerTo = insets1 < insets2
```

After obtaining the insets, the general approach is to set it to the `padding` of the `View`
so that it "makes way" for the system to occupy the position.

Whether it is `InsetsWrapper` or `Insets`, you do not need to use a form such as
`View.setPadding(insets.left, insets.top, insets.right, insets.bottom)`, which seems extremely unfriendly.

You can easily set it directly as the `padding` of a `View` using the following method.

> The following example

```kotlin
// Assume this is your current view.
val view: View
// Get the insets of the system bars.
val systemBars = insetsWrapper.systemBars
// Use insets to set the padding of the view.
view.setInsetsPadding(systemBars)
// Since the object demonstrated here is the system bars,
// you can only update the vertical (top and bottom) padding.
// Using the updateInsetsPadding method has the same effect as updatePadding.
view.updateInsetsPadding(systemBars, vertical = true)
```

As we mentioned above, to create a `WindowInsetsWrapper`, you need an existing `WindowInsetsCompat`.

For backward compatibility reasons, you can use `ViewCompat.setOnApplyWindowInsetsListener` to set a change listener for `View`.

Its essential function is to control the transfer of window insets, window insets are transferred from the root view to
the sub view through the `View.onApplyWindowInsets` method.

Delivery will not stop until you explicitly consume it using `WindowInsetsCompat.CONSUMED`.

> The following example

```kotlin
// Assume this is your current view.
val view: View
// Set view's window insets change listener.
ViewCompat.setOnApplyWindowInsetsListener(view) { view, insets ->
    // insets is the current WindowInsetsCompat.
    // You can create WindowInsetsWrapper through it.
    val insetsWrapper = insets.createWrapper()
    // Consume the window insets at the last bit and stop passing them down.
    WindowInsetsCompat.CONSUMED // Or fill in the current insets and continue passing down.
}
```

This approach seems cumbersome, so `BetterAndroid` also provides you with a simpler method.

For example, we need to know the space occupied by the input method and set the `padding` from window insets for the input method layout.

At this point you can use `View.handleOnWindowInsetsChanged` to directly get a `WindowInsetsWrapper`.

> The following example

```kotlin
// Assume this is your input method layout.
val imeSpaceLayout: FrameLayout
// Handle view's window insets change listener.
imeSpaceLayout.handleOnWindowInsetsChanged { imeSpaceLayout, insetsWrapper ->
    // Set the padding provided by ime.
    imeSpaceLayout.setInsetsPadding(insetsWrapper.ime)
    // Or use ime to update the padding at the bottom.
    imeSpaceLayout.updateInsetsPadding(insetsWrapper.ime, bottom = true)
}
```

If you want to consume window insets from the subview so that they are no longer passed down, you just need to set `consumed = true` in the method parameters.

> The following example

```kotlin
// Handle view's window insets change listener.
imeSpaceLayout.handleOnWindowInsetsChanged(consumed = true) { imeSpaceLayout, insetsWrapper ->
    // The content is the same as above.
}
```

If you want to animate window insets when they change as well, you don't need to reset a `View.setWindowInsetsAnimationCallback`.

You just need to set `animated = true` in the method parameters so that the callback will be triggered every time window insets change.

> The following example

```kotlin
// Handle view's window insets change listener.
imeSpaceLayout.handleOnWindowInsetsChanged(animated = true) { imeSpaceLayout, insetsWrapper ->
    // The content is the same as above.
}
```

::: warning

This feature was introduced starting with Android 11, in previous systems, callbacks were still triggered immediately, so no animation effects would be produced.

:::

In addition, when you set up window insets change listeners, you don't need to care when the listeners were set, you can remove them at any time.

This operation will remove all `View.setOnApplyWindowInsetsListener` and `View.setWindowInsetsAnimationCallback`.

> The following example

```kotlin
// Assume this is your current view.
Val view: View
// Remove view's window insets change listener.
view.removeWindowInsetsListener()
```

::: warning

You can only set one window insets listener for a `View`, repeatedly set listeners will be overwritten by the last one.

:::

If you want to get window insets directly from the current `View`, then you can also create a `WindowInsetsWrapper` using the following method.

> The following example

```kotlin
// Assume this is your current view.
val view: View
// Create a WindowInsetsWrapper.
val insetsWrapper = view.createRootWindowInsetsWrapper()
// You can also create it through the from method.
val insetsWrapper = WindowInsetsWrapper.from(view)
// Get the insets of the system bars.
// If window insets cannot be obtained through view, null will be returned.
val systemBars = insetsWrapper?.systemBars
```

In addition to the above approach, `WindowInsetsWrapper` also provides a `WindowInsetsWrapper.Absolute`, which you can directly pass without any listener
and use `Window.getDecorView` to gets an absolute insets.

> The following example

```kotlin
// Assume this is your activity.
Val activity: Activity
// Under normal circumstances, you can get the window through the current activity.
val window = activity.window
// Create a WindowInsetsWrapper.Absolute.
val absoluteWrapper = WindowInsetsWrapper.Absolute.from(window)
// Get the insets of the status bars.
val statusBar = absoluteWrapper.statusBar
// Get the insets of the navigation bars.
val navigationBar = absoluteWrapper.navigationBar
// Get the insets of the system bars.
val systemBars = absoluteWrapper.systemBars
```

::: warning

The values obtained in this way are for reference only.

We do not recommend obtaining insets in this way, when the current device has a cutout display, these values may be inaccurate.

:::

Below are all the insets provided in `WindowInsetsWrapper`.

| Insets                    | Description                                                                                     |
| ------------------------- | ----------------------------------------------------------------------------------------------- |
| `statusBars`              | Status bars.                                                                                    |
| `navigationBars`          | Navigation bars.                                                                                |
| `captionBar`              | Caption bar.                                                                                    |
| `systemBars`              | System bars. (`captionBar` + `statusBars` + `navigationBars`)                                   |
| `ime`                     | Input method.                                                                                   |
| `tappableElement`         | Tappable element.                                                                               |
| `systemGestures`          | System gestures.                                                                                |
| `mandatorySystemGestures` | Mandatory system gestures.                                                                      |
| `displayCutout`           | cutout display. (notch screen)                                                                  |
| `waterFall`               | Waterfall screen. (curved screen)                                                               |
| `safeGestures`            | Safe gestures. (`systemGestures` + `mandatorySystemGestures` + `waterFall` + `tappableElement`) |
| `safeDrawing`             | Safe drawing. (`displayCutout` + `systemBars` + `ime`)                                          |
| `safeDrawingIgnoringIme`  | Safe drawing. (ignoring `ime`) (`displayCutout` + `systemBars`)                                 |
| `safeContent`             | Safe content. (`safeDrawing` + `safeGestures`)                                                  |

Below are all the insets provided in `WindowInsetsWrapper.Absolute`.

| Insets           | Description                                    |
| ---------------- | ---------------------------------------------- |
| `statusBars`     | Status bars.                                   |
| `navigationBars` | Navigation bars.                               |
| `systemBars`     | System bars. (`statusBars` + `navigationBars`) |

### System Bars (Status Bars, Navigation Bars, etc)

::: tip Contents of This Section

[SystemBarsController](kdoc://ui-component/ui-component/com.highcapable.betterandroid.ui.component.systembar/-system-bars-controller)

System bars controller.

[ISystemBarsController](kdoc://ui-component/ui-component/com.highcapable.betterandroid.ui.component.proxy/-i-system-bars-controller)

System bars controller interface.

[SystemBarStyle](kdoc://ui-component/ui-component/com.highcapable.betterandroid.ui.component.systembar.style/-system-bar-style)

System bar style.

[SystemBars](kdoc://ui-component/ui-component/com.highcapable.betterandroid.ui.component.systembar.type/-system-bars)

System bars type.

[SystemBarBehavior](kdoc://ui-component/ui-component/com.highcapable.betterandroid.ui.component.systembar.type/-system-bar-behavior)

System bars behavior.

:::

The serious adaptation problem of Android development lies in the confusion of terminal devices without unified development specifications.

In order to provide users with a better experience, when should the status bars and navigation bars be displayed or hidden,
the color and background of the status bars and navigation bars, etc.

These are all issues that developers need to consider during the development process.

So `BetterAndroid` docks and encapsulates the system bars adaptation solution provided by `androidx` and integrates it into `SystemBarsController`.

Now, you can call it very conveniently to easily implement a series of solutions for the operating system bars.

`SystemBarsController` supports at least Android 5.0 and solves compatibility issues in some manufacturers' customized systems.

`AppBindingActivity`, `AppViewsActivity`, `AppComponentActivity`, `AppBindingFragment`, `AppViewsFragment`
have implemented the `ISystemBarsController` interface by default, you can directly use `systemBars` to obtain `SystemBarsController`.

But you can still create a `SystemBarsController` manually using the `Activity.getWindow` object in `Activity`.

> The following example

```kotlin
class YourActivity : AppCompatActivity() {

    // Create a lazy object.
    val systemBars by lazy { SystemBarsController.from(window) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Create your binding.
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Initialize systemBars using the current root view.
        systemBars.init(binding.root)
    }

    override fun onDestroy() {
        super.onDestroy()
        // Destroy systemBars, which will restore the state before initialization.
        // Optional, to prevent memory leaks.
        systemBars.destroy()
    }
}
```

::: warning

When using the `init` method, it is recommended and recommended to pass in your own root view, otherwise `android.R.id.content` will be used by default as the root view.

You <u>**should avoid using it as the root view**</u>, this is uncontrollable, you should be able to maintain your own root view at any time in `Activity`.

If you are not using `ViewBinding`, `AppViewsActivity` and `AppComponentActivity` have overridden the `setContentView` method for you by default.

It will automatically load your root view into `SystemBarsController` when you use this method.

You can also manually override the `setContentView` method to achieve this functionality.

> The following example

```kotlin
override fun setContentView(layoutResID: Int) {
    super.setContentView(layoutResID)
    // The first child view is your root view.
    val rootView = findViewById<ViewGroup>(android.R.id.content).getChildAt(0)
    // Initialize systemBars using the current root view.
    systemBars.init(rootView)
}
```

:::

The following is a detailed usage introduction of `SystemBarsController`.

Initialize `SystemBarsController` and handle window insets `padding` of root view.

> The following example

```kotlin
// Assume this is your current root view.
val rootView: ViewGroup
// Initialize SystemBarsController.
// Your root view must have been set to a parent layout, otherwise an exception will be thrown.
systemBars.init(rootView)
// You can customize the window insets that handle the root view.
systemBars.init(rootView, edgeToEdgeInsets = { systemBars })
// If you don't want SystemBarsController to automatically handle the root view's window insets for you,
// you can directly set edgeToEdgeInsets to null.
systemBars.init(rootView, edgeToEdgeInsets = null)
```

::: warning

`SystemBarsController` will automatically set `Window.setDecorFitsSystemWindows(false)` during initialization
(on cutout display devices, `layoutInDisplayCutoutMode` will also be set to `LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES`),
you just need to set `edgeToEdgeInsets` in `init` (the default setting),
then your root view will have a window insets `padding` controlled by `safeDrawingIgnoringIme`,
which is why you should be able to maintain your own root view at any time in `Activity`.

If you set `edgeToEdgeInsets` to `null` in `init`, your root view will fully expand to full screen.

The above effect is equivalent to `enableEdgeToEdge` provided in `androidx.activity:activity`.

Without any action, your layout will be blocked by system bars or dangerous areas of the system (such as cutout displays), which will affect the user experience.

If you want to maintain and manage the `padding` of the current root view yourself, you must ensure that your interface elements can correctly adapt to the spacing provided by window insets.

You can go to the [Insets](#insets) section of the previous section learn more about window insets.

You no longer need to use `enableEdgeToEdge`, `SystemBarsController` will hold this effect by default after initialization,
you should use `edgeToEdgeInsets` to control the window insets `padding` of the root view.

:::

::: tip

In Jetpack Compose, you can use `AppComponentActivity` to get a `SystemBarsController` initialized with `edgeToEdgeInsets = null`,
then use Jetpack Compose to set window insets.

`BetterAndroid` also provides extension support for it, for more functions, you can refer to [compose-multiplatform](../library/compose-multiplatform.md).

:::

Set the behavior of system bars.

This determines the system-controlled behavior when showing or hiding system bars.

> The following example

```kotlin
systemBars.behavior = SystemBarBehavior.SHOW_TRANSIENT_BARS_BY_SWIPE
```

The following are all behaviors provided in `SystemBarBehavior`, those marked with `*` are the default behaviors.

| Behavior                        | Description                                                                                                                                                   |
| ------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `DEFAULT`                       | Default behavior controlled by the system.                                                                                                                    |
| *`SHOW_TRANSIENT_BARS_BY_SWIPE` | A system bar that can be popped up by gesture sliding in full screen and displayed as a translucent system bar, and continues to hide after a period of time. |

Show, hide system bars.

> The following example

```kotlin
// Enter immersive mode (full screen mode).
// Hide status bars and navigation bars at the same time.
systemBars.hide(SystemBars.ALL)
// Separately control the status bars and navigation bars.
systemBars.hide(SystemBars.STATUS_BARS)
systemBars.hide(SystemBars.NAVIGATION_BARS)
// Exit immersive mode (full screen mode).
// Show status bars and navigation bars at the same time.
systemBars.show(SystemBars.ALL)
// Separately control the status bars and navigation bars.
systemBars.show(SystemBars.STATUS_BARS)
systemBars.show(SystemBars.NAVIGATION_BARS)
```

::: tip

If you need to control the showing and hiding of the input method (IME), you can refer to [ui-extension → View Extension](../library/ui-extension.md#view-extension).

:::

Set the style of the system bars.

You can customize the appearance of the status bars and navigation bars.

::: warning

In systems below Android 6.0, the content of the status bars does not support inversion, if you set a bright color, it will be automatically processed as a semi-transparent mask.

However, for MIUI and Flyme systems that have added the inverse color function themselves, they will use their own private solutions to achieve the inverse color effect.

In systems below Android 8, the content of the navigation bars does not support inversion, and the processing method is the same as above.

:::

> The following example

```kotlin
// Set the style of the status bars.
// Note: Please make sure to import com.highcapable.betterandroid.ui.component.systembar.style
//       SystemBarStyle under the package name, not androidx.activity.SystemBarStyle.
systemBars.statusBarStyle = SystemBarStyle(
    // Set background color.
    color = Color.WHITE,
    // Set content color.
    darkContent = true
)
// Set the style of the navigation bars.
systemBars.navigationBarStyle = SystemBarStyle(
    // Set background color.
    color = Color.WHITE,
    // Set content color.
    darkContent = true
)
// You can set the style of the status bars and navigation bars at once.
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
// You can also set the style of the status bars and navigation bars at the same time.
systemBars.setStyle(
    style = SystemBarStyle(
        color = Color.WHITE,
        darkContent = true
    )
)
```

The following are the preset styles provided in `SystemBarStyle`, the ones marked with `*` are the default styles.

| Style              | Description                                                                                                                                   |
| ------------------ | --------------------------------------------------------------------------------------------------------------------------------------------- |
| `Auto`             | The system dark mode is a pure black background + light content color, and the light mode is a pure white background + dark content color.    |
| *`AutoTransparent` | The light content color is used in the system dark mode, and the dark content color is used in the light mode, with a transparent background. |
| `Light`            | Pure white background + dark content color.                                                                                                   |
| `LightScrim`       | Translucent pure white background + dark content color.                                                                                       |
| `LightTransparent` | Transparent background + dark content color.                                                                                                  |
| `Dark`             | Pure black background + light content color.                                                                                                  |
| `DarkScrim`        | Translucent solid black background + light content color.                                                                                     |
| `DarkTransparent`  | Transparent background + light content color.                                                                                                 |

::: tip

When the app is first cold-launched, the color of the system bars will be determined by the attributes you set in `styles.xml`.

In order to provide a better user experience during cold launch, you can refer to the following examples.

> The following example

```xml
<style name="Theme.MyApp.Demo" parent="Theme.MaterialComponents.DayNight.NoActionBar">
    <!-- Set the status bar color. -->
    <item name="android:statusBarColor">@color/colorPrimary</item>
    <!-- Set the navigation bar color. -->
    <item name="android:navigationBarColor">@color/colorPrimary</item>
    <!-- Set the status bar content color. -->
    <item name="android:windowLightStatusBar">true</item>
    <!-- Set the navigation bar content color. -->
    <item name="android:windowLightNavigationBar">true</item>
</style>
```

:::

Destroy `SystemBarsController`.

This will restore the state before initialization, including the status bars, navigation bars color, etc.

> The following example

```kotlin
// Destroy SystemBarsController, which will restore the state before initialization.
systemBars.destroy()
// You can use isDestroyed at any time to determine whether
// the current SystemBarsController has been destroyed.
val isDestroyed = systemBars.isDestroyed
```

::: warning

After using `SystemBarsController`, the `WindowInsetsController` of the current root view `rootView` has been automatically taken over by it.

Please do not manually set parameters such as `isAppearanceLightStatusBars` and `isAppearanceLightNavigationBars` in `WindowInsetsController`,
this may cause the actual effects of `statusBarStyle`, `navigationBarStyle`, `setStyle` and other functions to display abnormally.

:::