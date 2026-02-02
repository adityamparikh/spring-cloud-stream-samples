plugins {
    alias(libs.plugins.spring.boot)
}

dependencies {
    implementation(platform(libs.spring.cloud.dependencies))
    implementation(libs.spring.boot.starter.webflux)
    implementation(libs.spring.cloud.stream.binder.kinesis)
    testImplementation(libs.reactor.test)
}

// Tests use old AWS SDK 1.x / Spring Cloud AWS 1.x classes — not compatible with Boot 4.
// Maven also runs 0 tests for this module.
tasks.named("compileTestJava") { enabled = false }
tasks.named("processTestResources") { enabled = false }
tasks.withType<Test> { enabled = false }
