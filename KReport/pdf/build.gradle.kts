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
    implementation("org.apache.pdfbox:pdfbox:3.0.1")
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])

            groupId = "org.example"
            artifactId = "pdf"
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