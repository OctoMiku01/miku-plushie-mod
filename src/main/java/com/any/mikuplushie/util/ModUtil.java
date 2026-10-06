package com.any.mikuplushie.util;

import com.any.mikuplushie.entity.AbstractPlushEntity;
import com.any.mikuplushie.registry.ModBlocks;
import com.any.mikuplushie.registry.ModEntities;
import com.any.mikuplushie.registry.ModSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public class ModUtil {

    //IDS ARE TAKEN FROM THE REGISTRY NOW (TRANSLATION KEYS ARE NOT RELIABLE FOR THIS ANYMORE)
    public static String getBlockIdFromBlockPos(Level world, BlockPos pos){
        return getBlockIdFromBlock(world.getBlockState(pos).getBlock());
    }

    public static String getBlockIdFromBlockState(BlockState state){
        return getBlockIdFromBlock(state.getBlock());
    }

    public static String getBlockIdFromItem(Item item){
        return BuiltInRegistries.ITEM.getKey(item).getPath();
    }

    public static String getBlockIdFromBlock(Block block){
        return BuiltInRegistries.BLOCK.getKey(block).getPath();
    }

    public static String getEntityId(EntityType<?> entityType){
        return BuiltInRegistries.ENTITY_TYPE.getKey(entityType).getPath();
    }

    public static String getEntityNameFromBlockId(String blockId){
        String[] blockIdWords = blockId.split("_");
        return blockIdWords[0] + "_" + blockIdWords[1];
    }

    public static String getFirstNameFromBlockId(String blockId){
        String[] blockIdWords = blockId.split("_");
        return blockIdWords[0];
    }

    //ALL PLUSH ENTITY TYPES WHOSE ID CONTAINS THE GIVEN NAME
    public static List<EntityType<? extends AbstractPlushEntity>> getPlushEntityTypes(String entityName){
        List<EntityType<? extends AbstractPlushEntity>> entityTypes = new ArrayList<>();
        for (EntityType<? extends AbstractPlushEntity> entityType : ModEntities.PLUSH_ENTITIES) {
            if (getEntityId(entityType).contains(entityName)) {
                entityTypes.add(entityType);
            }
        }
        return entityTypes;
    }

    public static SoundEvent getPlushSoundEvent(String plushName, String action){
        String firstName = getFirstNameFromBlockId(plushName);

        for (SoundEvent soundEvent : ModSoundEvents.MIKU_PLUSHIES_SOUND_EVENTS){
            //GET SOUND EVENT
            String soundEventId = soundEvent.location().getPath();
            if (soundEventId.contains(firstName) && soundEventId.contains("_" + action)){
                return soundEvent;
            }
        }

        return ModSoundEvents.MIKU_PLUSHIES_SOUND_EVENTS.getFirst();
    }

    public static void playPlushSound(Level world, BlockPos position, String plushName, String action){
        if (!plushName.equals(ModUtil.getBlockIdFromBlock(ModBlocks.KONOHA_PLUSH))){
            SoundEvent soundEvent = getPlushSoundEvent(plushName, action);
            world.playLocalSound(position.getX(), position.getY(), position.getZ(),
                soundEvent, SoundSource.BLOCKS, 0.5F, 1, true);
        }
    }

    //SOME VANILLA SOUNDS ARE HOLDERS, SOME ARE PLAIN SOUND EVENTS. THESE OVERLOADS ACCEPT BOTH
    public static SoundEvent sound(SoundEvent soundEvent) {
        return soundEvent;
    }

    public static SoundEvent sound(Holder<SoundEvent> soundEvent) {
        return soundEvent.value();
    }
}
