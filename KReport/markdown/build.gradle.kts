plugins {
    kotlin("jvm")
    `maven-publish`
}

group = "org.example"
version = "unspecified"

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation(project(":spec"))
    implementation("org.commonmark:commonmark:0.21.0")
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])

            groupId = "org.example"
            artifactId = "markdown"
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