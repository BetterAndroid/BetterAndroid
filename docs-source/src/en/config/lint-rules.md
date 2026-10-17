# Lint Rules

> To help developers discover potential problems at compile time and keep API usage consistent, `BetterAndroid` provides a built-in set of Android Lint rules.

## Rule List

The following are all Lint rules currently in effect (Only effective for Kotlin language).

### ui-component

<div class="lint-rules-table">

| Issue ID                                                                                                                                                                                              | Category       | Severity        | Priority | Brief Description                                            |
| ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | -------------- | --------------- | -------- | ------------------------------------------------------------ |
| [MultipleSystemBarsControllerProperties](repo://tree/main/ui-component-lint/src/main/java/com/highcapable/betterandroid/ui/component/lint/detector/MultipleSystemBarsControllerPropertiesDetector.kt) | `CORRECTNESS`  | `ERROR`         | `10`     | Only one SystemBarsController property is allowed in a class |
| [ReplaceWithNotificationAction](repo://tree/main/ui-component-lint/src/main/java/com/highcapable/betterandroid/ui/component/lint/detector/NotificationActionUsageDetector.kt)                         | `PRODUCTIVITY` | `INFORMATIONAL` | `5`      | Use ui-component's notification action APIs instead          |
| [ReplaceWithNotificationComponent](repo://tree/main/ui-component-lint/src/main/java/com/highcapable/betterandroid/ui/component/lint/detector/NotificationUsageDetector.kt)                            | `PRODUCTIVITY` | `INFORMATIONAL` | `5`      | Use ui-component's notification APIs instead                 |
| [ReplaceWithSystemBarsController](repo://tree/main/ui-component-lint/src/main/java/com/highcapable/betterandroid/ui/component/lint/detector/SystemBarsControllerUsageDetector.kt)                     | `PRODUCTIVITY` | `INFORMATIONAL` | `5`      | Use ui-component's SystemBarsController instead              |

### ui-component-adapter

| Issue ID                                                                                                                                                                                         | Category       | Severity        | Priority | Brief Description                                             |
| ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ | -------------- | --------------- | -------- | ------------------------------------------------------------- |
| [ReplaceWithRecyclerAdapterExtension](repo://tree/main/ui-component-adapter-lint/src/main/java/com/highcapable/betterandroid/ui/component/adapter/lint/detector/RecyclerAdapterUsageDetector.kt) | `PRODUCTIVITY` | `INFORMATIONAL` | `5`      | Use ui-component-adapter's RecyclerAdapter extensions instead |
| [ReplaceWithRecyclerViewExtension](repo://tree/main/ui-component-adapter-lint/src/main/java/com/highcapable/betterandroid/ui/component/adapter/lint/detector/RecyclerViewUsageDetector.kt)       | `PRODUCTIVITY` | `INFORMATIONAL` | `5`      | Use ui-component-adapter's RecyclerView extensions instead    |

### ui-extension

| Issue ID                                                                                                                                                                                 | Category       | Severity        | Priority | Brief Description                                           |
| ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | -------------- | --------------- | -------- | ----------------------------------------------------------- |
| [ReplaceWithActivityExtension](repo://tree/main/ui-extension-lint/src/main/java/com/highcapable/betterandroid/ui/extension/lint/detector/ActivityUsageDetector.kt)                       | `PRODUCTIVITY` | `INFORMATIONAL` | `5`      | Use ui-extension's activity extensions instead              |
| [ReplaceWithBackPressedExtension](repo://tree/main/ui-extension-lint/src/main/java/com/highcapable/betterandroid/ui/extension/lint/detector/BackPressedUsageDetector.kt)                 | `PRODUCTIVITY` | `INFORMATIONAL` | `5`      | Use ui-extension's back pressed extensions instead          |
| [ReplaceWithBitmapExtension](repo://tree/main/ui-extension-lint/src/main/java/com/highcapable/betterandroid/ui/extension/lint/detector/BitmapUsageDetector.kt)                           | `PRODUCTIVITY` | `INFORMATIONAL` | `5`      | Use ui-extension's bitmap decode extensions instead         |
| [ReplaceWithCoroutinesExtension](repo://tree/main/ui-extension-lint/src/main/java/com/highcapable/betterandroid/ui/extension/lint/detector/CoroutinesUsageDetector.kt)                   | `PRODUCTIVITY` | `INFORMATIONAL` | `5`      | Use ui-extension's coroutine extensions instead             |
| [ReplaceWithContextExtension](repo://tree/main/ui-extension-lint/src/main/java/com/highcapable/betterandroid/ui/extension/lint/detector/ContextUsageDetector.kt)                         | `PRODUCTIVITY` | `INFORMATIONAL` | `5`      | Use ui-extension's context host activity extensions instead |
| [ReplaceWithDrawableExtension](repo://tree/main/ui-extension-lint/src/main/java/com/highcapable/betterandroid/ui/extension/lint/detector/DrawableUsageDetector.kt)                       | `PRODUCTIVITY` | `INFORMATIONAL` | `5`      | Use ui-extension's drawable extensions instead              |
| [ReplaceWithFragmentExtension](repo://tree/main/ui-extension-lint/src/main/java/com/highcapable/betterandroid/ui/extension/lint/detector/FragmentUsageDetector.kt)                       | `PRODUCTIVITY` | `INFORMATIONAL` | `5`      | Use ui-extension's fragment manager extensions instead      |
| [ReplaceWithHandleOnWindowInsetsChanged](repo://tree/main/ui-extension-lint/src/main/java/com/highcapable/betterandroid/ui/extension/lint/detector/WindowInsetsListenerUsageDetector.kt) | `PRODUCTIVITY` | `INFORMATIONAL` | `5`      | Use ui-extension's window insets extensions instead         |
| [ReplaceWithLayoutInflaterExtension](repo://tree/main/ui-extension-lint/src/main/java/com/highcapable/betterandroid/ui/extension/lint/detector/LayoutInflaterUsageDetector.kt)           | `PRODUCTIVITY` | `INFORMATIONAL` | `5`      | Use ui-extension's layout inflater extensions instead       |
| [ReplaceWithLifecycleExtension](repo://tree/main/ui-extension-lint/src/main/java/com/highcapable/betterandroid/ui/extension/lint/detector/LifecycleUsageDetector.kt)                     | `PRODUCTIVITY` | `INFORMATIONAL` | `5`      | Use ui-extension's lifecycle observer extensions instead    |
| [ReplaceWithLifecycleOwnerExtension](repo://tree/main/ui-extension-lint/src/main/java/com/highcapable/betterandroid/ui/extension/lint/detector/LifecycleOwnerUsageDetector.kt)           | `PRODUCTIVITY` | `INFORMATIONAL` | `5`      | Use ui-extension's lifecycle owner extensions instead       |
| [ReplaceWithRecyclerViewExtension](repo://tree/main/ui-extension-lint/src/main/java/com/highcapable/betterandroid/ui/extension/lint/detector/RecyclerViewUsageDetector.kt)               | `PRODUCTIVITY` | `INFORMATIONAL` | `5`      | Use ui-extension's RecyclerView extensions instead          |
| [ReplaceWithResourcesExtension](repo://tree/main/ui-extension-lint/src/main/java/com/highcapable/betterandroid/ui/extension/lint/detector/ResourcesUsageDetector.kt)                     | `PRODUCTIVITY` | `INFORMATIONAL` | `5`      | Use ui-extension's resources compat extensions instead      |
| [ReplaceWithTextViewExtension](repo://tree/main/ui-extension-lint/src/main/java/com/highcapable/betterandroid/ui/extension/lint/detector/TextViewUsageDetector.kt)                       | `PRODUCTIVITY` | `INFORMATIONAL` | `5`      | Use ui-extension's TextView extensions instead              |
| [ReplaceWithToastExtension](repo://tree/main/ui-extension-lint/src/main/java/com/highcapable/betterandroid/ui/extension/lint/detector/ToastUsageDetector.kt)                             | `PRODUCTIVITY` | `INFORMATIONAL` | `5`      | Use ui-extension's toast extensions instead                 |
| [ReplaceWithViewBindingExtension](repo://tree/main/ui-extension-lint/src/main/java/com/highcapable/betterandroid/ui/extension/lint/detector/ViewBindingUsageDetector.kt)                 | `PRODUCTIVITY` | `INFORMATIONAL` | `5`      | Use ui-extension's ViewBinding extensions instead           |
| [ReplaceWithViewExtension](repo://tree/main/ui-extension-lint/src/main/java/com/highcapable/betterandroid/ui/extension/lint/detector/ViewUsageDetector.kt)                               | `PRODUCTIVITY` | `INFORMATIONAL` | `5`      | Use ui-extension's view extensions instead                  |
| [ReplaceWithViewImeExtension](repo://tree/main/ui-extension-lint/src/main/java/com/highcapable/betterandroid/ui/extension/lint/detector/ViewImeVisibilityUsageDetector.kt)               | `PRODUCTIVITY` | `INFORMATIONAL` | `5`      | Use ui-extension's IME extensions instead                   |
| [ReplaceWithViewOutlineProviderExtension](repo://tree/main/ui-extension-lint/src/main/java/com/highcapable/betterandroid/ui/extension/lint/detector/ViewOutlineProviderUsageDetector.kt) | `PRODUCTIVITY` | `INFORMATIONAL` | `5`      | Use ui-extension's outline provider extensions instead      |
| [ReplaceWithViewTooltipTextCompatExtension](repo://tree/main/ui-extension-lint/src/main/java/com/highcapable/betterandroid/ui/extension/lint/detector/ViewTooltipTextUsageDetector.kt)   | `PRODUCTIVITY` | `INFORMATIONAL` | `5`      | Use ui-extension's tooltip text extensions instead          |
| [ReplaceWithViewWalkExtension](repo://tree/main/ui-extension-lint/src/main/java/com/highcapable/betterandroid/ui/extension/lint/detector/ViewWalkUsageDetector.kt)                       | `PRODUCTIVITY` | `INFORMATIONAL` | `5`      | Use ui-extension's view walk extensions instead             |

### system-extension

| Issue ID                                                                                                                                                                         | Category       | Severity        | Priority | Brief Description                                     |
| -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | -------------- | --------------- | -------- | ----------------------------------------------------- |
| [ReplaceWithAndroidVersion](repo://tree/main/system-extension-lint/src/main/java/com/highcapable/betterandroid/system/extension/lint/detector/AndroidVersionUsageDetector.kt)    | `PRODUCTIVITY` | `INFORMATIONAL` | `5`      | Use system-extension's AndroidVersion instead         |
| [ReplaceWithApplicationExtension](repo://tree/main/system-extension-lint/src/main/java/com/highcapable/betterandroid/system/extension/lint/detector/ApplicationUsageDetector.kt) | `PRODUCTIVITY` | `INFORMATIONAL` | `5`      | Use system-extension's application extensions instead |
| [ReplaceWithBroadcastExtension](repo://tree/main/system-extension-lint/src/main/java/com/highcapable/betterandroid/system/extension/lint/detector/BroadcastUsageDetector.kt)     | `PRODUCTIVITY` | `INFORMATIONAL` | `5`      | Use system-extension's broadcast extensions instead   |
| [ReplaceWithClipboardExtension](repo://tree/main/system-extension-lint/src/main/java/com/highcapable/betterandroid/system/extension/lint/detector/ClipboardUsageDetector.kt)     | `PRODUCTIVITY` | `INFORMATIONAL` | `5`      | Use system-extension's clipboard extensions instead   |
| [ReplaceWithIntentExtension](repo://tree/main/system-extension-lint/src/main/java/com/highcapable/betterandroid/system/extension/lint/detector/IntentUsageDetector.kt)           | `PRODUCTIVITY` | `INFORMATIONAL` | `5`      | Use system-extension's intent extensions instead      |
| [ReplaceWithServiceExtension](repo://tree/main/system-extension-lint/src/main/java/com/highcapable/betterandroid/system/extension/lint/detector/ServiceUsageDetector.kt)         | `PRODUCTIVITY` | `INFORMATIONAL` | `5`      | Use system-extension's service extensions instead     |

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

## Feedback

Currently, most Lint detectors are completed by AI Agent, and there may still be issues, such as complex UAST scenarios that have not been tested.
If you find that a Lint rule is misreporting or the Quick Fix cannot correctly fix the problem during use, you can provide an Issue ID to directly feedback to us on [GitHub Issues](repo://issues).

If you think some rules are not reasonable and overly restrictive on the code, or if you have some new rule suggestions,
please also feel free to provide feedback to us, and we will evaluate and adjust them.