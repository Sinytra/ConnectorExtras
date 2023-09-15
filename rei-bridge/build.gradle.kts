plugins {
    java
    `maven-publish`
    id("dev.architectury.loom") version "1.3-SNAPSHOT"
}

val versionMc: String by rootProject
val versionForge: String by rootProject
val yarnMappings: String by rootProject

group = "dev.su5ed.sinytra"
version = "0.0.0-SNAPSHOT"

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
    withSourcesJar()
}

loom {
    forge {
        mixinConfig("mixins.reibridge.json")
    }
    mixin {
        defaultRefmapName.set("refmap.reibridge.json")
    }
}

repositories {
    maven {
        name = "RoughlyEnoughItems"
        url = uri("https://maven.shedaniel.me")
    }
    maven {
        name = "Sinytra"
        url = uri("https://maven.su5ed.dev/releases")
    }
}

dependencies {
    minecraft("com.mojang:minecraft:$versionMc")
    mappings(loom.officialMojangMappings())
    forge("net.minecraftforge:forge:$versionMc-$versionForge")

    implementation(group = "dev.su5ed.sinytra", name = "fabric-loader", version = "2.3.4+0.14.21+1.20.1")
    implementation(group = "dev.su5ed.sinytra.fabric-api", name = "fabric-transfer-api-v1", version = "3.3.1+6acac45477")

    modImplementation(group = "me.shedaniel", name = "RoughlyEnoughItems-forge", version = "12.0.652")
    modRuntimeOnly(group = "dev.architectury", name = "architectury-forge", version = "9.1.12")
    modRuntimeOnly(group = "me.shedaniel.cloth", name = "cloth-config-forge", version = "11.1.106")
}

tasks {
    jar {
        manifest.attributes("Implementation-Version" to project.version)
    }
}
