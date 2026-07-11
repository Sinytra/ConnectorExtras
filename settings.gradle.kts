pluginManagement {
    repositories {
        gradlePluginPortal()
        maven {
            name = "NeoForged"
            url = uri("https://maven.neoforged.net/releases")
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

include(
    "rei-bridge",
    "energy-bridge",
//    "emi-bridge",
    "terrablender-bridge",
    "modmenu-bridge",
    "forgeconfigapiport",
    "extras-utils",
    "jei-bridge"
)
