plugins {
    id("dev.architectury.loom")
}

val versionForge: String by rootProject

dependencies {
    mappings(loom.officialMojangMappings())
    neoForge("net.neoforged:neoforge:$versionForge")

    implementation(group = "org.sinytra", name = "forgified-fabric-loader", version = "2.5.29+0.16.0+1.21")
    modImplementation(group = "org.sinytra.forgified-fabric-api", name = "fabric-api-lookup-api-v1", version = "1.6.68+c21168c3d1")
    modImplementation(group = "org.sinytra.forgified-fabric-api", name = "fabric-transfer-api-v1", version = "5.1.16+7f12564fd1")

    implementation(project(path = ":intermediary-deobf", configuration = "modRuntimeClasspathMainMapped"))
}
