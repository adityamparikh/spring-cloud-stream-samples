plugins {
    alias(libs.plugins.spring.boot)
}

dependencies {
    implementation(project(":function-based-stream-app-samples:couchbase-stream-applications:couchbase-consumer"))
    implementation(libs.spring.cloud.stream)
    implementation(libs.spring.cloud.stream.binder.kafka)
    testImplementation(libs.spring.cloud.stream.test.binder)
    testImplementation(libs.testcontainers.couchbase)
    testImplementation(libs.testcontainers.kafka)
    testImplementation(libs.testcontainers.junit.jupiter)
    testImplementation(libs.spring.cloud.stream.apps.test.support)
}

// Tests require running Couchbase container; Maven also skips tests for this module.
tasks.named("compileTestJava") { enabled = false }
tasks.named("processTestResources") { enabled = false }
tasks.withType<Test> { enabled = false }
