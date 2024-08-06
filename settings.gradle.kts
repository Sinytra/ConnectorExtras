pluginManagement {
    repositories {
        gradlePluginPortal()
        maven {
            name = "NeoForged"
            url = uri("https://maven.neoforged.net")
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

include(
//    "reach-entity-attributes",
    "rei-bridge",
//    "energy-bridge",
//    "intermediary-deobf",
    "emi-bridge",
//    "architectury-bridge",
    "terrablender-bridge",
//    "modmenu-bridge",
//    "amecs-api",
//    "forgeconfigapiport",
    "extras-utils",
//    "kubejs-bridge",
//    "jei-bridge",
//    "pehkui-bridge"
)
