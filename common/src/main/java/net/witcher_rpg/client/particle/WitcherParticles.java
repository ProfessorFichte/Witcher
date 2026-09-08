package net.witcher_rpg.client.particle;

import net.minecraft.particle.DefaultParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;


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




    public static void register(){
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(MOD_ID, "igni_sign_cast"), IGNI_SIGN);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(MOD_ID, "yrden_sign_cast"), YRDEN_SIGN);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(MOD_ID, "aard_sign_cast"), AARD_SIGN);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(MOD_ID, "quen_sign_cast"), QUEN_SIGN);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(MOD_ID, "axii_sign_cast"), AXII_SIGN);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(MOD_ID, "yrden_impact"), YRDEN_IMPACT);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(MOD_ID, "yrden_cloud"), YRDEN_CLOUD);
    }
}