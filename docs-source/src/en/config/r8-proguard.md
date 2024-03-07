# R8 & Proguard Obfuscate

> In most scenarios, the app packages can be compressed through obfuscation,
> here is an introduction to how to configure obfuscation rules.

`BetterAndroid` uses the reflection function to load `ViewBinding`, so you need to ensure that
the `ViewBinding` related functions are not obfuscated, otherwise it will not be loaded.

If you reference [ui-component](../library/ui-component.md) or [ui-extension](../library/ui-extension.md) related functions,
please add the following obfuscation rules.

```
-keep class * extends android.app.Activity
-keep class * implements androidx.viewbinding.ViewBinding {
    <init>();
    *** bind(***);
    *** inflate(***);
}
```