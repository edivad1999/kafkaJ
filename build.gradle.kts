plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.9.0"
    id("org.jetbrains.intellij") version "1.17.1"
    id("org.jetbrains.compose") version "1.5.2"
}

group = "com.edivad1999"
version = "1.1"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    maven("https://androidx.dev/storage/compose-compiler/repository/")
    maven("https://www.jetbrains.com/intellij-repository/releases")
    maven("https://packages.jetbrains.team/maven/p/kpm/public")
    maven("https://github.com/JetBrains/jewel")
}
dependencies {
//    implementation("org.jetbrains.jewel:jewel-ide-laf-bridge:0.7.2") {
//        exclude(group = "org.jetbrains.kotlinx")
//    }
    implementation(compose.desktop.currentOs) {
//        exclude(group = "org.jetbrains.compose.material")
//        exclude(group = "org.jetbrains.kotlinx")
    }
}
// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
    version.set("2023.1")
    type.set("IC") // Target IDE Platform

    plugins.set(listOf("org.jetbrains.kotlin"))
}
kotlin {
    jvmToolchain(17)
}
tasks {
    wrapper {
        gradleVersion = "8.2"
    }
    // Set the JVM compatibility versions

    patchPluginXml {
        sinceBuild = "23.*"
        untilBuild = ""
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }
}
