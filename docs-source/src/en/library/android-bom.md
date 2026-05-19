# android-bom

![Maven Central](https://img.shields.io/maven-central/v/com.highcapable.betterandroid/android-bom?logo=apachemaven&logoColor=orange&style=flat-square)
<span style="margin-left: 5px"/>
![Maven metadata URL](https://img.shields.io/maven-metadata/v?metadataUrl=https%3A%2F%2Fraw.githubusercontent.com%2FHighCapable%2Fmaven-repository%2Frefs%2Fheads%2Fmain%2Frepository%2Freleases%2Fcom%2Fhighcapable%2Fbetterandroid%2Fandroid-bom%2Fmaven-metadata.xml&logo=apachemaven&logoColor=orange&label=highcapable-maven-releases&style=flat-square)

This is the BOM dependency for unified version management of Android modules.

## Configure Dependency

You can add this module to your project using the following method.

### Version Catalog (Recommended)

Add dependency in your project's `gradle/libs.versions.toml`.

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

Configure dependency in your project's `build.gradle.kts`.

```kotlin
implementation(platform(libs.betterandroid.android.bom))

implementation(libs.betterandroid.ui.component)
implementation(libs.betterandroid.ui.component.adapter)
implementation(libs.betterandroid.ui.extension)
implementation(libs.betterandroid.system.extension)
```

Please change `<version>` to the version displayed at the top of this document.

### Traditional Method

Configure dependency in your project's `build.gradle.kts`.

```kotlin
implementation(platform("com.highcapable.betterandroid:android-bom:<version>"))

implementation("com.highcapable.betterandroid:ui-component")
implementation("com.highcapable.betterandroid:ui-component-adapter")
implementation("com.highcapable.betterandroid:ui-extension")
implementation("com.highcapable.betterandroid:system-extension")
```

Please change `<version>` to the version displayed at the top of this document.

## Function Introduction

`android-bom` does not contain actual code itself. It only serves as the BOM of Android modules for unified dependency version management.

It currently manages the versions of the following modules:

- [ui-component](./ui-component.md)
- [ui-component-adapter](./ui-component-adapter.md)
- [ui-extension](./ui-extension.md)
- [system-extension](./system-extension.md)

::: tip

Jetpack Compose related modules are not included in this BOM, and they currently still keep independent version.

:::