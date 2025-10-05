import org.jetbrains.gradle.ext.Gradle
import org.jetbrains.gradle.ext.RunConfigurationContainer
import kotlin.apply

plugins {
    `java-gradle-plugin`
    id("com.palantir.git-version") version "3.0.0"
    `maven-publish`
    //id("com.diffplug.spotless") version "6.25.0"
    id("org.jetbrains.gradle.plugin.idea-ext") version "1.1.10"
    id("com.github.gmazzo.buildconfig") version "5.3.5"
    id("com.gtnewhorizons.retrofuturagradle") version "1.4.5"
}

val gitVersion: groovy.lang.Closure<String> by extra

group = "com.circulation.metal_revolution"
version = "1.0.0"

// Add a source set for the functional test suite
val functionalTestSourceSet = sourceSets.create("functionalTest") {}

//tasks.jar.configure {
//    manifest {
//        val attributes = manifest.attributes
//        attributes["FMLCorePlugin"] = "com.circulation.metal_revolution.mixins.M3TEarlyMixinLoader"
//        attributes["FMLCorePluginContainsFMLMod"] = true
//    }
//}

minecraft {
    val args = mutableListOf("-ea:${project.group}")
    // Mixin args
    args.add("-Dmixin.hotSwap=true")
    args.add("-Dmixin.checks.interfaces=true")
    args.add("-Dmixin.debug.export=true")
    //args.add("-Dlegacy.debugClassLoading=true")
    //args.add("-Dlegacy.debugClassLoadingSave=true")
    extraRunJvmArguments.addAll(args)

    // If needed, add extra tweaker classes like for mixins.
    extraTweakClasses.add("org.spongepowered.asm.launch.MixinTweaker")
}

repositories {
    flatDir {
        dirs("libs")
    }
    maven {
        url = uri("https://maven.aliyun.com/nexus/content/groups/public/")
    }
    maven {
        url = uri("https://maven.aliyun.com/nexus/content/repositories/jcenter")
    }
    maven {
        url = uri("https://maven.cleanroommc.com")
    }
    maven {
        url = uri("https://cfa2.cursemaven.com")
    }
    maven {
        url = uri("https://cursemaven.com")
    }
    maven {
        url = uri("https://maven.blamejared.com/")
    }
    maven {
        url = uri("https://repo.spongepowered.org/maven")
    }
    maven {
        name = "GeckoLib"
        url = uri("https://dl.cloudsmith.io/public/geckolib3/geckolib/maven/")
    }
    maven {
        name = "OvermindDL1 Maven"
        url = uri("https://gregtech.overminddl1.com/")
    }
    maven {
        name = "GTNH Maven"
        url = uri("https://nexus.gtnewhorizons.com/repository/public/")
    }
    maven {
        url = uri("https://jitpack.io")
    }
    mavenCentral()
    gradlePluginPortal()
}

fun pluginDep(name: String, version: String): String {
    return "${name}:${name}.gradle.plugin:${version}"
}

