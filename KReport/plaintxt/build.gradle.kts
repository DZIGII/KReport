plugins {
    kotlin("jvm")
    `maven-publish`
}

group = "org.example"
version = "1.0.0"

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation(project(":spec"))
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])

            groupId = "org.example"
            artifactId = "palintxt"
            version = "1.0.0"
        }
    }
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(11)
}