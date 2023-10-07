plugins {
    id("dev.architectury.loom")
}

val versionMc: String by rootProject
val versionForge: String by rootProject

loom {
    forge {
        mixinConfig("mixins.reibridge.json")
    }
    mixin {
        defaultRefmapName.set("refmap.reibridge.json")
    }
}

repositories {
    maven {
        name = "RoughlyEnoughItems"
        url = uri("https://maven.shedaniel.me")
    }
}

dependencies {
    mappings(loom.officialMojangMappings())
    forge("net.minecraftforge:forge:$versionMc-$versionForge")

    modImplementation(group = "dev.su5ed.sinytra", name = "fabric-loader", version = "2.3.4+0.14.21+1.20.1")
    modImplementation(group = "dev.su5ed.sinytra.fabric-api", name = "fabric-transfer-api-v1", version = "3.3.1+6acac45477")

    modImplementation(group = "me.shedaniel", name = "RoughlyEnoughItems-forge", version = "12.0.652")
    modRuntimeOnly(group = "dev.architectury", name = "architectury-forge", version = "9.1.12")
    modRuntimeOnly(group = "me.shedaniel.cloth", name = "cloth-config-forge", version = "11.1.106")
}
