package com.any.mikuplushie.entity.client.render;

import com.any.mikuplushie.entity.AbstractPlushEntity;
import com.any.mikuplushie.entity.client.model.AbstractPlushModel;
import com.any.mikuplushie.entity.client.model.animations.PlushAnimations;
import com.any.mikuplushie.registry.ModBlocks;
import com.any.mikuplushie.util.ModUtil;
import com.geckolib.constant.dataticket.DataTicket;
import com.geckolib.renderer.GeoEntityRenderer;
import com.geckolib.renderer.base.BoneSnapshots;
import com.geckolib.renderer.base.RenderPassInfo;
import com.geckolib.renderer.layer.builtin.ItemInHandGeoLayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

/**
 * GeckoLib 5 renders from a render state instead of the entity itself.
 * Everything the model, the texture and the procedural animations need is captured in {@link #addRenderData}.
 */
public class AbstractPlushRender extends GeoEntityRenderer<AbstractPlushEntity, LivingEntityRenderState> {

    public static final String LEFT_HAND = "left_hand";
    public static final String RIGHT_HAND = "right_hand";

    //RENDER STATE DATA
    public static final DataTicket<String> PLUSH_NAME = DataTicket.create("miku_plushie_plush_name", String.class);
    public static final DataTicket<String> VARIANT = DataTicket.create("miku_plushie_variant", String.class);
    public static final DataTicket<Float> HEALTH_FACTOR = DataTicket.create("miku_plushie_health_factor", Float.class);
    public static final DataTicket<Boolean> SONG_PLAYING = DataTicket.create("miku_plushie_song_playing", Boolean.class);

    public AbstractPlushRender(EntityRendererProvider.Context context) {
        super(context, new AbstractPlushModel());

        // Add held item rendering, the plush models use their own hand bone names
        withRenderLayer(new ItemInHandGeoLayer<>(context, this, RIGHT_HAND, LEFT_HAND));
    }

    @Override
    public void addRenderData(AbstractPlushEntity animatable, @Nullable Void relatedObject, LivingEntityRenderState renderState, float partialTick) {
        renderState.addGeckolibData(PLUSH_NAME, animatable.getPlushName());
        renderState.addGeckolibData(VARIANT, animatable.getVariant());
        renderState.addGeckolibData(HEALTH_FACTOR, animatable.getHealth() / animatable.getMaxHealth());
        renderState.addGeckolibData(SONG_PLAYING, animatable.isSongPlaying());
    }

    @Override
    public @Nullable RenderType getRenderType(LivingEntityRenderState renderState, Identifier texture) {
        String variant = renderState.getOrDefaultGeckolibData(VARIANT, "");

        //USE TRANSLUCENT RENDER ON SPECIFIC VARIATION
        if (
            variant.equals(ModUtil.getBlockIdFromBlock(ModBlocks.MIKU_PLUSH_GHOST)) ||
            variant.equals(ModUtil.getBlockIdFromBlock(ModBlocks.TETO_PLUSH_WHATCHACALLITSNAME))
        ){
            return RenderTypes.entityTranslucent(texture);
        } else {
            return super.getRenderType(renderState, texture);
        }
    }

    //PROCEDURAL ANIMATIONS (WAS GeoModel#setCustomAnimations IN GECKOLIB 4)
    @Override
    public void adjustModelBonesForRender(RenderPassInfo<LivingEntityRenderState> renderPassInfo, BoneSnapshots snapshots) {
        LivingEntityRenderState renderState = renderPassInfo.renderState();
        String plushName = renderState.getOrDefaultGeckolibData(PLUSH_NAME, "");

        if (plushName.contains("miku") || plushName.contains("teto") || plushName.contains("neru")) {
            PlushAnimations.hairMovement(snapshots, renderState);
        }
        PlushAnimations.limbAnimations(snapshots, renderState);
    }
}
