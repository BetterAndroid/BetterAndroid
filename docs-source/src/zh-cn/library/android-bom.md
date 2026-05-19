# android-bom

![Maven Central](https://img.shields.io/maven-central/v/com.highcapable.betterandroid/android-bom?logo=apachemaven&logoColor=orange&style=flat-square)
<span style="margin-left: 5px"/>
![Maven metadata URL](https://img.shields.io/maven-metadata/v?metadataUrl=https%3A%2F%2Fraw.githubusercontent.com%2FHighCapable%2Fmaven-repository%2Frefs%2Fheads%2Fmain%2Frepository%2Freleases%2Fcom%2Fhighcapable%2Fbetterandroid%2Fandroid-bom%2Fmaven-metadata.xml&logo=apachemaven&logoColor=orange&label=highcapable-maven-releases&style=flat-square)

这是针对 Android 子模块统一版本管理的 BOM 依赖。

## 配置依赖

你可以使用如下方式将此模块添加到你的项目中。

### Version Catalog (推荐)

在你的项目 `gradle/libs.versions.toml` 中添加依赖。

```toml
[versions]
betterandroid-android-bom = "<version>"

[libraries]
betterandroid-android-bom = { module = "com.highcapable.betterandroid:android-bom", version.ref = "betterandroid-android-bom" }
betterandroid-ui-component = { module = "com.highcapable.betterandroid:ui-component" }
betterandroid-ui-component-adapter = { module = "com.highcapable.betterandroid:ui-component-adapter" }
betterandroid-ui-extension = { module = "com.highcapable.betterandroid:ui-extension" }
betterandroid-system-extension = { module = "com.highcapable.betterandroid:system-extension" }
```

在你的项目 `build.gradle.kts` 中配置依赖。

```kotlin
implementation(platform(libs.betterandroid.android.bom))

implementation(libs.betterandroid.ui.component)
implementation(libs.betterandroid.ui.component.adapter)
implementation(libs.betterandroid.ui.extension)
implementation(libs.betterandroid.system.extension)
```

请将 `<version>` 修改为此文档顶部显示的版本。

### 传统方式

在你的项目 `build.gradle.kts` 中配置依赖。

```kotlin
implementation(platform("com.highcapable.betterandroid:android-bom:<version>"))

implementation("com.highcapable.betterandroid:ui-component")
implementation("com.highcapable.betterandroid:ui-component-adapter")
implementation("com.highcapable.betterandroid:ui-extension")
implementation("com.highcapable.betterandroid:system-extension")
```

请将 `<version>` 修改为此文档顶部显示的版本。

## 功能介绍

`android-bom` 本身不包含实际代码，它仅作为 Android 子模块的 BOM 用于统一管理依赖版本。

目前它会管理以下模块的版本：

- [ui-component](./ui-component.md)
- [ui-component-adapter](./ui-component-adapter.md)
- [ui-extension](./ui-extension.md)
- [system-extension](./system-extension.md)

::: tip

Jetpack Compose 相关模块不包含在此 BOM 中，它们目前仍保持独立版本。

:::