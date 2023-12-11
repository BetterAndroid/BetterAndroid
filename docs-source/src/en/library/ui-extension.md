# ui-extension

![GitHub release](https://img.shields.io/github/v/release/BetterAndroid/BetterAndroid?display_name=release&logo=github&color=green&filter=ui-extension-*)
<span style="margin-left: 5px"/>
![Android Min SDK](https://img.shields.io/badge/Min%20SDK-21-orange?logo=android)

This is a dependency for UI (user interface) related extensions.

## Configure Dependency

You can add this module to your project using the following method.

### SweetDependency Method

Add dependency in your project's `SweetDependency` configuration file.

```yaml
libraries:
  com.highcapable.betterandroid:
    ui-extension:
      version: +
```

Configure dependency in your project `build.gradle.kts`.

```kotlin
implementation(com.highcapable.betterandroid.ui.extension)
```

### Traditional Method (Not Recommended)

Configure dependency in your project `build.gradle.kts`.

```kotlin
implementation("com.highcapable.betterandroid:ui-extension:<version>")
```

Please change `<version>` to the version displayed at the top of this document.

## Function Introduction

You can view the KDoc [click here](https://betterandroid.github.io/BetterAndroid/KDoc/ui-extension).

<!--------------- To be migrated ---------------

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

--------------- To be migrated --------------->