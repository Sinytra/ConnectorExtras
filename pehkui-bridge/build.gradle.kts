plugins {
    id("dev.architectury.loom")
}

val versionMc: String by rootProject
val versionForge: String by rootProject

loom {
    forge {
        mixinConfig("mixins.pehkuibridge.json")
    }
    mixin {
        defaultRefmapName.set("refmap.pehkuibridge.json")
    }
}

dependencies {
    mappings(loom.officialMojangMappings())
    forge("net.minecraftforge:forge:$versionMc-$versionForge")

    modImplementation(group = "dev.su5ed.sinytra.fabric-api", name = "fabric-api-base", version = "0.4.31+ef105b4977")

    modImplementation(group = "curse.maven", name = "pehkui-319596", version = "4974824")
}
