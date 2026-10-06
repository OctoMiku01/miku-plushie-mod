package com.any.mikuplushie;

import com.any.mikuplushie.entity.variant.PlushVariants;
import com.any.mikuplushie.registry.*;
import com.any.mikuplushie.worldgen.generators.ModWorldGeneration;
import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MikuPlushie implements ModInitializer {
	public static final String MOD_ID = "miku-plushie";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}

	@Override
	public void onInitialize() {
		LOGGER.info("Miku is now Joining Fabric!!!");
		ModBlocks.initialize();
		ModEntities.initialize();
		//SOUNDS MUST EXIST BEFORE THE PLUSH ITEMS, THEIR EQUIP SOUND IS A COMPONENT NOW
		ModSoundEvents.initialize();
		ModItems.initialize();
        ModCommands.initialize();
        ModResourcePacks.initialize();
		ModParticles.initialize();
		ModWorldGeneration.generateModWorldGeneration();
		PlushVariants.initialize();
        LOGGER.info("Miku: (^v^)/ Hi!!!");
	}
}
