# ProCosmetics

ProCosmetics is a cosmetics plugin for Minecraft servers that helps you monetize in an EULA-friendly way and make
your server more fun with engaging cosmetic features. It gives players many visual options and customization choices,
and works entirely without requiring a resource pack. Perfect for hub and lobby servers, ProCosmetics can also be set up
to work with survival and other server types. The plugin includes arrow effects, balloons, banners, death effects,
emotes, gadgets, miniatures, morphs, mounts, music, particle effects, pets, and statuses, giving players endless ways to
express themselves and stand out on your server.

ProCosmetics currently supports Spigot/Paper 1.20.6 and newer, and is maintained to ensure compatibility with each new
release. To keep the codebase fresh and take advantage of the newest features, we may drop support for older versions
when necessary.

## Documentation

Full documentation is available on our [Wiki](https://github.com/File14/ProCosmetics/wiki).

For support, join our [Discord server](https://discord.gg/ERVgpfg).

## Official Builds

Official and stable releases can be found on:
[SpigotMC](https://www.spigotmc.org).

## How to compile

1. ### Set up Spigot dependencies
   Use [BuildTools](https://www.spigotmc.org/wiki/buildtools/#wikiPage) to create a remapped Spigot jar for every latest
   Minecraft version (ex: 1.20.6, not 1.20) the plugin currently supports. Alternatively, run
   `bash install.sh` to put all dependencies on your local Gradle cache.

2. ### Build the plugin
   Build the project by executing the following tasks: `clean`, `build`, `obfuscate`. The jar file will be built in
   `build/libs/ProCosmetics.jar`.

## License

This project is licensed under the GNU General Public License v3.0. See [LICENSE](LICENSE).md for full details.