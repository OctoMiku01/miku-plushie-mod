package com.any.mikuplushie.datagen;

import com.any.mikuplushie.MikuPlushie;
import com.any.mikuplushie.entity.AbstractPlushEntity;
import com.any.mikuplushie.registry.ModEntities;
import com.any.mikuplushie.registry.ModItems;
import com.any.mikuplushie.util.ModUtil;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends FabricTagsProvider.ItemTagsProvider {

    public ModItemTagProvider(FabricPackOutput output,
                              CompletableFuture<HolderLookup.Provider> completableFuture) {
        super(output, completableFuture);
    }

    public static final TagKey<Item> PLUSHIES = TagKey.create(Registries.ITEM, MikuPlushie.id("plushies"));
    public static final TagKey<Item> TETO_PICKAXE = TagKey.create(Registries.ITEM, MikuPlushie.id("teto_pickaxe"));

    public static List<TagKey<Item>> PLUSH_TAGS = new ArrayList<>();

    private static ResourceKey<Item> key(Item item) {
        return ResourceKey.create(Registries.ITEM, BuiltInRegistries.ITEM.getKey(item));
    }

    @Override
    protected void addTags(HolderLookup.Provider wrapperLookup) {

        List<EntityType<? extends AbstractPlushEntity>> plushEntities = ModEntities.PLUSH_ENTITIES;

        PLUSH_TAGS.clear();
        for (EntityType<?> plushEntity : plushEntities){
            String plushName = ModUtil.getEntityId(plushEntity);
            PLUSH_TAGS.add(
                TagKey.create(Registries.ITEM, MikuPlushie.id(plushName))
            );
        }

        //ADD PLUSHIES TO RESPECTIVE TAGS
        for (int plush = 0; plush < ModItems.PLUSH_ITEMS.size(); plush++) {
            String plushName = ModUtil.getBlockIdFromItem(ModItems.PLUSH_ITEMS.get(plush));
            String plushTagName;
            for (TagKey<Item> tag : PLUSH_TAGS) {
                plushTagName = tag.location().getPath();
                if (plushName.contains(plushTagName)){
                    builder(tag).add(key(ModItems.PLUSH_ITEMS.get(plush)));
                }
            }
        }

        //ADD PICKAXES TO THEIR OWN TAG
        for (Item pickaxe : ModItems.PICKAXE_ITEMS){
            builder(TETO_PICKAXE).add(key(pickaxe));
            builder(ItemTags.CLUSTER_MAX_HARVESTABLES).add(key(pickaxe));
            builder(ItemTags.PICKAXES).add(key(pickaxe));
        }

        for (TagKey<Item> tag : PLUSH_TAGS){
            builder(PLUSHIES).addOptionalTag(tag);
        }

    }
}
