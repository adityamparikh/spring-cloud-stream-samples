plugins {
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.avro.gradle)
}

avro {
    setStringType("String")
}

tasks.named<com.github.davidmc24.gradle.plugin.avro.GenerateAvroJavaTask>("generateAvroJava") {
    source("src/main/resources/avro")
}

dependencies {
    implementation(libs.spring.cloud.stream.binder.kafka)
    implementation(libs.spring.cloud.stream.schema.registry.client)
    implementation(libs.avro)
    testImplementation(libs.spring.cloud.stream.test.binder)
    testImplementation(libs.awaitility)
}
