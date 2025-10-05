import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.apache.tools.ant.filters.ReplaceTokens
import java.net.URI

plugins {
    java
    id("com.gradleup.shadow") version "8.3.8"
}

group = "se.file14"
version = "2.0.0"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}

tasks.processResources {
    val projectVersion = project.version
    inputs.property("version", projectVersion)
    filesMatching("**/plugin.yml") {
        filter<ReplaceTokens>("tokens" to mapOf("VERSION" to projectVersion.toString()))
    }
}

// Configure shadow jar
tasks.named<ShadowJar>("shadowJar") {
    archiveClassifier.set("")

    // Exclude unnecessary files
    exclude("META-INF/maven/**")
    exclude("META-INF/versions/**")
    exclude("module-info.class")
    relocate("org.bstats", "se.file14.procosmetics.bstats")
}

// Make build depend on shadowJar instead of jar
tasks.named("build") {
    dependsOn("shadowJar")
}

// Disable the default jar task since we're using shadowJar
tasks.named("jar") {
    enabled = false
}

repositories {
    mavenLocal()
    mavenCentral()
    maven(url = "https://jitpack.io")
}

dependencies {
    subprojects.forEach {
        implementation(project(it.path))
    }
}

subprojects {
    apply(plugin = "java")

    repositories {
        mavenLocal()
        mavenCentral()

        // Spigot
        maven(url = "https://oss.sonatype.org/content/repositories/snapshots")
        maven(url = "https://hub.spigotmc.org/nexus/content/repositories/snapshots/")

        // Paper
        maven { url = uri("https://papermc.io/repo/maven-public/") }

        // Plugins
        maven(url = "https://jitpack.io")
        maven(url = "https://repo.essentialsx.net/releases/")
        maven(url = "https://repo.extendedclip.com/content/repositories/placeholderapi/")
        maven(url = "https://maven.enginehub.org/repo/")
        maven(url = "https://ci.ender.zone/plugin/repository/everything/")
        maven(url = "https://repo.rosewooddev.io/repository/public/")
    }
    val javaVersion = findProperty("javaVersion")?.toString()?.toIntOrNull() ?: 21

    tasks.withType<JavaCompile>().configureEach {
        javaCompiler.set(javaToolchains.compilerFor {
            languageVersion.set(JavaLanguageVersion.of(javaVersion))
        })
    }
}

