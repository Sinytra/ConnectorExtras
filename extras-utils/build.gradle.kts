plugins {
    id("dev.architectury.loom")
}

val versionForge: String by rootProject

dependencies {
    mappings(loom.officialMojangMappings())
    neoForge("net.neoforged:neoforge:$versionForge")
}

tasks.jar {
    manifest.attributes("FMLModType" to "LIBRARY")
}