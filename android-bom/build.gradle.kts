plugins {
    `java-platform`
    alias(libs.plugins.maven.publish)
}

group = gropify.project.groupName
version = gropify.project.android.bom.version

dependencies {
    constraints {
        api(projects.uiComponent)
        api(projects.uiComponentAdapter)
        api(projects.uiExtension)
        api(projects.systemExtension)
    }
}