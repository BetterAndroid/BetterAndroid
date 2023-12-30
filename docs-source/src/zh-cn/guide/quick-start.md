# 快速开始

> 集成 `BetterAndroid` 到你的项目中。

## 项目要求

项目需要使用 `Android Studio` 或 `IntelliJ IDEA` 创建且类型为 Android 或 Kotlin Multiplatform 项目并已集成 Kotlin 环境依赖。

- Android Studio (建议 [从这里](https://developer.android.com/studio) 获取最新版本)

- IntelliJ IDEA (建议 [从这里](https://www.jetbrains.com/idea) 获取最新版本)

- Kotlin 1.9.0+、Gradle 8+、Java 17+、Android Gradle Plugin 8+

### 配置存储库

`BetterAndroid` 的依赖发布在 **Maven Central** 和我们的公共存储库中，你可以使用如下方式配置存储库。

我们推荐使用 Kotlin DSL 作为 Gradle 构建脚本语言并推荐使用 [SweetDependency](https://github.com/HighCapable/SweetDependency) 来管理依赖。

#### SweetDependency (推荐)

在你的项目 `SweetDependency` 配置文件中配置存储库。

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
```

#### 传统方式

在你的项目 `build.gradle.kts` 中配置存储库。

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

### 配置 Java 版本

在你的项目 `build.gradle.kts` 中修改 Kotlin 的 Java 版本为 17 及以上。

```kt
android {
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}
```

## 功能一览

整个项目分为多个模块，这些模块可以互相独立存在，也可以互相依赖，你可以选择你希望引入的模块作为依赖应用到你的项目中。

你可以点击下方对应的模块前往查看详细的功能介绍。

- [ui-component](../library/ui-component.md)
- [ui-extension](../library/ui-extension.md)
- [system-extension](../library/system-extension.md)
- [permission-extension](../library/permission-extension.md)
- [compose-extension](../library/compose-extension.md)
  
## 项目模版

你可以使用我们提供的项目模版来快速创建一个集成了 `BetterAndroid` 依赖的项目。

- [android-app-template](https://github.com/BetterAndroid/android-app-template)
- [compose-multiplatform-template](https://github.com/BetterAndroid/compose-multiplatform-template)

## Demo

你可以在 [这里](repo://tree/main/samples) 找到一些示例，查看对应的演示项目来更好地了解这些功能的运作方式，快速地挑选出你需要的功能。

目前 Demo 功能尚未完善，将在 [Flexi UI](repo://FlexiUI) 发布后进行完善，你可以先行体验现有功能并可以向我们反馈 BUG。