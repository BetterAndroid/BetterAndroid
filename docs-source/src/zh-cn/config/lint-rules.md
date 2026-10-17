# Lint 静态检查规范

> 为了帮助开发者在编译期及时发现潜在问题、规范 API 调用，`BetterAndroid` 内置了一套 Android Lint 规则。

## 规则列表

以下是目前生效的所有 Lint 规则列表 (仅对 Kotlin 语言生效)。

### ui-component

<div class="lint-rules-table">

| Issue ID                                                                                                                                                                                              | 类别           | 级别            | 优先级 | 简要描述                                                     |
| ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | -------------- | --------------- | ------ | ------------------------------------------------------------ |
| [MultipleSystemBarsControllerProperties](repo://tree/main/ui-component-lint/src/main/java/com/highcapable/betterandroid/ui/component/lint/detector/MultipleSystemBarsControllerPropertiesDetector.kt) | `CORRECTNESS`  | `ERROR`         | `10`   | Only one SystemBarsController property is allowed in a class |
| [ReplaceWithNotificationAction](repo://tree/main/ui-component-lint/src/main/java/com/highcapable/betterandroid/ui/component/lint/detector/NotificationActionUsageDetector.kt)                         | `PRODUCTIVITY` | `INFORMATIONAL` | `5`    | Use ui-component's notification action APIs instead          |
| [ReplaceWithNotificationComponent](repo://tree/main/ui-component-lint/src/main/java/com/highcapable/betterandroid/ui/component/lint/detector/NotificationUsageDetector.kt)                            | `PRODUCTIVITY` | `INFORMATIONAL` | `5`    | Use ui-component's notification APIs instead                 |
| [ReplaceWithSystemBarsController](repo://tree/main/ui-component-lint/src/main/java/com/highcapable/betterandroid/ui/component/lint/detector/SystemBarsControllerUsageDetector.kt)                     | `PRODUCTIVITY` | `INFORMATIONAL` | `5`    | Use ui-component's SystemBarsController instead              |

### ui-component-adapter

| Issue ID                                                                                                                                                                                         | 类别           | 级别            | 优先级 | 简要描述                                                      |
| ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ | -------------- | --------------- | ------ | ------------------------------------------------------------- |
| [ReplaceWithRecyclerAdapterExtension](repo://tree/main/ui-component-adapter-lint/src/main/java/com/highcapable/betterandroid/ui/component/adapter/lint/detector/RecyclerAdapterUsageDetector.kt) | `PRODUCTIVITY` | `INFORMATIONAL` | `5`    | Use ui-component-adapter's RecyclerAdapter extensions instead |
| [ReplaceWithRecyclerViewExtension](repo://tree/main/ui-component-adapter-lint/src/main/java/com/highcapable/betterandroid/ui/component/adapter/lint/detector/RecyclerViewUsageDetector.kt)       | `PRODUCTIVITY` | `INFORMATIONAL` | `5`    | Use ui-component-adapter's RecyclerView extensions instead    |

### ui-extension

| Issue ID                                                                                                                                                                                 | 类别           | 级别            | 优先级 | 简要描述                                                    |
| ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | -------------- | --------------- | ------ | ----------------------------------------------------------- |
| [ReplaceWithActivityExtension](repo://tree/main/ui-extension-lint/src/main/java/com/highcapable/betterandroid/ui/extension/lint/detector/ActivityUsageDetector.kt)                       | `PRODUCTIVITY` | `INFORMATIONAL` | `5`    | Use ui-extension's activity extensions instead              |
| [ReplaceWithBackPressedExtension](repo://tree/main/ui-extension-lint/src/main/java/com/highcapable/betterandroid/ui/extension/lint/detector/BackPressedUsageDetector.kt)                 | `PRODUCTIVITY` | `INFORMATIONAL` | `5`    | Use ui-extension's back pressed extensions instead          |
| [ReplaceWithBitmapExtension](repo://tree/main/ui-extension-lint/src/main/java/com/highcapable/betterandroid/ui/extension/lint/detector/BitmapUsageDetector.kt)                           | `PRODUCTIVITY` | `INFORMATIONAL` | `5`    | Use ui-extension's bitmap decode extensions instead         |
| [ReplaceWithCoroutinesExtension](repo://tree/main/ui-extension-lint/src/main/java/com/highcapable/betterandroid/ui/extension/lint/detector/CoroutinesUsageDetector.kt)                   | `PRODUCTIVITY` | `INFORMATIONAL` | `5`    | Use ui-extension's coroutine extensions instead             |
| [ReplaceWithContextExtension](repo://tree/main/ui-extension-lint/src/main/java/com/highcapable/betterandroid/ui/extension/lint/detector/ContextUsageDetector.kt)                         | `PRODUCTIVITY` | `INFORMATIONAL` | `5`    | Use ui-extension's context host activity extensions instead |
| [ReplaceWithDrawableExtension](repo://tree/main/ui-extension-lint/src/main/java/com/highcapable/betterandroid/ui/extension/lint/detector/DrawableUsageDetector.kt)                       | `PRODUCTIVITY` | `INFORMATIONAL` | `5`    | Use ui-extension's drawable extensions instead              |
| [ReplaceWithFragmentExtension](repo://tree/main/ui-extension-lint/src/main/java/com/highcapable/betterandroid/ui/extension/lint/detector/FragmentUsageDetector.kt)                       | `PRODUCTIVITY` | `INFORMATIONAL` | `5`    | Use ui-extension's fragment manager extensions instead      |
| [ReplaceWithHandleOnWindowInsetsChanged](repo://tree/main/ui-extension-lint/src/main/java/com/highcapable/betterandroid/ui/extension/lint/detector/WindowInsetsListenerUsageDetector.kt) | `PRODUCTIVITY` | `INFORMATIONAL` | `5`    | Use ui-extension's window insets extensions instead         |
| [ReplaceWithLayoutInflaterExtension](repo://tree/main/ui-extension-lint/src/main/java/com/highcapable/betterandroid/ui/extension/lint/detector/LayoutInflaterUsageDetector.kt)           | `PRODUCTIVITY` | `INFORMATIONAL` | `5`    | Use ui-extension's layout inflater extensions instead       |
| [ReplaceWithLifecycleExtension](repo://tree/main/ui-extension-lint/src/main/java/com/highcapable/betterandroid/ui/extension/lint/detector/LifecycleUsageDetector.kt)                     | `PRODUCTIVITY` | `INFORMATIONAL` | `5`    | Use ui-extension's lifecycle observer extensions instead    |
| [ReplaceWithLifecycleOwnerExtension](repo://tree/main/ui-extension-lint/src/main/java/com/highcapable/betterandroid/ui/extension/lint/detector/LifecycleOwnerUsageDetector.kt)           | `PRODUCTIVITY` | `INFORMATIONAL` | `5`    | Use ui-extension's lifecycle owner extensions instead       |
| [ReplaceWithRecyclerViewExtension](repo://tree/main/ui-extension-lint/src/main/java/com/highcapable/betterandroid/ui/extension/lint/detector/RecyclerViewUsageDetector.kt)               | `PRODUCTIVITY` | `INFORMATIONAL` | `5`    | Use ui-extension's RecyclerView extensions instead          |
| [ReplaceWithResourcesExtension](repo://tree/main/ui-extension-lint/src/main/java/com/highcapable/betterandroid/ui/extension/lint/detector/ResourcesUsageDetector.kt)                     | `PRODUCTIVITY` | `INFORMATIONAL` | `5`    | Use ui-extension's resources compat extensions instead      |
| [ReplaceWithTextViewExtension](repo://tree/main/ui-extension-lint/src/main/java/com/highcapable/betterandroid/ui/extension/lint/detector/TextViewUsageDetector.kt)                       | `PRODUCTIVITY` | `INFORMATIONAL` | `5`    | Use ui-extension's TextView extensions instead              |
| [ReplaceWithToastExtension](repo://tree/main/ui-extension-lint/src/main/java/com/highcapable/betterandroid/ui/extension/lint/detector/ToastUsageDetector.kt)                             | `PRODUCTIVITY` | `INFORMATIONAL` | `5`    | Use ui-extension's toast extensions instead                 |
| [ReplaceWithViewBindingExtension](repo://tree/main/ui-extension-lint/src/main/java/com/highcapable/betterandroid/ui/extension/lint/detector/ViewBindingUsageDetector.kt)                 | `PRODUCTIVITY` | `INFORMATIONAL` | `5`    | Use ui-extension's ViewBinding extensions instead           |
| [ReplaceWithViewExtension](repo://tree/main/ui-extension-lint/src/main/java/com/highcapable/betterandroid/ui/extension/lint/detector/ViewUsageDetector.kt)                               | `PRODUCTIVITY` | `INFORMATIONAL` | `5`    | Use ui-extension's view extensions instead                  |
| [ReplaceWithViewImeExtension](repo://tree/main/ui-extension-lint/src/main/java/com/highcapable/betterandroid/ui/extension/lint/detector/ViewImeVisibilityUsageDetector.kt)               | `PRODUCTIVITY` | `INFORMATIONAL` | `5`    | Use ui-extension's IME extensions instead                   |
| [ReplaceWithViewOutlineProviderExtension](repo://tree/main/ui-extension-lint/src/main/java/com/highcapable/betterandroid/ui/extension/lint/detector/ViewOutlineProviderUsageDetector.kt) | `PRODUCTIVITY` | `INFORMATIONAL` | `5`    | Use ui-extension's outline provider extensions instead      |
| [ReplaceWithViewTooltipTextCompatExtension](repo://tree/main/ui-extension-lint/src/main/java/com/highcapable/betterandroid/ui/extension/lint/detector/ViewTooltipTextUsageDetector.kt)   | `PRODUCTIVITY` | `INFORMATIONAL` | `5`    | Use ui-extension's tooltip text extensions instead          |
| [ReplaceWithViewWalkExtension](repo://tree/main/ui-extension-lint/src/main/java/com/highcapable/betterandroid/ui/extension/lint/detector/ViewWalkUsageDetector.kt)                       | `PRODUCTIVITY` | `INFORMATIONAL` | `5`    | Use ui-extension's view walk extensions instead             |

### system-extension

| Issue ID                                                                                                                                                                         | 类别           | 级别            | 优先级 | 简要描述                                              |
| -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | -------------- | --------------- | ------ | ----------------------------------------------------- |
| [ReplaceWithAndroidVersion](repo://tree/main/system-extension-lint/src/main/java/com/highcapable/betterandroid/system/extension/lint/detector/AndroidVersionUsageDetector.kt)    | `PRODUCTIVITY` | `INFORMATIONAL` | `5`    | Use system-extension's AndroidVersion instead         |
| [ReplaceWithApplicationExtension](repo://tree/main/system-extension-lint/src/main/java/com/highcapable/betterandroid/system/extension/lint/detector/ApplicationUsageDetector.kt) | `PRODUCTIVITY` | `INFORMATIONAL` | `5`    | Use system-extension's application extensions instead |
| [ReplaceWithBroadcastExtension](repo://tree/main/system-extension-lint/src/main/java/com/highcapable/betterandroid/system/extension/lint/detector/BroadcastUsageDetector.kt)     | `PRODUCTIVITY` | `INFORMATIONAL` | `5`    | Use system-extension's broadcast extensions instead   |
| [ReplaceWithClipboardExtension](repo://tree/main/system-extension-lint/src/main/java/com/highcapable/betterandroid/system/extension/lint/detector/ClipboardUsageDetector.kt)     | `PRODUCTIVITY` | `INFORMATIONAL` | `5`    | Use system-extension's clipboard extensions instead   |
| [ReplaceWithIntentExtension](repo://tree/main/system-extension-lint/src/main/java/com/highcapable/betterandroid/system/extension/lint/detector/IntentUsageDetector.kt)           | `PRODUCTIVITY` | `INFORMATIONAL` | `5`    | Use system-extension's intent extensions instead      |
| [ReplaceWithServiceExtension](repo://tree/main/system-extension-lint/src/main/java/com/highcapable/betterandroid/system/extension/lint/detector/ServiceUsageDetector.kt)         | `PRODUCTIVITY` | `INFORMATIONAL` | `5`    | Use system-extension's service extensions instead     |

</div>

## 如何关闭或调整规则

如果你想关闭某一条规则，或将某一条规则调整为其它级别，可以在项目根目录创建 `lint.xml`。

> 示例如下

```xml
<?xml version="1.0" encoding="UTF-8"?>
<lint>
    <issue id="ReplaceWithViewExtension" severity="ignore" />
    <issue id="ReplaceWithAndroidVersion" severity="error" />
</lint>
```

其中 `severity` 常用值如下：

- `ignore`
- `warning`
- `error`
- `fatal`

你也可以直接在 Gradle 中进行控制。

> 示例如下

```kotlin
android {
    lint {
        disable += "ReplaceWithViewExtension"
        warning += "ReplaceWithToastExtension"
        error += "ReplaceWithAndroidVersion"
    }
}
```

当你只想检查部分规则时，也可以使用 `checkOnly`。

> 示例如下

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

## 问题反馈

目前大部分 Lint 检测器均由 AI Agent 代为完成，可能仍然存在问题，例如尚未测试过的复杂 UAST 语法树场景。如果你在使用过程中发现了 Lint 规则出现了误报或 Quick Fix 无法正确修复问题，可以通过提供 Issue ID 在 [GitHub Issues](repo://issues) 直接向我们反馈。

如果你认为一些规则不够合理出现过于约束代码的情况，或者你有一些新的规则建议，也欢迎向我们反馈，我们会进行评估和调整。