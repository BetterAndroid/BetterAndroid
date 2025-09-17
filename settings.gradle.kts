enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

plugins {
    id("com.highcapable.sweetdependency") version "1.0.4"
    id("com.highcapable.sweetproperty") version "1.0.8"
}

sweetDependency {
    isUseDependencyResolutionManagement = false
}

sweetProperty {
    global {
        sourcesCode {
            includeKeys("^project\\..*$".toRegex())
            isEnableRestrictedAccess = true
        }
    }

    rootProject {
        all {
            isEnable = false
        }
    }

    project(":samples") {
        all {
            isEnable = false
        }
    }
    project(":samples:app") {
        sourcesCode {
            isEnable = false
        }
    }
    project(
        ":ui-component",
        ":ui-component-adapter",
        ":ui-extension",
        ":system-extension"
    ) {
        sourcesCode {
            className = rootProject.name
        }
    }
    project(
        ":compose-extension",
        ":compose-multiplatform"
    ) {
        sourcesCode {
            isEnable = false
        }
    }
}

rootProject.name = "BetterAndroid"

include(":samples:app")
include(":ui-component", ":ui-component-adapter", ":ui-extension", ":system-extension", ":compose-extension", ":compose-multiplatform")