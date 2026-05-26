# Lint Rules

> To help developers discover potential problems at compile time and keep API usage consistent, `BetterAndroid` provides a built-in set of Android Lint rules.

## Rule List

The following are all Lint rules currently in effect.

### ui-component

<div class="lint-rules-table">

| Issue ID                                                                                                                                                                                              | Category      | Severity  | Priority | Brief Description                                             |
| ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ------------- | --------- | -------- | ------------------------------------------------------------- |
| [MultipleSystemBarsControllerProperties](repo://tree/main/ui-component-lint/src/main/java/com/highcapable/betterandroid/ui/component/lint/detector/MultipleSystemBarsControllerPropertiesDetector.kt) | `CORRECTNESS` | `ERROR`   | `8`      | Only one SystemBarsController property is allowed in a class. |
| [ReplaceWithNotificationComponent](repo://tree/main/ui-component-lint/src/main/java/com/highcapable/betterandroid/ui/component/lint/detector/NotificationUsageDetector.kt)                            | `USABILITY`   | `WARNING` | `5`      | Use ui-component's notification APIs instead.                 |
| [ReplaceWithSystemBarsController](repo://tree/main/ui-component-lint/src/main/java/com/highcapable/betterandroid/ui/component/lint/detector/SystemBarsControllerUsageDetector.kt)                     | `USABILITY`   | `WARNING` | `5`      | Use ui-component's SystemBarsController instead.              |

### ui-component-adapter

| Issue ID                                                                                                                                                                                         | Category    | Severity  | Priority | Brief Description                                              |
| ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ | ----------- | --------- | -------- | -------------------------------------------------------------- |
| [ReplaceWithRecyclerAdapterExtension](repo://tree/main/ui-component-adapter-lint/src/main/java/com/highcapable/betterandroid/ui/component/adapter/lint/detector/RecyclerAdapterUsageDetector.kt) | `USABILITY` | `WARNING` | `5`      | Use ui-component-adapter's RecyclerAdapter extensions instead. |
| [ReplaceWithRecyclerViewExtension](repo://tree/main/ui-component-adapter-lint/src/main/java/com/highcapable/betterandroid/ui/component/adapter/lint/detector/RecyclerViewUsageDetector.kt)       | `USABILITY` | `WARNING` | `5`      | Use ui-component-adapter's RecyclerView extensions instead.    |

### ui-extension

| Issue ID                                                                                                                                                                                 | Category    | Severity  | Priority | Brief Description                                                                                                          |
| ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ----------- | --------- | -------- | -------------------------------------------------------------------------------------------------------------------------- |
| [ReplaceWithActivityExtension](repo://tree/main/ui-extension-lint/src/main/java/com/highcapable/betterandroid/ui/extension/lint/detector/ActivityUsageDetector.kt)                       | `USABILITY` | `WARNING` | `5`      | Use ui-extension's `startActivity<T>(...)` instead.                                                                        |
| [ReplaceWithBackPressedExtension](repo://tree/main/ui-extension-lint/src/main/java/com/highcapable/betterandroid/ui/extension/lint/detector/BackPressedUsageDetector.kt)                 | `USABILITY` | `WARNING` | `5`      | Use ui-extension's back pressed extensions instead.                                                                        |
| [ReplaceWithBitmapExtension](repo://tree/main/ui-extension-lint/src/main/java/com/highcapable/betterandroid/ui/extension/lint/detector/BitmapUsageDetector.kt)                           | `USABILITY` | `WARNING` | `5`      | Use ui-extension's bitmap decode extensions instead.                                                                       |
| [ReplaceWithCoroutinesExtension](repo://tree/main/ui-extension-lint/src/main/java/com/highcapable/betterandroid/ui/extension/lint/detector/CoroutinesUsageDetector.kt)                   | `USABILITY` | `WARNING` | `5`      | Use ui-extension's coroutine extensions instead.                                                                           |
| [ReplaceWithDrawableExtension](repo://tree/main/ui-extension-lint/src/main/java/com/highcapable/betterandroid/ui/extension/lint/detector/DrawableUsageDetector.kt)                       | `USABILITY` | `WARNING` | `5`      | Use ui-extension's `Drawable.setPadding(size)` instead.                                                                    |
| [ReplaceWithFragmentExtension](repo://tree/main/ui-extension-lint/src/main/java/com/highcapable/betterandroid/ui/extension/lint/detector/FragmentUsageDetector.kt)                       | `USABILITY` | `WARNING` | `5`      | Use ui-extension's fragment manager extensions instead.                                                                    |
| [ReplaceWithHandleOnWindowInsetsChanged](repo://tree/main/ui-extension-lint/src/main/java/com/highcapable/betterandroid/ui/extension/lint/detector/WindowInsetsListenerUsageDetector.kt) | `USABILITY` | `WARNING` | `5`      | Use ui-extension's `handleOnWindowInsetsChanged(...)` instead.                                                             |
| [ReplaceWithLayoutInflaterExtension](repo://tree/main/ui-extension-lint/src/main/java/com/highcapable/betterandroid/ui/extension/lint/detector/LayoutInflaterUsageDetector.kt)           | `USABILITY` | `WARNING` | `5`      | Use ui-extension's `layoutInflater` extension instead.                                                                     |
| [ReplaceWithLifecycleExtension](repo://tree/main/ui-extension-lint/src/main/java/com/highcapable/betterandroid/ui/extension/lint/detector/LifecycleUsageDetector.kt)                     | `USABILITY` | `WARNING` | `5`      | Use ui-extension's lifecycle observer extensions instead.                                                                  |
| [ReplaceWithLifecycleOwnerExtension](repo://tree/main/ui-extension-lint/src/main/java/com/highcapable/betterandroid/ui/extension/lint/detector/LifecycleOwnerUsageDetector.kt)           | `USABILITY` | `WARNING` | `5`      | Use ui-extension's lifecycle owner extensions instead.                                                                     |
| [ReplaceWithRecyclerViewExtension](repo://tree/main/ui-extension-lint/src/main/java/com/highcapable/betterandroid/ui/extension/lint/detector/RecyclerViewUsageDetector.kt)               | `USABILITY` | `WARNING` | `5`      | Use ui-extension's `RecyclerView.layoutManager()` instead.                                                                 |
| [ReplaceWithResourcesExtension](repo://tree/main/ui-extension-lint/src/main/java/com/highcapable/betterandroid/ui/extension/lint/detector/ResourcesUsageDetector.kt)                     | `USABILITY` | `WARNING` | `5`      | Use ui-extension's resources compat extensions instead.                                                                    |
| [ReplaceWithTextViewExtension](repo://tree/main/ui-extension-lint/src/main/java/com/highcapable/betterandroid/ui/extension/lint/detector/TextViewUsageDetector.kt)                       | `USABILITY` | `WARNING` | `5`      | Use ui-extension's TextView extensions instead.                                                                            |
| [ReplaceWithToastExtension](repo://tree/main/ui-extension-lint/src/main/java/com/highcapable/betterandroid/ui/extension/lint/detector/ToastUsageDetector.kt)                             | `USABILITY` | `WARNING` | `5`      | Use ui-extension's `toast(...)` function instead of `Toast.makeText(...).show()`.                                          |
| [ReplaceWithViewBindingExtension](repo://tree/main/ui-extension-lint/src/main/java/com/highcapable/betterandroid/ui/extension/lint/detector/ViewBindingUsageDetector.kt)                 | `USABILITY` | `WARNING` | `5`      | Use ui-extension's ViewBinding extensions instead.                                                                         |
| [ReplaceWithViewExtension](repo://tree/main/ui-extension-lint/src/main/java/com/highcapable/betterandroid/ui/extension/lint/detector/ViewUsageDetector.kt)                               | `USABILITY` | `WARNING` | `5`      | Use ui-extension's `View.kt` instead.                                                                                      |
| [ReplaceWithViewImeExtension](repo://tree/main/ui-extension-lint/src/main/java/com/highcapable/betterandroid/ui/extension/lint/detector/ViewImeVisibilityUsageDetector.kt)               | `USABILITY` | `WARNING` | `5`      | Use ui-extension's `showIme()` or `hideIme()` instead.                                                                     |
| [ReplaceWithViewOutlineProviderExtension](repo://tree/main/ui-extension-lint/src/main/java/com/highcapable/betterandroid/ui/extension/lint/detector/ViewOutlineProviderUsageDetector.kt) | `USABILITY` | `WARNING` | `5`      | Use ui-extension's `outlineProvider(...)` instead.                                                                         |
| [ReplaceWithViewTooltipTextCompatExtension](repo://tree/main/ui-extension-lint/src/main/java/com/highcapable/betterandroid/ui/extension/lint/detector/ViewTooltipTextUsageDetector.kt)   | `USABILITY` | `WARNING` | `5`      | Use ui-extension's `tooltipTextCompat` instead of `TooltipCompat.setTooltipText(...)` or `ViewCompat.setTooltipText(...)`. |
| [ReplaceWithViewWalkExtension](repo://tree/main/ui-extension-lint/src/main/java/com/highcapable/betterandroid/ui/extension/lint/detector/ViewWalkUsageDetector.kt)                       | `USABILITY` | `WARNING` | `5`      | Use ui-extension's `walkToRoot()` or `walkThroughChildren()` instead.                                                      |

### system-extension

| Issue ID                                                                                                                                                                         | Category    | Severity  | Priority | Brief Description                                                           |
| -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ----------- | --------- | -------- | --------------------------------------------------------------------------- |
| [ReplaceWithAndroidVersion](repo://tree/main/system-extension-lint/src/main/java/com/highcapable/betterandroid/system/extension/lint/detector/AndroidVersionUsageDetector.kt)    | `USABILITY` | `WARNING` | `5`      | Use system-extension's `AndroidVersion` instead of `Build.VERSION.SDK_INT`. |
| [ReplaceWithApplicationExtension](repo://tree/main/system-extension-lint/src/main/java/com/highcapable/betterandroid/system/extension/lint/detector/ApplicationUsageDetector.kt) | `USABILITY` | `WARNING` | `5`      | Use system-extension's application extensions instead.                      |
| [ReplaceWithBroadcastExtension](repo://tree/main/system-extension-lint/src/main/java/com/highcapable/betterandroid/system/extension/lint/detector/BroadcastUsageDetector.kt)     | `USABILITY` | `WARNING` | `5`      | Use system-extension's broadcast extensions instead.                        |
| [ReplaceWithClipboardExtension](repo://tree/main/system-extension-lint/src/main/java/com/highcapable/betterandroid/system/extension/lint/detector/ClipboardUsageDetector.kt)     | `USABILITY` | `WARNING` | `5`      | Use system-extension's clipboard extensions instead.                        |
| [ReplaceWithIntentExtension](repo://tree/main/system-extension-lint/src/main/java/com/highcapable/betterandroid/system/extension/lint/detector/IntentUsageDetector.kt)           | `USABILITY` | `WARNING` | `5`      | Use system-extension's intent extensions instead.                           |
| [ReplaceWithServiceExtension](repo://tree/main/system-extension-lint/src/main/java/com/highcapable/betterandroid/system/extension/lint/detector/ServiceUsageDetector.kt)         | `USABILITY` | `WARNING` | `5`      | Use system-extension's service extensions instead.                          |

</div>

## How to Disable or Adjust Rules

If you want to disable a specific rule or change its severity, you can create a `lint.xml` file in the project root.

> The following example

```xml
<?xml version="1.0" encoding="UTF-8"?>
<lint>
    <issue id="ReplaceWithViewExtension" severity="ignore" />
    <issue id="ReplaceWithAndroidVersion" severity="error" />
</lint>
```

Common `severity` values are:

- `ignore`
- `warning`
- `error`
- `fatal`

You can also control them directly in Gradle.

> The following example

```kotlin
android {
    lint {
        disable += "ReplaceWithViewExtension"
        warning += "ReplaceWithToastExtension"
        error += "ReplaceWithAndroidVersion"
    }
}
```

If you only want to check part of the rules, you can also use `checkOnly`.

> The following example

```kotlin
android {
    lint {
        checkOnly += setOf(
            "ReplaceWithViewExtension",
            "ReplaceWithAndroidVersion"
        )
    }
}
```