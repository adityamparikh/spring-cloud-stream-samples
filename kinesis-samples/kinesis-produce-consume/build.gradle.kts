plugins {
    alias(libs.plugins.spring.boot)
}

dependencies {
    implementation(platform(libs.spring.cloud.dependencies))
    implementation(libs.spring.cloud.stream.binder.kinesis)
    implementation(libs.spring.boot.starter.security)
    implementation(libs.spring.boot.starter.data.rest)
    implementation(libs.spring.boot.starter.data.jpa)
    runtimeOnly(libs.h2)
}
