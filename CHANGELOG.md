# 3.1.2+1.20.1

> ### ⚠️ Read this before updating
>
> This release is a **major technical overhaul and is not backwards compatible.**
>
> - **Requires the matching Spell Engine and More RPG Library releases.** This version will not run on
>   Spell Engine **0.9.x**, and mods built against 0.9.x will not work alongside it.
> - **Update the whole set together.** Spell Engine, More RPG Library and every RPG Series mod must be on
>   matching versions. Mixing in an older add-on will break at startup or misbehave in play.
> - **Spell books must be re-obtained.** Spell books from an older world no longer carry valid
>   spell data. Re-craft them, or re-bind their spells at the Spell Binding Table.
>
> **Back up your world before updating.**

- Thanks to Daedelus for the PR!
- Ported to Minecraft 1.20.1 (Fabric + Forge 47). NeoForge is replaced by Forge on this line; the same
  Forge jar also loads on NeoForge 1.20.1.
- Content-equivalent to the 1.21.1 3.1.2 release: the Skill Tree passive/modifier expansion and the
  reworked Adrenaline gain & loss are both included.
- Requires the matching 1.20.1 releases of Spell Engine (1.10.5), Spell Power (1.6.0), More RPG Library
  (2.7.2) and Armor Model API (1.0.0).
- Every registry write goes through Forge's `RegisterEvent` window, so the mod also boots on Forge
  47.0-47.3 and on NeoForge 1.20.1, which never unlock the vanilla registries.
- Glyph and runestone slots are stored as item NBT instead of data components (1.20.1 has none).
  Existing 1.21 items do not carry over, but slots behave the same in game.
- Accessory slots use Trinkets 3.7.2 on Fabric and Curios 5.x on Forge.

### Accepted 1.20.1 limitations

- No custom map markers: the Witcher hideout and Feline scavenger-hunt maps use the vanilla red X
  marker, because 1.20.1 has no `map_decoration_type` registry.
- `sign_intensity` is registered from Java instead of shipping as a data-driven enchantment (1.20.1 has
  none). It boosts the Sign school exactly as before, but it no longer shows as an attribute line on the
  item tooltip and it cannot be overridden from a datapack.
- Quen Shield always runs its full duration - on 1.20.1 a status effect cannot end itself early once its
  absorption is used up. Quen Active is unaffected.
- There is no max-absorption attribute on 1.20.1, so the +8 / +4 absorption-cap modifiers on Quen Active
  and Quen Shield are dropped. The absorption both effects grant is unchanged.
- `#minecraft:undead` does not exist on 1.20.1, so silver and Yrden vulnerability use an explicit
  `#witcher_rpg:undead` list of the vanilla undead. Modded undead that only opt into the 1.21 vanilla tag
  are not covered.
