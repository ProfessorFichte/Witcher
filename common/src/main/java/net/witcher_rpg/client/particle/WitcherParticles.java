package net.witcher_rpg.client.particle;

import net.minecraft.particle.DefaultParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.LinkedHashMap;
import java.util.Map;


import static net.witcher_rpg.WitcherClassMod.MOD_ID;
public class WitcherParticles {
    private static DefaultParticleType simple() {
        return new DefaultParticleType(false) { };
    }

    public static final DefaultParticleType IGNI_SIGN = simple();
    public static final DefaultParticleType YRDEN_SIGN = simple();
    public static final DefaultParticleType AARD_SIGN = simple();
    public static final DefaultParticleType QUEN_SIGN = simple();
    public static final DefaultParticleType AXII_SIGN = simple();
    public static final DefaultParticleType YRDEN_IMPACT = simple();
    public static final DefaultParticleType YRDEN_CLOUD = simple();




    /// Creation only - the map the Fabric path registers, and the map Forge's `PARTICLE_TYPE`
    /// `RegisterEvent` window feeds to its own `RegisterHelper`.
    public static Map<Identifier, DefaultParticleType> particlesToRegister() {
        var map = new LinkedHashMap<Identifier, DefaultParticleType>();
        map.put(new Identifier(MOD_ID, "igni_sign_cast"), IGNI_SIGN);
        map.put(new Identifier(MOD_ID, "yrden_sign_cast"), YRDEN_SIGN);
        map.put(new Identifier(MOD_ID, "aard_sign_cast"), AARD_SIGN);
        map.put(new Identifier(MOD_ID, "quen_sign_cast"), QUEN_SIGN);
        map.put(new Identifier(MOD_ID, "axii_sign_cast"), AXII_SIGN);
        map.put(new Identifier(MOD_ID, "yrden_impact"), YRDEN_IMPACT);
        map.put(new Identifier(MOD_ID, "yrden_cloud"), YRDEN_CLOUD);
        return map;
    }

    public static void register(){
        particlesToRegister().forEach((id, type) -> Registry.register(Registries.PARTICLE_TYPE, id, type));
    }
}
