import me.modmuss50.mpp.ReleaseType
import net.fabricmc.loom.util.FileSystemUtil
import org.gradle.jvm.tasks.Jar
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.StandardCopyOption

plugins {
    java
    id("dev.architectury.loom") version "1.3-SNAPSHOT"
    id("me.modmuss50.mod-publish-plugin") version "0.3.+"
    id("net.neoforged.gradleutils") version "2.0.+"
}

val versionMc: String by rootProject
val versionForge: String by rootProject
val versionConnectorExtras: String by rootProject
val curseForgeId: String by project
val modrinthId: String by project
val githubRepository: String by project
val publishBranch: String by project
val connectorCurseForge: String by project
val connectorModrinth: String by project

val CI: Provider<String> = providers.environmentVariable("CI")

group = "dev.su5ed.sinytra"
version = "$versionConnectorExtras+$versionMc"
// Append git commit hash for dev versions
if (!CI.isPresent) {
    version = "$version+dev-${gradleutils.gitInfo["hash"]}"
}
println("Project version: $version")

allprojects {
    apply(plugin = "java")
    apply(plugin = "dev.architectury.loom")

    java {
        toolchain.languageVersion.set(JavaLanguageVersion.of(17))
        withSourcesJar()
    }

    repositories {
        maven {
            name = "Sinytra"
            url = uri("https://maven.su5ed.dev/releases")
        }
    }

    dependencies {
        minecraft(group = "com.mojang", name = "minecraft", version = versionMc)
    }

    tasks {
        jar {
            manifest.attributes("Implementation-Version" to provider { project.version })
        }
    }
}

subprojects {
    afterEvaluate {
        if (version == "unspecified") {
            version = rootProject.version
            logger.lifecycle("Setting default version of project $name to $version")
        }
    }
}

repositories {
    maven {
        name = "Cursemaven"
        url = uri("https://cursemaven.com")
    }
}

dependencies {
    mappings(loom.officialMojangMappings())
    forge("net.minecraftforge:forge:$versionMc-$versionForge")

    includeProject("reach-entity-attributes")
    includeProject("rei-bridge")
    includeProject("emi-bridge")
    includeProject("energy-bridge")
    includeProject("architectury-bridge")
    includeProject("terrablender-bridge")
    includeProject("geckolib-fabric-compat")
    includeProject("modmenu-bridge")

    // Misc
    modImplementation("curse.maven:mcpitanlibarch-682213:4723157")
}

fun DependencyHandlerScope.includeProject(name: String) {
    runtimeOnly(project(path = ":$name", configuration = "namedElements"))
    include(project(":$name"))
}

val relocateDirectory: Jar.(String, String) -> Unit by extra { from, to ->
    doLast {
        FileSystemUtil.getJarFileSystem(archiveFile.get().asFile.toPath(), false).use { fs ->
            val fromPath = fs.getPath(from)
            val toPath = fs.getPath(to)
            Files.walk(fromPath).forEach {
                Files.move(
                    it,
                    toPath.resolve(fromPath.relativize(it)).also { Files.createDirectories(it.parent) },
                    StandardCopyOption.COPY_ATTRIBUTES
                )
            }
            Files.walk(fromPath).sorted(Comparator.reverseOrder()).forEach(Files::delete)
        }
    }
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

publishMods {
    file.set(tasks.remapJar.flatMap { it.archiveFile })
    changelog.set(providers.environmentVariable("CHANGELOG").orElse("# $version"))
    type.set(providers.environmentVariable("PUBLISH_RELEASE_TYPE").orElse("alpha").map(ReleaseType::of))
    modLoaders.add("forge")
    dryRun.set(!CI.isPresent)

    github {
        accessToken.set(providers.environmentVariable("GITHUB_TOKEN"))
        repository.set(githubRepository)
        commitish.set(publishBranch)
    }
    curseforge {
        accessToken.set(providers.environmentVariable("CURSEFORGE_TOKEN"))
        projectId.set(curseForgeId)
        minecraftVersions.add(versionMc)
        requires {
            slug.set(connectorCurseForge)
        }
        optional { slug.set("roughly-enough-items") }
        optional { slug.set("emi") }
        optional { slug.set("terrablender") }
        optional { slug.set("architectury-api") }
        optional { slug.set("geckolib") }
    }
    modrinth {
        accessToken.set(providers.environmentVariable("MODRINTH_TOKEN"))
        projectId.set(modrinthId)
        minecraftVersions.add(versionMc)
        requires {
            id.set(connectorModrinth)
        }
        optional { id.set("nfn13YXA") } // REI
        optional { id.set("fRiHVvU7") } // EMI
        optional { id.set("kkmrDlKT") } // TerraBlender
        optional { id.set("lhGA9TYQ") } // Architectury API
        optional { id.set("8BmcQJ2H") } // Geckolib
        optional { id.set("mOgUt4GM") } // Mod Menu
    }
}