// Obfuscation Tasks
abstract class VersionedObfTask @Inject constructor(
    private val subproject: Project,
    private val execOperations: ExecOperations
) : DefaultTask() {

    // Configure inputs and paths at configuration time
    @get:Input
    val minecraftVersion: String = subproject.property("remapMinecraftVersion") as String

    @get:Input
    val nmsName: String = subproject.name

    @get:Input
    val projectName: String = project.name

    @get:Input
    val projectVersion: String = project.version.toString()

    @get:Internal
    val homeDir: File = project.gradle.gradleUserHomeDir.parentFile

    @get:Internal
    val buildDir: File = project.layout.buildDirectory.get().asFile

    init {
        group = "jar preparation"
        description = "Generates an obfuscated version of the jar to use with Spigot!"
        dependsOn("shadowJar")
    }

    private fun resolveSpecialSourceJar(): File {
        val envDir = project.findProperty("SpecialSourcePath")?.toString()?.takeIf { it.isNotBlank() }
            ?: System.getenv("SpecialSourcePath")?.takeIf { it.isNotBlank() }

        val candidate = if (envDir != null) {
            File(envDir, "SpecialSource.jar")
        } else {
            File(project.projectDir, "SpecialSource.jar")
        }

        // If found, use it
        if (candidate.exists()) {
            return candidate
        }

        // If not found, we download it to the project root
        val downloadTarget = File(project.projectDir, "SpecialSource.jar")

        if (!downloadTarget.exists()) {
            val downloadUrl =
                "https://repo1.maven.org/maven2/net/md-5/SpecialSource/1.11.5/SpecialSource-1.11.5-shaded.jar"

            logger.lifecycle("SpecialSource.jar not found at ${candidate.absolutePath}")
            logger.lifecycle("Downloading SpecialSource.jar from $downloadUrl to ${downloadTarget.absolutePath} ...")

            // Make parent directory if needed
            downloadTarget.parentFile?.let { if (!it.exists()) it.mkdirs() }

            // Download
            URI(downloadUrl).toURL().openStream().use { input ->
                downloadTarget.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            logger.lifecycle("Downloaded SpecialSource.jar to ${downloadTarget.absolutePath}")
        }
        return downloadTarget
    }

    @TaskAction
    fun obfuscate() {
        val specialSourceFile = resolveSpecialSourceJar()

        val libsDir = "$buildDir/libs"
        val inputJar = "$libsDir/$projectName-$projectVersion.jar"
        val obfJar = "$libsDir/$projectName-$projectVersion-obf.jar"

        // Validate required files exist
        require(specialSourceFile.exists()) {
            "SpecialSource.jar not found at ${specialSourceFile.absolutePath}"
        }

        val mojangMap =
            "$homeDir/.m2/repository/org/spigotmc/minecraft-server/$minecraftVersion-R0.1-SNAPSHOT/minecraft-server-$minecraftVersion-R0.1-SNAPSHOT-maps-mojang.txt"
        val spigotMap =
            "$homeDir/.m2/repository/org/spigotmc/minecraft-server/$minecraftVersion-R0.1-SNAPSHOT/minecraft-server-$minecraftVersion-R0.1-SNAPSHOT-maps-spigot.csrg"
        val mojangRemap =
            "$homeDir/.m2/repository/org/spigotmc/spigot/$minecraftVersion-R0.1-SNAPSHOT/spigot-$minecraftVersion-R0.1-SNAPSHOT-remapped-mojang.jar"
        val obfuscationRemap =
            "$homeDir/.m2/repository/org/spigotmc/spigot/$minecraftVersion-R0.1-SNAPSHOT/spigot-$minecraftVersion-R0.1-SNAPSHOT-remapped-obf.jar"

        // First pass: mojang -> obf
        execOperations.exec {
            workingDir(buildDir)
            commandLine(
                "java", "-cp", "$specialSourceFile${File.pathSeparator}$mojangRemap",
                "net.md_5.specialsource.SpecialSource", "--live", "--only", "se/file14/procosmetics/$nmsName", "-q",
                "-i", inputJar, "-o", obfJar, "-m", mojangMap, "--reverse"
            )
        }

        // Second pass: obf -> spigot
        execOperations.exec {
            workingDir(buildDir)
            commandLine(
                "java", "-cp", "$specialSourceFile${File.pathSeparator}$obfuscationRemap",
                "net.md_5.specialsource.SpecialSource", "--live", "--only", "se/file14/procosmetics/$nmsName", "-q",
                "-i", obfJar, "-o", inputJar, "-m", spigotMap
            )
        }
    }
}

subprojects.forEach {
    if (it.extra.has("remapMinecraftVersion")) {
        tasks.register("obf${it.name.replaceFirstChar { char -> char.uppercaseChar() }}", VersionedObfTask::class, it)
    }
}

tasks.register("obfuscate") {
    dependsOn(tasks.withType<VersionedObfTask>())
    group = "jar preparation"
}

tasks.register("copyJarToPluginsFolder") {
    val folderPath = providers.environmentVariable("JarFolderPath")
    val projectName = project.name
    val projectVersion = project.version.toString()

    onlyIf { folderPath.isPresent }

    dependsOn("obfuscate")

    doLast {
        copy {
            from("build/libs/$projectName-$projectVersion.jar")
            into(folderPath.get())
            rename { it.replace("-$projectVersion", "") }
        }
    }
}