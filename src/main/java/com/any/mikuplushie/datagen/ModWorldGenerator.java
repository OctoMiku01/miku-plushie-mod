package com.any.mikuplushie.datagen;

import com.any.mikuplushie.MikuPlushie;
import com.any.mikuplushie.registry.ModBlocks;
import com.any.mikuplushie.worldgen.ModConfiguredFeatures;
import com.any.mikuplushie.worldgen.ModPlacedFeatures;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Writes the leek world generation as plain JSON.
 * <p>
 * Since 26.1 the "random_patch" feature is gone, patches are made out of placement modifiers instead
 * (count + random_offset + block_predicate_filter), exactly like the vanilla "patch_berry_common" feature.
 * The old patch (8 tries, xz spread 2, y spread 1) is translated 1:1.
 */
public class ModWorldGenerator implements DataProvider {
    private final PackOutput.PathProvider configuredFeatures;
    private final PackOutput.PathProvider placedFeatures;

    public ModWorldGenerator(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        this.configuredFeatures = output.createPathProvider(PackOutput.Target.DATA_PACK, "worldgen/configured_feature");
        this.placedFeatures = output.createPathProvider(PackOutput.Target.DATA_PACK, "worldgen/placed_feature");
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cachedOutput) {
        List<CompletableFuture<?>> futures = new ArrayList<>();

        //CONFIGURED FEATURE: PLACE A SINGLE WILD LEEK
        JsonObject state = new JsonObject();
        state.addProperty("Name", BuiltInRegistries.BLOCK.getKey(ModBlocks.WILD_LEEK_CROP).toString());
        JsonObject toPlace = new JsonObject();
        toPlace.addProperty("type", "minecraft:simple_state_provider");
        toPlace.add("state", state);
        JsonObject config = new JsonObject();
        config.add("to_place", toPlace);
        JsonObject leek = new JsonObject();
        leek.addProperty("type", "minecraft:simple_block");
        leek.add("config", config);
        futures.add(DataProvider.saveStable(cachedOutput, leek,
            this.configuredFeatures.json(ModConfiguredFeatures.LEEK_KEY.identifier())));

        //PLACED FEATURE: RARE PATCHES OF 8 TRIES
        JsonArray placement = new JsonArray();
        placement.add(modifier("minecraft:rarity_filter", "chance", 8));
        placement.add(modifier("minecraft:in_square"));
        JsonObject heightmap = modifier("minecraft:heightmap");
        heightmap.addProperty("heightmap", "MOTION_BLOCKING");
        placement.add(heightmap);
        placement.add(modifier("minecraft:biome"));
        placement.add(modifier("minecraft:count", "count", 8));
        JsonObject randomOffset = modifier("minecraft:random_offset");
        randomOffset.add("xz_spread", trapezoid(2));
        randomOffset.add("y_spread", trapezoid(1));
        placement.add(randomOffset);
        JsonObject predicate = new JsonObject();
        predicate.addProperty("type", "minecraft:matching_block_tag");
        predicate.addProperty("tag", "minecraft:air");
        JsonObject predicateFilter = modifier("minecraft:block_predicate_filter");
        predicateFilter.add("predicate", predicate);
        placement.add(predicateFilter);

        JsonObject placed = new JsonObject();
        placed.addProperty("feature", ModConfiguredFeatures.LEEK_KEY.identifier().toString());
        placed.add("placement", placement);
        futures.add(DataProvider.saveStable(cachedOutput, placed,
            this.placedFeatures.json(ModPlacedFeatures.LEEK_PLACED_KEY.identifier())));

        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
    }

    private static JsonObject modifier(String type) {
        JsonObject modifier = new JsonObject();
        modifier.addProperty("type", type);
        return modifier;
    }

    private static JsonObject modifier(String type, String key, int value) {
        JsonObject modifier = modifier(type);
        modifier.addProperty(key, value);
        return modifier;
    }

    //SAME DISTRIBUTION THE OLD RANDOM PATCH SPREAD HAD
    private static JsonObject trapezoid(int spread) {
        JsonObject trapezoid = new JsonObject();
        trapezoid.addProperty("type", "minecraft:trapezoid");
        trapezoid.addProperty("max", spread);
        trapezoid.addProperty("min", -spread);
        trapezoid.addProperty("plateau", 0);
        return trapezoid;
    }

    @Override
    public String getName() {
        return MikuPlushie.MOD_ID + " World Gen";
    }
}
