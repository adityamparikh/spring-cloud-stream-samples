plugins {
    alias(libs.plugins.spring.boot)
}

dependencies {
    implementation(platform(libs.spring.cloud.dependencies))
    implementation(libs.spring.cloud.stream.binder.kafka.streams)
    implementation(libs.spring.cloud.stream)
    testImplementation(libs.spring.kafka.test)
    testImplementation(libs.kafka.streams.test.utils)
}
