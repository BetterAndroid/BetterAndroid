# Better Android

[![GitHub license](https://img.shields.io/github/license/BetterAndroid/BetterAndroid?color=blue&style=flat-square)](https://github.com/BetterAndroid/BetterAndroid/blob/main/LICENSE)
[![Telegram](https://img.shields.io/badge/discussion-Telegram-blue.svg?logo=telegram&style=flat-square)](https://t.me/BetterAndroid)
[![Telegram](https://img.shields.io/badge/discussion%20dev-Telegram-blue.svg?logo=telegram&style=flat-square)](https://t.me/HighCapable_Dev)
[![QQ](https://img.shields.io/badge/discussion%20dev-QQ-blue.svg?logo=tencent-qq&logoColor=red&style=flat-square)](https://qm.qq.com/cgi-bin/qm/qr?k=Pnsc5RY6N2mBKFjOLPiYldbAbprAU3V7&jump_from=webapi&authKey=X5EsOVzLXt1dRunge8ryTxDRrh9/IiW1Pua75eDLh9RE3KXE+bwXIYF5cWri/9lf)

<img src="https://github.com/BetterAndroid/.github/blob/main/img-src/logo.png?raw=true" width = "100" height = "100" alt="LOGO"/>

为 Android 创造更多有用的工具扩展。

[English](README.md) | 简体中文

| <img src="https://github.com/BetterAndroid/.github/blob/main/img-src/logo.png?raw=true" width = "30" height = "30" alt="LOGO"/> | [BetterAndroid](https://github.com/BetterAndroid) |
|---------------------------------------------------------------------------------------------------------------------------------|---------------------------------------------------|

这个项目属于上述组织，**点击上方链接关注这个组织**，发现更多好项目。

## 项目缘由

随着 Android 系统功能变得越来越丰富，原生 Android 开发需要学习的内容也越来越多，针对 Android 平台的各种工具依赖也是数不胜数， 但是各种问题也是层出不穷。

最初创建这个项目的缘由为我在做 Android 开发过程中遇到的一些问题，以及系统的 API 调用方式过于繁琐，所以此项目本着 “不重复造轮子” 的理论， 从 UI
到系统层面，为开发者在系统原有 API 的基础上，对功能进行扩展，并解决了一些在 Android 开发过程中遇到的 “黑盒问题” 和针对第三方
ROM 和第三方厂商定制的 “Android 系统” 以及 Kotlin 开发语言进行了优化，使得这套工具能够做到在无侵入、 原生态的情况下为开发者提供更加友好便捷的开发体验。

这个项目的问世也得益于我早期的一些 Android 项目，有大量的功能是从 [轻享阅](https://github.com/PureReader) 项目解耦合出来的。

## 功能一览

`BetterAndroid` 将 Android 开发中常见但繁琐的系统版本、ROM 判断、UI 组件、系统栏、列表适配器以及通知等能力拆分为多个可独立引入的模块。你可以只选择当前项目需要的部分，也可以通过
BOM 统一管理多个 Android 子模块版本。

如果你的代码里到处都是 `Build.VERSION.SDK_INT`、`Build.VERSION_CODES` 或硬编码 API 等级，`system-extension` 可以让系统版本判断更直观，同时也提供了对常见第三方
ROM 的识别能力，适合处理厂商定制系统带来的兼容逻辑。

```kotlin
AndroidVersion.require(AndroidVersion.T) {
    // 执行相关代码
}

if (RomType.matches(RomType.HyperOS)) {
    // 执行相关代码
}
```

在传统 View 项目中，`ui-component` 提供了带有 `ViewBinding`、基础视图和 Compose 入口能力的预置 `Activity`、`Fragment`，让页面初始化不再需要反复编写
`setContentView`、`inflate` 等样板代码。

```kotlin
class MainActivity : AppBindingActivity<ActivityMainBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.mainText.text = "Hello World!"
    }
}
```

系统栏和 Window Insets 是很多应用适配全面屏、异形屏、沉浸式体验时最容易踩坑的部分。`SystemBarsController` 会接管系统栏样式、可见性和基础 Edge-to-Edge
行为，配合 `ui-extension` 中的 Insets 扩展可以减少大量重复兼容处理。

```kotlin
systemBars.init(rootView)
systemBars.behavior = SystemBarBehavior.SHOW_TRANSIENT_BARS_BY_SWIPE
systemBars.hide(SystemBars.STATUS_BARS)
```

对于列表页面，`ui-component-adapter` 将 `RecyclerView.Adapter`、`ViewBinding`、点击事件和差分更新整合到更紧凑的 DSL 中。你可以快速绑定数据，也可以通过
`RecyclerAsyncDiffer` 使用更现代化的列表提交方式。

```kotlin
val adapter = recyclerView.bindAdapter<MyEntity> {
    onBindData { listData }
    onBindItemView<AdapterMyLayoutBinding> { binding, entity, position ->
        binding.textView.text = entity.name
    }
    onItemViewClick { itemView, entity, position ->
        // Your code here.
    }
}
```

系统通知同样被封装为更易读的 DSL，通知渠道会在首次推送时自动创建并完成低版本兼容处理，你仍然可以继续使用与 `NotificationCompat.Builder` 对齐的参数。

```kotlin
val notification = context.createNotification(
    channel = NotificationChannel("my_channel_id") {
        name = "My Channel"
        description = "My channel description."
    }
) {
    smallIconResId = R.drawable.ic_my_notification
    contentTitle = "My Notification"
    contentText = "Hello World!"
}

notification.post()
```

## 开始使用

| <img src="https://github.com/BetterAndroid/.github/blob/main/img-src/logo.png?raw=true" width = "30" height = "30" alt="LOGO"/> | [BetterAndroid 文档](https://betterandroid.github.io/BetterAndroid/zh-cn) |
|---------------------------------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------|

你可以前往文档页面查看更多详细教程和内容。

### 下一步做什么？

1. **引入依赖**: 找到你需要的模块并将其添加到你的项目中。
2. **同步项目**: 在 Gradle 同步后，你就可以开始使用 `BetterAndroid` 了。

在打开的页面中，选择侧边栏的 **快速开始** 章节以继续阅读。

## 更多项目

<!--suppress HtmlDeprecatedAttribute -->
<div align="center">
    <h2>嘿，还请君留步！👋</h2>
    <h3>如果你觉得这个项目能给你提供帮助，不妨继续往下看看我的更多项目吧！</h3>
    <h3>如果这些项目能为你提供帮助，不妨为我点个关注或者 star ⭐️ 吧！</h3>
    <h1><a href="https://github.com/fankes/fankes/blob/main/project-promote/README-zh-CN.md">→ 查看更多关于我的项目，请点击这里 ←</a></h1>
</div>

## Star History

![Star History Chart](https://api.star-history.com/svg?repos=BetterAndroid/BetterAndroid&type=Date)

## 许可证

- [Apache-2.0](https://www.apache.org/licenses/LICENSE-2.0)

```
Apache License Version 2.0

Copyright (C) 2019 HighCapable

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

版权所有 © 2019 HighCapable