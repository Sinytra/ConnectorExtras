import org.gradle.jvm.tasks.Jar

plugins {
    id("dev.architectury.loom")
}

val versionMc: String by rootProject
val versionForge: String by rootProject
val relocateDirectory: Jar.(String, String) -> Unit by rootProject.extra

dependencies {
    mappings(loom.officialMojangMappings())
    forge("net.minecraftforge:forge:$versionMc-$versionForge")

    compileOnly(project(":extras-utils"))
    
    modImplementation(group = "dev.su5ed.sinytra", name = "fabric-loader", version = "2.3.4+0.14.21+1.20.1")

    modImplementation(group = "com.github.glitchfiend", name = "TerraBlender-forge", version = "1.20.1-3.0.0.167")
}
 
tasks.remapJar {
    relocateDirectory(this, "terrablender/api", "relocate/terrablender/api")
}
