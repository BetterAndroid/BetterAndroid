plugins {
    autowire(libs.plugins.android.application) apply false
    autowire(libs.plugins.android.library) apply false
    autowire(libs.plugins.kotlin.android) apply false
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
    }
    tasks.register("publishKDoc") {
        group = "documentation"
        dependsOn("dokkaHtml")
        doLast {
            val docsDir = rootProject.projectDir.resolve("docs").resolve(name)
            if (docsDir.exists()) docsDir.deleteRecursively() else docsDir.mkdirs()
            layout.buildDirectory.dir("dokka/html").get().asFile.copyRecursively(docsDir)
        }
    }
}

fun libraryProjects(action: Action<in Project>) {
    val libraries = listOf("ui-component", "ui-extension", "system-extension")
    allprojects { if (libraries.contains(name)) action.execute(this) }
}