package com.any.mikuplushie.datagen;

import com.any.mikuplushie.MikuPlushie;
import com.any.mikuplushie.util.ModUtil;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.properties.Property;

import java.util.Optional;

public class BlockModels {

    private static ModelTemplate block(String parent, TextureSlot... requiredTextureKeys) {
        return new ModelTemplate(Optional.of(MikuPlushie.id("block/" + parent)), Optional.empty(), requiredTextureKeys);
    }

    private static TextureMapping cross(Identifier texture) {
        return new TextureMapping().put(TextureSlot.CROSS, new Material(texture));
    }

    //PARENT MODELS
    public static final ModelTemplate LEEK_MODEL = block("leek", TextureSlot.CROSS);

    //CROP BLOCKSTATE GEN
    public static void registerCrop(BlockModelGenerators blockStateModelGenerator, Block crop, Property<Integer> ageProperty, int... ageTextureIndices) {
        if (ageProperty.getPossibleValues().size() != ageTextureIndices.length) {
            throw new IllegalArgumentException();
        } else {
            Int2ObjectMap<Identifier> int2ObjectMap = new Int2ObjectOpenHashMap<>();
            PropertyDispatch<MultiVariant> blockStateVariantMap = PropertyDispatch.initial(ageProperty).generate((integer) -> {
                int ageIntProp = ageTextureIndices[integer];
                Identifier identifier = int2ObjectMap.computeIfAbsent(ageIntProp, (j) -> {
                    String suffix = "_stage" + ageIntProp;
                    return LEEK_MODEL.createWithSuffix(crop, suffix,
                        cross(ModelLocationUtils.getModelLocation(crop, suffix)), blockStateModelGenerator.modelOutput);
                });
                return BlockModelGenerators.plainVariant(identifier);
            });
            blockStateModelGenerator.blockStateOutput.accept(MultiVariantGenerator.dispatch(crop).with(blockStateVariantMap));
        }
    }

    //WILD CROP BLOCKSTATE GEN
    public static void registerWildCrop(BlockModelGenerators blockstateModelGenerator, Block block, Block tamedBlock) {
        int maxAge = CropBlock.MAX_AGE;
        TextureMapping textureMap = cross(
            MikuPlushie.id("block/" + ModUtil.getBlockIdFromBlock(tamedBlock) + "_stage" + maxAge)
        );
        blockstateModelGenerator.createCrossBlock(block, BlockModelGenerators.PlantType.NOT_TINTED, textureMap);
    }

}
