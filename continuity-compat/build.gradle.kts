plugins {
    id("dev.architectury.loom")
}

val versionMc: String by rootProject
val versionForge: String by rootProject

loom {
    forge {
        mixinConfig("mixins.connectorextras_continuity_compat.json")
    }
    mixin {
        defaultRefmapName.set("refmap.connectorextras_continuity_compat.json")
    }
}

dependencies {
    mappings(loom.officialMojangMappings())
    forge(group = "net.minecraftforge", name = "forge", version = "$versionMc-$versionForge")

    implementation(project(path = ":intermediary-deobf", configuration = "modRuntimeClasspathMainMapped"))
}
