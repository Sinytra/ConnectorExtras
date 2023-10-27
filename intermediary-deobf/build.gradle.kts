plugins {
    id("dev.architectury.loom")
}

val versionTechRebornEnergy: String by project

repositories {
    maven {
        name = "GeckoLib"
        url = uri("https://dl.cloudsmith.io/public/geckolib3/geckolib/maven")
    }
    maven {
        name = "CurseMaven"
        url = uri("https://cursemaven.com")
        content {
            includeGroup("curse.maven")
        }
    }
}

dependencies {
    mappings(loom.officialMojangMappings())

    modImplementation(group = "teamreborn", name = "energy", version = versionTechRebornEnergy) {
        isTransitive = false
    }
    modImplementation(group = "software.bernie.geckolib", name = "geckolib-fabric-1.20.1", version = "4.2.2") {
        isTransitive = false
    }
    modImplementation(group = "curse.maven", name = "continuity-531351", version = "4779473")
}
