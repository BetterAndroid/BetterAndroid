# ui-component

![GitHub release](https://img.shields.io/github/v/release/BetterAndroid/BetterAndroid?display_name=release&logo=github&color=green&filter=ui-component-*)
<span style="margin-left: 5px"/>
![Android Min SDK](https://img.shields.io/badge/Min%20SDK-21-orange?logo=android)

This is a dependency for UI (user interface) related components.

## Configure Dependency

You can add this module to your project using the following method.

### SweetDependency Method

Add dependency in your project's `SweetDependency` configuration file.

```yaml
libraries:
  com.highcapable.betterandroid:
    ui-component:
      version: +
```

Configure dependency in your project `build.gradle.kts`.

```kotlin
implementation(com.highcapable.betterandroid.ui.component)
```

### Traditional Method (Not Recommended)

Configure dependency in your project `build.gradle.kts`.

```kotlin
implementation("com.highcapable.betterandroid:ui-component:<version>")
```

Please change `<version>` to the version displayed at the top of this document.

## Function Introduction

You can view the KDoc [click here](https://betterandroid.github.io/BetterAndroid/KDoc/ui-component).

<!--------------- To be migrated ---------------

**Functional Structure**

- [x] Activity Components
    - AppBindingActivity
        - Activity with view binding (Inherited from AppCompatActivtiy)
    - AppViewsActivity
        - Basic view component Activity (Inherited from AppCompatActivtiy)
    - AppComponentActivity
        - Basic component Activity (Inherited from ComponentActivtiy)
- [x] Fragment Components
    - AppBindingFragment
        - Fragment with view binding
    - AppViewsFragment
        - Basic view component Fragment
- [x] Adapter Components
    - CommonAdapterBuilder
        - Build BaseAdapter
    - PagerAdapterBuilder
        - Build PagerAdapter
    - RecyclerAdapterBuilder
        - Build RecyclerView.Adapter
- [x] System Event Components
    - BackPressedController
        - Return to event controller
- [x] Notification Components
    - NotificationCreator
        - System notification builder
- [x] System Bars Components (status bars, navigation bars, etc.)
    - SystemBarsController
        - System bars controller

--------------- To be migrated --------------->