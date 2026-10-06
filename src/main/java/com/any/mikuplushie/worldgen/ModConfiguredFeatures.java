package com.any.mikuplushie.worldgen;

import com.any.mikuplushie.MikuPlushie;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

/**
 * The configured features are generated as JSON by {@link com.any.mikuplushie.datagen.ModWorldGenerator}.
 */
public class ModConfiguredFeatures {

    //FEATURE KEYS
    public static final ResourceKey<ConfiguredFeature<?, ?>> LEEK_KEY = registerKey("leek");

    public static ResourceKey<ConfiguredFeature<?, ?>> registerKey (String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, MikuPlushie.id(name));
    }
}
