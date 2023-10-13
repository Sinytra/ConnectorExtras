plugins {
    id("dev.architectury.loom")
}

val versionMc: String by rootProject
val versionForge: String by rootProject

loom {
    forge {
        mixinConfig("mixins.connectorextras_geckolib_fabric_compat.json")
    }
    mixin {
        defaultRefmapName.set("refmap.connectorextras_geckolib_fabric_compat.json")
    }
}

dependencies {
    mappings(loom.officialMojangMappings())
    forge(group = "net.minecraftforge", name = "forge", version = "$versionMc-$versionForge")

    modImplementation(group = "dev.su5ed.sinytra.fabric-api", name = "fabric-rendering-v1", version = "3.0.7+e48e6c9077")

    implementation(project(path = ":intermediary-deobf", configuration = "modRuntimeClasspathMainMapped"))
}
