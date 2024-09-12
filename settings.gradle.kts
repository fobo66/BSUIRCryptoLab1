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
            version("kotlin", "2.0.20")
            library("cli", "org.jetbrains.kotlinx:kotlinx-cli:0.3.6")
            plugin("detekt", "io.gitlab.arturbosch.detekt").version("1.23.7")
        }
    }
}
