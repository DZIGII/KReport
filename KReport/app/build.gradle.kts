plugins {
    kotlin("jvm")
    id("com.github.johnrengelman.shadow") version "8.1.1"
    application
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
    runtimeOnly(project(":plaintxt"))
    runtimeOnly(project(":pdf"))
    runtimeOnly(project(":html"))
    runtimeOnly(project(":markdown"))
    implementation("com.opencsv:opencsv:5.8")
    implementation("com.github.doyaaaaaken:kotlin-csv-jvm:1.9.2")
    implementation("org.xerial:sqlite-jdbc:3.45.1.0")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(11)
}

tasks {
    shadowJar {
        archiveBaseName.set("kreport")
        archiveVersion.set("1.0")
        archiveClassifier.set("")

        mergeServiceFiles()
    }
}

application {
    mainClass.set("App")
}