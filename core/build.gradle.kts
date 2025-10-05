dependencies {
    // Spigot API
    compileOnly("org.spigotmc:spigot:1.21.8-R0.1-SNAPSHOT")
    compileOnly("org.spigotmc:spigot-api:1.21.8-R0.1-SNAPSHOT")

    // Project dependencies
    implementation(project(":api"))

    // Runtime libraries (will be shaded)
    //implementation("com.github.koca2000:NoteBlockAPI:1.6.3") temporarily disabled
    implementation("com.github.ashtton:NoteBlockAPI:78f2966ccd")
    implementation("com.zaxxer:HikariCP:7.0.2")
    implementation("org.xerial:sqlite-jdbc:3.50.3.0")
    implementation("org.bstats:bstats-bukkit:3.1.0")
    
    implementation("net.kyori:adventure-api:4.24.0")
    implementation("net.kyori:adventure-text-minimessage:4.24.0")
    implementation("net.kyori:adventure-platform-bukkit:4.4.1")

    // Plugin hooks
    compileOnly("me.clip:placeholderapi:2.11.6") {
        exclude(group = "net.kyori")
    }
    compileOnly("com.github.MilkBowl:VaultAPI:1.7.1")
    compileOnly("net.essentialsx:EssentialsX:2.21.2")
    compileOnly("com.github.Zrips:CMI-API:9.7.14.3")
    compileOnly("com.sk89q.worldguard:worldguard-bukkit:7.0.14")
    compileOnly("org.black_ixx:playerpoints:3.3.2")
    compileOnly("com.github.LeonMangler:SuperVanish:6.2.19")
}