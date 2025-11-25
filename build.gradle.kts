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
    libVersion?.takeIf { it.isNotEmpty() }?.let { consumer.invoke(it) }
}

var loader: String = name.split("-")[1]


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
            put("mod_issue_tracker", property("mod_issue") as String)
            put("pack_format", when (property("deps.minecraft")) {
                "1.20.1" -> 15
                "1.21.1" -> 34
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

        configs.register("tooltips_core")

    }
}


// Stonecutter constants for mod loaders.
// See https://stonecutter.kikugie.dev/stonecutter/guide/comments#condition-constants

stonecutter {
    constants.putAll(mapOf<String, Boolean>(
        "fabric" to loader.equals("fabric"),
        "neoforge" to loader.equals("neoforge"),
        "forge" to loader.equals("forge"),
        "vanilla" to loader.equals("vanilla")

    ))

}

val fzzyConfigVersion = findProperty("deps.fzzy_config_version")
val fzzyMinecraftVersion = when (minecraft) {
    "1.21.1" -> "1.21"
    "1.21.4" -> "1.21.3"
    else -> minecraft
}

dependencies {

    val version = 42
    add("compileOnly", "org.projectlombok:lombok:1.18.${version}")
    add("annotationProcessor", "org.projectlombok:lombok:1.18.${version}")
    add("testCompileOnly", "org.projectlombok:lombok:1.18.${version}")
    add("testAnnotationProcessor", "org.projectlombok:lombok:1.18.${version}")

    if (modstitch.isModDevGradleLegacy) {
        modstitchModImplementation("me.fzzyhmstrs:fzzy_config:${fzzyConfigVersion}+${fzzyMinecraftVersion}+forge")
    } else {
        modstitchModImplementation(("me.fzzyhmstrs:fzzy_config:${fzzyConfigVersion}+${fzzyMinecraftVersion}+neoforge"))
    }

    doLib{
        modstitchModImplementation("maven.modrinth:nirvana-library:${loader}-${minecraft}-${libVersion}")
        modstitchModRuntimeOnly("maven.modrinth:common-network:${property("deps.common_network")}")
    }


    prop("deps.fabricapi"){
        modstitchModImplementation("net.fabricmc.fabric-api:fabric-api:$it")
    }
}