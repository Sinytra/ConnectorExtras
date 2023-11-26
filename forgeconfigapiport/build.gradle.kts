plugins {
    id("dev.architectury.loom")
}

val versionMc: String by rootProject
val versionForge: String by rootProject

dependencies {
    mappings(loom.officialMojangMappings())
    forge(group = "net.minecraftforge", name = "forge", version = "$versionMc-$versionForge")

    modImplementation(group = "dev.su5ed.sinytra.fabric-api", name = "fabric-api-base", version = "0.4.30+ef105b4977")
}
