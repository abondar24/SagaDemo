dependencies {
    implementation(project(":Message"))
    implementation("org.apache.camel.springboot:camel-rest-starter:4.7.0")
    implementation("org.apache.camel.springboot:camel-servlet-starter:4.7.0")
    implementation("org.apache.camel.springboot:camel-springdoc-starter:4.7.0")
}

tasks.bootJar {
    enabled = true
    mainClass.set("org.abondar.experimental.sagademo.api.OrderApiApplication")
}