plugins {
    alias(libs.plugins.spring.boot)
}

tasks.named<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    enabled = false
}

tasks.named<Jar>("jar") {
    enabled = true
}

springBoot {
    mainClass.set("")
}

dependencies {
    implementation(platform(libs.spring.cloud.dependencies))
    implementation(libs.spring.cloud.stream.binder.kafka)
    implementation(libs.spring.integration.jdbc)
    testImplementation(libs.spring.cloud.stream.test.support)
    testImplementation(libs.spring.integration.test)
    testImplementation(libs.hsqldb)
    testImplementation(libs.spring.kafka.test)
    testImplementation(libs.junit.vintage.engine)
}
