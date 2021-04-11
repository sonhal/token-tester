import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val token_validation_version: String by project

plugins {
    application
    kotlin("jvm") version "1.4.31"
    id("com.google.cloud.tools.jib") version "2.8.0"
}

group = "no.sonhal"
version = "1.0-SNAPSHOT"
val mainClassKt = "no.sonhal.ApplicationKt"

repositories {
    mavenCentral()
}

application {
    mainClassName = mainClassKt
}


dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlin_version")
    implementation("io.ktor:ktor-server-cio:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-mustache:$ktor_version")
    implementation("io.ktor:ktor-server-host-common:$ktor_version")
    implementation("io.ktor:ktor-locations:$ktor_version")
    implementation("io.ktor:ktor-metrics:$ktor_version")
    implementation("io.ktor:ktor-server-sessions:$ktor_version")
    implementation("io.ktor:ktor-jackson:$ktor_version")
    implementation("io.ktor:ktor-client-core:$ktor_version")
    implementation("io.ktor:ktor-client-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-client-cio:$ktor_version")

    // JWT validation
    implementation("io.ktor:ktor-auth:$ktor_version")
    implementation("no.nav.security:token-validation-ktor:$token_validation_version")
    implementation("io.ktor:ktor-auth-jwt:$ktor_version")


    testImplementation(kotlin("test-junit5"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.0")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
    targetCompatibility = "11"
}

jib {
    from {
        image = "gcr.io/distroless/java:11"
    }
    to {
        image = "ghcr.io/sonhal/token-tester:latest"
    }
    container {
        ports = listOf("8080")
        mainClass = mainClassKt
        creationTime = "USE_CURRENT_TIMESTAMP"
    }
}