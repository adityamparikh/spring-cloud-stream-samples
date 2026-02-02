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
}

tasks.withType<Test> {
    useJUnitPlatform()
}
