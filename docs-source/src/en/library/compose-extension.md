# compose-extension

![GitHub release](https://img.shields.io/github/v/release/BetterAndroid/BetterAndroid?display_name=release&logo=github&color=green&filter=compose-extension-*)
<span style="margin-left: 5px"/>
![Android Min SDK](https://img.shields.io/badge/Min%20SDK-21-orange?logo=android)

This is a dependency for Jetpack Compose related functional extensions and supports multiple platforms.

Currently supported platforms: Android, iOS, Desktop (JVM).

## Configure Dependency

You can add this module to your project using the following method.

This is a Kotlin Multiplatform dependency, you need the `org.jetbrains.kotlin.multiplatform` plugin to apply the relevant dependencies.

### SweetDependency Method

Add dependencies to your project `SweetDependency` configuration file.

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

Configure dependencies in your project `build.gradle.kts`.

If you use multi-platform dependencies in a regular project, you only need to deploy the corresponding platform suffix dependencies as needed.

```kotlin
implementation(com.highcapable.betterandroid.compose.extension.android)
implementation(com.highcapable.betterandroid.compose.extension.iosarm64)
implementation(com.highcapable.betterandroid.compose.extension.iosx64)
implementation(com.highcapable.betterandroid.compose.extension.iossimulatorarm64)
implementation(com.highcapable.betterandroid.compose.extension.desktop)
```

If you use multi-platform dependencies in a multi-platform project, you need to add the `compose-extension` dependency in `commonMain`.

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

### Traditional Method (Not Recommended)

Configure dependencies in your project `build.gradle.kts`.

If you use multi-platform dependencies in a regular project, you only need to deploy the corresponding platform suffix dependencies as needed.

```kotlin
implementation("com.highcapable.betterandroid:compose-extension-android:<version>")
implementation("com.highcapable.betterandroid:compose-extension-iosarm64:<version>")
implementation("com.highcapable.betterandroid:compose-extension-iosx64:<version>")
implementation("com.highcapable.betterandroid:compose-extension-iossimulatorarm64:<version>")
implementation("com.highcapable.betterandroid:compose-extension-desktop:<version>")
```

If you use multi-platform dependencies in a multi-platform project, you need to add the `compose-extension` dependency in `commonMain`.

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

Please change `<version>` to the version displayed at the top of this document.

## Function Introduction

You can view the KDoc [click here](kdoc://compose-extension).

<!--------------- To be migrated ---------------

**Functional Structure**

- [x] UI Related Function Extensions
    - Color, Border, BorderStroke
        - Compose basic UI system extension
    - ComponentPadding
        - Extension of PaddingValues to have copy and other functions
    - Foundation
        - Basic function extensions
    - ImageVector
        - Vector drawing extensions
    - Unit
        - orNull extension for layout measurement units etc
    - Dialog, Popup
        - Special writing optimization for Android platform
- [x] Platform Features Extensions
    - SystemBars, WindowInsets
        - System bars and window insets related function extensions, supports Android and iOS
    - BackHandler
        - Back event handler, only support Android

--------------- To be migrated --------------->