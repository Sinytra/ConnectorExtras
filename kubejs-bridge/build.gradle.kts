plugins {
    id("dev.architectury.loom")
}

val versionMc: String by rootProject
val versionForge: String by rootProject

loom {
    forge {
        mixinConfig("mixins.connectorextras_kubejs_bridge.json")
    }
    mixin {
        defaultRefmapName.set("refmap.connectorextras_kubejs_bridge.json")
    }
}

repositories {
    maven {
        url = uri("https://maven.architectury.dev")
        content {
            includeGroup("dev.architectury")
        }
    }
    maven {
        url = uri("https://maven.saps.dev/minecraft")
        content {
            includeGroup("dev.latvian.mods")
        }
    }
}

dependencies {
    mappings(loom.officialMojangMappings())
    forge(group = "net.minecraftforge", name = "forge", version = "$versionMc-$versionForge")

    modImplementation("org.sinytra:Connector:1.0.0-beta.44+1.20.1")
    modImplementation("dev.latvian.mods:kubejs-forge:2001.6.4-build.111")
    modImplementation("dev.latvian.mods:rhino-forge:2001.2.2-build.13")
    modImplementation("dev.architectury:architectury-forge:9.1.12")
}
