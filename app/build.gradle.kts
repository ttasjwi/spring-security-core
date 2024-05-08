dependencies {
    // web
    implementation(Dependencies.SPRING_WEB)

    // security
    implementation(Dependencies.SPRING_SECURITY)

    // thymeleaf
    implementation(Dependencies.THYMELEAF)

    // thymeleaf
    implementation(Dependencies.THYMELEAF_EXTRAS_SECURITY)

    // logging
    implementation(project(":support:logging"))
}

tasks.getByName("bootJar") {
    enabled = true
}

tasks.getByName("jar") {
    enabled = false
}
