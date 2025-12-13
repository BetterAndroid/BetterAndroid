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
        mavenLocal()
    }
}

plugins {
    id("com.highcapable.gropify") version "1.0.1"
}

gropify {
    global {
        android {
            includeKeys("^project\\..*$".toRegex())
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
    projects(":samples:app") {
        android {
            isEnabled = false
        }
    }
    projects(
        ":ui-component",
        ":ui-component-adapter",
        ":ui-extension",
        ":system-extension"
    ) {
        android {
            className = rootProject.name
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
    projects(
        ":ui-extension-lint"
    ) {
        jvm {
            className = rootProject.name
        }
    }
}

rootProject.name = "BetterAndroid"

include(":samples:app")
include(
    ":ui-component",
    ":ui-component-adapter",
    ":ui-extension",
    ":ui-extension-lint",
    ":system-extension",
    ":compose-extension",
    ":compose-multiplatform"
)