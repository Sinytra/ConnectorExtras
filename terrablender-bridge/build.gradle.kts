repositories {
    maven("https://maven.minecraftforge.net")
}

dependencies {
    implementation(project(":extras-utils"))

    implementation("org.sinytra:forgified-fabric-loader:2.5.90+0.19.5+26.1.2")
    implementation("com.github.glitchfiend:TerraBlender-neoforge:26.1.2-26.1.2.0.3")
}

tasks {
    compileJava {
        doLast {
            val classesDir = destinationDirectory
            val outDir = classesDir.get().asFile
            val src = File(outDir, "terrablender")
            val dest = File(outDir, "relocated/terrablender")

            if (src.exists()) {
                dest.parentFile.mkdirs()
                if (dest.exists()) dest.deleteRecursively()

                src.copyRecursively(dest, overwrite = true)
                src.deleteRecursively()
            }
        }
    }
}