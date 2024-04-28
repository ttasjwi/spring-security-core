tasks.getByName("bootJar") {
    enabled = true
}

tasks.getByName("jar") {
    enabled = false
}

dependencies {
    // spring security
    implementation(Dependencies.SPRING_SECURITY)
    testImplementation(Dependencies.SPRING_SECURITY_TEST)
}
