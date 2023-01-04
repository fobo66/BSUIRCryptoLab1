rootProject.name = "lab1"

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
    versionCatalogs {
        create("libs") {
            version("kotlin", "1.8.0")
            library("cli", "org.jetbrains.kotlinx:kotlinx-cli:0.3.5")
        }
    }
}
