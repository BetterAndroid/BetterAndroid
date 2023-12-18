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

<!--------------- 待转移 ---------------

**功能结构**

- [x] 系统组件扩展
    - ApplicationFactory
        - 应用相关功能扩展，例如 PackageManager、PackageInfo、ApplicationInfo
    - BroadcastFactory
        - 系统广播相关功能扩展
    - ClipboardFactory
        - 系统剪贴板扩展
    - IntentFactory
        - Intent 相关功能扩展
    - ServiceFactory
        - 系统服务扩展
- [x] 系统工具扩展
    - SystemVersion
        - 整合了 android.os.Build 的 SDK 工具
    - SystemKind
        - 收集了常见的第三方 ROM 或厂商定制 Android 系统种类工具
    - SystemProperties
        - 对不可直接访问的 android.os.SystemProperties 的扩展

--------------- 待转移 --------------->