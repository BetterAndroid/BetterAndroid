plugins {
    autowire(libs.plugins.kotlin.multiplatform)
    autowire(libs.plugins.android.library)
    autowire(libs.plugins.jetbrains.compose)
    autowire(libs.plugins.kotlin.dokka)
    autowire(libs.plugins.maven.publish)
}

group = property.project.compose.extension.groupName

kotlin {
    androidTarget {
        publishLibraryVariants("release")
    }
    jvm("desktop")
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = property.project.compose.extension.iosModuleName
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
                optIn("kotlinx.cinterop.ExperimentalForeignApi")
                optIn("kotlinx.cinterop.BetaInteropApi")
                optIn("androidx.compose.ui.ExperimentalComposeUiApi")
                optIn("androidx.compose.foundation.ExperimentalFoundationApi")
            }
        }
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(androidx.core.core.ktx)
                implementation(androidx.activity.activity)
                implementation(projects.uiComponent)
            }
        }
        val desktopMain by getting
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }
    }
    @Suppress("OPT_IN_USAGE")
    compilerOptions {
        // Workaround for https://youtrack.jetbrains.com/issue/KT-61573
        freeCompilerArgs = listOf("-Xexpect-actual-classes")
    }
}

android {
    namespace = property.project.compose.extension.groupName
    compileSdk = property.project.android.compileSdk

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        minSdk = property.project.android.minSdk
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

mavenPublishing {
    coordinates(property.project.groupName, property.project.compose.extension.moduleName, property.project.compose.extension.version)
    pom {
        name = property.project.name
        description = property.project.description
        url = property.project.url
        licenses {
            license {
                name = property.project.licence.name
                url = property.project.licence.url
                distribution = property.project.licence.url
            }
        }
        developers {
            developer {
                id = property.project.developer.id
                name = property.project.developer.name
                email = property.project.developer.email
            }
        }
        scm {
            url = property.maven.publish.scm.url
            connection = property.maven.publish.scm.connection
            developerConnection = property.maven.publish.scm.developerConnection
        }
    }
    publishToMavenCentral(com.vanniktech.maven.publish.SonatypeHost.S01)
    signAllPublications()
}