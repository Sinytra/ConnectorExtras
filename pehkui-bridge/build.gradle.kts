plugins {
    id("dev.architectury.loom")
}

val versionForge: String by rootProject

dependencies {
    mappings(loom.officialMojangMappings())
    neoForge("net.neoforged:neoforge:$versionForge")

    modImplementation(group = "org.sinytra.forgified-fabric-api", name = "fabric-api-base", version = "0.4.42+d1308dedd1")

    modImplementation(group = "curse.maven", name = "pehkui-319596", version = "5446174")
}
