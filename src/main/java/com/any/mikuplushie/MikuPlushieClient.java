package com.any.mikuplushie;

import com.any.mikuplushie.entity.AbstractPlushEntity;
import com.any.mikuplushie.entity.client.render.AbstractPlushRender;
import com.any.mikuplushie.particle.PlushSpawnParticle;
import com.any.mikuplushie.registry.ModEntities;
import com.any.mikuplushie.registry.ModParticles;
import com.geckolib.loading.math.MathParser;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleProviderRegistry;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.entity.EntityType;

@Environment(EnvType.CLIENT)
public class MikuPlushieClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
        //BLOCK RENDER LAYERS: SINCE 26.1 MINECRAFT PICKS CUTOUT/TRANSLUCENT FROM THE TEXTURES ITSELF,
        //SO THE OLD BlockRenderLayerMap REGISTRATION IS NOT NEEDED ANYMORE

        //GLIB QUERY
        //THE BLOCKBENCH FILES DEFINE "q.miku.is_game = math.pi/180" BECAUSE math.atan RETURNS DEGREES THERE,
        //GECKOLIB'S math.atan ALREADY RETURNS RADIANS, SO THE FACTOR IS 1 IN GAME (90 / PI MADE THE ATTACK
        //ANIMATIONS SPIN THE BODY UP TO ~700 DEGREES)
        MathParser.setVariable("query.miku.is_game", controllerState -> 1);

        //ENTITIES RENDERERS
        for (EntityType<? extends AbstractPlushEntity> plushEntity : ModEntities.PLUSH_ENTITIES) {
            EntityRenderers.register(plushEntity, AbstractPlushRender::new);
        }

        //PARTICLE
        ParticleProviderRegistry.getInstance().register(ModParticles.MIKU_SPAWN, PlushSpawnParticle.Factory::new);
	}
}
