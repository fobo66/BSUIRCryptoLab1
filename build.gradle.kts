import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version libs.versions.kotlin
    application
}

application {
    mainClass.set("io.fobo66.crypto.Lab1Kt")
}

allprojects {
    repositories {
        mavenCentral()
    }
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation(libs.cli)
}
