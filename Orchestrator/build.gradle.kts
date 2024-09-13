dependencies {
    implementation(project(":Message"))
    implementation("org.apache.camel.springboot:camel-lra-starter:4.7.0")
    implementation("org.apache.camel.springboot:camel-saga-starter:4.7.0")

}

tasks.bootJar {
    enabled = true
    mainClass.set("org.abondar.experimental.sagademo.orchestrator.OrchestratorApplication")
}