# Better Android

[![GitHub license](https://img.shields.io/github/license/BetterAndroid/BetterAndroid?color=blue&style=flat-square)](https://github.com/BetterAndroid/BetterAndroid/blob/main/LICENSE)
[![Telegram](https://img.shields.io/badge/discussion-Telegram-blue.svg?logo=telegram&style=flat-square)](https://t.me/BetterAndroid)
[![Telegram](https://img.shields.io/badge/discussion%20dev-Telegram-blue.svg?logo=telegram&style=flat-square)](https://t.me/HighCapable_Dev)
[![QQ](https://img.shields.io/badge/discussion%20dev-QQ-blue.svg?logo=tencent-qq&logoColor=red&style=flat-square)](https://qm.qq.com/cgi-bin/qm/qr?k=Pnsc5RY6N2mBKFjOLPiYldbAbprAU3V7&jump_from=webapi&authKey=X5EsOVzLXt1dRunge8ryTxDRrh9/IiW1Pua75eDLh9RE3KXE+bwXIYF5cWri/9lf)

<img src="https://github.com/BetterAndroid/.github/blob/main/img-src/logo.png?raw=true" width = "100" height = "100" alt="LOGO"/>

Create more useful tool extensions for Android.

English | [简体中文](README-zh-CN.md)

| <img src="https://github.com/BetterAndroid/.github/blob/main/img-src/logo.png?raw=true" width = "30" height = "30" alt="LOGO"/> | [BetterAndroid](https://github.com/BetterAndroid) |
|---------------------------------------------------------------------------------------------------------------------------------|---------------------------------------------------|

This project belongs to the organization above. **Click the link to follow us** and discover more awesome projects.

## Project Reason

As the Android system continues to evolve, native development demands a growing learning curve. While there are countless utilities available, new
fragmentation issues and edge cases emerge endlessly.

I originally created this project to address the friction I encountered in daily Android development—mostly dealing with cumbersome and outdated
system APIs. Guided by the principle of "not reinventing the wheel", BetterAndroid focuses on enhancing what is already there.

From UI rendering to system-level interactions, it seamlessly extends native APIs. It is explicitly designed to tackle "black box" scenarios, bridging
the gap between official behaviors and the quirks of third-party OEM ROMs.

Leveraging the power of Kotlin, everything provided here remains strictly non-intrusive. It ensures you can enjoy a smoother, more elegant developer
experience without compromising the native ecosystem.

This library is a culmination of solutions refined across multiple real-world apps, with a significant portion of its core battle-tested and decoupled
from my earlier project, [PureReader](https://github.com/PureReader).

## Features Overview

`BetterAndroid` splits common but verbose Android work into several modules that can be introduced independently, including system version checks, ROM
detection, UI components, system bars, adapters, and notifications. You can pick only what your project needs, or use the BOM to manage multiple
Android module versions together.

If your code is filled with `Build.VERSION.SDK_INT`, `Build.VERSION_CODES`, or hard-coded API levels, `system-extension` makes version checks easier
to read. It also provides detection for common third-party ROMs, which is useful when handling compatibility logic for OEM-customized Android systems.

```kotlin
AndroidVersion.require(AndroidVersion.T) {
    // Execute relevant code.
}

if (RomType.matches(RomType.HyperOS)) {
    // Execute relevant code.
}
```

For traditional View-based projects, `ui-component` provides preset `Activity` and `Fragment` components with `ViewBinding`, base views, and Compose
entry support. This reduces repeated `setContentView`, `inflate`, and binding boilerplate during page initialization.

```kotlin
class MainActivity : AppBindingActivity<ActivityMainBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.mainText.text = "Hello World!"
    }
}
```

System bars and Window Insets are easy places to hit edge-to-edge, cutout, and immersive-mode compatibility issues. `SystemBarsController` takes over
system bar styles, visibility, and basic Edge-to-Edge behavior, while the Insets extensions in `ui-extension` help reduce repeated compatibility
handling.

```kotlin
systemBars.init(rootView)
systemBars.behavior = SystemBarBehavior.SHOW_TRANSIENT_BARS_BY_SWIPE
systemBars.hide(SystemBars.STATUS_BARS)
```

For list screens, `ui-component-adapter` integrates `RecyclerView.Adapter`, `ViewBinding`, item click events, and diff updates into a compact DSL. You
can bind data quickly, or use `RecyclerAsyncDiffer` for a more modern list submission workflow.

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

System notifications are also wrapped as a more readable DSL. Notification channels are created automatically on the first post and compatibility for
lower Android versions is handled for you, while the remaining parameters stay aligned with `NotificationCompat.Builder`.

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

## Get Started

| <img src="https://github.com/BetterAndroid/.github/blob/main/img-src/logo.png?raw=true" width = "30" height = "30" alt="LOGO"/> | [BetterAndroid Documentation](https://betterandroid.github.io/BetterAndroid/en) |
|---------------------------------------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------|

You can go to the documentation page for more detailed tutorials and content.

### What's next?

1. **Add dependencies**: Find the module you need and add it to your project.
2. **Sync the project**: After a Gradle sync, you can start using `BetterAndroid`.

In the opened page, select the **Quick Start** section in the sidebar to continue reading.

## More Projects

<!--suppress HtmlDeprecatedAttribute -->
<div align="center">
    <h2>Hey, wait a second! 👋</h2>
    <h3>If this project was helpful, why not stick around and check out more of my work below?</h3>
    <h3>Feel free to leave a follow or a star ⭐️ if they bring you value!</h3>
    <h1><a href="https://github.com/fankes/fankes/blob/main/project-promote/README.md">→ Click here to discover more of my projects ←</a></h1>
</div>

## Star History

![Star History Chart](https://api.star-history.com/svg?repos=BetterAndroid/BetterAndroid&type=Date)

## License

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

Copyright © 2019 HighCapable