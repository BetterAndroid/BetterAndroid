# 基本配置

> 这里介绍了 `BetterAndroid` 可能会用到的项目功能的配置方法。 

## View Binding

如果你引用了 [ui-component](../library/ui-component.md) 或 [ui-extension](../library/ui-extension.md) 相关功能，
你需要在 `buildFeatures` 中启用 `viewBinding`。

```kotlin
android {
    buildFeatures {
        viewBinding = true
    }
}
```

## Jetpack Compose

如果你引用了 [compose-extension](../library/compose-extension.md) 相关功能，你需要在 `buildFeatures` 中启用 `compose`。

请参考 [这里](https://developer.android.com/jetpack/androidx/releases/compose-kotlin) 设置正确的 `kotlinCompilerExtensionVersion`。

```kotlin
android {
    composeOptions {
        kotlinCompilerExtensionVersion = "/** Compose Compiler Version */"
    }
    buildFeatures {
        compose = true
    }
}
```