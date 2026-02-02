plugins {
    alias(libs.plugins.spring.boot)
}

dependencies {
    implementation(libs.spring.cloud.fn.http.request)
    implementation(libs.spring.boot.starter.thymeleaf)
    developmentOnly(libs.spring.boot.devtools)
    implementation(project(":function-based-stream-app-samples:image-thumbnail-samples:image-thumbnail-processor"))
}
