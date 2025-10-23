plugins {
    java
    `maven-publish`
    signing
}

group = "se.file14"
version = "1.0.0"

java {
    withSourcesJar()
    withJavadocJar()
}

repositories {
    mavenCentral()
}

dependencies {
    // Spigot API
    compileOnly("org.spigotmc:spigot-api:1.21.10-R0.1-SNAPSHOT")

    compileOnly("net.kyori:adventure-api:4.25.0")
    compileOnly("net.kyori:adventure-text-minimessage:4.25.0")
    compileOnly("it.unimi.dsi:fastutil:8.5.18")
    compileOnly("io.netty:netty-handler:4.2.7.Final")

    // Annotations
    compileOnly("org.jetbrains:annotations:26.0.2-1")

    // NoteBlockAPI
    // compileOnly("com.github.koca2000:NoteBlockAPI:1.6.3") // temporarily disabled
    compileOnly("com.github.ashtton:NoteBlockAPI:78f2966ccd")
}

fun requiredProperty(name: String): String =
    (project.findProperty(name) as? String)?.takeIf { it.isNotBlank() }
        ?: throw GradleException("Missing required property '$name' in gradle.properties")

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])

            groupId = group.toString()
            artifactId = "ProCosmetics-api"
            version = version.toString()

            pom {
                name.set("ProCosmetics API")
                description.set("API module for the ProCosmetics Minecraft plugin (Java Edition).")
                url.set("https://github.com/file14/ProCosmetics")

                licenses {
                    license {
                        name.set("GNU General Public License v3.0")
                        url.set("https://www.gnu.org/licenses/gpl-3.0.en.html")
                    }
                }

                developers {
                    developer {
                        id.set("se.file14")
                        name.set("File14")
                        //email.set("email@example.com") // optional
                    }
                }

                scm {
                    connection.set("scm:git:https://github.com/File14/ProCosmetics.git")
                    developerConnection.set("scm:git:ssh://git@github.com:File14/ProCosmetics.git")
                    url.set("https://github.com/File14/ProCosmetics")
                }
            }
        }
    }

    repositories {
        maven {
            name = "CentralPortal"
            url = uri("https://central.sonatype.com/api/v1/publisher/upload")


            credentials {
                username = requiredProperty("centralUsername")
                password = requiredProperty("centralPassword")
            }
        }
    }
}

signing {
    useGpgCmd()
    sign(publishing.publications["mavenJava"])
}