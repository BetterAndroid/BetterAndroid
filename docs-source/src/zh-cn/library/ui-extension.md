# ui-extension

![GitHub release](https://img.shields.io/github/v/release/BetterAndroid/BetterAndroid?display_name=release&logo=github&color=green&filter=ui-extension-*)
<span style="margin-left: 5px"/>
![Android Min SDK](https://img.shields.io/badge/Min%20SDK-21-orange?logo=android)

这是针对 UI (用户界面) 相关功能扩展的一个依赖。

## 配置依赖

你可以使用如下方式将此模块添加到你的项目中。

### SweetDependency (推荐)

在你的项目 `SweetDependency` 配置文件中添加依赖。

```yaml
libraries:
  com.highcapable.betterandroid:
    ui-extension:
      version: +
```

在你的项目 `build.gradle.kts` 中配置依赖。

```kotlin
implementation(com.highcapable.betterandroid.ui.extension)
```

### 传统方式

在你的项目 `build.gradle.kts` 中配置依赖。

```kotlin
implementation("com.highcapable.betterandroid:ui-extension:<version>")
```

请将 `<version>` 修改为此文档顶部显示的版本。

## 功能介绍

你可以 [点击这里](kdoc://ui-extension) 查看 KDoc。

<!--------------- 待转移 ---------------

**功能结构**

- [x] 基础功能扩展
    - ColorsFactory
        - 系统颜色功能扩展
    - DimensionFactory
        - 布局尺寸功能扩展，例如 dp、px
    - DrawableFactory
        - 可绘制组件扩展
    - ResourcesFactory
        - App 资源扩展
    - ViewBindingFactory
        - 布局绑定扩展
- [x] 系统特性扩展
    - SystemColors
        - 系统的动态取色，在 Android 12 添加
- [x] 界面组件扩展
    - ActivityFactory
        - Activity 扩展
    - FragmentFactory
        - Fragment 扩展
    - ToastFactory
        - Toast 扩展
    - WindowFactory
        - Windows 扩展，例如调整指定界面的屏幕亮度
- [x] 图形功能扩展
    - BitmapFactory
        - 处理位图相关功能的扩展
    - BitmapBlurFactory
        - 处理位图模糊的扩展
- [x] 视图组件扩展
    - ViewFactory
        - 基础视图组件扩展
    - TextViewFactory
        - 文本框相关功能扩展

--------------- 待转移 --------------->