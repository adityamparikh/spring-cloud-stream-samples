plugins {
    alias(libs.plugins.spring.boot)
}

dependencies {
    implementation(platform(libs.spring.cloud.dependencies))
    implementation(libs.spring.boot.starter.webflux)
    implementation(libs.spring.cloud.stream.binder.kinesis)
    testImplementation(libs.reactor.test)
    testImplementation(libs.junit.vintage.engine)
}
