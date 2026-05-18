# Migration Guide

> This page centrally records future breaking changes and recommended migration paths across `BetterAndroid` modules.

## Migrate BackPressedController

When handling system back pressed events, if you previously used `BackPressedController`
from `ui-component → System Event`, it is now recommended to migrate to the extension-based usage
in [ui-extension → BackPressed Extension](../library/ui-extension.md#backpressed-extension), which is built on the official `OnBackPressedDispatcher`.

The new approach no longer requires an extra controller, interface access, or manual destroy handling,
and directly reuses the lifecycle management already provided by AndroidX.

### How to Migrate

The previous usage usually looked like this.

> The following example

```kotlin
val backPressed by lazy { BackPressedController.from(this) }

val callback = backPressed.addCallback {
    // Your code here.
}

backPressed.trigger()
backPressed.destroy()
```

Now you can directly change it to the following.

> The following example

```kotlin
val callback = addBackPressedCallback {
    // Your code here.
}
```

If you need to continue dispatching the current back event inside the current callback,
you can directly use `trigger()`.

> The following example

```kotlin
addBackPressedCallback {
    trigger()
    trigger(removed = true)
}
```

For `Fragment`, the lifecycle is bound to the current `viewLifecycleOwner` by default.

> The following example

```kotlin
addBackPressedCallback {
    // Your code here.
}
```

If you register a back pressed callback from a `View`, you can also migrate it directly like this.

> The following example

```kotlin
binding.root.addBackPressedCallback {
    // Your code here.
}
```

### Mapping

- `BackPressedController.from(activity)` → `activity.addBackPressedCallback { ... }`
- `backPressed.addCallback { ... }` → `addBackPressedCallback { ... }`
- `backPressed.trigger()` → call `trigger()` inside `BackPressedCallback`
- `backPressed.destroy()` → no longer needed, handled by the official lifecycle
- `IBackPressedController` → no longer needed