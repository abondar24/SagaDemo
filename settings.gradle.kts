plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "SagaDemo"

include(
    "Common"
//    "orchestrator-order-service",
//    "choreography-order-service",
//    "payment-service",
//    "inventory-service",
//    "notification-service"
)