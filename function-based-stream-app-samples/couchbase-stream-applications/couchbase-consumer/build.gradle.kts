plugins {
    alias(libs.plugins.spring.boot)
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
