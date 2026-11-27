# Changelog

All notable changes to Witcher are documented in this file.
The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/).

## [3.2.1+1.21.1]

### Added

- NeoForge-style metal tags: `c:ingots`, `c:nuggets`, `c:raw_materials` plus per-metal subtags such as `c:ingots/silver`, `c:nuggets/steel` and `c:raw_materials/dark_iron`. The existing tags stay.

### Fixed

- Fixed Type for Silver Nugget

## [3.2.0+1.21.1]

### Changed

**Glyph & Runestone System: Switched to Jewelry Sockets**
Witcher's own Trinket/Curio-based Glyph & Runestone slot items have been fully replaced by Jewelry's socket system. 
Glyphs and Runestones are no longer separate equipment-slot items you wear alongside your gear — they're now socketed directly into compatible Witcher armor and swords via Jewelry.
- Glyphs and Runestones are crafted directly in a crafting grid (or Crafter) using a base item and a themed catalyst material:
  - **Lesser Glyph of Infusion** + catalyst -> a Lesser Glyph with the matching Sign
    - Lapis Lazuli -> Aard, Glowing Ink Sac -> Axii, Blaze Powder -> Igni, Turtle Scute -> Quen, Amethyst Shard -> Yrden
  - **Lesser Runestone** + catalyst -> a Lesser Runestone with the matching effect
    - Magma Cream -> Dazhbog, Netherite Scrap -> Chernobog, Spider Eye -> Devana, Pufferfish -> Morana, Rabbit's Foot -> Perun, Gunpowder -> Stribog, Ender Pearl -> Svarog, Echo Shard -> Triglav, Amethyst Shard -> Veles, Prismarine Crystals -> Zoria
  - Both base items now show a tooltip hinting that they need to be infused with materials
  - 4x identical Lesser Glyphs/Runestones can be combined into 1 Normal-tier Glyph/Runestone, and 4x identical Normal ones into 1 Greater-tier one
- Added EMI support for both recipe types, under a new "Glyph & Runestone Infusion" category

**DISCLAIMER for Modpack Developers & Existing Worlds**
- **Jewelry is required only for the Glyph & Runestone system.** 
- Without Jewelry installed (or with it installed but the Glyph/Runestone feature config toggles turned off), none of the items, recipes, or sockets in this section are registered.
- **The old Trinket/Curio-based Glyph & Runestone slot items are gone.** Previously, Glyphs and Runestones could be worn as separate accessory items. 
- Any Glyph/Runestone trinket items sitting in players' equipped Trinket/Curio slots or inventories on existing worlds are now orphaned and will no longer function.
- Compatible Witcher armor pieces and swords now need Jewelry sockets (via item components/tags) to hold Glyphs and Runestones.
- **Item ID change**: `witcher_rpg:lesser_glyph` has been renamed to `witcher_rpg:lesser_glyph_of_infusion`.
- Jewelry's Gem Cutting Table cant produce any Witcher glyphs/runestones.

**Dependency Updates**
- Adopt Spell Engine 1.10.9 & More RPG Library 2.8.0
- **BREAKING for modpack/datapack developers:** More RPG Library's namespace was renamed `more_rpg_classes` -> `mrpg_lib` (and its advancement root moved to `mrpg_lib:root`). If you have any data files, loot tables, tags or advancements that reference the old `more_rpg_classes:`/`more_rpg_content:` ids from this mod or MRPGL, update them to `mrpg_lib:`
- Updated internal advancement parents, mod dependency declarations (fabric.mod.json, neoforge.mods.toml)
- Added new optional dependencies: Jewelry 2.5.1 (required for the Glyph & Runestone socketing above).

**Other Changes**
- Witcher equipment is now affiliated loot: it drops more often for players carrying the matching spell book
- Fixed Witcher armor, ingot and sword recipes never unlocking in the recipe book (their advancements pointed at the wrong recipe ids)
- Blocks of Silver, Raw Silver, Steel, Meteorite, Meteorite Silver, Dark Iron and Dark Steel now drop themselves when mined
- Added a third Witcher grave variant to the cave graves

## [3.1.4+1.21.1]

### Fixed

- Fixed the invisible Kaer Morhen Armor Set Model

## [3.1.3+1.21.1]

### Added

