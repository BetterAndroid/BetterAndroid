# Basic Configuration

> Here is an introduction to the configuration methods of project features that may be used by `BetterAndroid`.

## View Binding

If you reference [ui-component](../library/ui-component.md) or [ui-extension](../library/ui-extension.md) related functions,
you need to enable `viewBinding` in `buildFeatures`.

```kotlin
android {
    buildFeatures {
        viewBinding = true
    }
}
```

## Jetpack Compose

If you reference [compose-extension](../library/compose-extension.md) related functions, you need to enable `compose` in `buildFeatures`.

Please refer [here](https://developer.android.com/jetpack/androidx/releases/compose-kotlin) to set the correct `kotlinCompilerExtensionVersion`.

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