dependencies {
    implementation(project(":Message"))
}


tasks.bootJar {
    enabled = true
    mainClass.set("org.abondar.experimental.sagademo.inventory.InventoryApplication")
}