- Add missing Curios data files for Witcher Medallion - Necklace Slots (Neoforge)
- Added missing witcher status effect textures

### Changed

- The Yrden Cloud now does not deal damage to undead entities anymore
- There is a Spell Modifier to choose in the Skill Tree soon, that will give the yrden cloud the ability to damage entities.
- Improved Tooltip-Descriptions for Spells, that inflict status effects.

### Fixed

- Fixed a damaging miscalculation for the Witcher Senses Exposed effect, resulting in absurdly high damage numbers

## [3.1.2+1.21.1]

### Changed

- Created and Modified a lot of Passives and Modifiers for the upcoming Skill Tree Update

### Fixed

- Fixed and Improved the whole Adrenaline Gain & Loss Mechanic

## [3.1.1+1.21.1]

### Changed

- Drop Forgified Fabric API (FFAPI) as a required dependency
- Replace AzurelibArmor with Armor Model API

## [3.1.0+1.21.1]

### Changed

- Adopt Spell Engine 1.10 - Thanks Daedelus for the PR!
- Skill Tree Prep with Modifiers & Passives!

## [3.0.0+1.21.1]

**Spell Expansion**
**Balancing & Internal Changes:**

### Added

- Added a small Easter Egg for the Witcher Medallion's Trinkets, increasing a certain Spell :)

### Changed

- Adapt to Spell Engine 1.9.10+ API Changes
- T3 Witcher Senses: Reveals all enemies within range, making them Glow and exposing their weaknesses. 
- Critical hits against exposed enemies deal more damage and are more likely to land.
- The exposing glow and increased Critical Hits for Melee attacks only works for the caster of the effect.
- Code Clean Up
- Battle Trance now 100% stacks Adrenaline with every Melee Hit
- Reduced Amount of Extra Damage from Battle Trance
- Rend is now a charged Spell, the longer you hold the stronger and further the hit goes.
- Slightly Improved Rend's Animations
- Wolf School Technique, Aerondight- & Iris Charge now use Spell Engine's GlowingItemStatusEffect Renderer
- The Yrden Circle Model received a new Animation with the new Spell Engine API functions

## [2.8.1+1.21.1]

### Removed

- Remove debugging Loggers from the GlyphAnvilHandler I forgot Github issue #14

## [2.8.0+1.21.1]

**Update to use Spell Engine 1.9.0**
**Witcher Signs**
**Witcher Fencing Skills**
**Other Fixes and Changes**

### Added

- Added the new Fast Attack & Strong Attack Melee Skills as Tier 2 Choices

### Changed

- DISCLAIMER: All spell books and spell scrolls will be reset, due to major API changes.
- The Witcher Sign Spell Book now contains all signs including their alternate sign spells and you can bind 4 signs
- Aard & Igni are Tier 1 Choices
- Axii, Quen & Yrden are Tier 2 Choices
- Igni Firestream & Aard Sweep are Tier 3 Choices
- Quen Active Shield, Axii Puppet & Yrden Magic Trap are Tier 4 Choices
- Same signs from the same group share cooldowns, for example Aard & Aard Sweep.
- This was added as a small drawback, due to the Witcher having 4 available spells in one spell book
- Many Sign Spell Cooldowns where lowered, to match them more towards Wizard Spell Cooldowns
- The Witcher Fencing Book now only contains 3 spells
- **Witcher Reflexes** are now the Weapon Skill for all the Witcher Swords
- Battle Trance is now a Tier 3 Spell
- Rend & Whirl are now Tier 4 Choices for the Fencing Book
- Rend was changed to the 
- The Witcher Reflexes Weapon Skill is now channeled and blocks the next Hit and Arrow Show like a shield
- In the Master Witcher Spell Book you still can choose between a mix of Sign and Fencing Spells
- It now can contain 5 spells max and can bind multiple spells from a tier

### Fixed

- Fixed Witcher Armor Diagram Smithing Upgrades always using Silver or Steel Ingots

## [2.7.2+1.21.1]

### Added

- add more glyph slot base numbers via id string search
- add more runestone slot base numbers via id string search

### Changed

- The Project is now completely working with datagen (item models, spells, lang-file, etc.)
- re-structure advancements

### Fixed

- Fix Sign particles not working

## [2.7.1+1.21.1]

### Added

