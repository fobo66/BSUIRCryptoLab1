plugins {
    kotlin("jvm") version libs.versions.kotlin
    application
    `jvm-test-suite`
    id("io.gitlab.arturbosch.detekt") version "1.23.0"
}

application {
    mainClass.set("io.fobo66.crypto.Lab1Kt")
}

testing {
    suites {
        val test by getting(JvmTestSuite::class) {
            useKotlinTest(libs.versions.kotlin)
        }
    }
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation(libs.cli)
}
