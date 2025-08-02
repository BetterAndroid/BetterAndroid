# R8 & Proguard Obfuscation

> In most scenarios, app packages can be compressed through obfuscation.
> Here is an introduction to how to configure obfuscation rules.

`BetterAndroid` uses reflection functionality to load `ViewBinding`, so you need to ensure that
the `ViewBinding` related functions are not obfuscated, otherwise it will fail to load.

If you reference [ui-component](../library/ui-component.md) or [ui-extension](../library/ui-extension.md) related functions,
please add the following obfuscation rules.

```
-keep class * extends android.app.Activity
-keep class * implements androidx.viewbinding.ViewBinding {
    <init>();
    *** bind(***);
    *** inflate(...);
}
```