- Add Witcher Swords to Spell Blade Weapon Type so they can use Spell Crit Damage Enchantments
- Add missing sunstone spell icon

### Changed

- Trying to fix crucial crash on Neoforge

### Fixed

- Fix runestones getting added to glyph tags
- Fix some Loot Table Injections

## [2.7.0+1.21.1]

### Added

**New Content! Runestone & Glyph Attachement**
- Glyphs can now be added on Witcher Armor Chestplates (Amount of Glyph Slots Increases with every Armor-Tier)
- New Runestones can now be added on Witcher Swords (Amount of Runestone Slots Increases with every Weapon-Tier, Relic Swords have the highest Slot Amount)
- Compatibility for other items can be added via Item Tags
- Slot Amounts can be set via Data Components
- Glyphs and Runestones can be removed in the Anvil with a Minecraft Vanilla Stone Block

### Changed

**Internal Changes and Fixes**
- Remove Passive Spells from Greater Glyphs (would be to op)
- Add Critical Strike Compat for the Melee Witcher School
- Add Conditional Crit Damage Mod Attributes to Feline & Ursine Armor
- Nerfed Specific Sign Glyph Attributes
- Re-Balance some Sword Attributes!
- Fix some Armor Model Clipping
- add global elemental weaknesses for WitcherSpellSchools
- Scavenger Hunt Maps to Underground Witcher Hideouts can now be found in Old Ruins & Witcher Graves
- There is a small chance that Silver Ore drops the "Pure Silver" item

## [2.6.3+1.21.1]

### Changed

- Update to newest MRPG-Lib Version

### Fixed

- fix mismatched Kaer Morhen Model (Boots and Pants were switched)

## [2.6.2+1.21.1]

### Changed

- forgot to register Witcher Particles in neo environment
- update azurelib

### Fixed

- fix some armor item values

## [2.6.1+1.21.1]

### Fixed

- fix lang files

## [2.6.0+1.21.1]

**Content Update**
**Visual Changes**
**Internal Changes**

### Added

- Added Dimeritium Ingot as upgrade material for Grandmaster Armor Sets
- New Mod Icon
- Added many spell modifiers & passives for the skill tree and future content (More will be added)

### Changed

- NeoForge Beta!
- [New Witcher School Armor Tiers] T4 Mastercrafted & T5 Grandmaster
- Each new Armor Tier comes with special equipment set bonuses
- These armor sets can be upgraded with new Diagrams
- Improve Sign Particle Visuals
- Changed Spell Book Textures to be more in line with the RPG-Series Books
- Improve how the Quen Active Shield works
- Improve how Witcher Reflexes work, it's now an activated status effect that blocks the next incoming arrow or melee hit
- Witcher Reflexes cannot be executed, if the player casts a spell, uses an item or does not have an item in the main hand in the #minecraft:sword tag
- Inject Witcher Upgrade Diagrams in more Vanilla Structures

### Removed

- Removed Witcher Medallion Passives, would be too much with the new Set Equipment Bonuses

## [2.5.2+1.21.1]

### Changed

- Update Spell Textures
- Update Mob Effect Textures

### Fixed

- Fix Crash without Trinkets

## [2.5.1+1.21.1]

### Changed

- Spell Engine & other Dependency Update

### Fixed

- Fix Iris Sword Passive
- Fix Some Item Models

## [2.5.0+1.21.1]

### Added

**New Relic Swords:**
- [DISCLAIMER] The Swords are only registered if the Arsenal (RPG Series) Mod is installed
- Azure Wrath (Silver Sword - Tier 4)
- Chance to inflict Effect that Blocks Healing
- Reach of the Damned (Silver Sword - Tier 4)
- Deals extra magic damage, when the target is below 50% health
- Iris (Steel Sword - Tier 5)
- The Sword Charges itself up every attack (Aerondight Steel Sword counterpart)
**New Additional Equipment**
- Add Witcher School Medallions (Necklace Trinket Slot)
- Added some Sign Glyphs
- Added new Relic Items

### Changed

**TECHNICAL & INTERNAL CHANGES**
- Sign Intensity Enchantment now only goes on Witcher Armor
- Swords also use Spell Power Enchantment, which now also boosts Witcher Signs Damage
- Add Spell Datagen
- Add Loot Injection via Tags
- Removed specific Witcher Armor School Gear and Relic Swords from RPG Series Loot Tag
- Fix Witcher Melee spell school double counting sharpness enchantment

