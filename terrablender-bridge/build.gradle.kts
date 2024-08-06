import org.gradle.jvm.tasks.Jar

plugins {
    id("dev.architectury.loom")
}

val versionForge: String by rootProject
val relocateDirectory: Jar.(String, String) -> Unit by rootProject.extra

repositories {
    maven("https://maven.minecraftforge.net")
}

dependencies {
    mappings(loom.officialMojangMappings())
    neoForge("net.neoforged:neoforge:$versionForge")

    compileOnly(project(":extras-utils"))

    modImplementation(group = "org.sinytra", name = "forgified-fabric-loader", version = "2.5.29+0.16.0+1.21")

    modImplementation(group = "com.github.glitchfiend", name = "TerraBlender-forge", version = "1.20.1-3.0.0.167")
}
 
tasks.remapJar {
    relocateDirectory(this, "terrablender/api", "relocate/terrablender/api")
}
