plugins {
    `java-library`
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

dependencies {
    implementation(libs.assertj.core)
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.spring.core)
    // Align junit-platform-launcher with BOM-managed junit-platform-engine version
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
