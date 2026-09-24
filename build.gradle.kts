import dev.kikugie.stonecutter.data.ParsedVersion

plugins {
    id("dev.isxander.modstitch.base") version "0.8.5"
}

fun prop(name: String, consumer: (prop: String) -> Unit) {
    (findProperty(name) as? String?)
        ?.let(consumer)
}

val version: String = property("mod_version")!! as String

val minecraft = property("deps.minecraft") as String

// Stonecutter constants for mod loaders.
// See https://stonecutter.kikugie.dev/stonecutter/guide/comments#condition-constants
var constraint: String = name.substringAfterLast('-')
stonecutter {
    constants.match(
        constraint,
        "fabric",
        "neoforge",
        "forge"
    )

    constants {
        put("forgelike", constraint in listOf("neoforge", "forge"))
    }
}

modstitch {
    minecraftVersion = minecraft
    println("Building $version")
    println("Loader: $constraint")
    println("Platform: $platform")

    val parsedVersion = ParsedVersion(minecraft)
    javaVersion = when {
        parsedVersion >= "26.1" -> 25
        parsedVersion >= "1.20.5" -> 21
        parsedVersion >= "1.18" -> 17
        parsedVersion >= "1.17" -> 16
        else -> 8
    }

    // If parchment doesnt exist for a version yet you can safely
    // omit the "deps.parchment" property from your versioned gradle.properties
    parchment {
        prop("deps.parchment") { mappingsVersion = it }
    }

    // This metadata is used to fill out the information inside
    // the metadata files found in the templates folder.
    metadata {
        modId = property("mod_id")!! as String
        modName = "DistantHorizonsZSTD"
        modVersion = "$version+$constraint"
        modGroup = "me.andreasmelone"
        modAuthor = "AndreasMelone"
        modDescription = "Tiny minecraft mod that ships Android zstd native libraries for Distant Horizons!"
        modLicense = "LGPL-3.0"

        fun <K : Any, V : Any> MapProperty<K, V>.populate(block: MapProperty<K, V>.() -> Unit) {
            block()
        }

        replacementProperties.populate {
            // You can put any other replacement properties/metadata here that
            // modstitch doesn't initially support. Some examples below.
            put("mod_issue_tracker", "https://github.com/RaydanOMGr/DistantHorizonsZSTD/issues")
            put("mod_repo", "https://github.com/RaydanOMGr/DistantHorizonsZSTD")
            put("minecraft_version", minecraft)
            put("java_version", "" + javaVersion.get())
            prop("deps.forge") { put("forge_version", it.substringAfterLast('-')) }
        }
    }

    // Fabric Loom (Fabric)
    loom {
        // It's not recommended to store the Fabric Loader version in properties.
        // Make sure its up to date.
        fabricLoaderVersion = "0.19.3"

        // Configure loom like normal in this block.
        configureLoom {
            runConfigs.all {
                ideConfigGenerated(true)
            }
        }
    }

    // ModDevGradle (NeoForge, Forge, Forgelike)
    moddevgradle {
        prop("deps.forge") { forgeVersion = it }
        prop("deps.neoform") { neoFormVersion = it }
        prop("deps.neoforge") { neoForgeVersion = it }
        prop("deps.mcp") { mcpVersion = it }

        // Configures client and server runs for MDG, it is not done by default
        defaultRuns()

        // This block configures the `neoforge` extension that MDG exposes by default,
        // you can configure MDG like normal from here
//        configureNeoforge {
//            runs.all {
//                disableIdeRun()
//            }
//        }
    }

    mixin {
        // You do not need to specify mixins in any mods.json/toml file if this is set to
        // true, it will automatically be generated.
        addMixinsToModManifest = true

        configs.register("distanthorizonszstd")

        // Most of the time you wont ever need loader specific mixins.
        // If you do, simply make the mixin file and add it like so for the respective loader:
        // if (isLoom) configs.register("examplemod-fabric")
        // if (isModDevGradleRegular) configs.register("examplemod-neoforge")
        // if (isModDevGradleLegacy) configs.register("examplemod-forge")
    }
}

java {
    withSourcesJar()
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.jar {
    exclude("net/minecraftforge/fml/**")
}

// All dependencies should be specified through modstitch's proxy configuration.
// Wondering where the "repositories" block is? Go to "stonecutter.gradle.kts"
// If you want to create proxy configurations for more source sets, such as client source sets,
// use the modstitch.createProxyConfigurations(sourceSets["client"]) function.
dependencies {
    modstitchImplementation("maven.modrinth:distanthorizons:3.3.2-$minecraft")

    modstitch.moddevgradle {
        if(modstitch.isModDevGradleLegacy) {
            modstitchCompileOnly(annotationProcessor("io.github.llamalad7:mixinextras-common:0.5.5")!!)
            modstitchImplementation(modstitchJiJ("io.github.llamalad7:mixinextras-forge:0.5.5")!!)

        }
    }
}