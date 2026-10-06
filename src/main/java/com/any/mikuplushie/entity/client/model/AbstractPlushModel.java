package com.any.mikuplushie.entity.client.model;

import com.any.mikuplushie.MikuPlushie;
import com.any.mikuplushie.entity.AbstractPlushEntity;
import com.any.mikuplushie.entity.client.render.AbstractPlushRender;
import com.any.mikuplushie.registry.ModBlocks;
import com.any.mikuplushie.util.ModUtil;
import com.geckolib.model.GeoModel;
import com.geckolib.renderer.base.GeoRenderState;
import net.minecraft.resources.Identifier;

/**
 * GeckoLib 5 resolves its assets from {@code assets/miku-plushie/geckolib/models/...} and
 * {@code assets/miku-plushie/geckolib/animations/...}, the ids no longer contain the folder or the file extension.
 */
public class AbstractPlushModel extends GeoModel<AbstractPlushEntity> {

    private final Identifier animations = MikuPlushie.id("plush");

    @Override
    public Identifier getModelResource(GeoRenderState renderState) {

        String entity = renderState.getOrDefaultGeckolibData(AbstractPlushRender.PLUSH_NAME, "miku_plush");
        String variant = renderState.getOrDefaultGeckolibData(AbstractPlushRender.VARIANT, entity);

        if (variant.equals(entity)){
            return MikuPlushie.id("entity/" + entity);
        }
        //VARIANTS THAT USE THE 2ND MODEL
        else if (
            variant.equals(ModUtil.getBlockIdFromBlock(ModBlocks.MIKU_PLUSH_MUSHROOM)) ||
            variant.equals(ModUtil.getBlockIdFromBlock(ModBlocks.MIKU_PLUSH_WEREWOMAN)) ||
            variant.equals(ModUtil.getBlockIdFromBlock(ModBlocks.MIKU_PLUSH_PATATI)) ||
            variant.equals(ModUtil.getBlockIdFromBlock(ModBlocks.MIKU_PLUSH_PATATA)) ||
            variant.equals(ModUtil.getBlockIdFromBlock(ModBlocks.MIKU_PLUSH_DEVIL)) ||
            variant.equals(ModUtil.getBlockIdFromBlock(ModBlocks.MIKU_PLUSH_WITCH))) {
            return MikuPlushie.id("entity/" + entity + "_2");
        }
        //VARIANTS THAT USE THE 3RD MODEL
        else if (
            variant.equals(ModUtil.getBlockIdFromBlock(ModBlocks.MIKU_PLUSH_XMAS_TREE)) ||
            variant.equals(ModUtil.getBlockIdFromBlock(ModBlocks.MIKU_PLUSH_SONIC)) ||
            variant.equals(ModUtil.getBlockIdFromBlock(ModBlocks.MIKU_PLUSH_DIGITAL_STARS_2025)) ||
            variant.equals(ModUtil.getBlockIdFromBlock(ModBlocks.MIKU_PLUSH_ROTTEN_GIRL)) ||
            variant.equals(ModUtil.getBlockIdFromBlock(ModBlocks.MIKU_PLUSH_PSYCHO_MODE)) ||
            variant.equals(ModUtil.getBlockIdFromBlock(ModBlocks.MIKU_PLUSH_DONT_BELIEVE_IN_T)) ||
            variant.equals(ModUtil.getBlockIdFromBlock(ModBlocks.MIKU_PLUSH_STATIC)) ||
            variant.equals(ModUtil.getBlockIdFromBlock(ModBlocks.MIKU_PLUSH_MOCHIMOCHI)) ||
            variant.equals(ModUtil.getBlockIdFromBlock(ModBlocks.MIKU_PLUSH_MONITORING)) ||
            variant.equals(ModUtil.getBlockIdFromBlock(ModBlocks.MIKU_PLUSH_HOLLOW_KNIGHT)) ||
            variant.equals(ModUtil.getBlockIdFromBlock(ModBlocks.MIKU_PLUSH_HORNET)) ||
            variant.equals(ModUtil.getBlockIdFromBlock(ModBlocks.MIKU_PLUSH_LUCARIO_Z))||
            variant.equals(ModUtil.getBlockIdFromBlock(ModBlocks.MIKU_PLUSH_PPPP))
        ) {
            return MikuPlushie.id("entity/" + entity + "_3");
        }
        return MikuPlushie.id("entity/" + entity);
    }

    @Override
    public Identifier getTextureResource(GeoRenderState renderState) {
        String entity = renderState.getOrDefaultGeckolibData(AbstractPlushRender.PLUSH_NAME, "miku_plush");
        String variant = renderState.getOrDefaultGeckolibData(AbstractPlushRender.VARIANT, entity);
        return MikuPlushie.id("textures/block/" + variant.replace('_', '-') + ".png");
    }

    @Override
    public Identifier getAnimationResource(AbstractPlushEntity animatable) {
        return animations;
    }
}