dependencies {
    annotationProcessor("com.github.bsideup.jabel:jabel-javac-plugin:1.0.1")
    testAnnotationProcessor("com.github.bsideup.jabel:jabel-javac-plugin:1.0.1")
    compileOnly("com.github.bsideup.jabel:jabel-javac-plugin:1.0.1") { isTransitive = false }
    // workaround for https://github.com/bsideup/jabel/issues/174
    annotationProcessor("net.java.dev.jna:jna-platform:5.13.0")

    // All these plugins will be present in the classpath of the project using our plugin, but not activated until explicitly applied

    // Settings plugins
    api(pluginDep("com.diffplug.blowdryerSetup", "1.7.1"))
    api(pluginDep("org.gradle.toolchains.foojay-resolver-convention", "0.9.0"))

    // Project plugins
    api(pluginDep("com.gradleup.shadow", "8.3.5"))
    api(pluginDep("com.palantir.git-version", "3.1.0"))
    api(pluginDep("org.jetbrains.kotlin.jvm", "2.1.0"))
    api(pluginDep("org.jetbrains.kotlin.kapt", "2.1.0"))
    api(pluginDep("com.google.devtools.ksp", "2.1.0-1.0.29"))
    api(
        pluginDep(
            "org.ajoberstar.grgit",
            "4.1.1"
        )
    ) // 4.1.1 is the last jvm8 supporting version, unused, available for addon.gradle
    api(pluginDep("de.undercouch.download", "5.6.0"))
    api(pluginDep("com.github.gmazzo.buildconfig", "3.1.0")) // Unused, available for addon.gradle
    api(pluginDep("com.modrinth.minotaur", "2.8.7"))
    api(pluginDep("net.darkhax.curseforgegradle", "1.1.26"))

    testImplementation("org.junit.jupiter:junit-jupiter:5.9.3")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    annotationProcessor("org.spongepowered:mixin:0.8.5:processor")

    val mixin: String =
        modUtils.enableMixins("curse.maven:!unimixins-826970:6953804", "mixins.metal_revolution.refmap.json").toString()
    api(mixin) {
        isTransitive = false
    }

    implementation("com.github.GTNewHorizons:GTNHLib:0.6.39:dev") {
        isTransitive = false
    }
    implementation(rfg.deobf("curse.maven:muya1-7-10-530214:4364097"))
    implementation(rfg.deobf("curse.maven:manametal-531708:6943759"))
    implementation("com.github.GTNewHorizons:CodeChickenCore:1.4.7:dev") {
        isTransitive = false
    }
    implementation("com.github.GTNewHorizons:NotEnoughItems:2.7.89-GTNH:dev") {
        isTransitive = false
    }
    implementation(rfg.deobf("curse.maven:smooth-285742:2614474"))
    implementation(rfg.deobf("curse.maven:forge-nbtedit-for-1-7-10-381388:2949679"))
    implementation(rfg.deobf("curse.maven:trivialthoughts-967327:6971415"))
}

gradlePlugin {
    plugins {
        website.set("https://github.com/GTNewHorizons/GTNHGradle")
        vcsUrl.set("https://github.com/GTNewHorizons/GTNHGradle.git")
        isAutomatedPublishing = false
        create("gtnhGradle") {
            id = "com.gtnewhorizons.gtnhgradle"
            implementationClass = "com.gtnewhorizons.gtnhgradle.GTNHGradlePlugin"
            displayName = "GTNHGradle"
            description = "Shared buildscript logic for all GTNH mods and some other 1.7.10 mods"
            tags.set(listOf("minecraft", "modding"))
        }
        create("gtnhConvention") {
            id = "com.gtnewhorizons.gtnhconvention"
            implementationClass = "com.gtnewhorizons.gtnhgradle.GTNHConventionPlugin"
            displayName = "GTNHConvention"
            description = "Shared buildscript logic for all GTNH mods and some other 1.7.10 mods - automatically applies all features"
            tags.set(listOf("minecraft", "modding"))
        }
        create("gtnhSettingsConvention") {
            id = "com.gtnewhorizons.gtnhsettingsconvention"
            implementationClass = "com.gtnewhorizons.gtnhgradle.GTNHSettingsConventionPlugin"
            displayName = "GTNHConvention"
            description = "Shared Settings logic for all GTNH mods and some other 1.7.10 mods"
            tags.set(listOf("minecraft", "modding"))
        }
    }
}

// Spotless autoformatter
// See https://github.com/diffplug/spotless/tree/main/plugin-gradle
// Can be locally toggled via spotless:off/spotless:on comments
//spotless {
//    encoding("UTF-8")
//
//    format ("misc") {
//        target(".gitignore")
//
//        trimTrailingWhitespace()
//        indentWithSpaces(4)
//        endWithNewline()
//    }
//    java {
//        target("src/*/java/**/*.java", "src/*/scala/**/*.java")
//
//        toggleOffOn()
//        removeUnusedImports()
//        trimTrailingWhitespace()
//        eclipse("4.19").configFile("spotless.eclipseformat.xml")
//    }
//}

buildConfig {
    useJavaOutput()
    this.packageName = "com.circulation.metal_revolution"
    buildConfigField("VERSION", version.toString())
}

// Enable Jabel for java 8 bytecode from java 17 sources
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
        vendor.set(JvmVendorSpec.AZUL)
    }
    withSourcesJar()
    //withJavadocJar()
}
tasks.javadoc {
    javadocTool.set(javaToolchains.javadocToolFor {
        languageVersion.set(JavaLanguageVersion.of(21))
        vendor.set(JvmVendorSpec.AZUL)
    })
    with(options as StandardJavadocDocletOptions) {
        links(
            "https://docs.gradle.org/${gradle.gradleVersion}/javadoc/",
            "https://docs.oracle.com/en/java/javase/21/docs/api/"
        )
    }
}
tasks.withType<JavaCompile> {
    sourceCompatibility = "21" // for the IDE support
    options.release.set(8)
    options.encoding = "UTF-8"

    javaCompiler.set(javaToolchains.compilerFor {
        languageVersion.set(JavaLanguageVersion.of(21))
        vendor.set(JvmVendorSpec.AZUL)
    })
}

