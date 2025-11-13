import org.gradle.kotlin.dsl.configureEach
import org.gradle.kotlin.dsl.register

plugins {
    id("dev.isxander.modstitch.base") version "0.7.1-unstable"
}

fun prop(name: String, consumer: (prop: String) -> Unit) {
    (findProperty(name) as? String?)
        ?.let(consumer)
}
val mod_version = property("mod_version") as String
val mod_id = property("mod_id") as String
val minecraft = property("deps.minecraft") as String
val libVersion = findProperty("deps.lib_version") as String?
val minecraftVersionSplit = minecraft.split('.')
fun doLib(consumer: (prop: String) -> Unit){
    libVersion?.takeIf { it.isNotEmpty() }?.let { consumer }
}

val loader = when {
    modstitch.isLoom -> "fabric"
    modstitch.isModDevGradleRegular -> "neoforge"
    modstitch.isModDevGradleLegacy -> "forge"
    else -> throw IllegalStateException("Unsupported loader")
}
modstitch {
    minecraftVersion = minecraft
    javaVersion = if (modstitch.isModDevGradleLegacy) 17 else 21

    // If parchment doesnt exist for a version yet you can safely
    // omit the "deps.parchment" property from your versioned gradle.properties
    parchment {
        prop("deps.parchment") { mappingsVersion = it }
    }

    // This metadata is used to fill out the information inside
    // the metadata files found in the templates folder.
    metadata {
        modId = mod_id
        modName = property("mod_name") as String
        modVersion = property("mod_version") as String
        modGroup = property("mod_group_id") as String
        modAuthor = property("mod_authors") as String

        fun <K, V> MapProperty<K, V>.populate(block: MapProperty<K, V>.() -> Unit) {
            block()
        }

        replacementProperties.populate {
            // You can put any other replacement properties/metadata here that
            // modstitch doesn't initially support. Some examples below.
            put("mod_issue_tracker", property("mod_issue") as String)
            put("pack_format", when (property("deps.minecraft")) {
                "1.20.1" -> 15
                "1.21.4" -> 46
                else -> throw IllegalArgumentException("Please store the resource pack version for ${property("deps.minecraft")} in build.gradle.kts! https://minecraft.wiki/w/Pack_format")
            }.toString())

            prop("deps.fzzy_config_version"){
                put("fzzy_config_version", it)
            }

            doLib{
                put("lib_version", it)
            }

        }
    }

    loom {

        prop("deps.fabricLoader") { fabricLoaderVersion = it }

        // Configure loom like normal in this block.
        configureLoom {
            runConfigs.all {
                ideConfigGenerated(false)
            }
        }
    }

    // ModDevGradle (NeoForge, Forge, Forgelike)
    moddevgradle {
        prop("deps.forge") { forgeVersion = it }
        prop("deps.neoforge") { neoForgeVersion = it }
        prop("deps.mcp") { mcpVersion = it }

        configureNeoForge {

            runs {
                configureEach {
                    systemProperty("neoforge.enabledGameTestNamespaces", mod_id)
                    disableIdeRun()
                    jvmArguments.add("-XX:+AllowEnhancedClassRedefinition")
                }
                register("client") {
                    client()
                }
                if(minecraftVersionSplit[2].toInt() >= 4 ){
                    register("clientData") {
                        clientData()
                        programArguments.addAll("--mod", mod_id, "--all", "--output", file("src/generated/resources/").getAbsolutePath(), "--existing", file("src/main/resources/").getAbsolutePath())
                    }

                    register("serverData") {
                        serverData()
                        programArguments.addAll("--mod", mod_id, "--all", "--output", file("src/generated/resources/").getAbsolutePath(), "--existing", file("src/main/resources/").getAbsolutePath())
                    }
                } else {
                    register("data") {
                        data()
                        programArguments.addAll("--mod", mod_id, "--all", "--output", file("src/generated/resources/").getAbsolutePath(), "--existing", file("src/main/resources/").getAbsolutePath())
                    }
                }

                register("server") {
                    server()
                }
                afterEvaluate{
                    this@runs.names.forEach {
                        val capitalizedName = it.replaceFirstChar(Char::uppercaseChar)
                        project.tasks.named<JavaExec>("run$capitalizedName") {
                            val toolchain = project.extensions.getByType<JavaToolchainService>()
                            javaLauncher.set(
                                toolchain.launcherFor {
                                    languageVersion.set(JavaLanguageVersion.of(project.modstitch.javaVersion.get()))
                                    vendor.set(JvmVendorSpec.JETBRAINS)
                                }
                            )
                        }
                    }
                }
            }




            runs.all {
                val capitalizedName = name.replaceFirstChar(Char::uppercaseChar)

                /*
                project.tasks.named<JavaExec>("run$capitalizedName") {
                    val toolchain = project.extensions.getByType<JavaToolchainService>()
                    javaLauncher.set(
                        toolchain.launcherFor {
                            languageVersion.set(JavaLanguageVersion.of(project.modstitch.javaVersion.get()))
                            vendor.set(JvmVendorSpec.JETBRAINS)
                        }
                    )
                }

                jvmArguments.add("-XX:+AllowEnhancedClassRedefinition")*/
            }

            mods {
                register("main") {
                    sourceSet(sourceSets.main.get())
                }
            }


        }

    }

    mixin {
        // You do not need to specify mixins in any mods.json/toml file if this is set to
        // true, it will automatically be generated.
        addMixinsToModManifest = true

        configs.register("examplemod")

        // Most of the time you wont ever need loader specific mixins.
        // If you do, simply make the mixin file and add it like so for the respective loader:
        // if (isLoom) configs.register("examplemod-fabric")
        // if (isModDevGradleRegular) configs.register("examplemod-neoforge")
        // if (isModDevGradleLegacy) configs.register("examplemod-forge")
    }
}


// Stonecutter constants for mod loaders.
// See https://stonecutter.kikugie.dev/stonecutter/guide/comments#condition-constants
var constraint: String = name.split("-")[1]
stonecutter {
    consts(
        "fabric" to constraint.equals("fabric"),
        "neoforge" to constraint.equals("neoforge"),
        "forge" to constraint.equals("forge"),
        "vanilla" to constraint.equals("vanilla")
    )
}


dependencies {
    val version = 42
    add("compileOnly", "org.projectlombok:lombok:1.18.${version}")
    add("annotationProcessor", "org.projectlombok:lombok:1.18.${version}")
    add("testCompileOnly", "org.projectlombok:lombok:1.18.${version}")
    add("testAnnotationProcessor", "org.projectlombok:lombok:1.18.${version}")

    doLib{
        modstitchModImplementation("maven.modrinth:nirvana-library:$loader-$minecraft-$it")
    }


    modstitch.loom {
        modstitchModImplementation("net.fabricmc.fabric-api:fabric-api:0.112.0+1.21.4")
    }
}