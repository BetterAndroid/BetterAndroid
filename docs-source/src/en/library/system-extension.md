# system-extension

![GitHub release](https://img.shields.io/github/v/release/BetterAndroid/BetterAndroid?display_name=release&logo=github&color=green&filter=system-extension-*)
<span style="margin-left: 5px"/>
![Android Min SDK](https://img.shields.io/badge/Min%20SDK-21-orange?logo=android)

This is a dependency for system layer related extensions.

## Configure Dependency

You can add this module to your project using the following method.

### SweetDependency Method

Add dependency in your project's `SweetDependency` configuration file.

```yaml
libraries:
  com.highcapable.betterandroid:
    system-extension:
      version: +
```

Configure dependency in your project `build.gradle.kts`.

```kotlin
implementation(com.highcapable.betterandroid.system.extension)
```

### Traditional Method (Not Recommended)

Configure dependency in your project `build.gradle.kts`.

```kotlin
implementation("com.highcapable.betterandroid:system-extension:<version>")
```

Please change `<version>` to the version displayed at the top of this document.

## Function Introduction

You can view the KDoc [click here](https://betterandroid.github.io/BetterAndroid/KDoc/system-extension).

<!--------------- To be migrated ---------------

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

--------------- To be migrated --------------->