import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.20"
    application
}

application {
    mainClass.set("io.fobo66.crypto.Lab1")
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
    implementation("commons-cli:commons-cli:1.5.0")
}
