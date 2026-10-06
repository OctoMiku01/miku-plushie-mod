package com.any.mikuplushie.registry;

import com.any.mikuplushie.MikuPlushie;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.fabricmc.fabric.api.resource.v1.pack.PackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class ModResourcePacks {

    public static void initialize () {
        MikuPlushie.LOGGER.info("Registering " + MikuPlushie.MOD_ID + " Resource Packs");

        FabricLoader.getInstance().getModContainer(MikuPlushie.MOD_ID).ifPresent(modContainer ->
            ResourceLoader.registerBuiltinPack(asId("en_us_dub"), modContainer,
                Component.literal("EN_US-DUB"), PackActivationType.NORMAL)
        );
        FabricLoader.getInstance().getModContainer(MikuPlushie.MOD_ID).ifPresent(modContainer ->
            ResourceLoader.registerBuiltinPack(asId("legacy_textures"), modContainer,
                Component.literal("Legacy Textures"), PackActivationType.NORMAL)
        );
    }

    public static Identifier asId(String path) {
        return MikuPlushie.id(path);
    }
}
