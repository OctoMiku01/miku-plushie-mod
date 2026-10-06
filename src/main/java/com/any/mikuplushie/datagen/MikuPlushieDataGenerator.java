package com.any.mikuplushie.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class MikuPlushieDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		pack.addProvider(ModModelProvider::new);
		pack.addProvider(ModLootTableProvider::new);
		pack.addProvider(ModRecipeProvider::new);
		pack.addProvider(ModItemTagProvider::new);
		pack.addProvider(ModEntityTagProvider::new);
		//WORLD GEN AND VILLAGER TRADES ARE WRITTEN AS PLAIN JSON (SAME FORMAT AS VANILLA 26.2)
		pack.addProvider(ModWorldGenerator::new);
		pack.addProvider(ModDataJsonProvider::new);
	}
}
