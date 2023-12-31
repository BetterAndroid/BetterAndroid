import com.vanniktech.maven.publish.AndroidSingleVariantLibrary
import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.KotlinMultiplatform
import com.vanniktech.maven.publish.MavenPublishBaseExtension
import org.jetbrains.dokka.gradle.DokkaTask

plugins {
    autowire(libs.plugins.kotlin.multiplatform) apply false
    autowire(libs.plugins.kotlin.android) apply false
    autowire(libs.plugins.android.application) apply false
    autowire(libs.plugins.android.library) apply false
    autowire(libs.plugins.jetbrains.compose) apply false
    autowire(libs.plugins.kotlin.dokka) apply false
    autowire(libs.plugins.maven.publish) apply false
}

libraryProjects {
    afterEvaluate {
        configure<PublishingExtension> {
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
        configure<MavenPublishBaseExtension> {
            if (name == Libraries.COMPOSE_EXTENSION)
                configure(KotlinMultiplatform(javadocJar = JavadocJar.None()))
            else configure(AndroidSingleVariantLibrary(publishJavadocJar = false))
        }
    }
    tasks.withType<DokkaTask>().configureEach {
        val configuration = """{ "footerMessage": "BetterAndroid | Apache-2.0 License | Copyright (C) 2019-2024 HighCapable" }"""
        pluginsMapConfiguration.set(mapOf("org.jetbrains.dokka.base.DokkaBase" to configuration))
    }
    tasks.register("publishKDoc") {
        group = "documentation"
        dependsOn("dokkaHtml")
        doLast {
            val docsDir = rootProject.projectDir
                .resolve("docs-source")
                .resolve("dist")
                .resolve("KDoc")
                .resolve(project.name)
            if (docsDir.exists()) docsDir.deleteRecursively() else docsDir.mkdirs()
            layout.buildDirectory.dir("dokka/html").get().asFile.copyRecursively(docsDir)
        }
    }
}

fun libraryProjects(action: Action<in Project>) {
    val libraries = listOf(
        Libraries.UI_COMPONENT,
        Libraries.UI_EXTENSION,
        Libraries.SYSTEM_EXTENSION,
        Libraries.COMPOSE_EXTENSION
    )
    allprojects { if (libraries.contains(name)) action.execute(this) }
}

object Libraries {
    const val UI_COMPONENT = "ui-component"
    const val UI_EXTENSION = "ui-extension"
    const val SYSTEM_EXTENSION = "system-extension"
    const val COMPOSE_EXTENSION = "compose-extension"
}