## [2.4.11+1.21.1]

### Changed

- MRPGLib 2.3 Update
- Aard Signs now can deflect arrows, if timed perfectly

## [2.4.10+1.21.1]

### Changed

**TECHNICAL CHANGES**
- reset config for equipment -> v2
- Spell Engine 1.7

**BALANCING CHANGES**
- Decrease Winter's Blade Passive Cooldown

## [2.4.9+1.21.1]

[DISCLAIMER]

### Added

- Added Silver & Steel Nuggets
- Added Blocks for the Witcher Metals
- Added Raw Silver Block

### Changed

- This is also an early hotfix:
- 1. fixing the crash with the Quen Sign.
- 2. fixing false attack speed and range values
- Some new added content is not available yet because of that
- It will be added in the next update soon (2.5.0 - 1.21.1)

**VISUAL CHANGES**
- Update Spell Book & Scrolls Textures
- Update Witcher Metal & Ore Textures
- Update some Witcher Sword Color Palette

**BALANCING CHANGES**
- Aard-, Axii-, Igni-, Yrden- & Quen Intensity got removed
- Sign Intensity is now an enchantment for witcher swords and the armor
- this was done to reduce the amount of enchantments
- Increase Steel Sword Passive Chance
- Rend now works like a short ranged projectile, that pierces through all targets
- Rend now applies the Grievous Wounds Effect
- increase Better Combat Attack Range
- Fix wrong attack speed values for witcher swords

**TECHNICAL CHANGES**
- The Stagger Effect was moved to the More RPG Lib Mod
- Change Effect registration to SpellEngine API register

## [2.4.8+1.21.1]

### Fixed

- fix witcher grave spawning in the sky

## [2.4.7+1.21.1]

### Changed

- nerf Aerondight Sword

### Fixed

- fix server crash because of two registered aerondight swords

## [2.4.6+1.21.1]

### Added

- add some target modifiers to existing spells

### Changed

- The Yrden Circle now deals damage to undead and yrden vulnerable entities

### Fixed

- fix some target modifiers in Passive Spell Impact
- fix some passive spells

## [2.4.5+1.21.1]

### Added

- add some biomes to the witcher grave list

### Changed

- Update for newest Spell Engine API
- The special Witcher Swords now use the new Spell Engine Passive System
- The special Witcher Swords are only available if the "Arsenal - RPG Series" Mod is installed
- this can be ignored with the tweaks config

### Fixed

- fix advancement error
- fix lootable scrolls not having the witcher spell scroll texture and name

## [2.4.4+1.21.1]

### Added

- Add Spell Scroll Textures

### Changed

- Renamed signs_spell_book to base_signs_spell_book

## [2.4.3+1.21.1]

### Changed

- Spell Engine 1.6 Update
- changed some tags
- Update Better Combat Weapon Attribute

## [2.4.2+1.21.1]

### Added

- add new Bettercombat animations, created by Tenebris Mors, Thanks!

## [2.4.1+1.21.1]

### Added

- Add TweaksConfig, so special Weapons can also be used without Better End & Nether and the Aether

### Fixed

- Fix fabric.mod.json

## [2.4.0+1.21.1]

### Added

- Add Dark Oak Forest Witcher Grave Structure
- New Fencing Spell Tier-list: 1.Witcher Reflexes 2.Battle Trance 3.Rend 4.Whirl
- added Witcher Related Spell Scrolls to the Witcher Grave-Loot Tables
- Add Particles To the Axii Puppet Effect

### Changed

- Spell Engine 1.5 Update
- changed some cooldowns and casting times
- Igni & Aard are now Tier 1 Spells
- Yrden, Axii & Quen stay at Tier 2
- Reduce Rend Cooldown & Cast Duration
- Update to Fabric Loom 1.9

### Removed

- remove Aard-Spells ability to block arrows

### Fixed

- fix Attribute Registry
- Fixed Axii Puppet effect, that the affected mob targets other mobs

## [2.3.1+1.21.1]

### Fixed

- fixed crash -> forgot ServerWorld Check on Yrden particle effect spawner

## [2.3.0+1.21.1]

