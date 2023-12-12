# compose-extension

![GitHub release](https://img.shields.io/github/v/release/BetterAndroid/BetterAndroid?display_name=release&logo=github&color=green&filter=compose-extension-*)
<span style="margin-left: 5px"/>
![Android Min SDK](https://img.shields.io/badge/Min%20SDK-21-orange?logo=android)

这是针对 Jetpack Compose 相关功能扩展的一个依赖，同时支持多平台。

目前支持的平台：Android、iOS、Desktop (JVM)。

## 配置依赖

你可以使用如下方式将此模块添加到你的项目中。

这是一个 Kotlin Multiplatform 依赖，你需要 `org.jetbrains.kotlin.multiplatform` 插件来应用相关依赖。

### SweetDependency 方式

在你的项目 `SweetDependency` 配置文件中添加依赖。

```yaml
libraries:
  com.highcapable.betterandroid:
    # commonMain
    compose-extension:
      version: +
    # androidMain
    compose-extension-android:
      version-ref: <this>::compose-extension
    # iosArm64Main
    compose-extension-iosarm64:
      version-ref: <this>::compose-extension
    # iosX64Main
    compose-extension-iosx64:
      version-ref: <this>::compose-extension
    # iosSimulatorArm64Main
    compose-extension-iossimulatorarm64:
      version-ref: <this>::compose-extension
    # desktopMain
    compose-extension-desktop:
      version-ref: <this>::compose-extension
```

在你的项目 `build.gradle.kts` 中配置依赖。

如果你在普通的项目中使用多平台依赖，你只需要按需部署对应平台后缀的依赖即可。

```kotlin
implementation(com.highcapable.betterandroid.compose.extension.android)
implementation(com.highcapable.betterandroid.compose.extension.iosarm64)
implementation(com.highcapable.betterandroid.compose.extension.iosx64)
implementation(com.highcapable.betterandroid.compose.extension.iossimulatorarm64)
implementation(com.highcapable.betterandroid.compose.extension.desktop)
```

如果你在多平台项目中使用多平台依赖，你需要在 `commonMain` 中添加 `compose-extension` 依赖。

```kotlin
kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(com.highcapable.betterandroid.compose.extension)
            }
        }
    }
}
```

### 传统方式 (不推荐)

在你的项目 `build.gradle.kts` 中配置依赖。

如果你在普通的项目中使用多平台依赖，你只需要按需部署对应平台后缀的依赖即可。

```kotlin
implementation("com.highcapable.betterandroid:compose-extension-android:<version>")
implementation("com.highcapable.betterandroid:compose-extension-iosarm64:<version>")
implementation("com.highcapable.betterandroid:compose-extension-iosx64:<version>")
implementation("com.highcapable.betterandroid:compose-extension-iossimulatorarm64:<version>")
implementation("com.highcapable.betterandroid:compose-extension-desktop:<version>")
```

如果你在多平台项目中使用多平台依赖，你需要在 `commonMain` 中添加 `compose-extension` 依赖。

```kotlin
kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("com.highcapable.betterandroid:compose-extension:<version>")
            }
        }
    }
}
```

请将 `<version>` 修改为此文档顶部显示的版本。

## 功能介绍

你可以 [点击这里](kdoc://compose-extension) 查看 KDoc。

<!--------------- 待转移 ---------------

**功能结构**

- [x] UI 相关功能扩展
    - Color、Border、BorderStroke
        - Compose 基础 UI 系统扩展
    - ComponentPadding
        - 对 PaddingValues 的扩展，使其具有 copy 等功能
    - Foundation
        - 基础功能扩展
    - ImageVector
        - 矢量图绘制扩展
    - Unit
        - 布局测量单位等的 orNull 扩展
    - Dialog、Popup
        - 针对 Android 平台的特殊写法优化
- [x] 平台特殊功能扩展
    - SystemBars、WindowInsets
        - 系统栏、Window Insets 相关功能扩展，支持 Android、iOS
    - BackHandler
        - 返回事件处理器，仅支持 Android

--------------- 待转移 --------------->