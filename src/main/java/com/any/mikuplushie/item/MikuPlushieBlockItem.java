package com.any.mikuplushie.item;

import com.any.mikuplushie.MikuPlushie;
import com.any.mikuplushie.datagen.ModItemTagProvider;
import com.any.mikuplushie.util.ModUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.block.Block;

import java.util.function.Consumer;

//THE HEAD EQUIPMENT (AND ITS SOUND) IS NOW AN EQUIPPABLE COMPONENT, SEE ModItems#registerPlush
public class MikuPlushieBlockItem extends BlockItem {

	public MikuPlushieBlockItem(Block block, Properties settings) {
		super(block, settings);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag flag) {
		tooltip.accept(Component.translatable("item." + MikuPlushie.MOD_ID + "." + ModUtil.getBlockIdFromItem(stack.getItem()) + ".tooltip"));
		super.appendHoverText(stack, context, display, tooltip, flag);
	}

	public static void PlayMikuSound(LivingEntity entity){
		ItemStack stack = entity.getItemInHand(entity.getUsedItemHand());

		if (stack.is(ModItemTagProvider.PLUSHIES)){
			String currentPlush = ModUtil.getBlockIdFromItem(stack.getItem());
			ModUtil.playPlushSound(entity.level(), entity.blockPosition(), currentPlush, "dor");
		}

	}
}
