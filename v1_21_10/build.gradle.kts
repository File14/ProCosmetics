dependencies {
    compileOnly("org.spigotmc:spigot:1.21.10-R0.1-SNAPSHOT:remapped-mojang")

    compileOnly("net.kyori:adventure-api:4.25.0")
    compileOnly("net.kyori:adventure-platform-bukkit:4.4.1")

    implementation(project(":api"))
    implementation(project(":core"))
}
