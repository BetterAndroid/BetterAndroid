# ui-component

![GitHub release](https://img.shields.io/github/v/release/BetterAndroid/BetterAndroid?display_name=release&logo=github&color=green&filter=ui-component-*)
<span style="margin-left: 5px"/>
![Android Min SDK](https://img.shields.io/badge/Min%20SDK-21-orange?logo=android)

这是针对 UI (用户界面) 相关组件的一个依赖。

## 配置依赖

你可以使用如下方式将此模块添加到你的项目中。

### SweetDependency 方式

在你的项目 `SweetDependency` 配置文件中添加依赖。

```yaml
libraries:
  com.highcapable.betterandroid:
    ui-component:
      version: +
```

在你的项目 `build.gradle.kts` 中配置依赖。

```kotlin
implementation(com.highcapable.betterandroid.ui.component)
```

### 传统方式 (不推荐)

在你的项目 `build.gradle.kts` 中配置依赖。

```kotlin
implementation("com.highcapable.betterandroid:ui-component:<version>")
```

请将 `<version>` 修改为此文档顶部显示的版本。

## 功能介绍

你可以 [点击这里](kdoc://ui-component) 查看 KDoc。

<!--------------- 待转移 ---------------

**结构一览**

- [x] Activity 组件
    - AppBindingActivity
        - 带有视图绑定的 Activity (继承于 AppCompatActivtiy)
    - AppViewsActivity
        - 基础视图组件 Activity (继承于 AppCompatActivtiy)
    - AppComponentActivity
        - 基础组件 Activity (继承于 ComponentActivtiy)
- [x] Fragment 组件
    - AppBindingFragment
        - 带有视图绑定的 Fragment
    - AppViewsFragment
        - 基础视图组件 Fragment
- [x] 适配器组件
    - CommonAdapterBuilder
        - 构建 BaseAdapter
    - PagerAdapterBuilder
        - 构建 PagerAdapter
    - RecyclerAdapterBuilder
        - 构建 RecyclerView.Adapter
- [x] 系统事件组件
    - BackPressedController
        - 返回事件控制器
- [x] 通知组件
    - NotificationCreator
        - 系统通知构建器
- [x] 系统栏组件 (状态栏、导航栏等)
    - SystemBarsController
        - 系统栏控制器

--------------- 待转移 --------------->