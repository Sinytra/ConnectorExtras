repositories {
    maven {
        url = uri("https://maven.architectury.dev")
        content {
            includeGroup("dev.architectury")
        }
    }
    maven {
        url = uri("https://maven.latvian.dev/releases")
        content {
            includeGroup("dev.latvian.mods")
            includeGroup("dev.latvian.apps")
        }
    }
    maven {
        url = uri("https://maven.latvian.dev/mirror")
        content {
            includeGroup("com.github.rtyley")
        }
    }
}

dependencies {
    implementation("org.sinytra:forgified-fabric-loader:2.5.84+0.19.3+26.1.2")

    implementation("dev.latvian.mods:kubejs-neoforge:26.1.2-8.0.3")
    implementation("dev.latvian.mods:rhino-neoforge:2006.2.4-build.17")

    implementation("dev.architectury:architectury-neoforge:20.0.7")
}
