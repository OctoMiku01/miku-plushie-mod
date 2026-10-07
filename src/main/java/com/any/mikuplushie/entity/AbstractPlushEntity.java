package com.any.mikuplushie.entity;

import com.any.mikuplushie.entity.goals.MikuDelayedAttackGoal;
import com.any.mikuplushie.entity.variant.PlushVariants;
import com.any.mikuplushie.registry.ModBlocks;
import com.any.mikuplushie.registry.ModEntities;
import com.any.mikuplushie.registry.ModItems;
import com.any.mikuplushie.util.ModUtil;
import com.geckolib.animatable.GeoEntity;
import com.geckolib.animatable.instance.AnimatableInstanceCache;
import com.geckolib.animatable.manager.AnimatableManager;
import com.geckolib.animation.AnimationController;
import com.geckolib.animation.RawAnimation;
import com.geckolib.util.GeckoLibUtil;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class AbstractPlushEntity extends TamableAnimal implements GeoEntity {

    private static final EntityDataAccessor<Integer> SPAWN_AGE = SynchedEntityData.defineId(AbstractPlushEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(AbstractPlushEntity.class, EntityDataSerializers.INT);

    //WANDER MODE (FOLLOW -> SIT -> WANDER -> FOLLOW)
    private boolean wandering;
    //CENTER OF THE WANDER AREA AND MAX DISTANCE (IN BLOCKS) FROM IT
    public static final int WANDER_RADIUS = 25;
    private @Nullable BlockPos wanderCenter;

    //DANCE GLOBALS
    boolean songPlaying;
    @Nullable BlockPos songSource;

    //GLIB VARIABLES
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public static final RawAnimation IDLE = RawAnimation.begin().thenLoop("misc.idle");
    public static final RawAnimation SIT = RawAnimation.begin().thenLoop("misc.sit");
    public static final RawAnimation SIT_DANCE = RawAnimation.begin().thenLoop("misc.sit-dance");
    protected static final List<RawAnimation> ATTACK_ANIMATIONS = List.of(
        RawAnimation.begin().thenPlay("attack.swipe"),
        RawAnimation.begin().thenPlay("attack.swipe2"),
        RawAnimation.begin().thenPlay("attack.swipe3")
    );
    public static final RawAnimation SPAWN = RawAnimation.begin().thenPlay("misc.spawn");

    protected AbstractPlushEntity(EntityType<? extends TamableAnimal> entityType, Level world) {
        super(entityType, world);
    }

    //GOALS
    @Override
    public void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(2, new MikuDelayedAttackGoal(this, 1.5F, true));
        //FOLLOW OWNER (DISABLED WHILE WANDERING)
        this.goalSelector.addGoal(4, new FollowOwnerGoal(this,1.0F, 5F, 1F) {
            @Override
            public boolean canUse() {
                return !AbstractPlushEntity.this.isWandering() && super.canUse();
            }

            @Override
            public boolean canContinueToUse() {
                return !AbstractPlushEntity.this.isWandering() && super.canContinueToUse();
            }
        });
        this.goalSelector.addGoal(6, new TemptGoal(this, 1.5, Ingredient.of(ModItems.LEEK), false));
        //WALK AROUND FREELY (ONLY IN WANDER MODE, WITHIN WANDER_RADIUS OF THE START POINT)
        this.goalSelector.addGoal(7, new WanderAroundCenterGoal(this));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, AbstractPlushEntity.class, 8F));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8F));
        this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(3, new HurtByTargetGoal(this).setAlertOthers());
    }

    //DO NOT ATTACK SOME ENTITIES
    @Override
    public boolean wantsToAttack(LivingEntity target, LivingEntity owner) {
        return !(target instanceof AbstractPlushEntity)
            && !(target instanceof Creeper)
            && !(target instanceof Ghast)
            ;
    }

    //ATTRIBUTES
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
            .add(Attributes.MAX_HEALTH, 20.0F)
            .add(Attributes.MOVEMENT_SPEED, 0.3F)
            .add(Attributes.ATTACK_DAMAGE, 2.0F)
            //REQUIRED BY THE TEMPT GOAL SINCE 1.21.5 (SAME VALUE AS VANILLA ANIMALS)
            .add(Attributes.TEMPT_RANGE, 10.0F);
    }

    //ENTITY POSES
    public static final EntityDimensions STANDING_DIMENSIONS = EntityDimensions
        .scalable(ModEntities.PLUSH_WIDTH, 1F)
        .withEyeHeight(0.85F);
    public static final EntityDimensions SITTING_DIMENSIONS = EntityDimensions
        .scalable(ModEntities.PLUSH_WIDTH, 0.8F)
        .withEyeHeight(0.6F);

    //HASH MAP OF ENTITY POSES
    private static final Map<Pose, EntityDimensions> POSE_DIMENSIONS = ImmutableMap.<Pose, EntityDimensions>builder()
        .put(Pose.STANDING, STANDING_DIMENSIONS)
        .put(Pose.SITTING, SITTING_DIMENSIONS)
        .build();

    //SET BASE DIMENSIONS
    @Override
    public EntityDimensions getDefaultDimensions(Pose pose) {
        return POSE_DIMENSIONS.getOrDefault(pose, STANDING_DIMENSIONS);
    }

    //LIST OF AVAILABLE POSES
    @Override
    public ImmutableList<Pose> getDismountPoses() {
        return ImmutableList.of(Pose.STANDING, Pose.SITTING);
    }

    //UPDATE POSE
    protected void updatePose() {
        if (this.isInSittingPose()) {
            this.setPose(Pose.SITTING);
        } else {
            this.setPose(Pose.STANDING);
        }
    }

    //UPDATE ENTITY POSE ON TICK METHOD
    @Override
    public void tick() {
        super.tick();
        this.updatePose();
    }

    public List<RawAnimation> getDances(){
        return  List.of(
            RawAnimation.begin().thenLoop("misc.dance.generic.caramelldansen")
        );
    }

    //ANIMATION CONTROLLER
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<AbstractPlushEntity>("Plush", 2, state -> {
            List<RawAnimation> DANCES = getDances();

            //SITTING ANIMATIONS
            if (this.isInSittingPose()) {
                //SONG PLAYING NEARBY
                if (this.isSongPlaying()){
                    return state.setAndContinue(SIT_DANCE);
                } else {
                    return state.setAndContinue(SIT);
                }
            }

            //STANDING UP ANIMATIONS
            else {

                //SPAWN ANIMATION
                if (this.entityData.get(SPAWN_AGE) < 10){
                    return state.setAndContinue(SPAWN);
                }

                //DANCE
                else if (this.isSongPlaying()){
                    RawAnimation currentAnimation = state.controller().getCurrentRawAnimation();

                    for (RawAnimation animation : DANCES){
                        //IF ALREADY DANCING THEN CONTINUE
                        if (animation.equals(currentAnimation)){
                            return state.setAndContinue(animation);
                        }
                    }
                    //IF LIST IS TOO SMALL THEN GET 1ST ENTRY
                    if (DANCES.size() == 1){
                        return state.setAndContinue(DANCES.getFirst());
                    }
                    //RANDOMLY SELECT DANCE ANIMATION FROM LIST
                    else {
                        return state.setAndContinue(DANCES.get(this.random.nextInt(
                            0, DANCES.size()-1)
                        ));
                    }

                }

                //ATTACKING
                else if (this.swinging) {
                    RawAnimation currentAnimation = state.controller().getCurrentRawAnimation();

                    for (RawAnimation animation : ATTACK_ANIMATIONS){
                        //IF ALREADY ATTACKING THE CONTINUE
                        if (animation.equals(currentAnimation)){
                            return state.setAndContinue(animation);
                        }
                    }
                    //RANDOMLY SELECT ATTACK ANIMATION FROM LIST
                    return state.setAndContinue(ATTACK_ANIMATIONS.get(this.random.nextInt(
                        0, ATTACK_ANIMATIONS.size()-1)
                    ));
                }

                //IDLE
                else {
                    return state.setAndContinue(IDLE);
                }
            }
        }));

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    //HAND SWING DURATION (MADE OVERRIDABLE BY THE CLASS TWEAKER)
    @Override
    public final int getCurrentSwingDuration() {
        return 10;
    }

    //STATIC SOUND PITCH
    @Override
    public float getVoicePitch() {
        return 1F;
    }

    //DEATH SOUND
    @Override
    public @Nullable SoundEvent getDeathSound() {
        return ModUtil.getPlushSoundEvent(this.getPlushName(), "bye");
    }

    //ENTITY RIGHT CLICK
    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack playerItemStack = player.getItemInHand(player.getUsedItemHand());
        FoodProperties foodComponent = playerItemStack.get(DataComponents.FOOD);
        ItemStack entityHandStack = this.getMainHandItem();


        //TAMED INTERACTION
        if (this.onGround() && this.isTame() && this.isOwnedBy(player)) {
            //DO STUFF ON SERVER
            if (this.level() instanceof ServerLevel serverLevel) {
                //DROP HELD ITEM
                if (player.isShiftKeyDown() && playerItemStack.isEmpty()) {
                    this.spawnAtLocation(serverLevel, entityHandStack);
                    this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
                    return InteractionResult.SUCCESS;
                }

                //LEEK HEAL
                else if (this.getHealth() < this.getMaxHealth() && playerItemStack.is(ModItems.LEEK)) {
                    if (!player.getAbilities().instabuild){
                        playerItemStack.shrink(1);
                    }
                    float nutrition = foodComponent != null ? (float)foodComponent.nutrition() : 1.0F;
                    this.heal(nutrition);
                    this.playSound(ModUtil.sound(SoundEvents.GENERIC_EAT), 1, 1);
                    return InteractionResult.SUCCESS;
                }

                //CYCLE MODE: FOLLOW -> SIT -> WANDER -> FOLLOW
                else {
                    String mode;
                    if (this.isOrderedToSit()) {
                        //SIT -> WANDER
                        this.setSitting(false);
                        this.setWandering(true);
                        mode = "wander";
                    } else if (this.isWandering()) {
                        //WANDER -> FOLLOW
                        this.setWandering(false);
                        mode = "follow";
                    } else {
                        //FOLLOW -> SIT
                        this.setSitting(true);
                        mode = "sit";
                    }
                    this.getNavigation().stop();

                    //SHOW THE NEW MODE ABOVE THE HOTBAR
                    if (player instanceof ServerPlayer serverPlayer) {
                        serverPlayer.sendSystemMessage(Component.translatable(
                            "entity.miku-plushie.mode." + mode, this.getDisplayName()
                        ), true);
                    }
                    return InteractionResult.SUCCESS;
                }

            } else if (this.getHealth() < this.getMaxHealth() && playerItemStack.is(ModItems.LEEK)) {
                for (int particle = 0; particle < 20; particle++) {
                    this.level().addParticle(
                        new BlockParticleOption(ParticleTypes.BLOCK, ModBlocks.LEEK_CROP.getStateForAge(7)),
                        this.getX(),
                        this.getY() + 0.5D,
                        this.getZ(),
                        this.random.nextGaussian() * 0.1,
                        this.random.nextGaussian() * 0.1,
                        this.random.nextGaussian() * 0.1
                    );
                }
            }

            return InteractionResult.SUCCESS;
        }

        //OTHER PLAYER INTERACTION
        else {
            return super.mobInteract(player, hand);
        }
    }

    //SIT / STAND UP
    protected void setSitting(boolean sitting) {
        this.setOrderedToSit(sitting);
        this.setInSittingPose(sitting);
        this.setPose(sitting ? Pose.SITTING : Pose.STANDING);
    }

    //WANDER MODE GETTER AND SETTER
    public boolean isWandering() {
        return this.wandering;
    }

    public void setWandering(boolean wandering) {
        this.wandering = wandering;
        //REMEMBER WHERE WANDERING STARTED
        if (wandering) {
            if (this.wanderCenter == null) {
                this.wanderCenter = this.blockPosition();
            }
        } else {
            this.wanderCenter = null;
        }
    }

    public @Nullable BlockPos getWanderCenter() {
        return this.wanderCenter;
    }

    //HORIZONTAL DISTANCE CHECK AGAINST THE WANDER AREA
    public boolean isInsideWanderArea(Vec3 pos) {
        if (this.wanderCenter == null) {
            return true;
        }
        double dx = pos.x() - (this.wanderCenter.getX() + 0.5D);
        double dz = pos.z() - (this.wanderCenter.getZ() + 0.5D);
        return dx * dx + dz * dz <= (double) WANDER_RADIUS * WANDER_RADIUS;
    }

    //RANDOM STROLL THAT STAYS WITHIN WANDER_RADIUS BLOCKS OF THE WANDER CENTER
    static class WanderAroundCenterGoal extends WaterAvoidingRandomStrollGoal {
        private final AbstractPlushEntity plush;

        WanderAroundCenterGoal(AbstractPlushEntity plush) {
            super(plush, 0.8F);
            this.plush = plush;
        }

        private boolean isActive() {
            return this.plush.isWandering() && !this.plush.isOrderedToSit();
        }

        @Override
        public boolean canUse() {
            if (!this.isActive()) {
                return false;
            }
            //WALK BACK RIGHT AWAY IF OUTSIDE THE AREA (E.G. AFTER A FIGHT)
            if (!this.plush.isInsideWanderArea(this.plush.position())) {
                this.triggerImmediately();
            }
            return super.canUse();
        }

        @Override
        public boolean canContinueToUse() {
            return this.isActive() && super.canContinueToUse();
        }

        @Override
        protected @Nullable Vec3 getPosition() {
            BlockPos center = this.plush.getWanderCenter();
            if (center == null) {
                return super.getPosition();
            }

            //OUTSIDE THE AREA: HEAD BACK TOWARDS THE CENTER
            if (!this.plush.isInsideWanderArea(this.plush.position())) {
                return LandRandomPos.getPosTowards(this.plush, 10, 7, Vec3.atBottomCenterOf(center));
            }

            //INSIDE THE AREA: PICK A RANDOM TARGET THAT STAYS INSIDE
            for (int attempt = 0; attempt < 10; attempt++) {
                Vec3 target = super.getPosition();
                if (target != null && this.plush.isInsideWanderArea(target)) {
                    return target;
                }
            }
            return null;
        }
    }

    //PICK UP SWORDS FORM THE GROUND
    @Override
    public boolean wantsToPickUp(ServerLevel level, ItemStack stack) {
        return stack.is(ItemTags.SWORDS);
    }

    @Override
    public boolean canPickUpLoot() {
        return true;
    }

    //HANDLE NEARBY SONG PLAYING
    @Override
    public void aiStep() {
        super.aiStep();
        this.updateSwingTime();

        //GET NEARBY SONG PLAYING
        if (
            this.songSource == null
                || !this.songSource.closerToCenterThan(this.position(), 8D)
                || !this.level().getBlockState(this.songSource).is(Blocks.JUKEBOX)
        )
        {
            this.songPlaying = false;
            this.songSource = null;
        }

        //INCREMENT SPAWN TIMER IF IT'S LESS THAN 10
        if (this.entityData.get(SPAWN_AGE) < 10){
            this.entityData.set(SPAWN_AGE, Math.min(this.tickCount, 10));
        }

    }

    //IS SONG PLAYING FUNCTION
    public boolean isSongPlaying() {
        return this.songPlaying;
    }

    //NEARBY SONG PLAYING
    @Override
    public void setRecordPlayingNearby(BlockPos songPosition, boolean playing) {
        this.songSource = songPosition;
        this.songPlaying = playing;
    }

    //REGISTRY NAME OF THE ENTITY, E.G. "miku_plush"
    public String getPlushName(){
        return ModUtil.getEntityId(this.getType());
    }

    //LIST OF VARIANTS
    protected List<String> getVariantList(){
        List<String> variantList = null;
        for (List<String> variants : PlushVariants.ALL_PLUSH_VARIANTS) {
            if (variants.contains(this.getPlushName())) {
                variantList = variants;
            }
        }
        return variantList;
    }

    //GET THE VARIANT DATA TRACKER
    protected EntityDataAccessor<Integer> getVariantDataTracker(){
        return VARIANT;
    }

    //GET DATA TRACKER VALUE
    private int getTrackedVariant() {
        return this.entityData.get(this.getVariantDataTracker());
    }

    //GET VARIANT NAME
    public String getVariant() {
        return this.getVariantList().get(this.entityData.get(this.getVariantDataTracker()));
    }

    //SET VARIANT BY ID
    public void setVariant(Integer variant) {
        this.entityData.set(this.getVariantDataTracker(), variant/* & 255*/);
    }

    //SET UP DATA TRACKER
    @Override
    public void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(SPAWN_AGE, 0);
        builder.define(this.getVariantDataTracker(), 0);
    }

    //LOAD AND SAVE DATA
    @Override
    public void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.entityData.set(SPAWN_AGE, input.getIntOr("SpawnAge", 0));
        this.entityData.set(this.getVariantDataTracker(), input.getIntOr("Variant", 0));
        this.setWandering(input.getBooleanOr("Wandering", false));
        if (this.isWandering()) {
            BlockPos current = this.blockPosition();
            this.wanderCenter = new BlockPos(
                input.getIntOr("WanderCenterX", current.getX()),
                input.getIntOr("WanderCenterY", current.getY()),
                input.getIntOr("WanderCenterZ", current.getZ())
            );
        }
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return false;
    }

    @Override
    public void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putInt("SpawnAge", Math.min(this.tickCount, 11));
        output.putInt("Variant", this.getTrackedVariant());
        output.putBoolean("Wandering", this.isWandering());
        if (this.wanderCenter != null) {
            output.putInt("WanderCenterX", this.wanderCenter.getX());
            output.putInt("WanderCenterY", this.wanderCenter.getY());
            output.putInt("WanderCenterZ", this.wanderCenter.getZ());
        }
    }

    //NO CHILD
    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel world, AgeableMob entity) {
        return null;
    }

    public void setVariantByBlock(String variant) {
        for (int variation = 0; variation < this.getVariantList().size(); variation++) {
            if (this.getVariantList().get(variation).equals(variant))
                this.entityData.set(this.getVariantDataTracker(), variation);
        }
    }

}
