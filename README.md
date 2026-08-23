# <img alt="" width="128" src="https://github.com/RaydanOMGr/DistantHorizonsZSTD/blob/master/src/main/resources/assets/distanthorizonszstd/icon.png?raw=true"><br>DistantHorizonsZSTD
Tiny minecraft mod that ships Zstandard native libraries for [Distant Horizons](https://modrinth.com/mod/distanthorizons) on Android!
![screenshot](https://raw.githubusercontent.com/RaydanOMGr/RaydanOMGr.github.io/refs/heads/main/screenshots/dhzstd/dhzstd_screenshot_1.webp)

## Features
### Makes Distant Horizons work
For compression, Distant Horizons uses the Zstandard library through the zstd-jni bindings.
It ships this library itself, or rather a modified version of it, in order not to conflict with other mods that may ship other versions of the same library.
But it does not ship the library for android, which is exactly what this mod does

### Uncaps the Distant Horizons render distance
![](https://raw.githubusercontent.com/RaydanOMGr/RaydanOMGr.github.io/refs/heads/main/screenshots/dhzstd/dhzstd_screenshot_2.webp?)
Uncapping the render distance lets you set the LOD distance to something like 2 chunks for example.
On weak phones, you may not want the full 32 chunks of render distance that Distant Horizons puts as minimum,
instead you may want to cap to 8 or something similar in order to have these chunks with potentially better performance.

## Usage
Just put it into the mods folder in your instance, alongside the Distant Horizons mod, and enjoy!

## Contact
If you need help, have a question, or just want to chat, join the [Discord server](https://discord.com/invite/3vfnvBFRKv).
To report bugs or suggest features, please use the [GitHub repository](https://github.com/RaydanOMGr/DistantHorizonsZSTD).

## Credits
The scripts to build Zstd for Android and to patch it for Distant Horizons were made by the guys at [MojoLauncher](https://github.com/MojoLauncher/), make sure to check them out!

## Licensing
- This project is licensed under LGPL-3.0
- [Distant Horizons](https://modrinth.com/mod/distanthorizons) is licensed under LGPL-3.0
- [zstd-jni](https://github.com/luben/zstd-jni) is licensed under BSD-2-Clause
- [Zstandard](https://github.com/facebook/zstd) is dual-licensed under BSD OR GPLv2.
