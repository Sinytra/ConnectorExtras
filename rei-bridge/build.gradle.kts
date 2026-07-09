repositories {
    maven("https://maven.shedaniel.me")
}

dependencies {
    implementation("org.sinytra:forgified-fabric-loader:2.5.84+0.19.3+26.1.2")
    implementation("org.sinytra.forgified-fabric-api:fabric-api-lookup-api-v1:2.0.12+29e133704c")
    implementation("org.sinytra.forgified-fabric-api:fabric-transfer-api-v1:8.0.6+7b5559184c")

    implementation("me.shedaniel:RoughlyEnoughItems-neoforge:26.1.819")
    runtimeOnly("dev.architectury:architectury-neoforge:20.0.7")
    runtimeOnly("me.shedaniel.cloth:cloth-config-neoforge:26.1.154")
}
