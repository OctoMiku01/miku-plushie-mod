package com.any.mikuplushie.item;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ToolMaterial;

//TOOL MATERIALS ARE RECORDS SINCE 1.21.2
public class PlushToolMaterial {

    public static final ToolMaterial PLUSH_TOOL_MATERIAL = new ToolMaterial(
        BlockTags.INCORRECT_FOR_WOODEN_TOOL, //INCORRECT BLOCKS FOR DROPS
        500, //DURABILITY
        15F, //MINING SPEED
        0F, //ATTACK DAMAGE BONUS
        25, //ENCHANTABILITY
        ItemTags.DIAMOND_TOOL_MATERIALS //REPAIR ITEMS
    );

}
