package com.any.mikuplushie.worldgen;

import com.any.mikuplushie.MikuPlushie;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

/**
 * The placed features are generated as JSON by {@link com.any.mikuplushie.datagen.ModWorldGenerator}.
 */
public class ModPlacedFeatures {
    public static final ResourceKey<PlacedFeature> LEEK_PLACED_KEY = registerKey("leek_placed");

    public static ResourceKey<PlacedFeature> registerKey (String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, MikuPlushie.id(name));
    }
}
