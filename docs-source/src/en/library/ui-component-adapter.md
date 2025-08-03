# ui-component-adapter

![Maven Central](https://img.shields.io/maven-central/v/com.highcapable.betterandroid/ui-component-adapter?logo=apachemaven&logoColor=orange&style=flat-square)
<span style="margin-left: 5px"/>
![Maven metadata URL](https://img.shields.io/maven-metadata/v?metadataUrl=https%3A%2F%2Fraw.githubusercontent.com%2FHighCapable%2Fmaven-repository%2Frefs%2Fheads%2Fmain%2Frepository%2Freleases%2Fcom%2Fhighcapable%2Fbetterandroid%2Fui-component-adapter%2Fmaven-metadata.xml&logo=apachemaven&logoColor=orange&label=highcapable-maven-releases&style=flat-square)
<span style="margin-left: 5px"/>
![Android Min SDK](https://img.shields.io/badge/Min%20SDK-21-orange?logo=android&style=flat-square)

This is a dependency for UI (user interface) adapter components.

## Configure Dependency

You can add this module to your project using the following method.

### SweetDependency (Recommended)

Add dependency in your project's `SweetDependency` configuration file.

```yaml
libraries:
  com.highcapable.betterandroid:
    ui-component-adapter:
      version: +
```

Configure dependency in your project's `build.gradle.kts`.

```kotlin
implementation(com.highcapable.betterandroid.ui.component.adapter)
```

### Version Catalog

Add dependency in your project's `gradle/libs.versions.toml`.

```toml
[versions]
betterandroid-ui-component-adapter = "<version>"

[libraries]
betterandroid-ui-component-adapter = { module = "com.highcapable.betterandroid:ui-component-adapter", version.ref = "betterandroid-ui-component-adapter" }
```

Configure dependency in your project's `build.gradle.kts`.

```kotlin
implementation(libs.betterandroid.ui.component.adapter)
```

Please change `<version>` to the version displayed at the top of this document.

### Traditional Method

Configure dependency in your project's `build.gradle.kts`.

```kotlin
implementation("com.highcapable.betterandroid:ui-component-adapter:<version>")
```

Please change `<version>` to the version displayed at the top of this document.

## Function Introduction

You can view the KDoc [click here](kdoc://ui-component-adapter).

::: tip Contents of This Section

[BaseAdapterBuilder](kdoc://ui-component-adapter/ui-component-adapter/com.highcapable.betterandroid.ui.component.adapter/-base-adapter-builder)

Can be used to build a `BaseAdapter`.

[PagerAdapterBuilder](kdoc://ui-component-adapter/ui-component-adapter/com.highcapable.betterandroid.ui.component.adapter/-pager-adapter-builder)

Can be used to build a `PagerAdapter`.

[RecyclerAdapterBuilder](kdoc://ui-component-adapter/ui-component-adapter/com.highcapable.betterandroid.ui.component.adapter/-recycler-adapter-builder)

Can be used to build a `RecyclerView.Adapter`.

[PagerMediator](kdoc://ui-component-adapter/ui-component-adapter/com.highcapable.betterandroid.ui.component.adapter.mediator/-pager-mediator)

Pager mediator for `ViewPager`.

[RecyclerCosmetic](kdoc://ui-component-adapter/ui-component-adapter/com.highcapable.betterandroid.ui.component.adapter.recycler.cosmetic/-recycler-cosmetic)

Cosmetic of `LayoutManager` and `ItemDecoration` of `RecyclerView`.

[LinearHorizontalItemDecoration](kdoc://ui-component-adapter/ui-component-adapter/com.highcapable.betterandroid.ui.component.adapter.recycler.decoration/-linear-horizontal-item-decoration)

Linear horizontal list decoration for `RecyclerView`.

[LinearVerticalItemDecoration](kdoc://ui-component-adapter/ui-component-adapter/com.highcapable.betterandroid.ui.component.adapter.recycler.decoration/-linear-vertical-item-decoration)

Linear vertical list decoration for `RecyclerView`.

[GridVerticalItemDecoration](kdoc://ui-component-adapter/ui-component-adapter/com.highcapable.betterandroid.ui.component.adapter.recycler.decoration/-grid-vertical-item-decoration)

Grid vertical list decoration for `RecyclerView`.

[LinearLayoutManager](kdoc://ui-component-adapter/ui-component-adapter/com.highcapable.betterandroid.ui.component.adapter.recycler.layoutmanager/-linear-layout-manager)

Enhanced linear layout manager for `RecyclerView`.

[GridLayoutManager](kdoc://ui-component-adapter/ui-component-adapter/com.highcapable.betterandroid.ui.component.adapter.recycler.layoutmanager/-grid-layout-manager)

Enhanced grid layout manager for `RecyclerView`.

[RecyclerLayoutManager](kdoc://ui-component-adapter/ui-component-adapter/com.highcapable.betterandroid.ui.component.adapter.recycler.layoutmanager.base/-recycler-layout-manager)

Enhanced layout manager base class for `RecyclerView`.

[RecyclerAdapterWrapper](kdoc://ui-component-adapter/ui-component-adapter/com.highcapable.betterandroid.ui.component.adapter.recycler.wrapper/-recycler-adapter-wrapper)

Custom adapter wrapper class for `RecyclerView`.

[RecyclerView, RecyclerAdapter](kdoc://ui-component-adapter/ui-component-adapter/com.highcapable.betterandroid.ui.component.adapter.recycler.factory)

Extension methods for `RecyclerView` and its adapter builds.

[CommonAdapter](kdoc://ui-component-adapter/ui-component-adapter/com.highcapable.betterandroid.ui.component.adapter.factory)

Extension methods for the adapter build above.

[ViewHolderDelegate](kdoc://ui-component-adapter/ui-component-adapter/com.highcapable.betterandroid.ui.component.adapter.viewholder.delegate.base/-view-holder-delegate)

Custom `ViewHolder` delegate class.

[AdapterPosition](kdoc://ui-component-adapter/ui-component-adapter/com.highcapable.betterandroid.ui.component.adapter.entity/-adapter-position)

Adapter position entity.

:::

From the beginning of `ListView` to the emergence of `RecyclerView`, adapters in Android have always been one of the most troublesome problems for developers.

To address this problem, `BetterAndroid` encapsulates the adapters of the following components:

`ListView`, `AutoCompleteTextView`, `ListPopupWindow`, `RecyclerView`, `ViewPager`, `ViewPager2`

In Kotlin you can create a data adapter more easily.

Now, all you need is a data array and a custom adapter layout to create an adapter very quickly and bind to these components.

### Base Adapter

Create a `BaseAdapter` for `ListView`, `AutoCompleteTextView`, `ListPopupWindow`.

> The following example

```kotlin
// Assume that's your entity class.
data class MyEntity(
    var iconRes: Int,
    var name: String
)
// Assume that's the dataset you need to bind.
val listData = ArrayList<MyEntity>()
// Create and bind to a custom BaseAdapter.
val adapter = listView.bindAdapter<MyEntity> {
    // Bind the dataset.
    onBindData { listData }
    // Bind the custom adapter layout adapter_my_layout.xml
    onBindItemView<AdapterMyLayoutBinding> { binding, entity, position ->
        binding.iconView.setImageResource(entity.iconRes)
        binding.textView.text = entity.name
    }
    // Bind the click event for each item.
    onItemViewClick { itemView, entity, position ->
        // Your code here.
    }
}
```

If you want to manually create an adapter and bind to the above components, please refer to the following example.

> The following example

```kotlin
// Assume that's your current Context.
val context: Context
// Manually create a BaseAdapter.
val adapter = BaseAdapter<MyEntity>(context) {
    // The content is the same as above.
}
// Then bind to listView.
listView.adapter = adapter
```

Create a `PagerAdapter` for `ViewPager`.

> The following example

```kotlin
// Assume that's your entity class.
data class MyEntity(
    var iconRes: Int,
    var name: String
)
// Assume that's the dataset you need to bind.
val listData = ArrayList<MyEntity>()
// Create and bind to a custom PagerAdapter.
val adapter = viewPager.bindAdapter<MyEntity> {
    // Bind the dataset.
    onBindData { listData }
    // Bind the custom adapter layout adapter_my_layout.xml
    onBindPageView<AdapterMyLayoutBinding> { binding, entity, position ->
        binding.iconView.setImageResource(entity.iconRes)
        binding.textView.text = entity.name
    }
}
```

You can also use `dataSetCount` directly to not specify a dataset and only create multiple pages repeatedly.

> The following example

```kotlin
// Create and bind to a custom PagerAdapter.
val adapter = viewPager.bindAdapter {
    // Manually create two identical pages.
    dataSetCount = 2
    // Bind the custom adapter layout adapter_my_layout.xml
    onBindPageView<AdapterMyLayoutBinding> { binding, _, position ->
        // You can determine the position of the current page through position.
    }
}
```

You can also reuse the `onBindPageView` method to create multiple different pages, and the page order is determined by the creation order.

> The following example

```kotlin
// Create and bind to a custom PagerAdapter.
val adapter = viewPager.bindAdapter {
    // Bind the custom adapter layout adapter_my_layout_1.xml
    onBindPageView<AdapterMyLayout1Binding> { binding, _, position ->
        // You can determine the position of the current page through position.
    }
    // Bind the custom adapter layout adapter_my_layout_2.xml
    onBindPageView<AdapterMyLayout2Binding> { binding, _, position ->
        // You can determine the position of the current page through position.
    }
}
```

The number of pages created is the number of times the `onBindPageView` method is reused.

::: danger

If you reuse the `onBindPageView` method to create multiple different pages, you cannot specify `dataSetCount` or bind a dataset.

:::

If you need to handle `getPageTitle` and `getPageWidth` in `PagerAdapter`, you can use `PagerMediator` to accomplish this.

> The following example

```kotlin
// Create and bind to a custom PagerAdapter.
val adapter = viewPager.bindAdapter {
    // Bind PagerMediator for each item.
    onBindMediators {
        // Handle page titles.
        title = when (position) {
            0 -> "Home"
            else -> "Additional Page"
        }
        // Handle page width (ratio).
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
// Assume that's your current Context.
val context: Context
// Manually create a PagerAdapter.
val adapter = PagerAdapter<MyEntity>(context) {
    // The content is the same as above.
}
// Then bind to viewPager.
viewPager.adapter = adapter
```

### RecyclerView Adapter

Android Jetpack brings developers a more modern adapter component with richer functionality - `RecyclerView.Adapter`.

Create a regular `RecyclerView.Adapter` for `RecyclerView` and `ViewPager2`.

> The following example

```kotlin
// Assume that's your entity class.
data class MyEntity(
    var iconRes: Int,
    var name: String
)
// Assume that's the dataset you need to bind.
val listData = ArrayList<MyEntity>()
// Create and bind to a custom RecyclerView.Adapter.
val adapter = recyclerView.bindAdapter<MyEntity> {
    // Bind the dataset.
    onBindData { listData }
    // Bind the custom adapter layout adapter_my_layout.xml
    onBindItemView<AdapterMyLayoutBinding> { binding, entity, position ->
        binding.iconView.setImageResource(entity.iconRes)
        binding.textView.text = entity.name
    }
    // Set click event for each item.
    onItemViewClick { itemView, viewType, entity, position ->
        // Your code here.
    }
}
```

Create a multi-`View` type `RecyclerView.Adapter` for `RecyclerView` and `ViewPager2`.

> The following example

```kotlin
// Assume that's your entity class.
data class MyEntity(
    var iconRes: Int,
    var name: String,
    var title: String,
    var dataType: Int
)
// Assume that's the dataset you need to bind.
val listData = ArrayList<MyEntity>()
// Create and bind to a custom RecyclerView.Adapter.
val adapter = recyclerView.bindAdapter<MyEntity> {
    // Bind the dataset.
    onBindData { listData }
    // Bind View types.
    onBindViewType { entity, position -> entity.dataType }
    // Bind the custom adapter layout adapter_my_layout_1.xml
    onBindItemView<AdapterMyLayout1Binding>(viewType = 1) { binding, entity, position ->
        binding.iconView.setImageResource(entity.iconRes)
        binding.textView.text = entity.name
    }
    // Bind the custom adapter layout adapter_my_layout_2.xml
    onBindItemView<AdapterMyLayout2Binding>(viewType = 2) { binding, entity, position ->
        binding.iconView.setImageResource(entity.iconRes)
        binding.titleView.text = entity.title
    }
    // Set click event for each item.
    onItemViewClick { itemView, viewType, entity, position ->
        // Your code here.
    }
}
```

::: tip

In `RecyclerView.Adapter`, the `position` type in `onBindItemView` is `AdapterPosition` instead of `Int` as in [Base Adapter](#base-adapter).

Since `RecyclerView.Adapter` can be updated partially, after dynamically adding or removing items, the `onBindItemView` of existing items will not be called back again. At this time, you need a dynamic index instance like `AdapterPosition` to get the correct index of the current item through `position.value`.

`AdapterPosition` incorporates the `getLayoutPosition`, `getBindingAdapterPosition`, `getAbsoluteAdapterPosition` methods from `RecyclerView.ViewHolder`, which correspond to `position.layout`, `position.value` and `position.absolute`.

:::

Create header `View` and footer `View` for `RecyclerView`.

You can use the `onBindHeaderView` and `onBindFooterView` methods to add a header `View` and footer `View`. These are two special item layouts that are not counted in the bound data, and the index `position` called back through methods like `onBindItemView` is not affected.

::: warning

You can only add one header `View` and one footer `View` at the same time, and these added layouts do not support dynamic removal.

:::

> The following example

```kotlin
// Assume that's your entity class.
data class MyEntity(
    var iconRes: Int,
    var name: String
)
// Assume that's the dataset you need to bind.
val listData = ArrayList<MyEntity>()
// Create and bind to a custom RecyclerView.Adapter.
val adapter = recyclerView.bindAdapter<MyEntity> {
    // Bind the dataset.
    onBindData { listData }
    // Bind header View.
    onBindHeaderView<AdapterHeaderBinding> { binding ->
        binding.someText.text = "Header"
    }
    // Bind footer View.
    onBindFooterView<AdapterFooterBinding> { binding ->
        binding.someText.text = "Footer"
    }
    // Bind the custom adapter layout adapter_my_layout.xml
    onBindItemView<AdapterMyLayoutBinding> { binding, entity, position ->
        binding.iconView.setImageResource(entity.iconRes)
        binding.textView.text = entity.name
    }
}
```

In addition to using `ViewBinding` as shown in the above example, you can also use traditional layout resource IDs to bind them to adapter layouts.

> The following example

```kotlin
// Bind the custom adapter layout adapter_my_layout.xml
onBindItemView(R.layout.adapter_my_layout) { itemView, entity, position ->
    itemView.findViewById<ImageView>(R.id.icon_view).setImageResource(entity.iconRes)
    itemView.findViewById<TextView>(R.id.text_view).text = entity.name
}
```

If all layout loading methods do not meet your needs, you can also create a custom `ViewHolder` delegate class based on `ViewHolderDelegate`.

> The following example

```kotlin
// Create a delegate class to implement your own layout loading scheme.
// Here we assume that MyLayoutBinder is your layout loader.
class MyViewHolderDelegate(@LayoutRes private val resId: Int) : ViewHolderDelegate<MyLayoutBinder>() {

    override fun create(context: Context, parent: ViewGroup?): MyLayoutBinder {
        // Assume this is how your custom layout loader works.
        // Remember to pass in and implement the parent parameter,
        // because we need the parent's LayoutParams.
        // Note: Don't bind to parent now! The adapter does not allow
        // child layouts to hold parent layouts in advance.
        val binder = MyLayoutBinder.inflate(context, resId, parent, attachToParent = false)
        return binder
    }

    override fun getView(instance: MyLayoutBinder): View {
        // Get the required View from your layout loader.
        return instance.root
    }
}
```

Then, use your custom `ViewHolderDelegate`.

> The following example

```kotlin
// Bind your custom ViewHolderDelegate.
onBindItemView(MyViewHolderDelegate(R.layout.adapter_my_layout)) { delegate, entity, position ->
    // Here delegate is the MyLayoutBinder object,
    // assuming the following methods are all implemented by yourself.
    delegate.get<ImageView>(R.id.icon_view).setImageResource(entity.iconRes)
    delegate.get<TextView>(R.id.text_view).text = entity.name
}
```

::: warning

When you set header or footer `View`, when using `RecyclerView.Adapter`'s `notifyItemInserted`, `notifyItemRemoved`, `notifyItemChanged`, `notifyItemMoved` and other methods, there will be issues with index positions, because by default the `position` calculated by `onBindItemView` will not include header and footer layouts, and methods like `RecyclerView.scrollToPosition`, `RecyclerView.smoothScrollToPosition` will also be affected.

Since these methods are all `final` in `RecyclerView.Adapter` and cannot be overridden, in this case, `BetterAndroid` provides you with a solution. When using `RecyclerView.Adapter`, you can call the `wrapper` method to get a wrapper instance, which will automatically handle these issues for you.

> The following example

```kotlin
// Assume you have bound the adapter created using RecyclerAdapterBuilder to RecyclerView.
val recyclerView: RecyclerView
// Get the wrapper instance. If the target adapter is not
// created by RecyclerAdapterBuilder, it will return null.
val wrapper = recyclerView.adapter?.wrapper
// Normally use RecyclerView.Adapter's notification update methods.
wrapper?.notifyItemInserted(0)
wrapper?.notifyItemRemoved(0)
// Header or footer layouts need to be updated separately using the following methods.
wrapper?.notifyHeaderItemChanged()
wrapper?.notifyFooterItemChanged()
// Furthermore, you can manually use the following methods to
// determine whether header and footer layouts exist.
val hasHeaderView = wrapper?.hasHeaderView == true
val hasFooterView = wrapper?.hasFooterView == true
```

Going back to the issue we mentioned earlier, methods like `RecyclerView.scrollToPosition`, `RecyclerView.smoothScrollToPosition` will also be affected. In this case, you can use the `LinearLayoutManager`, `GridLayoutManager` and `RecyclerLayoutManager` provided under the `com.highcapable.betterandroid.ui.component.adapter.recycler.layoutmanager` package to solve this.

These encapsulated enhanced layout managers will be automatically integrated through the default `RecyclerCosmetic` (refer to [Recycler Cosmetic](#recycler-cosmetic) below). You don't need any manual operations. When you need to manually create `RecyclerView.LayoutManager`, we recommend that you inherit from the instances provided in this package.

When you use the `RecyclerView.LayoutManager` provided by `BetterAndroid`, since header or footer layouts will automatically handle `position`, when using `RecyclerView.scrollToPosition`, `RecyclerView.smoothScrollToPosition` to scroll to the top and bottom, you need to use `scrollToPosition(-1)` (top) or `scrollToPosition(lastIndex + 1)` (bottom).

Therefore, we always recommend that when you have the need to scroll to the top and bottom, use the `scrollToFirstPosition`, `scrollToLastPosition`, `smoothScrollToFirstPosition`, `smoothScrollToLastPosition` methods instead. They will automatically handle such issues (regardless of whether you use the `RecyclerView.LayoutManager` provided by `BetterAndroid`).

:::

### Fragment Adapter

Create a `FragmentPagerAdapter` for `ViewPager`.

::: warning

This usage has been deprecated by the official team. If possible, please start using `ViewPager2`.

:::

> The following example

```kotlin
// Assume that's your current FragmentActivity.
val activity: FragmentActivity
// Create and bind to a custom FragmentPagerAdapter.
val adapter = viewPager.bindFragments(activity) {
    // Set the number of Fragments to display.
    pageCount = 5
    // Bind each Fragment.
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

Same as the constructor method usage of `FragmentPagerAdapter`, you can also customize the `behavior` parameter.

If you are using it in a `Fragment`, you can fill in the current `Fragment` instance in the first parameter of `bindFragments`, and it will automatically bind to `getChildFragmentManager()`.

If you want to manually create a `FragmentPagerAdapter` and bind it to `ViewPager`, please refer to the following example.

> The following example

```kotlin
// Assume that's your current FragmentActivity.
val activity: FragmentActivity
// Manually create a FragmentPagerAdapter.
val adapter = FragmentPagerAdapter(activity) {
    // The content is the same as above.
}
// Then bind to viewPager.
viewPager.adapter = adapter
```

Create a `FragmentStateAdapter` for `ViewPager2`.

> The following example

```kotlin
// Assume that's your current FragmentActivity.
val activity: FragmentActivity
// Create and bind to a custom FragmentPagerAdapter.
val adapter = viewPager2.bindFragments(activity) {
    // Set the number of Fragments to display.
    pageCount = 5
    // Bind each Fragment.
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

If you are using it in a `Fragment`, you can fill in the current `Fragment` instance in the first parameter of `bindFragments`, and it will automatically bind to `getChildFragmentManager()`.

If you want to manually create a `FragmentPagerAdapter` and bind it to `ViewPager2`, please refer to the following example.

> The following example

```kotlin
// Assume that's your current FragmentActivity.
val activity: FragmentActivity
// Manually create a FragmentStateAdapter.
val adapter = FragmentStateAdapter(activity) {
    // The content is the same as above.
}
// Then bind to viewPager2.
viewPager2.adapter = adapter
```

### Recycler Cosmetic

If you want to manually create a `RecyclerView.Adapter` and bind it to `RecyclerView` and `ViewPager2`, please refer to the following example.

> The following example

```kotlin
// Assume that's your current Context.
val context: Context
// Manually create a RecyclerView.Adapter.
val adapter = RecyclerAdapter<CustomBean>(context) {
    // The content is the same as above.
}
// Manually create a decorator.
val cosmetic = RecyclerCosmetic.fromLinearVertical(context)
// Then bind to recyclerView.
recyclerView.layoutManager = cosmetic.layoutManager
recyclerView.addItemDecoration(cosmetic.itemDecoration) 
recyclerView.adapter = adapter
// When binding to viewPager2, you don't need to set layoutManager.
viewPager2.addItemDecoration(cosmetic.itemDecoration) 
viewPager2.adapter = adapter
```

`BetterAndroid` provides developers with several common adapter layout types for `RecyclerView` for your use.

You can specify a `RecyclerCosmetic` in the method parameters, which defaults to a linear vertical list cosmetic.

> The following example

```kotlin
// Create a linear vertical list with row spacing of 10dp.
val lvCosmetic = RecyclerCosmetic.fromLinearVertical(context, 10.toPx(context))
// Create a grid vertical list with column spacing of 10dp and row spacing of 10dp.
val gvCosmetic = RecyclerCosmetic.fromGridVertical(context, 10.toPx(context), 10.toPx(context))
// Taking lvCosmetic as an example.
// Use bindAdapter to bind to recyclerView.
recyclerView.bindAdapter<MyEntity>(lvCosmetic) {
    // ...
}
// Or, manually bind.
val adapter = RecyclerAdapter<MyEntity>(context) {
    // ...
}
recyclerView.layoutManager = lvCosmetic.layoutManager
recyclerView.addItemDecoration(lvCosmetic.itemDecoration)
recyclerView.adapter = adapter
```

::: tip

If you only need an `ItemDecoration`, you can create one through the preset `LinearHorizontalItemDecoration`, `LinearVerticalItemDecoration`, `GridVerticalItemDecoration`.

Here's a simple example.

> The following example

```kotlin
// Create a linear vertical ItemDecoration with row spacing of 10dp.
val itemDecoration = LinearVerticalItemDecoration(rowSpacing = 10.toPx(context))
// Set to recyclerView.
recyclerView.addItemDecoration(itemDecoration)
// If you need to update ItemDecoration parameters, you can use the update method.
itemDecoration.update(rowSpacing = 15.toPx(context))
// Then notify recyclerView to update.
recyclerView.invalidateItemDecorations()
```

:::

### Adapter Extensions

::: tip Contents of This Section

[RecyclerAdapter → notifyAllItemsInserted](kdoc://ui-component-adapter/ui-component-adapter/com.highcapable.betterandroid.ui.component.adapter.recycler.factory/notify-all-items-inserted)

[RecyclerAdapter → notifyAllItemsChanged](kdoc://ui-component-adapter/ui-component-adapter/com.highcapable.betterandroid.ui.component.adapter.recycler.factory/notify-all-items-changed)

[RecyclerAdapter → clearAndNotify](kdoc://ui-component-adapter/ui-component-adapter/com.highcapable.betterandroid.ui.component.adapter.recycler.factory/clear-and-notify)

[RecyclerAdapter → notifyDataSetChangedIgnore](kdoc://ui-component-adapter/ui-component-adapter/com.highcapable.betterandroid.ui.component.adapter.recycler.factory/notify-data-set-changed-ignore)

Extensions for `RecyclerView` adapter.

:::

The adapter extensions introduced in this section are mostly extensions of `RecyclerView.Adapter`.

Usually, we need to use methods like `notifyItemInserted`, `notifyItemChanged`, etc. to notify the adapter that the data has changed.

When we add data to the dataset all at once, we usually need to use `notifyItemRangeInserted` to notify the adapter that the data has changed.

> The following example

```kotlin
// Assume this is your RecyclerView.Adapter.
val adapter: RecyclerView.Adapter<*>
// Assume this is your dataset, initially empty.
val dataSet: MutableList<MyEntity>
// Add some data to the dataset.
dataSet.addAll(...)
// Notify the adapter that the data has changed.
adapter.notifyItemRangeInserted(0, dataSet.size)
```

When the data is confirmed to be added from 0, `BetterAndroid` provides you with a simpler way to complete this.
Now you can use the following method to notify the adapter that the data has changed.

> The following example

```kotlin
// Assume this is your RecyclerView.Adapter.
val adapter: RecyclerView.Adapter<*>
// Assume this is your dataset, initially empty.
val dataSet: MutableList<MyEntity>
// Add some data to the dataset.
dataSet.addAll(...)
// Notify the adapter that the data has changed.
adapter.notifyAllItemsInserted()
```

The above method will use `adapter.itemCount` by default to get the size of the dataset, no need to manually specify the range to be updated.

At this time, please ensure that your adapter returns the correct `itemCount`, otherwise, please manually pass in `dataSet`.

> The following example

```kotlin
// Notify the adapter that the data has changed.
adapter.notifyAllItemsInserted(dataSet)
```

Similarly, when the data is confirmed to have all changed (for example, in a multi-select state list, updating the selected and unselected checkbox states),
you can use the following method to notify the adapter that the data has changed.

> The following example

```kotlin
// Assume this is your RecyclerView.Adapter.
val adapter: RecyclerView.Adapter<*>
// Assume this is your dataset.
val dataSet: MutableList<MyEntity>
// Simulate operating the dataset (e.g., select all action).
dataSet.forEach { it.isSelected = true }
// Notify the adapter that the data has changed.
adapter.notifyAllItemsChanged()
```

Similarly, please ensure that your adapter returns the correct `itemCount`, otherwise, please manually pass in `dataSet`.

> The following example

```kotlin
// Notify the adapter that the data has changed.
adapter.notifyAllItemsChanged(dataSet)
```

When we need to clear the dataset and notify the adapter that the data has changed, we usually need to use `notifyItemRangeRemoved` to notify the adapter that the data has changed.

> The following example

```kotlin
// Assume this is your RecyclerView.Adapter.
val adapter: RecyclerView.Adapter<*>
// Assume this is your dataset.
val dataSet: MutableList<MyEntity>
// Save the current data size.
val count = dataSet.size
// Clear the dataset.
dataSet.clear()
// Notify the adapter that the data has changed.
adapter.notifyItemRangeRemoved(0, count)
```

This process is still cumbersome, `BetterAndroid` provides a simpler way for this. Now you can use the following method to clear the dataset and notify the adapter that the data has changed.
This method will automatically calculate the size of the dataset.

> The following example

```kotlin
// Assume this is your RecyclerView.Adapter.
val adapter: RecyclerView.Adapter<*>
// Assume this is your dataset.
val dataSet: MutableList<MyEntity>
// Clear the dataset and notify the adapter that the data has changed.
adapter.clearAndNotify(dataSet)
```

::: tip

There are also some other extensions that can be used. `notifyDataSetChangedIgnore` will ignore the Lint warnings given during coding and directly provide you with the use of `notifyDataSetChanged`.

However, this method is still not recommended because it will cause the entire list to refresh, which will cause performance issues in large datasets.

:::