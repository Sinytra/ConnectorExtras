repositories {
    maven {
        name = "FabricMC"
        url = uri("https://maven.fabricmc.net")
    }
}

dependencies {
    implementation("org.sinytra:forgified-fabric-loader:2.5.90+0.19.5+26.1.2")
    implementation("org.sinytra.forgified-fabric-api:fabric-api-lookup-api-v1:2.0.18+5f5192e39e")
    implementation("org.sinytra.forgified-fabric-api:fabric-transfer-api-v1:8.0.13+d6727f829e")

    implementation("teamreborn:energy:5.0.0") {
        isTransitive = false
    }
}
