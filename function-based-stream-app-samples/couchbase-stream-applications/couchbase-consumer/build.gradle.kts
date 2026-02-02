plugins {
    alias(libs.plugins.spring.boot)
}

tasks.named<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    enabled = false
}

tasks.named<Jar>("jar") {
    enabled = true
}

dependencies {
    implementation(libs.couchbase.java.client)
    compileOnly(libs.spring.boot.configuration.processor)
    annotationProcessor(libs.spring.boot.configuration.processor)
    implementation(libs.spring.cloud.fn.config.common)
    testImplementation(libs.reactor.test)
    testImplementation(libs.testcontainers.couchbase)
    testImplementation(libs.testcontainers.junit.jupiter)
}

// Tests require running Couchbase container; Maven also skips tests for this module.
tasks.named("compileTestJava") { enabled = false }
tasks.named("processTestResources") { enabled = false }
tasks.withType<Test> { enabled = false }
