# Quick Start

> Integrate `BetterAndroid` into your project.

## Project Requirements

The project needs to be created using `Android Studio` or `IntelliJ IDEA` and be of type Android or Kotlin Multiplatform
project and have integrated Kotlin environment dependencies.

- Android Studio (It is recommended to get the latest version [from here](https://developer.android.com/studio))

- IntelliJ IDEA (It is recommended to get the latest version [from here](https://www.jetbrains.com/idea))

- Kotlin 1.9.0+, Gradle 8+, Java 17+, Android Gradle Plugin 8+

### Configure Repositories

The dependencies of `BetterAndroid` are published in **Maven Central** and our public repository,
you can use the following method to configure repositories.

We recommend using Kotlin DSL as the Gradle build script language and [SweetDependency](https://github.com/HighCapable/SweetDependency)
to manage dependencies.

#### SweetDependency (Recommended)

Configure repositories in your project's `SweetDependency` configuration file.

```yaml
repositories:
  google:
  maven-central:
  # (Optional) You can add this URL to use our public repository
  # When Sonatype-OSS fails and cannot publish dependencies, this repository is added as a backup
  # For details, please visit: https://github.com/HighCapable/maven-repository
  highcapable-maven-releases:
    url: https://raw.githubusercontent.com/HighCapable/maven-repository/main/repository/releases
```

#### Traditional Method

Configure repositories in your project `build.gradle.kts`.

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

Modify the Java version of Kotlin in your project `build.gradle.kts` to 17 or above.

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

You can click on the corresponding modules below to view detailed function introduction.

- [ui-component](../library/ui-component.md)
- [ui-extension](../library/ui-extension.md)
- [system-extension](../library/system-extension.md)
- [permission-extension](../library/permission-extension.md)
- [compose-extension](../library/compose-extension.md)

## Demo

You can find some samples [here](repo://tree/main/samples) view the corresponding demo project to better understand how these functions work and quickly
select the functions you need.

At present, the demo functions is not perfect yet and will be improved after [Flexi UI](repo://FlexiUI) is released, you can
experience the existing functions first and report bugs to us.