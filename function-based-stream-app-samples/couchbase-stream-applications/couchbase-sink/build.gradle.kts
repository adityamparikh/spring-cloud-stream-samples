plugins {
    alias(libs.plugins.spring.boot)
}

dependencies {
    implementation(project(":function-based-stream-app-samples:couchbase-stream-applications:couchbase-consumer"))
    implementation(libs.spring.cloud.stream)
    implementation(libs.spring.cloud.stream.binder.kafka)
    testImplementation(libs.testcontainers.couchbase)
    testImplementation(libs.testcontainers.kafka)
    testImplementation(libs.testcontainers.junit.jupiter)
    testImplementation(libs.spring.cloud.stream.apps.test.support)
}
