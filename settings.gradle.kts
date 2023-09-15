pluginManagement {
    repositories {
        gradlePluginPortal()
        maven {
            name = "MinecraftForge"
            url = uri("https://maven.minecraftforge.net")
        }
        maven {
            name = "Sponge Snapshots"
            url = uri("https://repo.spongepowered.org/repository/maven-public")
        }
        maven {
            name = "Architectury"
            url = uri("https://maven.architectury.dev")
        }
        maven {
            name = "FabricMC"
            url = uri("https://maven.fabricmc.net")
        }
    }
    plugins {
        id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
    }
}

rootProject.name = "ConnectorExtras"

include("reach-entity-attributes", "rei-bridge", "energy-bridge", "intermediary-deobf")
