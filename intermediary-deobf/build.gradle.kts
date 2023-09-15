plugins {
    id("dev.architectury.loom")
}

val versionTechRebornEnergy: String by project

dependencies {
    mappings(loom.officialMojangMappings())

    modImplementation(group = "teamreborn", name = "energy", version = versionTechRebornEnergy) {
        isTransitive = false
    }
}
