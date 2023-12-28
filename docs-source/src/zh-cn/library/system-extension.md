# system-extension

![GitHub release](https://img.shields.io/github/v/release/BetterAndroid/BetterAndroid?display_name=release&logo=github&color=green&filter=system-extension-*)
<span style="margin-left: 5px"/>
![Android Min SDK](https://img.shields.io/badge/Min%20SDK-21-orange?logo=android)

这是针对系统层相关功能扩展的一个依赖。

## 配置依赖

你可以使用如下方式将此模块添加到你的项目中。

### SweetDependency (推荐)

在你的项目 `SweetDependency` 配置文件中添加依赖。

```yaml
libraries:
  com.highcapable.betterandroid:
    system-extension:
      version: +
```

在你的项目 `build.gradle.kts` 中配置依赖。

```kotlin
implementation(com.highcapable.betterandroid.system.extension)
```

### 传统方式

在你的项目 `build.gradle.kts` 中配置依赖。

```kotlin
implementation("com.highcapable.betterandroid:system-extension:<version>")
```

请将 `<version>` 修改为此文档顶部显示的版本。

## 功能介绍

你可以 [点击这里](kdoc://system-extension) 查看 KDoc。

### Application 扩展

::: tip 本节内容

[PackageInfoFlagsWrapper](kdoc://system-extension/system-extension/com.highcapable.betterandroid.system.extension.component/-package-info-flags-wrapper)

`PackageInfo` 的 `flags` 属性包装类。

[ApplicationInfoFlagsWrapper](kdoc://system-extension/system-extension/com.highcapable.betterandroid.system.extension.component/-application-info-flags-wrapper)

`ApplicationInfo` 的 `flags` 属性包装类。

[ApplicationFactory → getComponentName](kdoc://system-extension/system-extension/com.highcapable.betterandroid.system.extension.component/get-component-name)

[ApplicationFactory → hasPackage](kdoc://system-extension/system-extension/com.highcapable.betterandroid.system.extension.component/has-package)

[ApplicationFactory → hasLaunchActivity](kdoc://system-extension/system-extension/com.highcapable.betterandroid.system.extension.component/has-launch-activity)

[ApplicationFactory → getPackageInfo](kdoc://system-extension/system-extension/com.highcapable.betterandroid.system.extension.component/get-package-info)

[ApplicationFactory → getPackageInfoOrNull](kdoc://system-extension/system-extension/com.highcapable.betterandroid.system.extension.component/get-package-info-or-null)

[ApplicationFactory → getInstalledPackages](kdoc://system-extension/system-extension/com.highcapable.betterandroid.system.extension.component/get-installed-packages)

[ApplicationFactory → getInstalledPackagesOrNull](kdoc://system-extension/system-extension/com.highcapable.betterandroid.system.extension.component/get-installed-packages-or-null)

[ApplicationFactory → queryLaunchActivitiesForPackage](kdoc://system-extension/system-extension/com.highcapable.betterandroid.system.extension.component/query-launch-activities-for-package)

[ApplicationFactory → queryLaunchActivitiesForPackageOrNull](kdoc://system-extension/system-extension/com.highcapable.betterandroid.system.extension.component/query-launch-activities-for-package-or-null)

[ApplicationFactory → isComponentEnabled](kdoc://system-extension/system-extension/com.highcapable.betterandroid.system.extension.component/is-component-enabled)

[ApplicationFactory → enableComponent](kdoc://system-extension/system-extension/com.highcapable.betterandroid.system.extension.component/enable-component)

[ApplicationFactory → disableComponent](kdoc://system-extension/system-extension/com.highcapable.betterandroid.system.extension.component/disable-component)

[ApplicationFactory → resetComponent](kdoc://system-extension/system-extension/com.highcapable.betterandroid.system.extension.component/reset-component)

[ApplicationFactory → versionCodeCompat](kdoc://system-extension/system-extension/com.highcapable.betterandroid.system.extension.component/version-code-compat)

[ApplicationFactory → primaryCpuAbi](kdoc://system-extension/system-extension/com.highcapable.betterandroid.system.extension.component/primary-cpu-abi)

[ApplicationFactory → secondaryCpuAbi](kdoc://system-extension/system-extension/com.highcapable.betterandroid.system.extension.component/secondary-cpu-abi)

[ApplicationFactory → hasFlags](kdoc://system-extension/system-extension/com.highcapable.betterandroid.system.extension.component/has-flags)

适用于 `Application` 的相关扩展。

:::

`BetterAndroid` 为 `PackageManager`、`PackageInfo`、`ApplicationInfo` 等功能提供了扩展功能，你能够更加方便地使用这些功能。

它们被统一归类为 Application 扩展，意为应用程序的相关功能。

下面是这些扩展功能的使用方式及示例。

通过泛型的方式获取一个组件类的 `ComponentName`。

> 示例如下

```kotlin
// 假设这就是你的 Context
val context: Context
// 获取 MainActivity 的 ComponentName
val componentName = context.getComponentName<MainActivity>()
```

判断应用程序是否已经安装。

> 示例如下

```kotlin
// 假设这就是你的 Context
val context: Context
// 例如，判断 Chrome 是否已经安装
val hasChrome = context.packageManager.hasPackage("com.android.chrome")
```

判断应用程序是否存在可启动的 `Activity`。

此功能主要用于判断应用程序是否存在一个能从桌面启动的 `Activity`。

> 示例如下

```kotlin
// 假设这就是你的 Context
val context: Context
// 例如，判断 com.mydemo.test 应用程序
val hasLaunchActivity = context.packageManager.hasLaunchActivity("com.mydemo.test")
```

::: warning

从 Android 10 开始，即使应用程序没有 `Activity` 声明 `ACTION_MAIN` 和 `CATEGORY_LAUNCHER`，其图标也可能会显示在桌面上，点击会打开应用信息界面，但是这并不代表它存在可启动的 `Activity`。

:::

获取应用程序包信息。

`BetterAndroid` 为 `getPackageInfo` 提供了一个完全同名的重载方法，你无需考虑兼容性问题，使用 `PackageInfoFlagsWrapper` 作为 `flags` 的参数即可。

重载这个方法的原因是出自 Android 13 中，官方将 `Int` 类型 `flags` 的方法作废并启用了一套新方案，但是并没有提供任何兼容处理工具，后期却又在 Android 14 中取消作废，这会给开发者带来极大的困扰。

> 示例如下

```kotlin
// 假设这就是你的 Context
val context: Context
// 例如，获取 Chrome 的 PackageInfo
val packageInfo = context.packageManager.getPackageInfo("com.android.chrome")
// 你可以在第二位参数传入一个或多个 PackageInfoFlagsWrapper 对象来设置 flags，而不是位运算的方式
// PackageInfoFlagsWrapper 是对所有 PackageInfo 的 flags 的一个包装镜像
val packageInfo = context.packageManager
    .getPackageInfo("com.android.chrome", PackageInfoFlagsWrapper.GET_META_DATA)
```

::: tip

在不确定 `PackageInfo` 能否获取成功时，你可以将获取的方法替换为 `getPackageInfoOrNull`，这样在获取失败时将会返回 `null` 而不是抛出异常。

:::

获取已安装的应用程序包信息列表。

`BetterAndroid` 同样为 `getInstalledPackages` 提供了一个完全同名的重载方法，你无需考虑兼容性问题，使用 `PackageInfoFlagsWrapper` 作为 `flags` 的参数即可。

> 示例如下

```kotlin
// 假设这就是你的 Context
val context: Context
// 获取所有已安装的应用程序包信息列表
val packageInfos = context.packageManager.getInstalledPackages()
// 同样地，你可以在第二位参数传入一个或多个 PackageInfoFlagsWrapper 对象来设置 flags
val packageInfos = context.packageManager.getInstalledPackages(PackageInfoFlagsWrapper.GET_META_DATA)
```

::: tip

同样地，在不确定 `PackageInfo` 能否获取成功时，你可以将获取的方法替换为 `getInstalledPackagesOrNull`。

:::

查询应用程序的所有可启动 `Activity`。

此方法基于 `queryIntentActivities` 和 `getLaunchIntentForPackage` 实现，简化了获取过程。

> 示例如下

```kotlin
// 假设这就是你的 Context
val context: Context
// 例如，查询 Chrome 的所有可启动 Activity
val launchActivities = context.packageManager.queryLaunchActivitiesForPackage("com.android.chrome")
```

::: tip

同样地，在不确定 `ResolveInfo` 能否获取成功时，你可以将获取的方法替换为 `queryLaunchActivitiesForPackageOrNull`。

:::

判断应用程序声明的组件是否启用或处于默认状态。

`BetterAndroid` 封装了 `getComponentEnabledSetting` 方法，你可以使用以下方法来更快地对组件状态做出判断。

默认状态为应用程序自身在 `AndroidManifest.xml` 中声明的状态，如果没有声明则为启用状态。

> 示例如下

```kotlin
// 假设这就是你的 Context
val context: Context
// 获取 MainActivity 的 ComponentName
val mainComponent = context.getComponentName<MainActivity>()
// 判断 MainActivity 是否启用
val isEnabled = context.packageManager.isComponentEnabled(mainComponent)
```

启用、禁用或重置应用程序声明的组件。

`BetterAndroid` 封装了 `setComponentEnabledSetting` 方法，你可以使用以下方法来更快地完成这个操作。

重置操作将会重置为默认状态，即应用程序自身在 `AndroidManifest.xml` 中声明的状态，如果没有声明则为启用状态。

> 示例如下

```kotlin
// 假设这就是你的 Context
val context: Context
// 获取 MainActivity 的 ComponentName
val mainComponent = context.getComponentName<MainActivity>()
// 启用 MainActivity
context.packageManager.enableComponent(mainComponent)
// 禁用 MainActivity
context.packageManager.disableComponent(mainComponent)
// 重置 MainActivity
context.packageManager.resetComponent(mainComponent)
```

::: danger

你的应用程序除非处于系统用户组，否则没有权限启用或禁用其它应用程序的组件。

:::

获取应用程序的版本号 (Version Code)。

`longVersionCode` 是 Android 9 中引入的新功能，它是 `versionCode` 的扩容版本，用于解决 `versionCode` 位数不足的问题。

由于 `versionCode` 已被标记为作废状态，且开发者使用 `androidx` 提供的 `PackageInfoCompat.getLongVersionCode` 显得过于繁琐，这个方法也基本上不好找到，两个版本号的使用方法同时存在也会给开发者带来困扰。

出于此目的，`BetterAndroid` 封装了关于版本号的兼容实现，你现在无需考虑 `versionCode` 和 `longVersionCode`，你可以直接使用 `versionCodeCompat` 来获取应用程序的版本号，它的类型将始终保持为 `Long`。

> 示例如下

```kotlin
// 假设这就是你的 Context
val context: Context
// 例如，获取 Chrome 的版本号
val versionCode = context.packageManager.getPackageInfo("com.android.chrome").versionCodeCompat
```

获取应用程序的 CPU ABI 名称。

这是一个隐藏的 API，所以 `BetterAndroid` 通过反射的方式进行获取，你可能会在某些特定的场景中需要使用它。

> 示例如下

```kotlin
// 假设这就是你的 Context
val context: Context
// 例如，获取 Chrome 的 PackageInfo
val packageInfo = context.packageManager.getPackageInfo("com.android.chrome")
// 获取主 CPU ABI 名称
val primaryCpuAbi = packageInfo.applicationInfo.primaryCpuAbi
// 获取次 CPU ABI 名称
val secondaryCpuAbi = packageInfo.applicationInfo.secondaryCpuAbi
```

判断 `ApplicationInfo` 是否包含指定的 `flags`。

`BetterAndroid` 封装了通过位运算的方式判断 `flags` 的方法，你可以使用以下方法来更快地完成这个操作。

> 示例如下

```kotlin
// 假设这就是你的 Context
val context: Context
// 例如，获取 Chrome 的 ApplicationInfo
val applicationInfo = context.packageManager.getPackageInfo("com.android.chrome").applicationInfo
// 判断 Chrome 是否是系统应用
// 你可以传入一个或多个 ApplicationInfoFlagsWrapper 对象来设置 flags，而不是位运算的方式
// ApplicationInfoFlagsWrapper 是对所有 ApplicationInfo 的 flags 的一个包装镜像
// 为了便于阅读，所有的 flags 在 ApplicationInfoFlagsWrapper 中都移除了 FLAG 前缀
val isSystemApp = applicationInfo.hasFlags(ApplicationInfoFlagsWrapper.SYSTEM)
```

::: warning

在上述所有功能中，如果涉及查询自身应用程序以外的软件包行为时，在 Android 11 及之后需要 `QUERY_ALL_PACKAGES` 权限或明确配置一个 `queries` 属性列表。

请参考 [Package visibility filtering on Android](https://developer.android.com/training/package-visibility)。

:::

### 广播 (Broadcast) 扩展

::: tip 本节内容

[BroadcastFactory → registerReceiver](kdoc://system-extension/system-extension/com.highcapable.betterandroid.system.extension.component/register-receiver)

[BroadcastFactory → sendBroadcast](kdoc://system-extension/system-extension/com.highcapable.betterandroid.system.extension.component/send-broadcast)

适用于广播 (Broadcast) 的相关扩展。

:::

广播 (Broadcast) 是 Android 系统中非常重要的一个功能，它能够让应用程序之间进行通信。

`BetterAndroid` 为广播 (Broadcast) 提供了一个在运行时动态注册的解决方案，你能更简单地发送广播和创建 `BoardcastReceiver`。

你可以使用以下方式发送、接收无序广播而无需在 `AndroidManifest.xml` 中声明。

例如，我们要给 `com.example.app` 发送一个广播。

> 示例如下

```kotlin
// 假设这就是你的 Context
val context: Context
// 发送无序广播给 com.example.app
context.sendBroadcast("com.example.app") {
    // 设置 Action
    action = "com.example.app.action.KNOCK"
    // 传递一个字符串参数
    putExtra("greetings", "Hey you!")
}
// 你可以不指定接收者的包名，
// 这样将会被所有添加了以下 Action 的接收器接收
context.sendBroadcast {
    // 设置 Action
    action = "com.example.app.action.KNOCK"
    // 传递一个字符串参数
    putExtra("greetings", "Hey there!")
}
```

在 `com.example.app` 中，我们可以这样来接收这个广播。

> 示例如下

```kotlin
// 假设这就是你的 Context
val context: Context
// 创建意图过滤器
val filter = IntentFilter().apply {
    // 指定发送者的 Action
    addAction("com.example.app.action.KNOCK")
}
// 注册广播接收器
// 这里的回调为 onReceive 方法，是同步的 (主线程)
context.registerReceiver(filter) { context, intent ->
    // 获取传递的字符串参数
    val greetings = intent.getStringExtra("greetings")
    // 使用 Toast 展示接收到的参数
    context.toast(greetings)
}
```

::: tip

你可以在 `registerReceiver` 方法中设置 `exported` 参数 (默认为 `true`) 决定当前广播接收器是否需要添加 `Context.RECEIVER_EXPORTED`，如果你的广播不对外开放，你可以将其设置为 `false`。

:::

::: warning

在 Android 14 或更高版本中，运行时注册的广播接收器必须明确导出行为才能接收来自另一个应用程序的广播，如果当前目标 Android 版本为 14 及以上，
你必须确保 `exported` 参数为 `true` 才能接收来自另一个应用程序的广播，否则会直接抛出异常。

请参考 [Runtime-registered broadcasts receivers must specify export behavior](https://developer.android.com/about/versions/14/behavior-changes-14#runtime-receivers-exported)。

:::

### 剪贴板 (Clipboard) 扩展

::: tip 本节内容

[ClipDataItemBuilder](kdoc://system-extension/system-extension/com.highcapable.betterandroid.system.extension.component/-clip-data-item-builder)

`ClipData.Item` 构建器。

[ClipboardFactory → clipboardManager](kdoc://system-extension/system-extension/com.highcapable.betterandroid.system.extension.component/clipboard-manager)

[ClipboardFactory → copy](kdoc://system-extension/system-extension/com.highcapable.betterandroid.system.extension.component/copy)

[ClipboardFactory → listOfItems](kdoc://system-extension/system-extension/com.highcapable.betterandroid.system.extension.component/list-of-items)

[ClipboardFactory → ClipData](kdoc://system-extension/system-extension/com.highcapable.betterandroid.system.extension.component/-clip-data)

适用于剪贴板 (Clipboard) 的相关扩展。

:::

剪贴板是在应用程序开发中经常会用到的且非常重要的一个功能，但是它的使用方式不是很友好。

你需要使用 `getSystemService` 获取 `ClipboardManager`，然后再使用 `setPrimaryClip` 设置剪贴板内容，当然你也可以使用 `getPrimaryClip` 读取剪贴板内容。

有时候我们只需要设置或读取一个字符串，但是这些操作却需要写很多代码，这对于开发者来说是非常不友好的。

为此 `BetterAndroid` 为剪贴板 (Clipboard) 提供了一个更简单的解决方案，你可以直接使用以下方法来设置或读取剪贴板内容。

读取剪贴板中的内容。

现在，你可以直接将 `ClipData.getItemAt` 和 `ClipData.getItemCount` 替换为 `ClipData.listOfItems`。

这个方法将会返回一个 `List<ClipData.Item>`，你可以使用 `firstOrNull` 来获取第一个 `ClipData.Item`，或者使用 `isEmpty` 来直接判断剪贴板中是否存在内容，它的好处就是不再需要去考虑数组是不是会越界的问题。

> 示例如下

```kotlin
// 假设这就是你的 Context
val context: Context
// 获取剪贴板管理器
val manager = context.clipboardManager
// 获取剪贴板中第一个 ClipData.Item
// 通常情况下，你只需要获取第一个对象即可
val clipItem = manager.primaryClip?.listOfItems()?.firstOrNull()
// 获取已复制的文本
val copiedText = clipItem?.text
```

复制一条文本到剪贴板。

你不需要再去写 `setPrimaryClip(ClipData.newPlainText("Lable", "Text"))`，复制一条文本就应该简简单单。

> 示例如下

```kotlin
// 假设这就是你的 Context
val context: Context
// 复制一条文本到剪贴板
context.clipboardManager.copy("Hello World!")
// 为这个文本添加一个标签
context.clipboardManager.copy("Hello World!", "MyText")
```

复制 HTML 类型的文本、`Uri`、`Intent` 到剪贴板。

无论复制什么内容，你都可以使用 `copy` 方法来完成。

> 示例如下

```kotlin
// 假设这就是你的 Context
val context: Context
// 复制 HTML 类型的文本到剪贴板
context.clipboardManager.copy("Hello World!", "<b>Hello World!</b>")
// 复制 Uri 到剪贴板
context.clipboardManager.copy(Uri.parse("some://uri"), context.contentResolver)
// 复制 Intent 到剪贴板
context.clipboardManager.copy(Intent(Intent.ACTION_VIEW, Uri.parse("some://uri")))
```

复制自定义的 `ClipData` 内容到剪贴板。

你可以使用 `ClipData` 方法创建一个新的 `ClipData` 对象，然后复制到剪贴板。

> 示例如下

```kotlin
// 假设这就是你的 Context
val context: Context
// 创建 ClipData 对象
val clipData = ClipData {
    addText("Hello World!")
    addHtmlText("Hello World!", "<b>Hello World!</b>")
    addUri(Uri.parse("some://uri"), context.contentResolver)
    addIntent(Intent(Intent.ACTION_VIEW, Uri.parse("some://uri")))
}
// 复制到剪贴板
context.clipboardManager.copy(clipData)
```

::: warning

在 Android 10 或更高版本中，当应用程序处于后台时，除非你的应用程序是输入法 (IME)，否则无法读取剪贴板中的内容。

请参考 [Limited access to clipboard data](https://developer.android.com/about/versions/10/privacy/changes#clipboard-data)。

:::

### 意图 (Intent) 扩展

::: tip 本节内容

[IntentFactory → getSerializableExtraCompat](kdoc://system-extension/system-extension/com.highcapable.betterandroid.system.extension.component/get-serializable-extra-compat)

[IntentFactory → getSerializableCompat](kdoc://system-extension/system-extension/com.highcapable.betterandroid.system.extension.component/get-serializable-compat)

[IntentFactory → getParcelableExtraCompat](kdoc://system-extension/system-extension/com.highcapable.betterandroid.system.extension.component/get-parcelable-extra-compat)

[IntentFactory → getParcelableCompat](kdoc://system-extension/system-extension/com.highcapable.betterandroid.system.extension.component/get-parcelable-compat)

适用于 `Intent` 的相关扩展。

:::

目前，`Intent` 中的扩展方法仅用于处理 `Serializable` 和 `Parcelable` 类型的获取方法在 Android 13 中被标记为作废且官方未提供任何有效的兼容处理方式问题。

你可以使用 `BetterAndroid` 提供的兼容性处理方法来获取 `Serializable` 和 `Parcelable` 类型的数据。

> 示例如下

```kotlin
// 假设这就是你的 Intent
val intent: Intent
// 获取 Serializable 类型的数据
val myData = intent.getSerializableExtraCompat<MyData>("my_key_name")
val myData = intent.extras?.getSerializableCompat<MyData>("my_key_name")
// 获取 Parcelable 类型的数据
val myData = intent.getParcelableExtraCompat<MyData>("my_key_name")
val myData = intent.extras?.getParcelableCompat<MyData>("my_key_name")
```

以下是原始方法与兼容性处理方法的对照表。

| 原始方法                      | 兼容性处理方法                      |
| ----------------------------- | ----------------------------------- |
| `Intent.getSerializableExtra` | `Intent.getSerializableExtraCompat` |
| `Bundle.getSerializable`      | `Bundle.getSerializableCompat`      |
| `Intent.getParcelableExtra`   | `Intent.getParcelableExtraCompat`   |
| `Bundle.getParcelable`        | `Bundle.getParcelableCompat`        |

### 服务 (Service) 扩展

::: tip 本节内容

[ServiceFactory → startService](kdoc://system-extension/system-extension/com.highcapable.betterandroid.system.extension.component/start-service)

[ServiceFactory → startForegroundService](kdoc://system-extension/system-extension/com.highcapable.betterandroid.system.extension.component/start-foreground-service)

[ServiceFactory → startServiceOrElse](kdoc://system-extension/system-extension/com.highcapable.betterandroid.system.extension.component/start-service-or-else)

[ServiceFactory → startForegroundServiceOrElse](kdoc://system-extension/system-extension/com.highcapable.betterandroid.system.extension.component/start-foreground-service-or-else)

适用于 `Service` 的相关扩展。

:::

与启动 `Activity` 的方式类似，我们需要启动一个 `Service` 时，需要使用 `Intent` 创建一个 `Intent(this, MyService::class.java)`，然后调用 `startService(intent)` 来启动。

这样写起来大概不太友好，于是 `BetterAndroid` 为 `Service` 提供了扩展，现在你可以直接使用以下方式来启动一个 `Service`。

> 示例如下

```kotlin
// 假设这就是你的 Context
val context: Context
// 假设 MyService 就是你的目标 Service
context.startService<MyService>()
// 你可以使用以下方式创建一个 Intent 对象
context.startService<MyService> {
    // 在这里添加一些额外的参数
    putExtra("key", "value")
}
```

如果你需要启动一个外部应用程序的 `Service`，你可以使用以下方式。

> 示例如下

```kotlin
// 假设这就是你的 Context
val context: Context
// 假设你需要启动的应用程序包名为 com.example.app
// 假设你需要启动的 Service 类名为 com.example.app.MyService
context.startService("com.example.app", "com.example.app.MyService")
// 你依然可以使用以下方式创建一个 Intent 对象
context.startService("com.example.app", "com.example.app.MyService") {
    // 在这里添加一些额外的参数
    putExtra("key", "value")
}
```

::: tip

你可以使用 `startServiceOrElse` 及 `startForegroundServiceOrElse` 来代替 `startService` 及 `startForegroundService` 以判断 `Service` 是否能够启动成功，启动失败时此方法不会抛出异常，而是返回 `false`。

:::

::: warning

在 Android 8 或更高版本中，在一些情况下，你可能需要使用 `startForegroundService` 来启动一个前台服务。

请参考 [Background Execution Limits](https://developer.android.com/about/versions/oreo/background)。

:::

### 系统信息

::: tip 本节内容

[SystemVersion](kdoc://system-extension/system-extension/com.highcapable.betterandroid.system.extension.tool/-system-version)

系统版本工具。

[SystemKind](kdoc://system-extension/system-extension/com.highcapable.betterandroid.system.extension.tool/-system-kind)

系统种类工具。

[SystemProperties](kdoc://system-extension/system-extension/com.highcapable.betterandroid.system.extension.tool/-system-properties)

Android 的 `SystemProperties` 工具。

:::

也许你已经厌烦了代码中漂流各地的 `Build.VERSION.SDK_INT`、`Build.VERSION_CODES`，那么从现在开始，你不再需要使用它们了。

`BetterAndroid` 为你准备了更加简便的写法来取代它们。

在之前，我们需要判断当前系统的 API Level (Android API 等级)，基本上都会通过以下方式进行。

> 示例如下

```kotlin
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
    // 执行相关代码
}
// 或者使用硬编码 API 版本号的方式
if (Build.VERSION.SDK_INT >= 29) {
    // 执行相关代码
}
```

现在，你可以通过以下方式非常简单地完成这个操作。

> 示例如下

```kotlin
SystemVersion.require(SystemVersion.Q) {
    // 执行相关代码
}
// 或者使用硬编码 API 版本号的方式
SystemVersion.require(29) {
    // 执行相关代码
}
// result 在 API 大于等于 29 时将会得到 "target"，否则为 "legacy"
val result = SystemVersion.require(SystemVersion.Q, "legacy") { "target" }
// 如果是一个可为 null 的结果，你可以使用以下方式
val myData: MyData?
val result = SystemVersion.requireOrNull(SystemVersion.Q, MyData()) { myData }
```

你也可以使用以下方式进行判断。

> 示例如下

```kotlin
// 判断 API 是否小于 29
if (SystemVersion.isLowTo(SystemVersion.Q)) {
    // 执行相关代码
}
// 判断 API 是否大于 29
if (SystemVersion.isHighTo(SystemVersion.Q)) {
    // 执行相关代码
}
// 判断 API 是否小于等于 29
if (SystemVersion.isLowOrEqualsTo(SystemVersion.Q)) {
    // 执行相关代码
}
// 判断 API 是否大于等于 29
if (SystemVersion.isHighOrEqualsTo(SystemVersion.Q)) {
    // 执行相关代码
}
// 判断 API 在 26 与 29 之间
if (SystemVersion.isBetween(SystemVersion.O..SystemVersion.Q)) {
    // 执行相关代码
}
```

以下是各个 API 的常量映射对照表，在 Android 版本更新后，`BetterAndroid` 会同步更新这些常量。

| API 等级 | `SystemVersion` 名称 | `Build.VERSION_CODES` 名称 | 对应系统版本      |
| -------- | -------------------- | -------------------------- | ----------------- |
| 19       | `K`                  | `KITKAT`                   | 4.4.3、4.4.4      |
| 20       | `K_W`                | `KITKAT_WATCH`             | 4.4W              |
| 21       | `L`                  | `LOLLIPOP`                 | 5.0、5.0.2        |
| 22       | `L_MR1`              | `LOLLIPOP_MR1`             | 5.1、5.1.1        |
| 23       | `M`                  | `M`                        | 6.0、6.0.1        |
| 24       | `N`                  | `N`                        | 7.0               |
| 25       | `N_MR1`              | `N_MR1`                    | 7.1、7.1.1、7.1.2 |
| 26       | `O`                  | `O`                        | 8.0               |
| 27       | `O_MR1`              | `O_MR1`                    | 8.1               |
| 28       | `P`                  | `P`                        | 9                 |
| 29       | `Q`                  | `Q`                        | 10                |
| 30       | `R`                  | `R`                        | 11                |
| 31       | `S`                  | `S`                        | 12                |
| 32       | `S_V2`               | `S_V2`                     | 12.1、12L         |
| 33       | `T`                  | `TIRAMISU`                 | 13                |
| 34       | `U`                  | `UPSIDE_DOWN_CAKE`         | 14                |

随着各个厂商相继发布的自家品牌 Android 手机深度定制的 Android 系统越来越多，有时候我们非常有必要针对各个定制版本系统不同的功能去进行定向适配，但是如何判断这些系统的种类就是一个很大的问题。

通常情况下大家的解决方案都是去判断设备的型号从而确定是哪种定制系统，但是如果当前设备运行的不是你所判断的那种定制系统，例如用户自行刷机的案例，那么这种方案就会失效。

`BetterAndroid` 通过收集各种各样常见定制系统的对应特征，为你提供了一个简单、快速、高效的解决方案。

下面是判断当前系统种类的一个简单示例。

> 示例如下

```kotlin
// 判断当前系统种类是否为 MIUI
if (SystemKind.equals(SystemKind.MIUI)) {
    // 执行相关代码
}
```

没错，就是这么简单，如果你需要同时判断多个系统种类，你还可以使用以下方式。

> 示例如下

```kotlin
// 获取当前系统种类
val kind = SystemKind.get()
// 批量判断当前系统种类
when (kind) {
    SystemKind.MIUI -> {
        // 执行相关代码
    }
    SystemKind.COLOROS -> {
        // 执行相关代码
    }
    SystemKind.ORIGINOS -> {
        // 执行相关代码
    }
}
```

以下是目前收集的系统种类的常量对照表，如果你有更多系统种类的特征，欢迎 PR 或前往 [GitHub Issues](repo://issues) 向我们提出建议。

| `SystemKind` 名称 | 系统种类                                              |
| ----------------- | ----------------------------------------------------- |
| `DEFAULT`         | 默认、未分类 (原生或类原生以及当前未收集的系统种类)   |
| `HARMONYOS`       | [HarmonyOS](https://www.harmonyos.com/) (基于 AOSP)   |
| `EMUI`            | [EMUI](https://www.huaweicentral.com/emui)            |
| `MIUI`            | [MIUI](https://home.miui.com/)                        |
| `HYPEROS`         | [HyperOS](https://hyperos.mi.com/)                    |
| `COLOROS`         | [ColorOS](https://www.coloros.com/)                   |
| `FUNTOUCHOS`      | [FuntouchOS](https://www.vivo.com/funtouchos)         |
| `ORIGINOS`        | [OriginOS](https://www.vivo.com/originos)             |
| `FLYME`           | [Flyme](https://flyme.com/)                           |
| `ONEUI`           | [OneUI](https://www.samsung.com/one-ui)               |
| `ZUI`             | [ZUI](https://zui.com/)                               |
| `REDMAGICOS`      | [RedMagicOS](https://www.nubia.com/)                  |
| `NUBIAUI`         | [NubiaUI](https://www.nubia.com/)                     |
| `ROGUI`           | [RogUI](https://www.asus.com/)                        |
| `VISIONOS`        | [VisionOS](https://fans.hisense.com/forum-269-1.html) |

`SystemProperties` 是 Android 提供的一个能够在运行期间读取 `build.prop` 内容的工具，但是这个功能是不面向开发者开放的。

于是为了能够避免每次都使用反射的方式来访问 `SystemProperties`，`BetterAndroid` 镜像了 `SystemProperties` 的所有方法。

现在，你可以直接使用非反射的方式来访问 `SystemProperties`。

> 示例如下

```kotlin
// 例如，获取当前系统的构建 ID
val buildId = SystemProperties.get("ro.build.id")
// 获取当前系统的构建类型
val buildTags = SystemProperties.get("ro.system.build.tags")
// 获取当前设备支持的 CPU ABI 列表
val abis = SystemProperties.get("ro.system.product.cpu.abilist")
```

`BetterAndroid` 还为其提供了一个扩展用法。

> 示例如下

```kotlin
// 判断属性键值是否存在
// 例如一些 ROM 中特有的键值
val isExists = SystemProperties.contains("ro.miui.ui.version.name")
```