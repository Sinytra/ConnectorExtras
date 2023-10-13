plugins {
    id("dev.architectury.loom")
}

val versionMc: String by rootProject
val versionForge: String by rootProject

loom {
    forge {
        mixinConfig("mixins.connectorextras_modmenu_bridge.json")
    }
    mixin {
        defaultRefmapName.set("refmap.connectorextras_modmenu_bridge.json")
    }
}

dependencies {
    mappings(loom.officialMojangMappings())
    forge(group = "net.minecraftforge", name = "forge", version = "$versionMc-$versionForge")

    modImplementation(group = "dev.su5ed.sinytra", name = "fabric-loader", version = "2.3.4+0.14.21+1.20.1")
}
