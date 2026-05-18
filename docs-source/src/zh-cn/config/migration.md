# 迁移指南

> 这里集中记录 `BetterAndroid` 各模块后续有不兼容变更或推荐迁移路径的内容。

## 迁移 BackPressedController

在处理系统返回事件时，如果你之前使用的是 `ui-component → 系统事件` 中的 `BackPressedController`，
现在推荐迁移到 [ui-extension → BackPressed 扩展](../library/ui-extension.md#backpressed-扩展) 中基于官方 `OnBackPressedDispatcher` 的扩展写法。

新的方案不再需要额外的控制器、接口接入或手动销毁过程，而是直接复用 AndroidX 已有的生命周期管理能力。

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
val callback = addBackPressedCallback {
    // Your code here.
}
```

如果你需要在当前回调中继续分发这次返回事件，可以直接使用 `trigger()`。

> 示例如下

```kotlin
addBackPressedCallback {
    trigger()
    trigger(removed = true)
}
```

对于 `Fragment`，默认会自动使用当前 `viewLifecycleOwner` 绑定生命周期。

> 示例如下

```kotlin
addBackPressedCallback {
    // Your code here.
}
```

如果你是基于 `View` 注册返回事件回调，也可以直接迁移为如下方式。

> 示例如下

```kotlin
binding.root.addBackPressedCallback {
    // Your code here.
}
```

### 对照关系

- `BackPressedController.from(activity)` → `activity.addBackPressedCallback { ... }`
- `backPressed.addCallback { ... }` → `addBackPressedCallback { ... }`
- `backPressed.trigger()` → 在 `BackPressedCallback` 中调用 `trigger()`
- `backPressed.destroy()` → 不再需要，交由官方生命周期管理
- `IBackPressedController` → 不再需要