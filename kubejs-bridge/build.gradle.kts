plugins {
    id("dev.architectury.loom")
}

val versionForge: String by rootProject

repositories {
    maven {
        url = uri("https://maven.architectury.dev")
        content {
            includeGroup("dev.architectury")
        }
    }
    maven {
        url = uri("https://maven.saps.dev/minecraft")
        content {
            includeGroup("dev.latvian.mods")
        }
    }
}

dependencies {
    mappings(loom.officialMojangMappings())
    neoForge("net.neoforged:neoforge:$versionForge")

    modImplementation("org.sinytra:connector:2.0.0-beta.1+1.21+dev-g2cf85c4")
    modImplementation("dev.latvian.mods:kubejs-neoforge:2100.7.0-build.120")
    modImplementation("dev.latvian.mods:rhino-neoforge:2006.2.4-build.17")
    modImplementation(group = "dev.architectury", name = "architectury-neoforge", version = "13.0.6")
}
