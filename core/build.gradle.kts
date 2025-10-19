dependencies {
    // Spigot API
    compileOnly("org.spigotmc:spigot:1.21.10-R0.1-SNAPSHOT")
    compileOnly("org.spigotmc:spigot-api:1.21.10-R0.1-SNAPSHOT")

    // Project dependencies
    implementation(project(":api"))

    // Runtime libraries (will be shaded)
    //implementation("com.github.koca2000:NoteBlockAPI:1.6.3") temporarily disabled
    implementation("com.github.ashtton:NoteBlockAPI:78f2966ccd")
    implementation("org.mongodb:mongodb-driver-sync:5.6.1")
    implementation("com.zaxxer:HikariCP:7.0.2")
    {
        exclude(group = "com.google.code.gson", module = "gson")
    }
    implementation("redis.clients:jedis:7.0.0")
    {
        exclude(group = "com.google.code.gson", module = "gson")
        exclude(group = "com.google.guava")
    }
    implementation("org.bstats:bstats-bukkit:3.1.0")
    compileOnly("org.xerial:sqlite-jdbc:3.50.3.0") // Included in Spigot

    implementation("net.kyori:adventure-api:4.25.0")
    implementation("net.kyori:adventure-text-minimessage:4.25.0")
    implementation("net.kyori:adventure-platform-bukkit:4.4.1")

    // Plugin hooks
    compileOnly("me.clip:placeholderapi:2.11.6") {
        exclude(group = "net.kyori")
    }
    compileOnly("com.github.MilkBowl:VaultAPI:1.7.1")
    compileOnly("net.essentialsx:EssentialsX:2.21.2")
    {
        exclude(group = "io.papermc.paper")
    }
    compileOnly("com.github.Zrips:CMI-API:9.7.14.3")
    compileOnly("com.sk89q.worldguard:worldguard-bukkit:7.0.14") {
        exclude(group = "com.google.guava")
    }
    compileOnly("org.black_ixx:playerpoints:3.3.3")
    compileOnly("com.github.LeonMangler:SuperVanish:6.2.19")
}