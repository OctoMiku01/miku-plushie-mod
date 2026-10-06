package com.any.mikuplushie.registry;

import com.any.mikuplushie.MikuPlushie;
import com.any.mikuplushie.util.ModUtil;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;

import java.util.ArrayList;
import java.util.List;

public class ModSoundEvents {
	private ModSoundEvents() {
	}

	public static List<SoundEvent> MIKU_PLUSHIES_SOUND_EVENTS = new ArrayList<>();
	protected static List<String> SOUND_EVENT = List.of("oie", "dor", "bye", "equip");
	protected static List<String> MIKU_SOUND_EVENT = List.of("canudinho", "eat");

	private static SoundEvent registerSound(String id) {
		Identifier identifier = MikuPlushie.id(id);
		return Registry.register(BuiltInRegistries.SOUND_EVENT, identifier, SoundEvent.createVariableRangeEvent(identifier));
	}

	public static void initialize() {
		MikuPlushie.LOGGER.info("Registering " + MikuPlushie.MOD_ID + " Sounds");

		//DYNAMICALLY REGISTER SOUND EVENTS
		for (EntityType<?> plush : ModEntities.PLUSH_ENTITIES){

			//SKIP KONOHA PLUSH
			if (!plush.equals(ModEntities.KONOHA)){
				String plushName = ModUtil.getEntityId(plush).replace("_plush", "");

				//ADD MIKU SOUND EVENTS
				if (plush.equals(ModEntities.MIKU)){
					for (String mikuSoundEvent : MIKU_SOUND_EVENT){
						SoundEvent soundEvent = registerSound(plushName + "_" + mikuSoundEvent);
						MIKU_PLUSHIES_SOUND_EVENTS.add(soundEvent);
					}
				}

				//ADD REGULAR SOUND EVENTS
				for (String event : SOUND_EVENT){
					SoundEvent soundEvent = registerSound(plushName + "_" + event);
					MIKU_PLUSHIES_SOUND_EVENTS.add(soundEvent);
				}
			}
		}
	}
}
