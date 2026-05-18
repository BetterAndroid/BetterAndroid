plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.dokka)
    alias(libs.plugins.maven.publish)
}

group = gropify.project.groupName
version = gropify.project.compose.multiplatform.version

kotlin {
    android {
        namespace = gropify.project.compose.multiplatform.namespace
        compileSdk = gropify.project.android.compileSdk
        minSdk = gropify.project.android.minSdk
    }

    jvm("desktop")

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = gropify.project.compose.multiplatform.iosModuleName
            isStatic = true
        }
        iosTarget.compilations.getByName("main") {
            cinterops {
                //  Workaround to override uikit classes
                val uikit by cinterops.creating
            }
        }
    }

    jvmToolchain(17)

    sourceSets {
        all {
            languageSettings {
                optIn("androidx.compose.ui.ExperimentalComposeUiApi")
                optIn("androidx.compose.foundation.ExperimentalFoundationApi")
            }
        }

        val commonMain by getting {
            dependencies {
                implementation(libs.compose.runtime)
                implementation(libs.compose.foundation)

                implementation(projects.composeExtension)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(libs.androidx.core.ktx)
                implementation(libs.androidx.activity)
                implementation(libs.androidx.activity.compose)

                implementation(projects.uiComponent)
                implementation(projects.uiExtension)
            }
        }
        val desktopMain by getting
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        listOf(iosX64Main, iosArm64Main, iosSimulatorArm64Main).forEach {
            it.languageSettings {
                optIn("kotlinx.cinterop.ExperimentalForeignApi")
                optIn("kotlinx.cinterop.BetaInteropApi")
            }
        }
        val iosMain by creating {
            dependsOn(commonMain)
            languageSettings {
                optIn("kotlinx.cinterop.ExperimentalForeignApi")
                optIn("kotlinx.cinterop.BetaInteropApi")
            }
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }
    }

    compilerOptions {
        // Workaround for https://youtrack.jetbrains.com/issue/KT-61573
        freeCompilerArgs = listOf("-Xexpect-actual-classes")
    }
}