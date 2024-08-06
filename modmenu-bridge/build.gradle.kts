plugins {
    id("dev.architectury.loom")
}

val versionForge: String by rootProject
val versionYarn: String by rootProject

dependencies {
    mappings(loom.layered { 
        mappings("net.fabricmc:yarn:$versionYarn:v2")
        mappings("dev.architectury:yarn-mappings-patch-neoforge:1.21+build.4")
    })
    neoForge("net.neoforged:neoforge:$versionForge")

    modImplementation(group = "org.sinytra", name = "forgified-fabric-loader", version = "2.5.29+0.16.0+1.21")
}
