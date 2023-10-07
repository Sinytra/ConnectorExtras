import org.gradle.jvm.tasks.Jar

plugins {
    id("dev.architectury.loom")
}

val versionMc: String by rootProject
val versionForge: String by rootProject
val relocateDirectory: Jar.(String, String) -> Unit by rootProject.extra

loom {
    forge {
        mixinConfig("mixins.emibridge.json")
    }
    mixin {
        defaultRefmapName.set("refmap.emibridge.json")
    }
}

repositories {
    maven {
        name = "TerraformersMC"
        url = uri("https://maven.terraformersmc.com/releases")
    }
}

dependencies {
    mappings(loom.officialMojangMappings())
    forge("net.minecraftforge:forge:$versionMc-$versionForge")

    compileOnly(rootProject)

    modImplementation(group = "dev.su5ed.sinytra", name = "fabric-loader", version = "2.3.4+0.14.21+1.20.1")
    modImplementation(group = "dev.su5ed.sinytra.fabric-api", name = "fabric-transfer-api-v1", version = "3.3.1+6acac45477")

    modImplementation(group = "dev.emi", name = "emi-forge", version = "1.0.19+1.20.1")
}
 
tasks.remapJar {
    // Painfully move dev/emi into relocate/dev/emi - gradle does NOT make this easy ...
    relocateDirectory(this, "dev/emi", "relocate/dev/emi")
}
