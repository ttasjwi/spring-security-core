dependencies {
    // web
    implementation(Dependencies.SPRING_WEB)

    // security
    implementation(Dependencies.SPRING_SECURITY)

    // session
    implementation(Dependencies.SPRING_SESSION_CORE)

    // logging
    implementation(project(":support:logging"))
}

tasks.getByName("bootJar") {
    enabled = true
}

tasks.getByName("jar") {
    enabled = false
}
