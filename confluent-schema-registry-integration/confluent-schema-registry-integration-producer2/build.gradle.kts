plugins {
    alias(libs.plugins.spring.boot)
}

dependencies {
    implementation(libs.spring.cloud.stream.binder.kafka)
    implementation(libs.avro)
    implementation(libs.confluent.kafka.avro.serializer) {
        exclude(group = "org.slf4j", module = "slf4j-api")
        exclude(group = "org.slf4j", module = "slf4j-log4j12")
    }
    implementation(libs.confluent.kafka.schema.registry.client)
}
