dependencies {
    implementation(project(":Message"))

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    implementation("org.flywaydb:flyway-core:10.15.0")

    runtimeOnly("com.h2database:h2")

}


tasks.bootJar {
    enabled = true
    mainClass.set("org.abondar.experimental.sagademo.order.OrderApplication")
}