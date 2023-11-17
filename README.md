# Connector Extras

[![Build](https://github.com/Sinytra/ConnectorExtras/actions/workflows/build.yml/badge.svg)](https://github.com/Sinytra/ConnectorExtras/actions/workflows/build.yml)
[![GitHub Releases](https://img.shields.io/github/v/release/Sinytra/ConnectorExtras?style=flat&label=Release&include_prereleases&sort=semver)](https://github.com/Sinytra/ConnectorExtras/releases/latest)
[![CurseForge](https://cf.way2muchnoise.eu/title/connector-extras.svg)](https://legacy.curseforge.com/minecraft/mc-mods/connector-extras)
[![Modrinth](https://img.shields.io/modrinth/dt/FYpiwiBR?color=00AF5C&label=modrinth&style=flat&logo=modrinth)](https://modrinth.com/mod/connector-extras)
[![Discord](https://discordapp.com/api/guilds/1141048834177388746/widget.png?style=shield)](https://discord.gg/mamk7z3TKZ)

## About

An accompanying mod for Connector that adds integration with third-party APIs and mods.

## Modules

Connector Extras is divided into several submodules for each type of integration.

### Team Reborn Energy API Bridge

A bridge between Forge Energy (FE) and Team Reborn's [Energy API](https://github.com/TechReborn/Energy) (E) that allows
two-way conversion of energy units, for both BlockEntities and ItemStacks.
The default conversion ratio is `1 E : 10 FE`, and can be configured for both directions individually.
Setting a ratio to `0` will disable conversion in its direction.

### REI Plugin Platform Bridge

Runs [REI](https://github.com/shedaniel/RoughlyEnoughItems)'s Fabric plugin detector on Forge in addition to the default
one, allowing it to load plugins from mods of both platforms at once.
Includes parts of REI Fabric code in the `me.shedaniel.rei` package, licensed under the
[MIT License](https://github.com/shedaniel/RoughlyEnoughItems/blob/8c03832d5ae716beba4047166505181cadd76e75/LICENSE).

### EMI Plugin Platform Bridge

Similar to the REI Bridge, it loads [EMI](https://github.com/emilyploszaj/emi) plugins of Fabric mods on Forge.
Includes parts of EMI API code in the `dev.emi.emi.api` package, licensed under the
[MIT License](https://github.com/emilyploszaj/emi/blob/deef228b4a35cbcccd2e48e645118ed333a26013/LICENSE).

### Reach Entity Attributes - Forge

A Forge port of the Fabric [Reach Entity Attributes](https://github.com/JamiesWhiteShirt/reach-entity-attributes)
library, implemented alongside Forge's own reach attributes. Allows for compatiblity with Fabric mods that require it,
but wouldn't otherwise be compatible due to the heavy forge mixin conflicts of the original version.

### Architectury API Bridge

Registers mod event buses of Fabric mods to Architectury Forge to avoid registry init issues.

### Terrablender Bridge

Runs the `terrablender` of Fabric mods on Forge, making their usage with the Forge version
of [TerraBlender](https://github.com/Glitchfiend/TerraBlender) possible.  
Includes parts of Terrablender Fabric API code in the `terrablender.api` package, licensed under the
[LGPLv3 License](https://github.com/Glitchfiend/TerraBlender/blob/32fab3c4dcf710ba1a7251b742e757dbafe251e6/LICENSE).

### Geckolib

Geckolib Forge is incompatible with most Fabric mods that use Geckolib for Fabric. In these cases, Geckolib Fabric must
be used instead. Connector Extras fixes a few issues that have arised with this version:
- To improve compatibility, we bridge Gekolib's `RenderProvider` to Forge, fixing an issue that turned
some items invisible.
- Initialize `Minecraft#resourceManager` before Connector loads fabric mods, which is required to run Geckolib Fabric

### ModMenu

Integrates ModMenu the config screen factory with Forge's mod list gui. Includes the 
[ModMenu API](https://github.com/TerraformersMC/ModMenu/tree/91460879c6d0693b14d1632baccea56bf3619f2e/src/main/java/com/terraformersmc/modmenu/api),
licensed under the MIT license.

### Continuity Compat

Fixes Continuity's connected textures for modded blocks. By default, Continuity builds a map of model IDs to
all registered blocks in its client initializer (`ModelWrappingHandler#createBlockStateModelIdMap`).
However, this is too soon as it happens before blocks of Forge mods are registered. As a results, CTM models will not
be created for Forge mod blocks. To fix this issue, we patch Continuity to move the block state map initialization from
Fabric's client entrypoint to FML's client setup event, which is fired after registration has completed.

### Amecs API - Forge port

A Forge port of the Fabric [Amecs API](https://github.com/Siphalor/amecs-api)
library, implemented alongside Forge's key modifier system. Allows for compatiblity with Fabric mods that require it,
but wouldn't otherwise be compatible due to the heavy forge mixin conflicts of the original version.

## Get help

If you're having trouble using Connector Extras or believe it is not functioning correctly, ask us
on [GitHub Discussions](https://github.com/Sinytra/ConnectorExtras/discussions), our discord, or open an issue in this
repository.

Here's a few tips to follow when reporting issues:

1. Make sure you are using latest available version. Look for existing issues that might've already been answered /
   fixed.
2. Navigate to [the issues tab](https://github.com/Sinytra/ConnectorExtras/issues) and open
   a [new issue](https://github.com/Sinytra/ConnectorExtras/issues/new/choose). Select one of the available templates
   depending on the topic. Fill in the required fields. In order to increase our chances of identifying and reproducing
   the issue, please make sure to include as many details as possible.
3. Once you're done describing the problem, submit the issue. We'll get to you as soon as we can.

Please note that providing as many details as possible is crucial to help us find and resolve the issue faster, while
also getting you a fixed version ASAP.

## Downloads

Connector Extras is distributed on the following platforms:

- [GitHub Releases](https://github.com/Sinytra/ConnectorExtras/releases/latest)
- [CurseForge](https://legacy.curseforge.com/minecraft/mc-mods/connector-extras)
- [Modrinth](https://modrinth.com/mod/connector-extras)

## License

Connector Extras is licensed under the MIT license, but may include other projects licensed under different terms.
Please check each subfolder for exact licensing information.

## Contributing

Before you decide to make major changes, you might want to discuss them with us beforehand, so that you're not wasting
your time. To submit your changes to the project, you can contribute
via [Pull-Request](https://help.github.com/articles/creating-a-pull-request).

Here's a few tips to help get your PR approved:

* A PR should be focused on content, rather than syntax changes.
* Use the file you are editing as a style guide.
* Make sure your feature isn't already in the works, or hasn't been rejected previously.