**- Spell Engine 1.4 Update!**

### Changed

- AzureLib Armor 3.0 Update!
- Rend & Whirl range now matching the melee weapon distance
- Buffed the Range for the Axii Puppet spell

## [2.2.4+1.21]

2.2.3 - 1.21
2.2.2 - 1.21
2.2.1 - 1.21
2.2.0 - 1.21
2.1.0 - 1.21
**WITCHER SCHOOLS ARMOR UPDATE**
**INTERNAL CHANGES**
**BALANCING**
**ADDITIONS**
2.0.5 - 1.21
2.0.4 - 1.21
2.0.3
2.0.2
2.0.1
2.0.0

### Added

- added the missing sign intensity & adrenaline burst effect (Attribute Boosting Effect)
- added the Heavenly Witcher Sword (Aether Variant)

- Added magic damage types for each sign school and silver
- Added entity type tags for targets that are vulnerable to a specific sign
- Added Entity Type for Axii Immunity (Mostly for Bosses)
- Added a tweaks config file
- Added a Witcher Grave Structure for Witcher Related Loot
- Added the Stagger Effect
- Added missing textures of status effects
- Added "smithing template" diagrams for each witcher school armor (Enhanced & Superior Tier)
- New Armor: Wolven Witcher-School Armor
- added more configured_feature variants for the witcher ores
- added a meteorite like geode
- added netherrack dark iron ore
- new particles for the aard signs
- new Spell School Witcher_Melee, that the melee spells also benefit from adrenaline, to crit more & higher

### Changed

- ### **Spell Scroll Update**
- **Spell Allocation**
- The Alternate Signs can be found via the new loot-able Spell Scrolls
- The Alternate Signs can be added to the Master Witcher Spell Book
- This can be done on the Spell Binding Table
- The Master Witcher Spell Book can only contain 7 Spells
- **BALANCING**
- Whirl cast duration was reduced to 2.5 from 3.5
- Witcher Reflexes cast duration was increased to 0.5 from 0.25
- **GENERAL**
- renamed the spell book item ids

- Spell Engine 1.2 Update
- BREAKING FOR TEXTURE PACKS: Changed the paths for textures in the model files
- CONFIG RESET: Your custom item Configs got reset, the config file is now called: "items_v5"
- If a target is vulnerable to a specific sign, the damage gets increased
- Cleaned up the code of relic witcher swords
- Relic Witcher Swords can only be found in Witcher Related structure chests now and in the future (LNE Witcher Add On)

- Rend Spell damage now scales with adrenaline effect amplifier
- Casting damaging signs (aard & igni) increases your adrenaline now
- Made the passive for all relic swords configurable
- The ultimatum sword applies stagger instead of fire
- The Kaer Morhen Set now acts as an early- to mid-game armor choice
- Changed Armor Set Bonuses

- You now can level up your Witcher School Armor!
- Wolven Gear is a hybrid medium armor type
- Superior witcher gear is fireproof like Netherite gear

- forgot "has_meteorite" worldgen tag for the new meteorite "geode" feature

- **CONFIG** Your effects config got overwritten, use effects_v1 now
- forgot to add silver & steel swords to their tags
- tweaked some numbers for the ores
- made the dark iron ore brighter
- adrenaline effect amplifier now gets decreased on receiving damage (like in TW3 game)

- Ice And Fire Fabric Silver Ingot Compat

- Griffin School Armor used Ursine Armor texture, oops

- Spell Engine API Update (1.1.0 + 1.21.1)
- Updated SpellHelperMixin
- QuenActiveShield now stuns you for a short amount when removed, because it explodes in TW3 and knocks you back
- made the Master Witcher Book not stack able

-  1.21.x update
- alternate sign mode is disabled for 1.21 version, will be enabled later
- the alternate signs can still be found in the Master Witcher Spell Book
- Sign Intensity Enchant is for Weapons, added specific sign intensity enchants for all Sign-Forms for the armor
- Rend now stuns blocking entities and adds grievous wounds effect
- nerfed Whirl & Rend cooldown

### Fixed

- Fixed some errors regarding Witcher Ores

- Fix: Witcher Spell Books uncraftable since 2.2.2 #6

- fixed a issue with the wolven armor crafting recipe

- fixed tooltips for some witcher swords
