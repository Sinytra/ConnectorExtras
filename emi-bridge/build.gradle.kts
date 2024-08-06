import org.gradle.jvm.tasks.Jar

plugins {
    id("dev.architectury.loom")
}

val versionForge: String by rootProject
val relocateDirectory: Jar.(String, String) -> Unit by rootProject.extra

repositories {
    maven("https://maven.terraformersmc.com/releases")
}

dependencies {
    mappings(loom.officialMojangMappings())
    neoForge("net.neoforged:neoforge:$versionForge")

    compileOnly(project(":extras-utils"))

    modImplementation(group = "org.sinytra", name = "forgified-fabric-loader", version = "2.5.29+0.16.0+1.21")
    modImplementation(group = "org.sinytra.forgified-fabric-api", name = "fabric-api-lookup-api-v1", version = "1.6.68+c21168c3d1")
    modImplementation(group = "org.sinytra.forgified-fabric-api", name = "fabric-transfer-api-v1", version = "5.1.16+7f12564fd1")

    modImplementation(group = "dev.emi", name = "emi-neoforge", version = "1.1.10+1.21")
}
 
tasks.remapJar {
    // Painfully move dev/emi into relocate/dev/emi - gradle does NOT make this easy ...
    relocateDirectory(this, "dev/emi", "relocate/dev/emi")
}
