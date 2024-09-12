plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "SagaDemo"

include(
    "Notification",
    "Payment",
    "Inventory",
    "Order",
    "Message",
    "Orchestrator"
)