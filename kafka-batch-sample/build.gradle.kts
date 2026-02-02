plugins {
    alias(libs.plugins.spring.boot)
}

dependencies {
    implementation(platform(libs.spring.cloud.dependencies))
    implementation(libs.spring.cloud.stream)
    implementation(libs.spring.cloud.stream.binder.kafka)
    implementation(libs.spring.kafka)
    testImplementation(libs.spring.cloud.stream.test.support)
    testImplementation(libs.spring.kafka.test)
}
