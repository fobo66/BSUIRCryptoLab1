plugins {
    application
}

application {
    mainClassName = "io.fobo66.crypto.Lab1"
}

allprojects{
    repositories {
        mavenCentral()
    }
}

dependencies {
    implementation("commons-cli:commons-cli:1.5.0")
}