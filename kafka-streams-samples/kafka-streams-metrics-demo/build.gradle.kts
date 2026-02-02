plugins {
    alias(libs.plugins.spring.boot)
}

dependencies {
    implementation(platform(libs.spring.cloud.dependencies))
    implementation(libs.kafka.streams)
    implementation(libs.spring.cloud.stream)
    implementation(libs.spring.cloud.stream.binder.kafka.streams)
    implementation(libs.micrometer.registry.prometheus)
    testImplementation(libs.spring.cloud.stream.test.support)
}
