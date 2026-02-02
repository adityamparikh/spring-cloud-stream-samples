plugins {
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.jib)
}

dependencies {
    implementation(platform(libs.spring.cloud.dependencies))
    implementation(libs.spring.cloud.stream.binder.kafka.streams)
    implementation(libs.micrometer.registry.prometheus)
    implementation(libs.micrometer.registry.wavefront)
    implementation(libs.prometheus.rsocket.spring)
    testImplementation(libs.spring.kafka.test)
    testImplementation(libs.kafka.streams.test.utils)
    testImplementation(libs.junit.vintage.engine)
}
