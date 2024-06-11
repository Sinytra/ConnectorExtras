plugins {
    id("dev.architectury.loom")
}

val versionMc: String by rootProject
val versionForge: String by rootProject

loom {
    forge {
        mixinConfig("connectorextras_architectury_bridge.mixins.json")
    }
    mixin {
        defaultRefmapName.set("mixins.connectorextras_architectury_bridge.refmap.json")
    }
}

repositories {
    mavenLocal()
    maven {
        name = "Sinytra"
        url = uri("https://maven.su5ed.dev/releases")
    }
    maven { 
        name = "Architectury"
        url = uri("https://maven.architectury.dev")
    }
}

dependencies {
    mappings(loom.officialMojangMappings())
    forge(group = "net.minecraftforge", name = "forge", version = "$versionMc-$versionForge")

    modImplementation("org.sinytra:Connector:1.0.0-beta.44+1.20.1+dev-g91b161f") //mavenLocal
    //modImplementation(group = "curse.maven", name = "sinytra-connector-890127", version = "4774408")
    modImplementation(group = "dev.architectury", name = "architectury-forge", version = "9.1.12")
}
