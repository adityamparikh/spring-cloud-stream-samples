plugins {
    alias(libs.plugins.spring.boot) apply false
    alias(libs.plugins.spring.dependency.management) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.spring) apply false
}

val aggregatorModules = setOf(
    "confluent-schema-registry-integration",
    "function-based-stream-app-samples",
    "image-thumbnail-samples",
    "image-thumbnail-stream-sample",
    "image-thumbnail-processor",
    "couchbase-stream-applications",
    "kafka-e2e-kotlin-sample",
    "kafka-security-samples",
    "kafka-streams-samples",
    "kinesis-samples",
    "multi-functions-samples",
    "multi-binder-samples",
    "partitioning-samples",
    "kafka-partitioning",
    "rabbit-partitioning",
    "routing-samples",
    "spring-cloud-stream-schema-registry-integration",
    "testing-samples",
    "transaction-kafka-samples",
)

allprojects {
    group = "io.spring.cloud.stream.sample"
    version = "0.0.1-SNAPSHOT"
}

subprojects {
    if (name in aggregatorModules) return@subprojects

    apply(plugin = "java")
    apply(plugin = "io.spring.dependency-management")

    val catalog = rootProject.the<VersionCatalogsExtension>().named("libs")

    the<io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension>().apply {
        imports {
            mavenBom(catalog.findLibrary("spring-cloud-stream-dependencies").get().get().toString())
        }
    }

    configure<JavaPluginExtension> {
        toolchain {
            languageVersion = JavaLanguageVersion.of(17)
        }
    }

    dependencies {
        "implementation"(catalog.findLibrary("spring-boot-starter").get())
        "implementation"(catalog.findLibrary("spring-boot-starter-actuator").get())
        "implementation"(catalog.findLibrary("spring-boot-starter-web").get())
        "testImplementation"(catalog.findLibrary("spring-boot-starter-test").get())
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}
