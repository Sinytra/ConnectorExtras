import net.fabricmc.loom.util.FileSystemUtil
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.StandardCopyOption

plugins {
    java
    `maven-publish`
    id("dev.architectury.loom") version "1.3-SNAPSHOT"
}

val versionMc: String by rootProject
val versionForge: String by rootProject

group = "dev.su5ed.sinytra"

allprojects {
    apply(plugin = "java")
    apply(plugin = "dev.architectury.loom")

    version = "1.0.0"

    java {
        toolchain.languageVersion.set(JavaLanguageVersion.of(17))
        withSourcesJar()
    }

    dependencies {
        minecraft(group = "com.mojang", name = "minecraft", version = versionMc)
    }

    tasks {
        jar {
            manifest.attributes("Implementation-Version" to project.version)
        }
    }
}

dependencies {
    mappings(loom.officialMojangMappings())
    forge("net.minecraftforge:forge:$versionMc-$versionForge")

    includeProject("reach-entity-attributes")
    includeProject("rei-bridge")
    includeProject("energy-bridge")
}

fun DependencyHandlerScope.includeProject(name: String) {
    implementation(project(path = ":$name", configuration = "namedElements"))
    include(project(":$name"))
}

// Mitigate https://github.com/MinecraftForge/InstallerTools/issues/14 in fg.deobf
val relocateNestedJars by tasks.registering {
    val archiveFile = tasks.remapJar.flatMap { it.archiveFile }
    inputs.file(archiveFile)
    outputs.upToDateWhen { true }

    doLast {
        FileSystemUtil.getJarFileSystem(archiveFile.get().asFile.toPath(), false).use { fs ->
            val sourceDirectory = fs.getPath("META-INF", "jars")
            val destinationDirectory = fs.getPath("META-INF", "jarjar")
            Files.newDirectoryStream(sourceDirectory).forEach { path ->
                Files.move(path, destinationDirectory.resolve(path.fileName), StandardCopyOption.COPY_ATTRIBUTES)
            }
            Files.delete(sourceDirectory)

            val metadata = fs.getPath("META-INF", "jarjar", "metadata.json")
            val text = Files.readString(metadata, StandardCharsets.UTF_8)
            val replaced = text.replace("META-INF/jars/", "META-INF/jarjar/")
            Files.writeString(metadata, replaced, StandardCharsets.UTF_8)
        }
    }
}
tasks.remapJar {
    finalizedBy(relocateNestedJars)
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }

    repositories {
        maven {
            name = "Su5eD"
            url = uri("https://maven.su5ed.dev/releases")
            credentials {
                username = System.getenv("MAVEN_USER") ?: "not"
                password = System.getenv("MAVEN_PASSWORD") ?: "set"
            }
        }
    }
}