tasks.wrapper.configure {
    gradleVersion = "8.13"
    distributionType = Wrapper.DistributionType.ALL
}

tasks.updateDaemonJvm.configure {
    languageVersion = JavaLanguageVersion.of(21)
}

configurations["functionalTestRuntimeOnly"].extendsFrom(configurations["testRuntimeOnly"])
configurations["functionalTestImplementation"].extendsFrom(configurations["testImplementation"])
configurations["functionalTestAnnotationProcessor"].extendsFrom(configurations["testAnnotationProcessor"])

// Add a task to run the functional tests
val functionalTest by tasks.registering(Test::class) {
    testClassesDirs = functionalTestSourceSet.output.classesDirs
    classpath = functionalTestSourceSet.runtimeClasspath
    useJUnitPlatform()
}

gradlePlugin.testSourceSets.add(functionalTestSourceSet)

tasks.check {
    // Run the functional tests as part of `check`
    dependsOn(functionalTest)
}

tasks.test {
    // Use JUnit Jupiter for unit tests.
    useJUnitPlatform()
    // Skip git-based versioning inside the tests
    environment("VERSION", "1.0.0")
}


publishing {
    publications {
        create<MavenPublication>("gtnhGradle") {
            artifactId = "gtnhgradle"
            from(components["java"])
        }
        // From org.gradle.plugin.devel.plugins.MavenPluginPublishPlugin.createMavenMarkerPublication
        for (declaration in gradlePlugin.plugins) {
            create<MavenPublication>(declaration.name + "PluginMarkerMaven") {
                artifactId = declaration.id + ".gradle.plugin"
                groupId = declaration.id
                pom {
                    name.set(declaration.displayName)
                    description.set(declaration.description)
                    withXml {
                        val root = asElement()
                        val document = root.ownerDocument
                        val dependencies = root.appendChild(document.createElement("dependencies"))
                        val dependency = dependencies.appendChild(document.createElement("dependency"))
                        val groupId = dependency.appendChild(document.createElement("groupId"))
                        groupId.textContent = project.group.toString()
                        val artifactId = dependency.appendChild(document.createElement("artifactId"))
                        artifactId.textContent = "gtnhgradle"
                        val version = dependency.appendChild(document.createElement("version"))
                        version.textContent = project.version.toString()
                    }
                }
            }
        }
    }

    repositories {
        maven {
            url = uri("https://nexus.gtnewhorizons.com/repository/releases/")
            credentials {
                username = System.getenv("MAVEN_USER") ?: "NONE"
                password = System.getenv("MAVEN_PASSWORD") ?: "NONE"
            }
        }
    }
}

idea {
    module {
        isDownloadJavadoc = false
        isDownloadSources = true
        inheritOutputDirs = true // Fix resources in IJ-Native runs
    }
    project {
        this.withGroovyBuilder {
            "settings" {
                "runConfigurations" {
                    val self = this.delegate as RunConfigurationContainer
                    self.add(Gradle("1. Run Client").apply {
                        setProperty("taskNames", listOf("runClient"))
                    })
                    self.add(Gradle("2. Run Server").apply {
                        setProperty("taskNames", listOf("runServer"))
                    })
                    self.add(Gradle("3. Run Obfuscated Client").apply {
                        setProperty("taskNames", listOf("runObfClient"))
                    })
                    self.add(Gradle("4. Run Obfuscated Server").apply {
                        setProperty("taskNames", listOf("runObfServer"))
                    })
                    self.add(Gradle("5. Build Jars").apply {
                        setProperty("taskNames", listOf("build"))
                    })
                }
                "compiler" {
                    val self = this.delegate as org.jetbrains.gradle.ext.IdeaCompilerConfiguration
                    afterEvaluate {
                        self.javac.moduleJavacAdditionalOptions = mapOf(
                            (project.name + ".main") to
                                tasks.compileJava.get().options.compilerArgs.joinToString(" ") { '"' + it + '"' }
                        )
                    }
                }
            }
        }
    }
}
