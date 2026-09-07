package net.witcher_rpg.client.particle;

import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;


import static net.witcher_rpg.WitcherClassMod.MOD_ID;
public class WitcherParticles {
    private static SimpleParticleType simple() {
        return new SimpleParticleType(false) { };
    }

    public static final SimpleParticleType IGNI_SIGN = simple();
    public static final SimpleParticleType YRDEN_SIGN = simple();
    public static final SimpleParticleType AARD_SIGN = simple();
    public static final SimpleParticleType QUEN_SIGN = simple();
    public static final SimpleParticleType AXII_SIGN = simple();
    public static final SimpleParticleType YRDEN_IMPACT = simple();
    public static final SimpleParticleType YRDEN_CLOUD = simple();




    public static void register(){
        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(MOD_ID, "igni_sign_cast"), IGNI_SIGN);
        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(MOD_ID, "yrden_sign_cast"), YRDEN_SIGN);
        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(MOD_ID, "aard_sign_cast"), AARD_SIGN);
        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(MOD_ID, "quen_sign_cast"), QUEN_SIGN);
        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(MOD_ID, "axii_sign_cast"), AXII_SIGN);
        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(MOD_ID, "yrden_impact"), YRDEN_IMPACT);
        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(MOD_ID, "yrden_cloud"), YRDEN_CLOUD);
    }
}