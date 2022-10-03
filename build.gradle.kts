plugins {
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

tasks.withType<JavaCompile>().configureEach {
  options.compilerArgs.addAll(listOf("--add-modules", "java.xml"))
}

dependencies {
    implementation("commons-cli:commons-cli:1.5.0")
}
