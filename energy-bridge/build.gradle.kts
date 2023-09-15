plugins {
    id("dev.architectury.loom")
}

val versionMc: String by rootProject
val versionForge: String by rootProject

repositories {
    maven {
        name = "Sinytra"
        url = uri("https://maven.su5ed.dev/releases")
    }
}

dependencies {
    mappings(loom.officialMojangMappings())
    forge(group = "net.minecraftforge", name = "forge", version = "$versionMc-$versionForge")

    implementation(group = "dev.su5ed.sinytra", name = "fabric-loader", version = "2.3.4+0.14.21+1.20.1")
    modImplementation(group = "dev.su5ed.sinytra.fabric-api", name = "fabric-transfer-api-v1", version = "3.3.1+6acac45477")
    modImplementation(group = "dev.su5ed.sinytra.fabric-api", name = "fabric-api-lookup-api-v1", version = "1.6.35+561530ec77")

    implementation(project(path = ":intermediary-deobf", configuration = "modRuntimeClasspathMainMapped"))
}
