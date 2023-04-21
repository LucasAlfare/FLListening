plugins {
    kotlin("jvm") version "1.8.20"
    `maven-publish`
}

group = "com.lucasalfare.fllistening"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

publishing {
    publications {
        create<MavenPublication>("Maven") {
            from(components["kotlin"])
        }
    }
}