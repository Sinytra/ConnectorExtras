repositories {
    maven {
        name = "Fuzs Mod Resources"
        url = uri("https://raw.githubusercontent.com/Fuzss/modresources/main/maven/")
    }
}

dependencies {
    implementation("org.sinytra.forgified-fabric-api:fabric-api-base:2.0.3+b11575294c")

    jarJar(implementation("fuzs.forgeconfigapiport:forgeconfigapiport-common-forgeapi:26.1.5") {
        version { 
            strictly("[26.1,)")
            prefer("26.1.5")
        }
    })
    implementation("fuzs.forgeconfigapiport:forgeconfigapiport-neoforge:26.1.5")
}
