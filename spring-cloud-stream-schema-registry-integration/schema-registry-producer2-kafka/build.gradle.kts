plugins {
    alias(libs.plugins.spring.boot)
}

dependencies {
    implementation(libs.spring.cloud.stream.binder.kafka)
    implementation(libs.spring.cloud.stream.schema.registry.client)
    implementation(libs.avro)
    testImplementation(libs.spring.cloud.stream.test.binder)
    testImplementation(libs.awaitility)
}
