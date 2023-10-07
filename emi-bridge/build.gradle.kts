import net.fabricmc.loom.util.FileSystemUtil
import java.nio.file.Files
import java.nio.file.StandardCopyOption

plugins {
    id("dev.architectury.loom")
}

val versionMc: String by rootProject
val versionForge: String by rootProject

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

    modImplementation(group = "dev.su5ed.sinytra", name = "fabric-loader", version = "2.3.4+0.14.21+1.20.1")
    modImplementation(group = "dev.su5ed.sinytra.fabric-api", name = "fabric-transfer-api-v1", version = "3.3.1+6acac45477")

    modImplementation(group = "dev.emi", name = "emi-forge", version = "1.0.19+1.20.1")
}
 
tasks.remapJar {
    // Painfully move dev/emi into relocate/dev/emi - gradle does NOT make this easy ...
    doLast {
        FileSystemUtil.getJarFileSystem(archiveFile.get().asFile.toPath(), false).use { fs ->
            val from = fs.getPath("dev/emi")
            val to = fs.getPath("relocate/dev/emi")
            Files.walk(from).forEach {
                Files.move(it, to.resolve(from.relativize(it)).also { Files.createDirectories(it.parent) }, StandardCopyOption.COPY_ATTRIBUTES)
            }
            Files.walk(from).sorted(Comparator.reverseOrder()).forEach(Files::delete)
        }
    }
}
