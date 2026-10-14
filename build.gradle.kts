import me.modmuss50.mpp.ReleaseType
import org.gradle.kotlin.dsl.get

plugins {
    java
    id("net.neoforged.moddev") version "2.0.148"
    id("me.modmuss50.mod-publish-plugin") version "2.2.1"
    id("net.neoforged.gradleutils") version "6.0.0"
}

val versionMc = project.property("versionMc") as String
val versionNeoForge = project.property("versionNeoForge") as String
val versionConnectorExtras = project.property("versionConnectorExtras") as String

val curseForgeId = project.property("curseForgeId") as String
val modrinthId = project.property("modrinthId") as String

val connectorCurseForge = project.property("connectorCurseForge") as String
val connectorModrinth = project.property("connectorModrinth") as String

val CI: Provider<String> = providers.environmentVariable("CI")

group = "org.sinytra"
version = "$versionConnectorExtras+$versionMc"
// Append git commit hash for dev versions
if (!CI.isPresent) {
    version = "$version+dev-${gradleutils.gitInfo["hash"]}"
}
println("Project version: $version")

allprojects {
    apply(plugin = "java")
    apply(plugin = "net.neoforged.moddev")
    apply(plugin = "maven-publish")

    java {
        toolchain.languageVersion.set(JavaLanguageVersion.of(25))
        withSourcesJar()
    }

    neoForge {
        version = versionNeoForge
    
        runs {
            create("client") {
                client()
            }
    
            create("server") {
                server()
                programArgument("--nogui")
            }
    
            configureEach {
                systemProperty("forge.logging.markers", "REGISTRIES,SCAN,FMLHANDSHAKE,COREMOD")
                systemProperty("mixin.debug.export", "true")
            }
        }
    
        mods {
            create(project.name) {
                sourceSet(sourceSets.main.get())
            }
        }
    }

    repositories {
        maven {
            name = "NeoForge"
            url = uri("https://maven.neoforged.net/releases")
        }
        maven {
            name = "Sinytra"
            url = uri("https://maven.su5ed.dev/releases")
        }
        maven {
            name = "CurseMaven"
            url = uri("https://cursemaven.com")
            content {
                includeGroup("curse.maven")
            }
        }
    }

    tasks {
        jar {
            manifest.attributes("Implementation-Version" to provider { project.version })
        }
    }
    
    afterEvaluate { 
        extensions.getByType<PublishingExtension>().run {
            if (publications.isEmpty()) {
                publications {
                    create<MavenPublication>("mavenJava") {
                        group = rootProject.group
                        from(components["java"])
                    }
                }
            }
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

dependencies {
    includeProject("rei-bridge")
//    includeProject("emi-bridge")
    includeProject("energy-bridge")
    includeProject("terrablender-bridge")
    includeProject("modmenu-bridge")
    includeProject("forgeconfigapiport")
    includeProject("extras-utils")
    includeProject("jei-bridge")

    // Misc
    implementation("curse.maven:mcpitanlibarch-682213:8322093")
}

tasks {
    jar {
        doLast {
            val githubOutput = System.getenv("GITHUB_OUTPUT")
            if (githubOutput != null) {
                File(githubOutput).appendText("PRIMARY_ARTIFACT=${archiveFile.get().asFile.absolutePath}")
            }
        }
    }
}

publishMods {
    file.set(tasks.jar.flatMap { it.archiveFile })
    changelog.set(providers.environmentVariable("CHANGELOG").orElse("# $version"))
    type.set(providers.environmentVariable("PUBLISH_RELEASE_TYPE").orElse("alpha").map(ReleaseType::of))
    modLoaders.add("neoforge")
    dryRun.set(!CI.isPresent)

    github {
        accessToken.set(providers.environmentVariable("GITHUB_TOKEN"))
        repository = providers.environmentVariable("GITHUB_REPOSITORY").orElse("")
        commitish = providers.environmentVariable("GITHUB_SHA").orElse("")
    }
    curseforge {
        accessToken.set(providers.environmentVariable("CURSEFORGE_TOKEN"))
        projectId.set(curseForgeId)
        minecraftVersions.add(versionMc)
        client.set(true)
        server.set(true)
        requires {
            slug.set(connectorCurseForge)
        }
        optional { slug.set("roughly-enough-items") }
//        optional { slug.set("emi") }
        optional { slug.set("terrablender") }
        optional { slug.set("modmenu") }
        optional { slug.set("forge-config-api-port") }
        optional { slug.set("jei") }
    }
    modrinth {
        accessToken.set(providers.environmentVariable("MODRINTH_TOKEN"))
        projectId.set(modrinthId)
        minecraftVersions.add(versionMc)
        requires {
            id.set(connectorModrinth)
        }
        optional { id.set("nfn13YXA") } // REI
//        optional { id.set("fRiHVvU7") } // EMI
        optional { id.set("kkmrDlKT") } // TerraBlender
        optional { id.set("mOgUt4GM") } // Mod Menu
        optional { id.set("ohNO6lps") } // Forge Config API Port
        optional { id.set("u6dRKJwZ") } // JEI
    }
}

fun DependencyHandlerScope.includeProject(name: String) {
    api(jarJar(project(":$name")) {
        isTransitive = false
    })
}