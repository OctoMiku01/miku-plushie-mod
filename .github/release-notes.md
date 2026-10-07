## Miku Plushies 2.0.4 for Minecraft 26.2 (Fabric)

Unofficial port of [4nyNoob/miku-plushie-mod](https://github.com/4nyNoob/miku-plushie-mod) to Minecraft 26.2. This fork is mainly intended for private use; for official releases and support please use the original project.

### Requirements
- Minecraft 26.2
- Fabric Loader 0.19.5 or newer
- Fabric API 0.161.0+26.2 or newer
- GeckoLib 5.5.5 or newer (Fabric)
- Java 25

### Changes
- **New: Wander mode.** Right-clicking a tamed plushie now cycles *Follow → Sit → Wander*. In wander mode the plushie strolls around freely within 25 blocks of the spot where the mode was activated and walks back if it gets too far away. The current mode is shown above the hotbar.
- Added a German translation for the mode messages

### Previous changes (2.0.1 – 2.0.3)
- Fixed plushies looking down instead of up at the player (head, body and limb rotations were mirrored with GeckoLib 5)
- Fixed worlds failing to load/create ("Failed to load registries") because of outdated leek world generation data in 2.0.1
- Ported to Minecraft 26.2 (Mojang mappings, Fabric Loom 1.18, GeckoLib 5)
- Villager and wandering trader trades are now data driven
- Fixed missing `tempt_range` attribute crash
- Fixed plush tooltips
- Fixed plushies spinning during attack animations

> **AI disclaimer:** This port was created with the help of AI (Claude by Anthropic) and has only been tested to a limited extent, so it may contain bugs. All original assets, models, textures, sounds and the mod concept belong to 4nyNoob.
