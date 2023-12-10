# Better Android

[![GitHub license](https://img.shields.io/github/license/BetterAndroid/BetterAndroid?color=blue)](https://github.com/BetterAndroid/BetterAndroid/blob/main/LICENSE)
[![Telegram](https://img.shields.io/badge/discussion-Telegram-blue.svg?logo=telegram)](https://t.me/BetterAndroid)
[![Telegram](https://img.shields.io/badge/discussion%20dev-Telegram-blue.svg?logo=telegram)](https://t.me/HighCapable_Dev)

<img src="https://github.com/BetterAndroid/.github/blob/main/img-src/logo.png?raw=true" width = "100" height = "100" alt="LOGO"/>

Create more useful tool extensions for Android.

English | [简体中文](README-zh-CN.md)

| <img src="https://github.com/BetterAndroid/.github/blob/main/img-src/logo.png?raw=true" width = "30" height = "30" alt="LOGO"/> | [BetterAndroid](https://github.com/BetterAndroid) |
|---------------------------------------------------------------------------------------------------------------------------------|---------------------------------------------------|

This project belongs to the above-mentioned organization, **click the link above to follow this organization** and discover more good projects.

## Project Reason

As Android system features becomes more and more abundant, there is more and more features to learn for native Android development, and there are
countless tool dependencies for the Android platform, but various problems also arise in endlessly.

The reason for initially creating this project was some problems I encountered during the Android development process, and the system's API calling
method was too cumbersome, so this project is based on the theory of "not reinventing the wheel".

From the UI to the system level, it allows developers to expand functions based on the system's original API, and solves some "black box problems"
encountered in the Android development process and for third parties.

The optimization of ROM and the "Android system" customized by third-party manufacturers, as well as the optimization of the Kotlin development
language, enable this set of tools to be non-intrusive and provide developers with a more friendly and convenient development experience under the
original ecological environment.

The emergence of this project also benefited from some of my early Android projects, a large number of functions were decoupled from
the [PureReader](https://github.com/PureReader) project.

## Functional Overview

The entire project is divided into multiple modules, these modules can exist independently of each other or depend on each other,
you can choose the modules you want to introduce as dependencies and apply them to your project.

### ui-component

![GitHub release](https://img.shields.io/github/v/release/BetterAndroid/BetterAndroid?display_name=release&logo=github&color=green&filter=ui-component-*)

**Maven Notation**

```
com.highcapable.betterandroid:ui-component
```

This is a dependency for UI (user interface) related components.

You can view the KDoc [click here](https://betterandroid.github.io/BetterAndroid/ui-component).

**Functional Structure**

- [x] Activity Components
    - AppBindingActivity
        - Activity with view binding (Inherited from AppCompatActivtiy)
    - AppViewsActivity
        - Basic view component Activity (Inherited from AppCompatActivtiy)
    - AppComponentActivity
        - Basic component Activity (Inherited from ComponentActivtiy)
- [x] Fragment Components
    - AppBindingFragment
        - Fragment with view binding
    - AppViewsFragment
        - Basic view component Fragment
- [x] Adapter Components
    - CommonAdapterBuilder
        - Build BaseAdapter
    - PagerAdapterBuilder
        - Build PagerAdapter
    - RecyclerAdapterBuilder
        - Build RecyclerView.Adapter
- [x] System Event Components
    - BackPressedController
        - Return to event controller
- [x] Notification Components
    - NotificationCreator
        - System notification builder
- [x] System Bars Components (status bars, navigation bars, etc.)
    - SystemBarsController
        - System bars controller

### ui-extension

![GitHub release](https://img.shields.io/github/v/release/BetterAndroid/BetterAndroid?display_name=release&logo=github&color=green&filter=ui-extension-*)

**Maven Notation**

```
com.highcapable.betterandroid:ui-extension
```

This is a dependency for UI (user interface) related extensions.

You can view the KDoc [click here](https://betterandroid.github.io/BetterAndroid/ui-extension).

**Functional Structure**

- [x] Basic Function Extensions
    - ColorsFactory
        - System color function extensions
    - DimensionFactory
        - Layout size function extensions, such as dp, px
    - DrawableFactory
        - Drawable component extensions
    - ResourcesFactory
        - App resource extensions
    - ViewBindingFactory
        - Layout binding extensions
- [x] System Feature Extensions
    - SystemColors
        - System dynamic color selection, added in Android 12
- [x] Interface Component Extensions
    - ActivityFactory
        - Activity extensions
    - FragmentFactory
        - Fragment extensions
    - ToastFactory
        - Toast extensions
    - WindowFactory
        - Windows extensions, such as adjusting screen brightness for specified interfaces
- [x] Graphics Function Extensions
    - BitmapFactory
        - Extensions to handle bitmap related functions
    - BitmapBlurFactory
        - Extension to handle bitmap blur
- [x] View Component Extensions
    - ViewFactory
        - Basic view component extensions
    - TextViewFactory
        - Text box related function extensions

### system-extension

![GitHub release](https://img.shields.io/github/v/release/BetterAndroid/BetterAndroid?display_name=release&logo=github&color=green&filter=system-extension-*)

**Maven Notation**

```
com.highcapable.betterandroid:system-extension
```

This is a dependency for system layer related extensions.

You can view the KDoc [click here](https://betterandroid.github.io/BetterAndroid/system-extension).

**Functional Structure**

- [x] System Component Extensions
    - ApplicationFactory
        - Application related function extensions, such as PackageManager, PackageInfo, ApplicationInfo
    - BroadcastFactory
        - System broadcast related function extensions
    - ClipboardFactory
        - System clipboard extensions
    - IntentFactory
        - Intent related function extensions
    - ServiceFactory
        - System service extensions
- [x] System Tools Extensions
    - SystemVersion
        - Integrated SDK tools of android.os.Build
    - SystemKind
        - Collected common third-party ROMs or manufacturer-customized Android system type tool
    - SystemProperties
        - Extensions to android.os.SystemProperties that are not directly accessible

### permission-extension

![GitHub release](https://img.shields.io/github/v/release/BetterAndroid/BetterAndroid?display_name=release&logo=github&color=green&filter=permission-extension-*)

**Maven Notation**

```
com.highcapable.betterandroid:permission-extension
```

This is a dependency for application permission management, authorization and other related extensions.

This function is still in the development stage and will be gradually improved in the later stages.

There is actually a very mature framework for this
function, you can [click here](https://github.com/getActivity/XXPermissions) to view it.

## Get Started

Select the dependencies you need and integration them into your project,
We recommend using Kotlin DSL as the Gradle build script language and [SweetDependency](https://github.com/HighCapable/SweetDependency) to manage
dependencies.

### SweetDependency Method

Add the repositories and dependencies in your project's `SweetDependency` configuration file.

```yaml
repositories:
  google:
  maven-central:
  # (Optional) You can add this URL to use our public repository
  # When Sonatype-OSS fails and cannot publish dependencies, this repository is added as a backup
  # For details, please visit: https://github.com/HighCapable/maven-repository
  highcapable-maven-releases:
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

After adding it, run Gradle Sync and all dependencies will be autowired.

Next, deploy dependencies in your project `build.gradle.kts`.

```kotlin
dependencies {
    implementation(com.highcapable.betterandroid.ui.component)
    implementation(com.highcapable.betterandroid.ui.extension)
    implementation(com.highcapable.betterandroid.system.extension)
}
```

### Traditional Method (Not Recommended)

Add repositories in your project `build.gradle.kts`.

```kotlin
repositories {
    google()
    mavenCentral()
    // (Optional) You can add this URL to use our public repository
    // When Sonatype-OSS fails and cannot publish dependencies, this repository is added as a backup
    // For details, please visit: https://github.com/HighCapable/maven-repository
    maven("https://raw.githubusercontent.com/HighCapable/maven-repository/main/repository/releases")
}
```

Add dependencies in your project `build.gradle.kts`.

```kotlin
dependencies {
    implementation("com.highcapable.betterandroid:ui-component:<version>")
    implementation("com.highcapable.betterandroid:ui-extension:<version>")
    implementation("com.highcapable.betterandroid:system-extension:<version>")
}
```

Please change `<version>` to the latest version of the corresponding dependency in the **Functional Overview**.

## Obfuscate Rules

If you use `ui-component` or `ui-extension` and use `ViewBinding` related functions, please add the following obfuscation rules.

```proguard
-keep class * extends android.app.Activity
-keep class * implements androidx.viewbinding.ViewBinding {
    <init>();
    *** inflate(android.view.LayoutInflater);
}
```

## Demo

You can find `samples` in the directory of this project to view the corresponding demo to better understand how these functions work and quickly
select the functions you need.

At present, the demo functions is not perfect yet and will be improved after [Flexi UI](https://github.com/BetterAndroid/FlexiUI) is released, you can
experience the existing functions first and report bugs to us.

## Contribution

The maintenance of this project is inseparable from the support and contributions of all developers.

This project is currently in its early stages, and there may still be some problems or lack of functions you need.

If possible, feel free to submit a PR to contribute features you think are needed to this project or open an `issue` to make suggestions to us.

## Changelog

- [Click here](docs/changelog.md) to view the historical changelog

## Promotion

<!--suppress HtmlDeprecatedAttribute -->
<div align="center">
     <h2>Hey, please stay! 👋</h2>
     <h3>Here are related projects such as Android development tools, UI design, Gradle plugins, Xposed Modules and practical software. </h3>
     <h3>If the project below can help you, please give me a star! </h3>
     <h3>All projects are free, open source, and follow the corresponding open source license agreement. </h3>
     <h1><a href="https://github.com/fankes/fankes/blob/main/project-promote/README.md">→ To see more about my projects, please click here ←</a></h1>
</div>

## Star History

![Star History Chart](https://api.star-history.com/svg?repos=BetterAndroid/BetterAndroid&type=Date)

## License

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

Copyright © 2019-2023 HighCapable