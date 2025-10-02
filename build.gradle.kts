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
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_21
    }
}

tasks {
    withType<dev.detekt.gradle.Detekt>().configureEach {
        // Target version of the generated JVM bytecode. It is used for type resolution.
        jvmTarget = "21"
    }
    withType<dev.detekt.gradle.DetektCreateBaselineTask>().configureEach {
        // Target version of the generated JVM bytecode. It is used for type resolution.
        jvmTarget = "21"
    }
}

dependencies {
    implementation(libs.cli)
}
