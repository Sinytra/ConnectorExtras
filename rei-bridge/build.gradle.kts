repositories {
    maven("https://maven.shedaniel.me")
}

dependencies {
    implementation("org.sinytra:forgified-fabric-loader:2.5.90+0.19.5+26.1.2")
    implementation("org.sinytra.forgified-fabric-api:fabric-api-lookup-api-v1:2.0.18+5f5192e39e")
    implementation("org.sinytra.forgified-fabric-api:fabric-transfer-api-v1:8.0.13+d6727f829e")

    implementation("me.shedaniel:RoughlyEnoughItems-neoforge:26.1.819")
    runtimeOnly("dev.architectury:architectury-neoforge:20.0.7")
    runtimeOnly("me.shedaniel.cloth:cloth-config-neoforge:26.1.154")
}
