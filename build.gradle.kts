import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id(Plugins.SPRING_BOOT) version PluginVersions.SPRING_BOOT_VERSION
    id(Plugins.SPRING_DEPENDENCY_MANAGEMENT) version PluginVersions.DEPENDENCY_MANAGER_VERSION
    id(Plugins.KOTLIN_JVM) version PluginVersions.JVM_VERSION
    id(Plugins.KOTLIN_SPRING) version PluginVersions.SPRING_PLUGIN_VERSION
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
    apply { plugin(Plugins.KOTLIN_JVM) }
    apply { plugin(Plugins.KOTLIN_SPRING) }
    apply { plugin(Plugins.SPRING_BOOT) }
    apply { plugin(Plugins.SPRING_DEPENDENCY_MANAGEMENT) }


    dependencies {
        // kotlin
        implementation(Dependencies.KOTLIN_REFLECT)
        implementation(Dependencies.KOTLIN_JACKSON)

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
