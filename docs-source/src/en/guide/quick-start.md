# Quick Start

> Integrate `BetterAndroid` into your project.

## Project Requirements

The project needs to be created using `Android Studio` or `IntelliJ IDEA` and be of Android or Kotlin Multiplatform
type with integrated Kotlin environment dependencies.

- Android Studio (It is recommended to get the latest version from [here](https://developer.android.com/studio))

- IntelliJ IDEA (It is recommended to get the latest version from [here](https://www.jetbrains.com/idea))

- Kotlin 1.9.0+, Gradle 8+, Java 17+, Android Gradle Plugin 8+

### Configure Repositories

The dependencies of `BetterAndroid` are published in **Maven Central** and our public repository.
You can use the following method to configure repositories.

We recommend using Kotlin DSL as the Gradle build script language.

Configure repositories in your project's `build.gradle.kts`.

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

### Configure Java Version

Modify the Java version of Kotlin in your project's `build.gradle.kts` to 17 or above.

> Kotlin DSL

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

## Functional Overview

The entire project is divided into multiple modules, these modules can exist independently of each other or depend on each other,
you can choose the modules you want to introduce as dependencies and apply them to your project.

You can click on the corresponding modules below to view detailed functionality introductions.

- [ui-component](../library/ui-component.md)
- [ui-component-adapter](../library/ui-component-adapter.md)
- [ui-extension](../library/ui-extension.md)
- [system-extension](../library/system-extension.md)
- [permission-extension](../library/permission-extension.md)
- [compose-extension](../library/compose-extension.md)
- [compose-multiplatform](../library/compose-multiplatform.md)

## Project Template

You can use the project template we provide to quickly create a project that integrates `BetterAndroid` dependencies.

- [android-app-template](https://github.com/BetterAndroid/android-app-template)
- [android-compose-app-template](https://github.com/BetterAndroid/android-compose-app-template)
- [compose-multiplatform-template](https://github.com/BetterAndroid/compose-multiplatform-template)

## Demo

You can find some samples [here](repo://tree/main/samples) to view the corresponding demo project to better understand how these functions work and quickly
select the functionality you need.

Currently, the demo functionality is not yet complete, and may be gradually improved in the future.

But you can directly read the existing documents, which are sufficient to introduce related functions.

If you are using Jetpack Compose, you can refer to [Flexi UI](https://github.com/BetterAndroid/FlexiUI).