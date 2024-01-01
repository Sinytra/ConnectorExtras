plugins {
    id("dev.architectury.loom")
}

val versionMc: String by rootProject
val versionForge: String by rootProject

loom {
    forge {
        mixinConfig("mixins.jeibridge.json")
    }
    mixin {
        defaultRefmapName.set("refmap.jeibridge.json")
    }
}

repositories {
    maven {
        name = "Jared's maven"
        url = uri("https://maven.blamejared.com/")
    }
}

dependencies {
    mappings(loom.officialMojangMappings())
    forge("net.minecraftforge:forge:$versionMc-$versionForge")

    modImplementation(group = "dev.su5ed.sinytra", name = "fabric-loader", version = "2.3.4+0.14.21+1.20.1")
    modImplementation(group = "dev.su5ed.sinytra.fabric-api", name = "fabric-transfer-api-v1", version = "3.3.1+6acac45477")

    modImplementation(group = "mezz.jei", name = "jei-$versionMc-forge", version = "15.2.0.27")
}
