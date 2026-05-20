# Migration Guide

> This page centrally records future breaking changes and recommended migration paths across `BetterAndroid` modules.

## Migrate BackPressedController

When handling system back pressed events, if you previously used `BackPressedController`
from `ui-component → System Event`, it is now recommended to migrate to the extension-based usage
in [ui-extension → BackPressed Extension](../library/ui-extension.md#backpressed-extension), which is built on the official `OnBackPressedDispatcher`.

The new approach no longer requires an extra controller, interface access, or manual destroy handling,
and directly reuses the lifecycle management already provided by `androidx`.

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
val callback = OnBackPressedCallback {
    // Your code here.
}

onBackPressedDispatcher.addCallback(this, callback)
```

If you need to continue dispatching the current back event inside the current callback,
you can directly use `trigger(onBackPressedDispatcher)`.

> The following example

```kotlin
onBackPressedDispatcher.addCallback(this, OnBackPressedCallback {
    trigger(onBackPressedDispatcher)
    trigger(onBackPressedDispatcher, removed = true)
})
```

For `Fragment`, you can directly use `fragment.onBackPressedDispatcher`,
then bind it to `viewLifecycleOwner` or the `Fragment` itself as needed.

> The following example

```kotlin
onBackPressedDispatcher.addCallback(viewLifecycleOwner, OnBackPressedCallback {
    // Your code here.
})
```

If you register a back pressed callback from a `View`, you can also migrate it directly like this.

> The following example

```kotlin
binding.root.onBackPressedDispatcher.addCallback(
    binding.root.requireLifecycleOwner(),
    OnBackPressedCallback {
        // Your code here.
    }
)
```

### Mapping

- `BackPressedController.from(activity)` → `activity.onBackPressedDispatcher`
- `backPressed.addCallback { ... }` → `onBackPressedDispatcher.addCallback(this, OnBackPressedCallback { ... })`
- `backPressed.trigger()` → call `trigger(onBackPressedDispatcher)` inside `OnBackPressedCallback`
- `backPressed.destroy()` → no longer needed, handled by the official lifecycle
- `IBackPressedController` → no longer needed

## Migrate SystemColors

If you previously used `SystemColors` from `ui-extension → System Colors`, please note that this entire capability has now been removed.

`ui-extension` no longer introduces the `material` dependency, so the `SystemColors` related APIs are no longer retained.

### How to Migrate

If you only used `SystemColors.isAvailable` to determine whether the current system supports dynamic colors,
you can now migrate to the official `DynamicColors.isDynamicColorAvailable()`.

> The following example

```kotlin
// Before
val isAvailable = SystemColors.isAvailable

// After
val isAvailable = DynamicColors.isDynamicColorAvailable()
```

Other than that, `SystemColors.from(context)` and all color retrieval related usages are no longer supported,
and `ui-extension` no longer provides a replacement wrapper for them.

If your business still depends on this capability, please switch to your own `material` dependency solution directly,
or reevaluate whether you still need dynamic color retrieval logic.