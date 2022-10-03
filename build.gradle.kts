import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.20"
    application
}

application {
    mainClass.set("io.fobo66.crypto.Lab1Kt")
}

allprojects{
    repositories {
        mavenCentral()
    }
}

tasks.withType<KotlinCompile>().configureEach {
  kotlinOptions.jvmTarget = "11"
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-cli:0.3.5")
}
