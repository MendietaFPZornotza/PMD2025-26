plugins {
    java
    application
    id("org.javamodularity.moduleplugin") version "1.8.15"
    id("org.openjfx.javafxplugin") version "0.0.13"
    id("org.beryx.jlink") version "2.25.0"
    kotlin("jvm")
}

group = "org.beginsecure"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val junitVersion = "5.12.1"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

application {
    mainModule.set("org.beginsecure.mahaigaineko_app")
    mainClass.set("org.beginsecure.mahaigaineko_app.MahaigainekoApp")
}

javafx {
    version = "21.0.6"
    modules = listOf("javafx.controls", "javafx.fxml")
}

dependencies {
    implementation("commons-net:commons-net:3.11.1")
    implementation("io.github.cdimascio:dotenv-java:3.0.0")
    testImplementation("org.junit.jupiter:junit-jupiter-api:${junitVersion}")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${junitVersion}")
    implementation(kotlin("stdlib-jdk8"))
}

tasks.withType<Test> {
    useJUnitPlatform()
}

jlink {
    imageZip.set(layout.buildDirectory.file("/distributions/app-${javafx.platform.classifier}.zip"))
    options.set(listOf("--strip-debug", "--compress", "2", "--no-header-files", "--no-man-pages"))
    launcher {
        name = "app"
    }
}
