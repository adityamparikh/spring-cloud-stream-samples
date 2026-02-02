pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven { url = uri("https://repo.spring.io/release") }
        maven { url = uri("https://repo.spring.io/milestone") }
        maven { url = uri("https://repo.spring.io/snapshot") }
    }
}

dependencyResolutionManagement {
    repositoriesMode = RepositoriesMode.PREFER_SETTINGS
    repositories {
        mavenCentral()
        maven { url = uri("https://repo.spring.io/release") }
        maven { url = uri("https://repo.spring.io/milestone") }
        maven { url = uri("https://repo.spring.io/snapshot") }
        maven { url = uri("https://repo.spring.io/libs-snapshot-local") }
        maven { url = uri("https://repo.spring.io/libs-milestone-local") }
        maven { url = uri("https://packages.confluent.io/maven") }
        maven { url = uri("https://oss.jfrog.org/oss-snapshot-local") }
    }
}

rootProject.name = "spring-cloud-stream-samples"

// -- Top-level modules --
include("confluent-schema-registry-integration")
include("confluent-schema-registry-integration:confluent-schema-registry-integration-consumer")
include("confluent-schema-registry-integration:confluent-schema-registry-integration-producer1")
include("confluent-schema-registry-integration:confluent-schema-registry-integration-producer2")

include("function-based-stream-app-samples")
include("function-based-stream-app-samples:image-thumbnail-samples")
include("function-based-stream-app-samples:image-thumbnail-samples:image-thumbnail-processor")
include("function-based-stream-app-samples:image-thumbnail-samples:image-thumbnail-web")
include("function-based-stream-app-samples:image-thumbnail-samples:image-thumbnail-stream-sample:image-thumbnail-sink")
include("function-based-stream-app-samples:couchbase-stream-applications")
include("function-based-stream-app-samples:couchbase-stream-applications:couchbase-consumer")
include("function-based-stream-app-samples:couchbase-stream-applications:couchbase-sink")

include("kafka-batch-sample")
include("kafka-binder-native-app")

include("kafka-e2e-kotlin-sample")
include("kafka-e2e-kotlin-sample:customer-service")
include("kafka-e2e-kotlin-sample:order-service")
include("kafka-e2e-kotlin-sample:shipping-service")

include("kafka-native-serialization")

include("kafka-security-samples")
include("kafka-security-samples:kafka-ssl-demo")

include("kafka-streams-samples")
include("kafka-streams-samples:kafka-streams-avro")
include("kafka-streams-samples:kafka-streams-word-count")
include("kafka-streams-samples:kafka-streams-branching")
include("kafka-streams-samples:kafka-streams-dlq-sample")
include("kafka-streams-samples:kafka-streams-table-join")
include("kafka-streams-samples:kafka-streams-global-table-join")
include("kafka-streams-samples:kafka-streams-message-channel")
include("kafka-streams-samples:kafka-streams-product-tracker")
include("kafka-streams-samples:kafka-streams-recoverable")
include("kafka-streams-samples:kafka-streams-aggregate")
include("kafka-streams-samples:kafka-streams-to-rabbitmq-message-channel")
include("kafka-streams-samples:kafka-streams-inventory-count")
include("kafka-streams-samples:kafka-streams-metrics-demo")
include("kafka-streams-samples:kafka-streams-destination-pattern")
include("kafka-streams-samples:kafka-streams-jaas-security")
include("kafka-streams-samples:kafka-streams-multiple-input-topics")
include("kafka-streams-samples:kafka-streams-interactive-query")

include("kinesis-samples")
include("kinesis-samples:kinesis-produce-consume")
include("kinesis-samples:kinesis-to-webflux")

include("kotlin-rabbit-functions")

include("multi-functions-samples")
include("multi-functions-samples:multi-functions-kafka")
include("multi-functions-samples:multi-functions-rabbit")
include("multi-functions-samples:function-composition-kafka")
include("multi-functions-samples:function-composition-rabbit")

include("multi-binder-samples")
include("multi-binder-samples:multi-binder-kafka-rabbit")
include("multi-binder-samples:multi-binder-two-kafka-clusters")
include("multi-binder-samples:multi-binder-sendto-dest-header")
include("multi-binder-samples:kafka-multi-binder-jaas")
include("multi-binder-samples:multi-binder-kafka-streams")
include("multi-binder-samples:multi-binder-dynamic-destinations")

include("partitioning-samples")
include("partitioning-samples:kafka-partitioning")
include("partitioning-samples:kafka-partitioning:partitioning-producer-sample-kafka")
include("partitioning-samples:kafka-partitioning:partitioning-consumer-sample-kafka")
include("partitioning-samples:rabbit-partitioning")
include("partitioning-samples:rabbit-partitioning:partitioning-producer-sample-rabbit")
include("partitioning-samples:rabbit-partitioning:partitioning-consumer-sample-rabbit")

include("routing-samples")
include("routing-samples:message-routing-callback")

include("spring-cloud-stream-schema-registry-integration")
include("spring-cloud-stream-schema-registry-integration:schema-registry-producer1-kafka")
include("spring-cloud-stream-schema-registry-integration:schema-registry-producer2-kafka")
include("spring-cloud-stream-schema-registry-integration:schema-registry-consumer-kafka")

include("batch-producer-consumer")

include("testing-samples")
include("testing-samples:testing-demo")
include("testing-samples:test-embedded-kafka")

include("transaction-kafka-samples")
include("transaction-kafka-samples:transaction-http-source")
include("transaction-kafka-samples:transaction-spring-data-processor")
include("transaction-kafka-samples:transaction-logger-sink")
