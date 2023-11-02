plugins {
    autowire(libs.plugins.android.library)
    autowire(libs.plugins.kotlin.android)
    autowire(libs.plugins.kotlin.dokka)
    autowire(libs.plugins.maven.publish)
}

android {
    namespace = property.project.ui.component.groupName
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
    implementation(com.google.android.material.material)
    testImplementation(junit.junit)
    androidTestImplementation(androidx.test.ext.junit)
    androidTestImplementation(androidx.test.espresso.espresso.core)
}

tasks.register("publishKDoc") {
    group = "documentation"
    dependsOn("dokkaHtml")
    doLast {
        val docsDir = rootProject.projectDir.resolve("docs").resolve(property.project.ui.component.moduleName)
        if (docsDir.exists()) docsDir.deleteRecursively() else docsDir.mkdirs()
        layout.buildDirectory.dir("dokka/html").get().asFile.copyRecursively(docsDir)
    }
}

publishing {
    repositories {
        val repositoryDir = gradle.gradleUserHomeDir
            .resolve("highcapable-maven-repository")
            .resolve("repository")
        maven {
            name = "HighCapableMavenReleases"
            url = repositoryDir.resolve("releases").toURI()
        }
        maven {
            name = "HighCapableMavenSnapShots"
            url = repositoryDir.resolve("snapshots").toURI()
        }
    }
}

mavenPublishing {
    coordinates(property.project.groupName, property.project.ui.component.moduleName, property.project.ui.component.version)
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