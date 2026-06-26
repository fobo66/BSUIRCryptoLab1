import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin)
    application
    `jvm-test-suite`
    alias(libs.plugins.detekt)
}

application {
    mainClass = "io.fobo66.crypto.Lab1Kt"
}

testing {
    suites {
        named<JvmTestSuite>("test") {
            useKotlinTest(libs.versions.kotlin)
        }
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_25
    targetCompatibility = JavaVersion.VERSION_25
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_25
    }
}

tasks {
    withType<dev.detekt.gradle.Detekt>().configureEach {
        // Target version of the generated JVM bytecode. It is used for type resolution.
        jvmTarget = "25"
    }
    withType<dev.detekt.gradle.DetektCreateBaselineTask>().configureEach {
        // Target version of the generated JVM bytecode. It is used for type resolution.
        jvmTarget = "25"
    }
}

dependencies {
    implementation(libs.clikt)
}
