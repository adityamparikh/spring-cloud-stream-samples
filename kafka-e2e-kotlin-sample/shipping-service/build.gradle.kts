plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.avro.gradle)
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

avro {
    setStringType("String")
}

tasks.named<com.github.davidmc24.gradle.plugin.avro.GenerateAvroJavaTask>("generateAvroJava") {
    source("src/main/resources/avro")
}

dependencies {
    implementation(platform(libs.spring.cloud.dependencies))
    implementation(libs.kotlin.reflect)
    implementation(libs.kotlin.stdlib)
    implementation(libs.jackson.module.kotlin)
    implementation(libs.confluent.kafka.streams.avro.serde) {
        exclude(group = "org.slf4j", module = "slf4j-log4j12")
    }
    implementation(libs.confluent.kafka.avro.serializer)
    implementation(libs.confluent.kafka.schema.registry.client)
    implementation(libs.avro)
    implementation(libs.spring.cloud.stream.binder.kafka.streams)

    implementation(libs.spring.boot.starter.webflux)
    implementation(libs.spring.cloud.stream)
    testImplementation(libs.reactor.test)
    testImplementation(libs.spring.cloud.stream.test.support)
}
