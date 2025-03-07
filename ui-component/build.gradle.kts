plugins {
    autowire(libs.plugins.android.library)
    autowire(libs.plugins.kotlin.android)
    autowire(libs.plugins.kotlin.dokka)
    autowire(libs.plugins.maven.publish)
}

group = property.project.groupName
version = property.project.ui.component.version

android {
    namespace = property.project.ui.component.namespace
    compileSdk = property.project.android.compileSdk

    defaultConfig {
        minSdk = property.project.android.minSdk
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
        freeCompilerArgs = listOf(
            "-Xno-param-assertions",
            "-Xno-call-assertions",
            "-Xno-receiver-assertions"
        )
    }
    buildFeatures { viewBinding = true }
}

dependencies {
    implementation(projects.uiExtension)
    implementation(projects.systemExtension)
    implementation(com.highcapable.yukireflection.api)
    implementation(androidx.core.core.ktx)
    implementation(androidx.appcompat.appcompat)
    implementation(androidx.recyclerview.recyclerview)
    implementation(androidx.viewpager.viewpager)
    implementation(androidx.viewpager2.viewpager2)
    testImplementation(junit.junit)
    androidTestImplementation(androidx.test.ext.junit)
    androidTestImplementation(androidx.test.espresso.espresso.core)
}