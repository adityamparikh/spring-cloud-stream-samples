plugins {
    alias(libs.plugins.spring.boot)
}

tasks.named<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    enabled = false
}

tasks.named<Jar>("jar") {
    enabled = true
}

springBoot {
    mainClass.set("")
}

dependencies {
    implementation(platform(libs.spring.cloud.dependencies))
    implementation(libs.spring.cloud.stream.binder.kafka)
    implementation(libs.spring.integration.jdbc)
    testImplementation(libs.spring.cloud.stream.test.binder)
    testImplementation(libs.spring.integration.test)
    testImplementation(libs.hsqldb)
    testImplementation(libs.spring.kafka.test)
    // Boot 4 modular auto-configuration jars needed for @ImportAutoConfiguration(exclude) in tests
    testImplementation("org.springframework.boot:spring-boot-jdbc")
    testImplementation("org.springframework.boot:spring-boot-transaction")
    testImplementation("org.springframework.boot:spring-boot-kafka")
}
