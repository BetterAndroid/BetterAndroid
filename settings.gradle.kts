enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
plugins {
    id("com.highcapable.sweetdependency") version "1.0.3"
    id("com.highcapable.sweetproperty") version "1.0.5"
}
sweetProperty {
    global {
        sourcesCode {
            includeKeys("^project\\..*\$".toRegex())
            isEnableRestrictedAccess = true
        }
    }
    rootProject { all { isEnable = false } }
    project(":samples") { all { isEnable = false } }
    project(":samples:app") { sourcesCode { isEnable = false } }
    project(":ui-component", ":ui-extension", ":system-extension") { sourcesCode { className = rootProject.name } }
}
rootProject.name = "BetterAndroid"
include(":samples:app")
include(":ui-component", ":ui-extension", ":system-extension")