plugins {
    alias(libs.plugins.spring.boot)
}

dependencies {
    implementation(platform(libs.spring.cloud.stream.dependencies))
    implementation(libs.spring.cloud.stream)
    implementation(libs.spring.cloud.stream.binder.kafka)
    implementation(libs.spring.kafka)
    testImplementation(libs.spring.kafka.test)
    testImplementation(libs.junit.vintage.engine)
}
