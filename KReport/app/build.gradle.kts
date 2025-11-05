plugins {
    kotlin("jvm")
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
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(11)
}