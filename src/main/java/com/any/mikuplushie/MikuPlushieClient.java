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
        MathParser.setVariable("query.miku.is_game", controllerState -> 90 / Math.PI);

        //ENTITIES RENDERERS
        for (EntityType<? extends AbstractPlushEntity> plushEntity : ModEntities.PLUSH_ENTITIES) {
            EntityRenderers.register(plushEntity, AbstractPlushRender::new);
        }

        //PARTICLE
        ParticleProviderRegistry.getInstance().register(ModParticles.MIKU_SPAWN, PlushSpawnParticle.Factory::new);
	}
}
