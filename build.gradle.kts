import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version PluginVersions.SPRING_BOOT_VERSION
    id("io.spring.dependency-management") version PluginVersions.DEPENDENCY_MANAGER_VERSION

    id("org.jetbrains.kotlin.kapt") version PluginVersions.KAPT_VERSION
    kotlin("jvm") version PluginVersions.JVM_VERSION
    kotlin("plugin.spring") version PluginVersions.SPRING_PLUGIN_VERSION
    kotlin("plugin.jpa") version PluginVersions.JPA_PLUGIN_VERSION
}

group = "com.security"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_21
}

repositories {
    mavenCentral()
}

dependencies {
    // kotlin
    implementation(Dependencies.KOTLIN_REFLECT)
    implementation(Dependencies.KOTLIN_JDK)

    // spring
    implementation(Dependencies.SPRING_VALIDATION)
    implementation(Dependencies.SPRING_WEB)

    // thymeleaf
    implementation(Dependencies.THYMELEAF)
    implementation(Dependencies.THYMELEAF_EXTRAS)

    // spring security
    implementation(Dependencies.SPRING_SECURITY)
    testImplementation(Dependencies.SPRING_SECURITY_TEST)

    // json
    implementation(Dependencies.JACKSON)

    // logging
    implementation(Dependencies.KOTLIN_LOGGING)

    // test
    testImplementation(Dependencies.SPRING_TEST)
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "21"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
