package com.any.mikuplushie.entity.client.model.animations;

import com.any.mikuplushie.entity.client.render.AbstractPlushRender;
import com.geckolib.constant.DataTickets;
import com.geckolib.renderer.base.BoneSnapshots;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;

/**
 * Procedural plush animations. With GeckoLib 5 the bones are adjusted through {@link BoneSnapshots}
 * and all entity data comes from the render state, see {@link AbstractPlushRender#addRenderData}.
 */
public class PlushAnimations {

    public static void limbAnimations(BoneSnapshots bones, LivingEntityRenderState state){
        //LIMB ANIM VARIABLES
        float limbSwing = state.walkAnimationPos;
        float swingAmm = state.walkAnimationSpeed;
        float toRad = (float) (Math.PI / 180);
        float swingSpeed = 1F;

        //HEALTH DISPLAY
        float healthFactor = state.getOrDefaultGeckolibData(AbstractPlushRender.HEALTH_FACTOR, 1F);
        int bendAmount = 25;
        float healthBend = ((healthFactor) - 1) * bendAmount;

        boolean dancingOrAttacking = state.getOrDefaultGeckolibData(AbstractPlushRender.SONG_PLAYING, false)
            || state.getOrDefaultGeckolibData(DataTickets.SWINGING_ARM, false);

        //ROOT ANIMATION
        bones.ifPresent("root_offset", root -> {
            root.setRotZ((float) Math.sin(limbSwing * swingSpeed) * (swingAmm * 5 * toRad));
            root.setTranslateY((float) Math.sin(limbSwing * swingSpeed * 2) * (swingAmm * 1) + (swingAmm * 1));
        });

        //DISABLE ARM ANIMATIONS WHEN DANCING AND ATTACKING
        bones.ifPresent("left_arm_offset", leftArm -> leftArm.setRotX(dancingOrAttacking ? 0 :
            (float) Math.sin(limbSwing * swingSpeed) * (swingAmm * 50 * toRad) - (healthBend * toRad)));
        bones.ifPresent("right_arm_offset", rightArm -> rightArm.setRotX(dancingOrAttacking ? 0 :
            (float) Math.sin(limbSwing * swingSpeed) * (swingAmm * -50 * toRad) - (healthBend * toRad)));

        //LEGS ANIMATION
        bones.ifPresent("left_leg_offset", leftLeg ->
            leftLeg.setRotX((float) Math.sin(limbSwing * swingSpeed) * (swingAmm * -50 * toRad)));
        bones.ifPresent("right_leg_offset", rightLeg ->
            rightLeg.setRotX((float) Math.sin(limbSwing * swingSpeed) * (swingAmm * 50 * toRad)));

        //BODY ANIMATION
        bones.ifPresent("body_offset", body -> body.setRotX(healthBend * toRad));

        //HEAD ANIM
        float headPitch = state.xRot;
        float headYaw = state.yRot;
        bones.ifPresent("head_offset", head -> {
            head.setRotX((headPitch - healthBend) * toRad);
            head.setRotY(headYaw * toRad);
        });
    }

    public static void hairMovement(BoneSnapshots bones, LivingEntityRenderState state){
        //ANIM VARIABLES
        float limbSwing = state.walkAnimationPos;
        float swingAmm = state.walkAnimationSpeed;
        float toRad = (float) (Math.PI / 180);
        float swingSpeed = 1F;
        float headPitch = state.xRot;

        bones.ifPresent("hair_offset", hair -> {
            hair.setRotX(-headPitch * ((float) Math.PI / 180F));
            hair.setRotZ((float) Math.sin(limbSwing * swingSpeed - (45/20F)) * (swingAmm * -10 * toRad));
        });
    }

}
