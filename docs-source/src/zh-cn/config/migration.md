# 迁移指南

> 这里集中记录 `BetterAndroid` 各模块后续有不兼容变更或推荐迁移路径的内容。

## 迁移 BackPressedController

在处理系统返回事件时，如果你之前使用的是 `ui-component → 系统事件` 中的 `BackPressedController`，
现在推荐迁移到 [ui-extension → BackPressed 扩展](../library/ui-extension.md#backpressed-扩展) 中基于官方 `OnBackPressedDispatcher` 的扩展写法。

新的方案不再需要额外的控制器、接口接入或手动销毁过程，而是直接复用 `androidx` 已有的生命周期管理能力。

### 迁移方式

原来的写法通常如下。

> 示例如下

```kotlin
val backPressed by lazy { BackPressedController.from(this) }

val callback = backPressed.addCallback {
    // Your code here.
}

backPressed.trigger()
backPressed.destroy()
```

现在你可以直接改为如下方式。

> 示例如下

```kotlin
val callback = OnBackPressedCallback {
    // Your code here.
}

onBackPressedDispatcher.addCallback(this, callback)
```

如果你需要在当前回调中继续分发这次返回事件，可以直接使用 `trigger(onBackPressedDispatcher)`。

> 示例如下

```kotlin
onBackPressedDispatcher.addCallback(this, OnBackPressedCallback {
    trigger(onBackPressedDispatcher)
    trigger(onBackPressedDispatcher, removed = true)
})
```

对于 `Fragment`，你可以直接使用 `fragment.onBackPressedDispatcher`，
并按需绑定到 `viewLifecycleOwner` 或 `Fragment` 自身。

> 示例如下

```kotlin
onBackPressedDispatcher.addCallback(viewLifecycleOwner, OnBackPressedCallback {
    // Your code here.
})
```

如果你是基于 `View` 注册返回事件回调，也可以直接迁移为如下方式。

> 示例如下

```kotlin
binding.root.onBackPressedDispatcher.addCallback(
    binding.root.requireLifecycleOwner(),
    OnBackPressedCallback {
        // Your code here.
    }
)
```

### 对照关系

- `BackPressedController.from(activity)` → `activity.onBackPressedDispatcher`
- `backPressed.addCallback { ... }` → `onBackPressedDispatcher.addCallback(this, OnBackPressedCallback { ... })`
- `backPressed.trigger()` → 在 `OnBackPressedCallback` 中调用 `trigger(onBackPressedDispatcher)`
- `backPressed.destroy()` → 不再需要，交由官方生命周期管理
- `IBackPressedController` → 不再需要

## 迁移 SystemColors

如果你之前使用的是 `ui-extension → 系统颜色` 中的 `SystemColors`，现在需要注意这些用法已经被移除。

`ui-extension` 不再引入 `material` 依赖，因此 `SystemColors` 相关 API 不再继续保留。

### 迁移方式

如果你之前仅使用了 `SystemColors.isAvailable` 来判断当前系统是否支持动态主题色，现在你可以迁移到官方提供的 `DynamicColors.isDynamicColorAvailable()`。

> 示例如下

```kotlin
// 之前
val isAvailable = SystemColors.isAvailable

// 之后
val isAvailable = DynamicColors.isDynamicColorAvailable()
```

除此之外，`SystemColors.from(context)` 以及所有颜色获取相关写法均不再支持，也没有继续在 `ui-extension` 中提供替代封装。

如果你的业务仍然依赖这部分能力，请改为直接接入你自己的 `material` 依赖方案，或重新评估是否仍然需要动态主题色取值逻辑。