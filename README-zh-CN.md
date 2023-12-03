# Better Android

[![GitHub license](https://img.shields.io/github/license/BetterAndroid/BetterAndroid?color=blue)](https://github.com/BetterAndroid/BetterAndroid/blob/main/LICENSE)
[![Telegram](https://img.shields.io/badge/discussion-Telegram-blue.svg?logo=telegram)](https://t.me/BetterAndroid)
[![Telegram](https://img.shields.io/badge/discussion%20dev-Telegram-blue.svg?logo=telegram)](https://t.me/HighCapable_Dev)

<img src="https://github.com/BetterAndroid/.github/blob/main/img-src/logo.png?raw=true" width = "100" height = "100" alt="LOGO"/>

为 Android 创建更多有用的工具扩展。

[English](https://github.com/BetterAndroid/BetterAndroid/blob/main/README.md) | 简体中文

| <img src="https://github.com/BetterAndroid/.github/blob/main/img-src/logo.png?raw=true" width = "30" height = "30" alt="LOGO"/> | [BetterAndroid](https://github.com/BetterAndroid) |
|---------------------------------------------------------------------------------------------------------------------------------|---------------------------------------------------|

这个项目属于上述组织，**点击上方链接关注这个组织**，发现更多好项目。

## 项目缘由

随着 Android 系统功能变得越来越丰富，原生 Android 开发需要学习的内容也越来越多，针对 Android 平台的各种工具依赖也是数不胜数，
但是各种问题也是层出不穷。

最初创建这个项目的缘由为我在做 Android 开发过程中遇到的一些问题，以及系统的 API 调用方式过于繁琐，所以此项目本着“不重复造轮子”的理论，
从 UI 到系统层面，为开发者在系统原有 API 的基础上，对功能进行扩展，并解决了一些在 Android 开发过程中遇到的“黑盒问题”和针对第三方
ROM 和第三方厂商定制的“Android 系统”的优化， 以及针对 Kotlin 开发语言进行了优化，使得这套工具能够做到在无侵入、
原生态的情况下为开发者提供更加友好便捷的开发体验。

这个项目的问世也得益于我早期的一些 Android 项目，有大量的功能是从 [轻享阅](https://github.com/PureReader) 项目解耦合出来的。

## 功能一览

整个项目分为多个模块，这些模块可以互相独立存在，也可以互相依赖，你可以选择你希望引入的模块作为依赖应用到你的项目中。

### ui-component

![GitHub release](https://img.shields.io/github/v/release/BetterAndroid/BetterAndroid?display_name=release&logo=github&color=green&filter=ui-component-*)

**Maven Notation**

```
com.highcapable.betterandroid:ui-component
```

这是针对 UI (用户界面) 相关组件的一个依赖。

你可以 [点击这里](https://betterandroid.github.io/BetterAndroid/ui-component) 查看 KDoc。

**功能结构**

- [x] Activity 组件
    - AppBindingActivity
        - 带有视图绑定的 Activity (继承于 AppCompatActivtiy)
    - AppViewsActivity
        - 基础视图组件 Activity (继承于 AppCompatActivtiy)
    - AppComponentActivity
        - 基础组件 Activity (继承于 ComponentActivtiy)
- [x] Fragment 组件
    - AppBindingFragment
        - 带有视图绑定的 Fragment
    - AppViewsFragment
        - 基础视图组件 Fragment
- [x] 适配器组件
    - CommonAdapterBuilder
        - 构建 BaseAdapter
    - PagerAdapterBuilder
        - 构建 PagerAdapter
    - RecyclerAdapterBuilder
        - 构建 RecyclerView.Adapter
- [x] 系统事件组件
    - BackPressedController
        - 返回事件控制器
- [x] 通知组件
    - NotificationCreator
        - 系统通知构建器
- [x] 系统栏组件 (状态栏、导航栏等)
    - SystemBarsController
        - 系统栏控制器

### ui-extension

![GitHub release](https://img.shields.io/github/v/release/BetterAndroid/BetterAndroid?display_name=release&logo=github&color=green&filter=ui-extension-*)

**Maven Notation**

```
com.highcapable.betterandroid:ui-extension
```

这是针对 UI (用户界面) 相关功能扩展的一个依赖。

你可以 [点击这里](https://betterandroid.github.io/BetterAndroid/ui-extension) 查看 KDoc。

**功能结构**

- [x] 基础功能扩展
    - ColorsFactory
        - 系统颜色功能扩展
    - DimensionFactory
        - 布局尺寸功能扩展，例如 dp、px
    - DrawableFactory
        - 可绘制组件扩展
    - ResourcesFactory
        - App 资源扩展
    - ViewBindingFactory
        - 布局绑定扩展
- [x] 系统特性扩展
    - SystemColors
        - 系统的动态取色，在 Android 12 添加
- [x] 界面组件扩展
    - ActivityFactory
        - Activity 扩展
    - FragmentFactory
        - Fragment 扩展
    - ToastFactory
        - Toast 扩展
    - WindowFactory
        - Windows 扩展，例如调整指定界面的屏幕亮度
- [x] 图形功能扩展
    - BitmapFactory
        - 处理位图相关功能的扩展
    - BitmapBlurFactory
        - 处理位图模糊的扩展
- [x] 视图组件扩展
    - ViewFactory
        - 基础视图组件扩展
    - TextViewFactory
        - 文本框相关功能扩展

### system-extension

![GitHub release](https://img.shields.io/github/v/release/BetterAndroid/BetterAndroid?display_name=release&logo=github&color=green&filter=system-extension-*)

**Maven Notation**

```
com.highcapable.betterandroid:system-extension
```

这是针对系统层相关功能扩展的一个依赖。

你可以 [点击这里](https://betterandroid.github.io/BetterAndroid/system-extension) 查看 KDoc。

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

### permission-extension

![GitHub release](https://img.shields.io/github/v/release/BetterAndroid/BetterAndroid?display_name=release&logo=github&color=green&filter=permission-extension-*)

**Maven Notation**

```
com.highcapable.betterandroid:permission-extension
```

这是针对应用权限管理、授权等相关功能扩展的一个依赖。

此功能尚在开发阶段，将在后期逐渐进行完善，关于这个功能其实已经有了一个很成熟的框架，你可以 [点击这里](https://github.com/getActivity/XXPermissions) 前往查看。

## 开始使用

选择你需要的依赖引入你的项目，
我们推荐使用 Kotlin DSL 作为 Gradle 构建脚本语言并推荐使用 [SweetDependency](https://github.com/HighCapable/SweetDependency) 来管理依赖。

### SweetDependency 方式

在你的项目 `SweetDependency` 配置文件中添加存储库和依赖。

```yaml
repositories:
  google:
  maven-central:
  # (可选) 你可以添加此 URL 以使用我们的公共存储库
  # 当 Sonatype-OSS 发生故障无法发布依赖时，此存储库作为备选进行添加
  # 详情请前往：https://github.com/HighCapable/maven-repository
  highcapable-maven-releases:
    # 中国大陆用户请将下方的 "raw.githubusercontent.com" 修改为 "raw.gitmirror.com"
    url: https://raw.githubusercontent.com/HighCapable/maven-repository/main/repository/releases

libraries:
  com.highcapable.betterandroid:
    ui-component:
      version: +
    ui-extension:
      version: +
    system-extension:
      version: +
```

添加完成后运行一次 Gradle Sync，所有依赖版本将自动装配。

接下来，在你的项目 `build.gradle.kts` 中部署依赖。

```kotlin
dependencies {
    implementation(com.highcapable.betterandroid.ui.component)
    implementation(com.highcapable.betterandroid.ui.extension)
    implementation(com.highcapable.betterandroid.system.extension)
}
```

### 传统方式 (不推荐)

在你的项目 `build.gradle.kts` 中添加存储库。

```kotlin
repositories {
    google()
    mavenCentral()
    // (可选) 你可以添加此 URL 以使用我们的公共存储库
    // 当 Sonatype-OSS 发生故障无法发布依赖时，此存储库作为备选进行添加
    // 详情请前往：https://github.com/HighCapable/maven-repository
    // 中国大陆用户请将下方的 "raw.githubusercontent.com" 修改为 "raw.gitmirror.com"
    maven("https://raw.githubusercontent.com/HighCapable/maven-repository/main/repository/releases")
}
```

在你的项目 `build.gradle.kts` 中部署依赖。

```kotlin
dependencies {
    implementation("com.highcapable.betterandroid:ui-component:<version>")
    implementation("com.highcapable.betterandroid:ui-extension:<version>")
    implementation("com.highcapable.betterandroid:system-extension:<version>")
}
```

请将 `<version>` 修改为**功能一览**中对应依赖的最新版本。

## 混淆规则

如果你使用了 `ui-component` 或 `ui-extension`，并使用了 `ViewBinding` 相关功能，请添加以下混淆规则。

```proguard
-keep class * extends android.app.Activity
-keep class * implements androidx.viewbinding.ViewBinding {
    <init>();
    *** inflate(android.view.LayoutInflater);
}
```

## Demo

你可以在此项目的目录中找到 `samples` 查看对应的 Demo 来更好地了解这些功能的运作方式，快速地挑选出你需要的功能。

目前 Demo 功能尚未完善，将在 [Flexi UI](https://github.com/BetterAndroid/FlexiUI) 发布后进行完善，你可以先行体验现有功能并可以向我们反馈 BUG。

## 功能贡献

本项目的维护离不开各位开发者的支持和贡献，目前这个项目处于初期阶段，可能依然存在一些问题或者缺少你需要的功能，
如果可能，欢迎提交 PR 为此项目贡献你认为需要的功能或开启一个 `issue` 向我们提出建议。

## 更新日志

- [点击这里](https://github.com/BetterAndroid/BetterAndroid/blob/main/docs/changelog-zh-CN.md) 查看历史更新日志

## 项目推广

<!--suppress HtmlDeprecatedAttribute -->
<div align="center">
    <h2>嘿，还请君留步！👋</h2>
    <h3>这里有 Android 开发工具、UI 设计、Gradle 插件、Xposed 模块和实用软件等相关项目。</h3>
    <h3>如果下方的项目能为你提供帮助，不妨为我点个 star 吧！</h3>
    <h3>所有项目免费、开源，遵循对应开源许可协议。</h3>
    <h1><a href="https://github.com/fankes/fankes/blob/main/project-promote/README-zh-CN.md">→ 查看更多关于我的项目，请点击这里 ←</a></h1>
</div>

## 捐赠支持

工作不易，无意外情况此项目将继续维护下去，提供更多可能，欢迎打赏。

<img src="https://github.com/fankes/fankes/blob/main/img-src/payment_code.jpg?raw=true" width = "500" alt="Payment Code"/>

## Star History

![Star History Chart](https://api.star-history.com/svg?repos=BetterAndroid/BetterAndroid&type=Date)

## 许可证

- [Apache-2.0](https://www.apache.org/licenses/LICENSE-2.0)

```
Apache License Version 2.0

Copyright (C) 2019-2023 HighCapable

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

版权所有 © 2019-2023 HighCapable