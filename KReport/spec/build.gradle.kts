plugins {
    kotlin("jvm")
    `maven-publish`
    id("org.jetbrains.dokka") version "1.8.10"
}

group = "org.example"
version = "unspecified"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])

            groupId = "org.example"
            artifactId = "spec"
            version = "unspecified"
        }
    }
}

tasks.test {
    useJUnitPlatform()
}

tasks.javadoc {
    dependsOn(tasks.dokkaJavadoc)
    doLast {
        println("Javadoc task finished!")
    }
}

tasks.dokkaJavadoc {
    outputDirectory.set(file("build/dokka/javadoc"))
}

kotlin {
    jvmToolchain(11)
}