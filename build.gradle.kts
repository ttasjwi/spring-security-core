import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version PluginVersions.SPRING_BOOT_VERSION
    id("io.spring.dependency-management") version PluginVersions.DEPENDENCY_MANAGER_VERSION
    id("org.jetbrains.kotlin.jvm") version PluginVersions.JVM_VERSION
    id("org.jetbrains.kotlin.plugin.spring") version PluginVersions.SPRING_PLUGIN_VERSION
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
}

allprojects {
    group = "com.security"
    version = "0.0.1-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}

subprojects {
    apply { plugin("org.jetbrains.kotlin.jvm") }
    apply { plugin("org.jetbrains.kotlin.plugin.spring") }
    apply { plugin("org.springframework.boot") }
    apply { plugin("io.spring.dependency-management") }


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

        // json
        implementation(Dependencies.JACKSON)

        // logging
        implementation(Dependencies.KOTLIN_LOGGING)

        // test
        testImplementation(Dependencies.SPRING_TEST)
    }

    tasks.getByName("bootJar") {
        enabled = false
    }

    tasks.getByName("jar") {
        enabled = true
    }

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
