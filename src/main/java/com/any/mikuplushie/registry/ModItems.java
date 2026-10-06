package com.any.mikuplushie.registry;

import com.any.mikuplushie.MikuPlushie;
import com.any.mikuplushie.item.MikuPlushieBlockItem;
import com.any.mikuplushie.item.ModFoodComponents;
import com.any.mikuplushie.item.PlushToolMaterial;
import com.any.mikuplushie.util.ModUtil;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ModItems {

    public static List<Item> REGULAR_ITEMS = new ArrayList<>();
    public static List<Item> PLUSH_ITEMS = new ArrayList<>();
    public static List<Item> PICKAXE_ITEMS = new ArrayList<>();

    //CREATE CREATIVE TAB
	public static final ResourceKey<CreativeModeTab> MIKU_GROUP_KEY =
        ResourceKey.create(Registries.CREATIVE_MODE_TAB, MikuPlushie.id("item_group"));
	public static final CreativeModeTab MIKU_GROUP = FabricCreativeModeTab.builder()
		.icon(() -> new ItemStack(ModBlocks.MIKU_PLUSH))
		.title(Component.translatable("item.group.miku_plushies"))
		.displayItems((parameters, output) -> {
            REGULAR_ITEMS.forEach(output::accept);
            PLUSH_ITEMS.forEach(output::accept);
            PICKAXE_ITEMS.forEach(output::accept);
        })
		.build();


    //REGISTER REGULAR ITEMS
	public static final Item CANUDINHO =
        register("canudinho", Item::new, new Item.Properties().rarity(Rarity.RARE));
	public static final Item BAGUETTE =
        register("baguette", Item::new, new Item.Properties().food(ModFoodComponents.BAGUETTE));

    //SEEDS USE THEIR OWN ITEM NAME INSTEAD OF THE CROP BLOCK NAME
    public static final Item LEEK_SEEDS =
        register("leek_seeds", settings -> new BlockItem(ModBlocks.LEEK_CROP, settings), new Item.Properties().useItemDescriptionPrefix());
    public static final Item LEEK =
        register("leek", Item::new, new Item.Properties().food(ModFoodComponents.LEEK));

    public static final Item AKITA_NERU_PHONE =
        register("akita_neru_phone", Item::new, new Item.Properties());

    public static final Item VOCALOID_HEART =
        register("vocaloid_heart", Item::new, new Item.Properties());

    //REGISTER TETO PICKAXE ITEMS
    public static final Item TETO_PICKAXE = registerPickaxe("teto_pickaxe");
    public static final Item TETO_PICKAXE_MESMERIZER = registerPickaxe("teto_pickaxe_mesmerizer");
    public static final Item TETO_PICKAXE_BIRDBRAIN = registerPickaxe("teto_pickaxe_birdbrain");
    public static final Item TETO_PICKAXE_REGRET_ROCK = registerPickaxe("teto_pickaxe_regret_rock");
    public static final Item TETO_PICKAXE_DONT_BELIEVE_IN_T = registerPickaxe("teto_pickaxe_dont_believe_in_t");
    public static final Item TETO_PICKAXE_LIAR_DANCER = registerPickaxe("teto_pickaxe_liar_dancer");
    public static final Item TETO_PICKAXE_WHATCHACALLITSNAME = registerPickaxe("teto_pickaxe_whatchacallitsname");
    public static final Item TETO_PICKAXE_SOME_MORE_OF_THAT_SONG = registerPickaxe("teto_pickaxe_some_more_of_that_song");
    public static final Item TETO_PICKAXE_SYNTHV = registerPickaxe("teto_pickaxe_synthv");
    public static final Item TETO_PICKAXE_SPOKEN_FOR = registerPickaxe("teto_pickaxe_spoken_for");
    public static final Item TETO_PICKAXE_PPPP = registerPickaxe("teto_pickaxe_pppp");

    //REGISTER PLUSH ITEMS
    public static Item registerPlush(Block plushBlock) {
        String name = ModUtil.getBlockIdFromBlock(plushBlock);

        //PLUSHIES CAN BE WORN ON THE HEAD, THE EQUIP SOUND IS PART OF THE EQUIPPABLE COMPONENT NOW
        Holder<SoundEvent> equipSound = plushBlock.equals(ModBlocks.KONOHA_PLUSH)
            ? BuiltInRegistries.SOUND_EVENT.wrapAsHolder(ModUtil.sound(SoundEvents.WOOL_PLACE))
            : BuiltInRegistries.SOUND_EVENT.wrapAsHolder(ModUtil.getPlushSoundEvent(name, "equip"));

        Item.Properties settings = new Item.Properties()
            .useBlockDescriptionPrefix()
            .component(DataComponents.EQUIPPABLE, Equippable.builder(EquipmentSlot.HEAD)
                .setEquipSound(equipSound)
                .build());

        return register(name, props -> new MikuPlushieBlockItem(plushBlock, props), settings);
    }

    //REGISTER PICKAXES HELPER
    public static Item registerPickaxe(String name) {
        return register(name, Item::new, new Item.Properties()
            .pickaxe(PlushToolMaterial.PLUSH_TOOL_MATERIAL, 1f, -2.8F));
    }

    //REGISTER NORMAL ITEM
	public static Item register(String id, Function<Item.Properties, Item> itemFactory, Item.Properties settings) {
        //ITEM PROPERTIES NEED THE REGISTRY KEY SINCE 1.21.2
		ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, MikuPlushie.id(id));
        Item item = Registry.register(BuiltInRegistries.ITEM, itemKey, itemFactory.apply(settings.setId(itemKey)));

        //ADD TETO PICKAXES TO THE PICKAXES LIST
        if (id.contains("pickaxe")){
            PICKAXE_ITEMS.add(item);
        }
        //ADD REGULAR ITEMS TOO
        else if (!id.contains("plush")){
            REGULAR_ITEMS.add(item);
        }

        return item;
	}


	public static void initialize() {
        MikuPlushie.LOGGER.info("Registering " + MikuPlushie.MOD_ID + " Items");

        //REGISTER CREATIVE TAB
		Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, MIKU_GROUP_KEY, MIKU_GROUP);

        //CREATE A ITEM FOR EVERY PLUSH BLOCK
        for (Block plushblock : ModBlocks.PLUSH_BLOCKS) {
            Item plushItem = registerPlush(plushblock);
            PLUSH_ITEMS.add(plushItem);
        }
	}
}
