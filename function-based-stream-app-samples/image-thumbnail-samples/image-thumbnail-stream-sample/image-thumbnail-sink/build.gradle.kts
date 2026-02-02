plugins {
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.jib)
}

dependencies {
    implementation(platform(libs.spring.cloud.dependencies))
    implementation(libs.spring.cloud.stream)
    implementation(project(":function-based-stream-app-samples:image-thumbnail-samples:image-thumbnail-processor"))
    implementation(libs.spring.cloud.fn.file.consumer)
    implementation(libs.spring.cloud.stream.binder.kafka)
    implementation(libs.spring.cloud.stream.apps.composite.function.support)
    testImplementation(libs.spring.cloud.stream.test.binder)
    testImplementation(libs.awaitility)
    testImplementation(libs.reactor.test)
}
