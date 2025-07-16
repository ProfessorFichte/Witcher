# 2.4.9 - 1.21.1
[DISCLAIMER]
- This is also an early hotfix:
- 1. fixing the crash with the Quen Sign.
- 2. fixing false attack speed and range values
- Some new added content is not available yet because of that
- It will be added in the next update soon (2.5.0 - 1.21.1)
### VISUAL CHANGES
- Update Spell Book & Scrolls Textures
- Update Witcher Metal & Ore Textures
- Update some Witcher Sword Color Palette
### BALANCING CHANGES
- Aard-, Axii-, Igni-, Yrden- & Quen Intensity got removed
- Sign Intensity is now an enchantment for witcher swords and the armor
- this was done to reduce the amount of enchantments
- Increase Steel Sword Passive Chance
- Rend now works like a short ranged projectile, that pierces through all targets
- Rend now applies the Grievous Wounds Effect
- increase Better Combat Attack Range
- Fix wrong attack speed values for witcher swords
### NEW CONTENT
- Added Silver & Steel Nuggets
- Added Blocks for the Witcher Metals
- Added Raw Silver Block
[AVAILABLE NEXT UPDATE]
- Add Witcher School Medallions (Necklace Trinket Slot)
- Added some Sign Glyphs
### TECHNICAL CHANGES
- The Stagger Effect was moved to the More RPG Lib Mod
- Change Effect registration to SpellEngine API register

# 2.4.8 - 1.21.1
- fix witcher grave spawning in the sky

# 2.4.7 - 1.21.1
- fix server crash because of two registered aerondight swords
- nerf Aerondight Sword

# 2.4.6 - 1.21.1
- fix some target modifiers in Passive Spell Impact
- fix some passive spells
- add some target modifiers to existing spells
- The Yrden Circle now deals damage to undead and yrden vulnerable entities

# 2.4.5 - 1.21.1
- fix advancement error
- Update for newest Spell Engine API
- add some biomes to the witcher grave list
- The special Witcher Swords now use the new Spell Engine Passive System
- The special Witcher Swords are only available if the "Arsenal - RPG Series" Mod is installed
- this can be ignored with the tweaks config
- fix lootable scrolls not having the witcher spell scroll texture and name

# 2.4.4 - 1.21.1
- Add Spell Scroll Textures
- Renamed signs_spell_book to base_signs_spell_book

# 2.4.3 - 1.21.1
- Spell Engine 1.6 Update
- changed some tags
- Update Better Combat Weapon Attribute

# 2.4.2 - 1.21.1
- add new Bettercombat animations, created by Tenebris Mors, Thanks!

# 2.4.1 - 1.21.1
- Add TweaksConfig, so special Weapons can also be used without Better End & Nether and the Aether
- Fix fabric.mod.json

# 2.4.0 - 1.21.1
- Spell Engine 1.5 Update
- fix Attribute Registry
- changed some cooldowns and casting times
- Add Dark Oak Forest Witcher Grave Structure
- Igni & Aard are now Tier 1 Spells
- Yrden, Axii & Quen stay at Tier 2
- New Fencing Spell Tier-list: 1.Witcher Reflexes 2.Battle Trance 3.Rend 4.Whirl
- added Witcher Related Spell Scrolls to the Witcher Grave-Loot Tables
- Reduce Rend Cooldown & Cast Duration
- Update to Fabric Loom 1.9
- Add Particles To the Axii Puppet Effect
- Fixed Axii Puppet effect, that the affected mob targets other mobs
- remove Aard-Spells ability to block arrows


# 2.3.1 - 1.21.1
- fixed crash -> forgot ServerWorld Check on Yrden particle effect spawner

# 2.3.0 - 1.21.1
**- Spell Engine 1.4 Update!**
- AzureLib Armor 3.0 Update!
- Rend & Whirl range now matching the melee weapon distance
- Buffed the Range for the Axii Puppet spell

### 2.2.4 - 1.21
- Fixed some errors regarding Witcher Ores

2.2.3 - 1.21
- Fix: Witcher Spell Books uncraftable since 2.2.2 #6

2.2.2 - 1.21
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
- added the missing sign intensity & adrenaline burst effect (Attribute Boosting Effect)
- renamed the spell book item ids

2.2.1 - 1.21
- fixed a issue with the wolven armor crafting recipe

2.2.0 - 1.21
- Spell Engine 1.2 Update
- added the Heavenly Witcher Sword (Aether Variant)

2.1.0 - 1.21
**WITCHER SCHOOLS ARMOR UPDATE**
**INTERNAL CHANGES**
- BREAKING FOR TEXTURE PACKS: Changed the paths for textures in the model files
- CONFIG RESET: Your custom item Configs got reset, the config file is now called: "items_v5"
- Added magic damage types for each sign school and silver
- Added entity type tags for targets that are vulnerable to a specific sign
- Added Entity Type for Axii Immunity (Mostly for Bosses)
- If a target is vulnerable to a specific sign, the damage gets increased
- Cleaned up the code of relic witcher swords
- Added a tweaks config file
- Added a Witcher Grave Structure for Witcher Related Loot
- Relic Witcher Swords can only be found in Witcher Related structure chests now and in the future (LNE Witcher Add On)

**BALANCING**
- Rend Spell damage now scales with adrenaline effect amplifier
- Casting damaging signs (aard & igni) increases your adrenaline now
- Added the Stagger Effect
- Made the passive for all relic swords configurable
- The ultimatum sword applies stagger instead of fire
- Added missing textures of status effects
- The Kaer Morhen Set now acts as an early- to mid-game armor choice
- Changed Armor Set Bonuses

**ADDITIONS**
- You now can level up your Witcher School Armor!
- Added "smithing template" diagrams for each witcher school armor (Enhanced & Superior Tier)
- New Armor: Wolven Witcher-School Armor
- Wolven Gear is a hybrid medium armor type
- Superior witcher gear is fireproof like Netherite gear

2.0.5 - 1.21
- forgot "has_meteorite" worldgen tag for the new meteorite "geode" feature

2.0.4 - 1.21
- **CONFIG** Your effects config got overwritten, use effects_v1 now
- forgot to add silver & steel swords to their tags
- added more configured_feature variants for the witcher ores
- tweaked some numbers for the ores
- made the dark iron ore brighter
- added a meteorite like geode
- added netherrack dark iron ore
- adrenaline effect amplifier now gets decreased on receiving damage (like in TW3 game)

2.0.3
- Ice And Fire Fabric Silver Ingot Compat

2.0.2
- Griffin School Armor used Ursine Armor texture, oops

2.0.1
- Spell Engine API Update (1.1.0 + 1.21.1)
- Updated SpellHelperMixin
- QuenActiveShield now stuns you for a short amount when removed, because it explodes in TW3 and knocks you back
- fixed tooltips for some witcher swords
- made the Master Witcher Book not stack able

2.0.0
-  1.21.x update
- alternate sign mode is disabled for 1.21 version, will be enabled later
- the alternate signs can still be found in the Master Witcher Spell Book
- Sign Intensity Enchant is for Weapons, added specific sign intensity enchants for all Sign-Forms for the armor
- new particles for the aard signs
- new Spell School Witcher_Melee, that the melee spells also benefit from adrenaline, to crit more & higher
- Rend now stuns blocking entities and adds grievous wounds effect
- nerfed Whirl & Rend cooldown