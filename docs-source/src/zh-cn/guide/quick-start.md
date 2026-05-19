# 快速开始

> 集成 `BetterAndroid` 到你的项目中。

## 项目要求

项目需要使用 `Android Studio` 或 `IntelliJ IDEA` 创建且类型为 Android 或 Kotlin Multiplatform 项目并已集成 Kotlin 环境依赖。

- Android Studio (建议从 [这里](https://developer.android.com/studio) 获取最新版本)

- IntelliJ IDEA (建议从 [这里](https://www.jetbrains.com/idea) 获取最新版本)

- Kotlin 1.9.0+、Gradle 8+、Java 17+、Android Gradle Plugin 8+

### 配置存储库

`BetterAndroid` 的依赖发布在 **Maven Central** 和我们的公共存储库中，你可以使用如下方式配置存储库。

我们推荐使用 Kotlin DSL 作为 Gradle 构建脚本语言。

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

```kotlin
android {
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

tasks.withType<KotlinJvmCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}
```

## 功能一览

整个项目分为多个模块，这些模块可以互相独立存在，也可以互相依赖，你可以选择你希望引入的模块作为依赖应用到你的项目中。

你可以点击下方对应的模块前往查看详细的功能介绍。

::: tip 版本说明

Android 子模块从 `1.1.0` 起开始采用统一版本进行发布，通常情况下你只需要关注同一个主版本即可，你也可以直接参考下方的 [android-bom](../library/android-bom.md) 使用 BOM 统一管理依赖版本。

Jetpack Compose 子模块目前仍保持独立版本，这是因为部分模块仍处于 Alpha 阶段，版本节奏不会强制与其子模块保持一致。

详情请见 [更新日志](../about/changelog.md)。

:::

### Android

- [android-bom](../library/android-bom.md)
- [ui-component](../library/ui-component.md)
- [ui-component-adapter](../library/ui-component-adapter.md)
- [ui-extension](../library/ui-extension.md)
- [system-extension](../library/system-extension.md)
- [permission-extension](../library/permission-extension.md)

### Jetpack Compose

- [compose-extension](../library/compose-extension.md)
- [compose-multiplatform](../library/compose-multiplatform.md)

## Demo

你可以在 [这里](repo://tree/main/samples) 找到一些示例，查看对应的演示项目来更好地了解这些功能的运作方式，快速地挑选出你需要的功能。

目前 Demo 功能尚未完善，可能在后期会逐渐完善。

不过你可以直接阅读现有文档，它们已足够介绍相关功能。

如果你正在使用 Jetpack Compose，你可以参考 [Flexi UI](https://github.com/BetterAndroid/FlexiUI)。