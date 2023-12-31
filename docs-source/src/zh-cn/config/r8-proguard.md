# R8 与 Proguard 混淆

> 大部分场景下应用程序安装包可通过混淆压缩体积，这里介绍了混淆规则的配置方法。

`BetterAndroid` 使用了反射功能来装载 `ViewBinding`，所以你需要确保 `ViewBinding` 相关功能不被混淆，否则会无法装载。

如果你引用了 [ui-component](../library/ui-component.md) 或 [ui-extension](../library/ui-extension.md) 相关功能，请添加以下混淆规则。

```
-keep class * extends android.app.Activity
-keep class * implements androidx.viewbinding.ViewBinding {
    <init>();
    *** inflate(android.view.LayoutInflater);
}
```