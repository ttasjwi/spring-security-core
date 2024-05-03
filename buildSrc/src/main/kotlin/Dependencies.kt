object Dependencies {

    // kotlin
    const val KOTLIN_REFLECT = "org.jetbrains.kotlin:kotlin-reflect"
    const val KOTLIN_JACKSON = "com.fasterxml.jackson.module:jackson-module-kotlin"

    // spring
    const val SPRING_VALIDATION = "org.springframework.boot:spring-boot-starter-validation"

    // web
    const val SPRING_WEB = "org.springframework.boot:spring-boot-starter-web"
    const val THYMELEAF = "org.springframework.boot:spring-boot-starter-thymeleaf"
    const val THYMELEAF_EXTRAS = "org.thymeleaf.extras:thymeleaf-extras-springsecurity6"

    // security
    const val SPRING_SECURITY = "org.springframework.boot:spring-boot-starter-security"

    // logger
    const val KOTLIN_LOGGING = "io.github.oshai:kotlin-logging:${DependencyVersions.KOTLIN_LOGGING_VERSION}"

    // test
    const val SPRING_TEST = "org.springframework.boot:spring-boot-starter-test:${PluginVersions.SPRING_BOOT_VERSION}"
    const val SPRING_SECURITY_TEST = "org.springframework.security:spring-security-test"
}

