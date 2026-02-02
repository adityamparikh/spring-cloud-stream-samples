plugins {
    alias(libs.plugins.spring.boot)
}

dependencies {
    implementation(platform(libs.spring.cloud.dependencies))
    implementation(libs.spring.cloud.stream.binder.kafka)
    implementation(libs.spring.boot.starter.data.jpa)
    runtimeOnly(libs.mariadb.java.client)
    testImplementation(libs.spring.kafka.test)
}
