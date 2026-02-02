plugins {
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.jib)
}

dependencies {
    implementation(platform(libs.spring.cloud.dependencies))
    implementation(libs.spring.cloud.stream.binder.kafka.streams)
    implementation(libs.micrometer.registry.prometheus)
    testImplementation(libs.spring.kafka.test)
    testImplementation(libs.kafka.streams.test.utils)
}
