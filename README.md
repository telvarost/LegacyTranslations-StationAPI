# LegacyTranslations for Minecraft Beta 1.7.3

## This mod is not fully completed yet

### Addon Credits
* @Roadhog360 - Initially creating the Legacy Translation System for New Frontier Craft
* @Birevan - The foundation and inspiration for the creation of this addon, alongside the source of many of the internal textures and files that let it function
* @[NFC] Vivian -  Bugfixes, Documentation, & porting the Legacy Translation System to Vanilla

### Description
This mod adds custom language loading capabilities to the game. Rather than the hardcoded and static language list that Mojang's modern Minecraft provides the player, the Translation System has a custom language system much the same as texturepacks. This means that users can add their own languages and/or modify existing ones to their heart's content. Our hope is that this can eventually become the standard for community translations of these older versions of the game.

### Future Plans
Fabric and other variants without base class edits will be created in the future.
* (This repo is that fabric port)

### Notes
We've extended the available selection of valid characters in Beta to allow for more language translations beyond what was possible in Vanilla. Currently translations are limited to left-to-right character based languages, however future versions of the LT System will resolve this by adding Unicode support & right-to-left character support done by @icanttellyou

### Translations
Currently, no translations are built into the addon, as they all still need to receive ports to guarantee compatibility with the LT System. Translations will either need to be created yourself or re-used from New Frontier Craft. Be sure to get permission from the original language pack creators that created the NFC Translations before redistributing your updated versions of their work. Future versions of the LT System will bundle in a selection of language packs

## Installation using Prism Launcher

1. Download an instance of Babric for Prism Launcher: https://github.com/babric/prism-instance
2. Install Java 17, set the instance to use it, and disable compatibility checks on the instance: https://adoptium.net/temurin/releases/
3. Add StationAPI to the mod folder for the instance: https://jenkins.glass-launcher.net/job/StationAPI/lastSuccessfulBuild/
4. (Optional) Add Mod Menu to the mod folder for the instance: https://github.com/calmilamsy/ModMenu/releases
5. (Optional) Add GlassConfigAPI 2.0+ to the mod folder for the instance: https://maven.glass-launcher.net/#/releases/net/glasslauncher/mods/GlassConfigAPI
6. Add this mod to the mod folder for the instance: https://github.com/telvarost/LegacyTranslations-StationAPI/releases
7. Run and enjoy! 👍

## Feedback

Got any suggestions on what should be added next? Feel free to share it by [creating an issue](https://github.com/telvarost/LegacyTranslations-StationAPI/issues/new). Know how to code and want to do it yourself? Then look below on how to get started.

## Contributing

Thanks for considering contributing! To get started fork this repository, make your changes, and create a PR. 

If you are new to StationAPI consider watching the following videos on Babric/StationAPI Minecraft modding: https://www.youtube.com/watch?v=9-sVGjnGJ5s&list=PLa2JWzyvH63wGcj5-i0P12VkJG7PDyo9T
