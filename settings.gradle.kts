enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://raw.githubusercontent.com/HighCapable/maven-repository/main/repository/releases")
    }
}

plugins {
    id("com.highcapable.gropify") version "1.0.2"
}

gropify {
    global {
        sourceCode {
            includeKeys("^project\\..*$".toRegex())
            className = rootProject.name
            isRestrictedAccessEnabled = true
        }
    }

    rootProject {
        common {
            isEnabled = false
        }
    }

    projects(":samples") {
        common {
            isEnabled = false
        }
    }
    projects(":samples:demo-android") {
        android {
            isEnabled = false
        }
    }

    projects(":android-bom") {
        jvm {
            isEnabled = false
        }
    }

    projects(
        ":compose-extension",
        ":compose-multiplatform"
    ) {
        kmp {
            isEnabled = false
        }
    }
}

rootProject.name = "BetterAndroid"

include(":samples:demo-android")
include(":android-bom")
include(
    ":ui-component",
    ":ui-component-adapter",
    ":ui-extension",
    ":system-extension"
)
include(
    ":compose-extension",
    ":compose-multiplatform"
)
include(
    ":ui-component-lint",
    ":ui-component-adapter-lint",
    ":ui-extension-lint",
    ":system-extension-lint"
)