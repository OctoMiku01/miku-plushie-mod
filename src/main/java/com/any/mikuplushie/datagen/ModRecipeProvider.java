package com.any.mikuplushie.datagen;

import com.any.mikuplushie.registry.ModBlocks;
import com.any.mikuplushie.registry.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends FabricRecipeProvider {

    public ModRecipeProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected RecipeProvider createRecipeProvider(HolderLookup.Provider registryLookup, RecipeOutput exporter) {
        return new Recipes(registryLookup, exporter);
    }

    @Override
    public String getName() {
        return "Miku Plushie Recipes";
    }

    //SINCE 1.21.2 THE RECIPE HELPERS (shaped, shapeless, has...) LIVE IN A RecipeProvider INSTANCE
    private static class Recipes extends RecipeProvider {

        protected Recipes(HolderLookup.Provider registries, RecipeOutput output) {
            super(registries, output);
        }

        @Override
        public void buildRecipes() {

            shaped(RecipeCategory.FOOD, ModItems.CANUDINHO)
                .pattern("p")
                .pattern("p")
                .define('p', Items.PAPER)
                .unlockedBy(getHasName(Items.PAPER), has(Items.PAPER))
                .save(output);

            shaped(RecipeCategory.FOOD, ModItems.BAGUETTE)
                .pattern("www")
                .pattern("www")
                .define('w', Items.WHEAT)
                .unlockedBy(getHasName(Items.WHEAT), has(Items.WHEAT))
                .save(output);

            simpleShapeless(ModItems.AKITA_NERU_PHONE, Items.GOLD_INGOT, Items.REDSTONE, Items.STAINED_GLASS.pick(DyeColor.BLACK));

            shaped(RecipeCategory.FOOD, ModItems.VOCALOID_HEART)
                .pattern("LN")
                .pattern("BP")
                .define('L', ModItems.LEEK)
                .define('N', Items.NOTE_BLOCK)
                .define('B', ModItems.BAGUETTE)
                .define('P', ModItems.AKITA_NERU_PHONE)
                .unlockedBy(getHasName(ModItems.LEEK), has(ModItems.LEEK))
                .unlockedBy(getHasName(Items.NOTE_BLOCK), has(Items.NOTE_BLOCK))
                .unlockedBy(getHasName(ModItems.BAGUETTE), has(ModItems.BAGUETTE))
                .unlockedBy(getHasName(ModItems.AKITA_NERU_PHONE), has(ModItems.AKITA_NERU_PHONE))
                .save(output);

            //AIKO
            {
                shaped(RecipeCategory.DECORATIONS, ModBlocks.AIKO_PLUSH)
                    .pattern("121")
                    .pattern("131")
                    .define('1', Items.WOOL.pick(DyeColor.BLUE))
                    .define('2', Items.WOOL.pick(DyeColor.WHITE))
                    .define('3', Items.WOOL.pick(DyeColor.GREEN))
                    .unlockedBy(getHasName(Items.WOOL.pick(DyeColor.BLUE)), has(Items.WOOL.pick(DyeColor.BLUE)))
                    .unlockedBy(getHasName(Items.WOOL.pick(DyeColor.WHITE)), has(Items.WOOL.pick(DyeColor.WHITE)))
                    .unlockedBy(getHasName(Items.WOOL.pick(DyeColor.GREEN)), has(Items.WOOL.pick(DyeColor.GREEN)))
                    .save(output);
            }

            //NERU
            {
                shaped(RecipeCategory.DECORATIONS, ModBlocks.AKITA_NERU_PLUSH)
                    .pattern("121")
                    .pattern("131")
                    .define('1', Items.WOOL.pick(DyeColor.YELLOW))
                    .define('2', Items.WOOL.pick(DyeColor.WHITE))
                    .define('3', Items.WOOL.pick(DyeColor.BROWN))
                    .unlockedBy(getHasName(Items.WOOL.pick(DyeColor.YELLOW)), has(Items.WOOL.pick(DyeColor.YELLOW)))
                    .unlockedBy(getHasName(Items.WOOL.pick(DyeColor.WHITE)), has(Items.WOOL.pick(DyeColor.WHITE)))
                    .unlockedBy(getHasName(Items.WOOL.pick(DyeColor.BROWN)), has(Items.WOOL.pick(DyeColor.BROWN)))
                    .save(output);

                plushShapeless(ModBlocks.AKITA_NERU_PLUSH_TAILS, ModBlocks.AKITA_NERU_PLUSH, Items.WOOL.pick(DyeColor.YELLOW), Items.REDSTONE);
            }

            //RIN
            {
                shaped(RecipeCategory.DECORATIONS, ModBlocks.RIN_PLUSH)
                    .pattern("121")
                    .pattern("121")
                    .define('1', Items.WOOL.pick(DyeColor.YELLOW))
                    .define('2', Items.WOOL.pick(DyeColor.WHITE))
                    .unlockedBy(getHasName(Items.WOOL.pick(DyeColor.YELLOW)), has(Items.WOOL.pick(DyeColor.YELLOW)))
                    .unlockedBy(getHasName(Items.WOOL.pick(DyeColor.WHITE)), has(Items.WOOL.pick(DyeColor.WHITE)))
                    .save(output);
            }

            //LEN
            {
                shaped(RecipeCategory.DECORATIONS, ModBlocks.LEN_PLUSH)
                    .pattern("121")
                    .pattern("131")
                    .define('1', Items.WOOL.pick(DyeColor.YELLOW))
                    .define('2', Items.WOOL.pick(DyeColor.WHITE))
                    .define('3', Items.WOOL.pick(DyeColor.GRAY))
                    .unlockedBy(getHasName(Items.WOOL.pick(DyeColor.YELLOW)), has(Items.WOOL.pick(DyeColor.YELLOW)))
                    .unlockedBy(getHasName(Items.WOOL.pick(DyeColor.WHITE)), has(Items.WOOL.pick(DyeColor.WHITE)))
                    .unlockedBy(getHasName(Items.WOOL.pick(DyeColor.GRAY)), has(Items.WOOL.pick(DyeColor.GRAY)))
                    .save(output);
            }

            //KONOHA
            {
                shaped(RecipeCategory.DECORATIONS, ModBlocks.KONOHA_PLUSH)
                    .pattern("121")
                    .pattern("323")
                    .define('1', Items.WOOL.pick(DyeColor.WHITE))
                    .define('2', Items.WOOL.pick(DyeColor.LIGHT_GRAY))
                    .define('3', Items.WOOL.pick(DyeColor.LIME))
                    .unlockedBy(getHasName(Items.WOOL.pick(DyeColor.WHITE)), has(Items.WOOL.pick(DyeColor.WHITE)))
                    .unlockedBy(getHasName(Items.WOOL.pick(DyeColor.LIGHT_GRAY)), has(Items.WOOL.pick(DyeColor.LIGHT_GRAY)))
                    .unlockedBy(getHasName(Items.WOOL.pick(DyeColor.LIME)), has(Items.WOOL.pick(DyeColor.LIME)))
                    .save(output);
            }

            //LUKA
            {
                shaped(RecipeCategory.DECORATIONS, ModBlocks.LUKA_PLUSH)
                    .pattern("121")
                    .pattern("131")
                    .define('1', Items.WOOL.pick(DyeColor.PINK))
                    .define('2', Items.WOOL.pick(DyeColor.YELLOW))
                    .define('3', Items.WOOL.pick(DyeColor.BROWN))
                    .unlockedBy(getHasName(Items.WOOL.pick(DyeColor.PINK)), has(Items.WOOL.pick(DyeColor.PINK)))
                    .unlockedBy(getHasName(Items.WOOL.pick(DyeColor.YELLOW)), has(Items.WOOL.pick(DyeColor.YELLOW)))
                    .unlockedBy(getHasName(Items.WOOL.pick(DyeColor.BROWN)), has(Items.WOOL.pick(DyeColor.BROWN)))
                    .save(output);
            }

            //MIKU
            {
                shaped(RecipeCategory.DECORATIONS, ModBlocks.MIKU_PLUSH)
                    .pattern("121")
                    .pattern("131")
                    .define('1', Items.WOOL.pick(DyeColor.CYAN))
                    .define('2', Items.WOOL.pick(DyeColor.WHITE))
                    .define('3', Items.WOOL.pick(DyeColor.GRAY))
                    .unlockedBy(getHasName(Items.WOOL.pick(DyeColor.CYAN)), has(Items.WOOL.pick(DyeColor.CYAN)))
                    .unlockedBy(getHasName(Items.WOOL.pick(DyeColor.WHITE)), has(Items.WOOL.pick(DyeColor.WHITE)))
                    .unlockedBy(getHasName(Items.WOOL.pick(DyeColor.GRAY)), has(Items.WOOL.pick(DyeColor.GRAY)))
                    .save(output);

                plushShapeless(ModBlocks.MIKU_PLUSH_BR, ModBlocks.MIKU_PLUSH, Items.WOOL.pick(DyeColor.YELLOW), Items.WOOL.pick(DyeColor.GREEN));
                plushShapeless(ModBlocks.MIKU_PLUSH_BR_BA, ModBlocks.MIKU_PLUSH, Items.WOOL.pick(DyeColor.RED), Items.WOOL.pick(DyeColor.WHITE), Items.WOOL.pick(DyeColor.BLUE));
                plushShapeless(ModBlocks.MIKU_PLUSH_BIK, ModBlocks.MIKU_PLUSH, Items.WOOL.pick(DyeColor.BLUE), Items.WATER_BUCKET);
                plushShapeless(ModBlocks.MIKU_PLUSH_BR_BEACH, ModBlocks.MIKU_PLUSH, Items.WOOL.pick(DyeColor.YELLOW), Items.WOOL.pick(DyeColor.GREEN), Items.SAND);
                plushShapeless(ModBlocks.MIKU_PLUSH_BR_BRAID, ModBlocks.MIKU_PLUSH, Items.WOOL.pick(DyeColor.YELLOW), Items.WOOL.pick(DyeColor.GREEN), Items.GLOWSTONE);
                plushShapeless(ModBlocks.MIKU_PLUSH_BR_BA_DRUM, ModBlocks.MIKU_PLUSH, Items.WOOL.pick(DyeColor.RED), Items.WOOL.pick(DyeColor.WHITE), Items.WOOL.pick(DyeColor.BLUE), Items.NOTE_BLOCK);
                plushShapeless(ModBlocks.MIKU_PLUSH_BR_PA, ModBlocks.MIKU_PLUSH, Items.WOOL.pick(DyeColor.WHITE), Items.CORNFLOWER);
                plushShapeless(ModBlocks.MIKU_PLUSH_BR_SP, ModBlocks.MIKU_PLUSH, Items.WOOL.pick(DyeColor.WHITE), Items.WOOL.pick(DyeColor.RED), Items.WOOL.pick(DyeColor.BLACK), Items.CONCRETE.pick(DyeColor.GRAY));
                plushShapeless(ModBlocks.MIKU_PLUSH_BR_MG, ModBlocks.MIKU_PLUSH, Items.WOOL.pick(DyeColor.BROWN), Items.WOOL.pick(DyeColor.RED), Items.GOLD_NUGGET);
                plushShapeless(ModBlocks.MIKU_PLUSH_BR_BROWN_BRO, ModBlocks.MIKU_PLUSH, Items.WOOL.pick(DyeColor.BROWN), Items.WOOL.pick(DyeColor.BLACK), Items.IRON_NUGGET);
                plushShapeless(ModBlocks.MIKU_PLUSH_BR_ELECTRICIAN, ModBlocks.MIKU_PLUSH, Items.WOOL.pick(DyeColor.BROWN), Items.WOOL.pick(DyeColor.BLUE), Items.REDSTONE);
                plushShapeless(ModBlocks.MIKU_PLUSH_BR_BIK_ORANGE, ModBlocks.MIKU_PLUSH, Items.WOOL.pick(DyeColor.ORANGE), Items.WATER_BUCKET);
                plushShapeless(ModBlocks.MIKU_PLUSH_BR_AM, ModBlocks.MIKU_PLUSH, Items.WOOL.pick(DyeColor.GREEN), Items.WOOL.pick(DyeColor.YELLOW), Items.WOOL.pick(DyeColor.BLUE), Items.JUNGLE_SAPLING);
                plushShapeless(ModBlocks.MIKU_PLUSH_BR_FUT_FLA, ModBlocks.MIKU_PLUSH, Items.WOOL.pick(DyeColor.RED), Items.WOOL.pick(DyeColor.BLACK));
                plushShapeless(ModBlocks.MIKU_PLUSH_BR_FUT_CAM, ModBlocks.MIKU_PLUSH, Items.WOOL.pick(DyeColor.LIGHT_GRAY), Items.WOOL.pick(DyeColor.BLACK));
                plushShapeless(ModBlocks.MIKU_PLUSH_BR_FUT_CRVG, ModBlocks.MIKU_PLUSH, Items.WOOL.pick(DyeColor.WHITE), Items.WOOL.pick(DyeColor.BLACK), Items.CARTOGRAPHY_TABLE);
                plushShapeless(ModBlocks.MIKU_PLUSH_BR_GO, ModBlocks.MIKU_PLUSH, Items.WOOL.pick(DyeColor.YELLOW), Items.WOOL.pick(DyeColor.GREEN), Items.LEAD);
                plushShapeless(ModBlocks.MIKU_PLUSH_BR_SCHOOL_PE, ModBlocks.MIKU_PLUSH, Items.WOOL.pick(DyeColor.WHITE), Items.WOOL.pick(DyeColor.BLUE), Items.TUBE_CORAL_FAN);
                plushShapeless(ModBlocks.MIKU_PLUSH_BR_RS, ModBlocks.MIKU_PLUSH, Items.WOOL.pick(DyeColor.GRAY), Items.WOOL.pick(DyeColor.RED), Items.MOSS_BLOCK);
                plushShapeless(ModBlocks.MIKU_PLUSH_FROG, ModBlocks.MIKU_PLUSH, Items.WOOL.pick(DyeColor.LIGHT_BLUE), Items.TADPOLE_BUCKET);
                plushShapeless(ModBlocks.MIKU_PLUSH_MUSHROOM, ModBlocks.MIKU_PLUSH, Items.MOSS_BLOCK, Items.RED_MUSHROOM_BLOCK);
                plushShapeless(ModBlocks.MIKU_PLUSH_SENBONZAKURA, ModBlocks.MIKU_PLUSH, Items.WOOL.pick(DyeColor.GREEN), Items.CHERRY_LOG);
                plushShapeless(ModBlocks.MIKU_PLUSH_URAOTOMELOVERS, ModBlocks.MIKU_PLUSH, Items.WOOL.pick(DyeColor.WHITE), Items.WOOL.pick(DyeColor.BLACK));
                plushShapeless(ModBlocks.MIKU_PLUSH_PERSONADANCING, ModBlocks.MIKU_PLUSH, Items.WOOL.pick(DyeColor.WHITE), Items.WOOL.pick(DyeColor.BLACK), Items.NOTE_BLOCK);
                plushShapeless(ModBlocks.MIKU_PLUSH_HELLOPLANET, ModBlocks.MIKU_PLUSH, Items.WOOL.pick(DyeColor.WHITE), Items.WOOL.pick(DyeColor.LIME), Items.WOOL.pick(DyeColor.MAGENTA));
                plushShapeless(ModBlocks.MIKU_PLUSH_HACHUNE, ModBlocks.MIKU_PLUSH, Items.LILY_OF_THE_VALLEY);
                plushShapeless(ModBlocks.MIKU_PLUSH_ZATSUNE, ModBlocks.MIKU_PLUSH, Items.WOOL.pick(DyeColor.BLACK), Items.WOOL.pick(DyeColor.BLACK));
                plushShapeless(ModBlocks.MIKU_PLUSH_INFINITY, ModBlocks.MIKU_PLUSH, Items.ENDER_EYE);
                plushShapeless(ModBlocks.MIKU_PLUSH_VAMPIRE, ModBlocks.MIKU_PLUSH, Items.FERMENTED_SPIDER_EYE);
                plushShapeless(ModBlocks.MIKU_PLUSH_WEREWOMAN, ModBlocks.MIKU_PLUSH, Items.BONE, Items.MUTTON);
                plushShapeless(ModBlocks.MIKU_PLUSH_JASON, ModBlocks.MIKU_PLUSH, Items.BIRCH_PLANKS, Items.WOOL.pick(DyeColor.BROWN), Items.IRON_SWORD);
                plushShapeless(ModBlocks.MIKU_PLUSH_MICHAEL_MYERS, ModBlocks.MIKU_PLUSH, Items.WOOL.pick(DyeColor.BROWN), Items.WOOL.pick(DyeColor.BLUE), Items.IRON_SWORD);
                plushShapeless(ModBlocks.MIKU_PLUSH_PUMPKIN, ModBlocks.MIKU_PLUSH, Items.CARVED_PUMPKIN);
                plushShapeless(ModBlocks.MIKU_PLUSH_GHOSTFACE, ModBlocks.MIKU_PLUSH, Items.BIRCH_PLANKS, Items.WOOL.pick(DyeColor.BLACK), Items.IRON_SWORD);
                plushShapeless(ModBlocks.MIKU_PLUSH_FRANKENSTEIN, ModBlocks.MIKU_PLUSH, Items.WOOL.pick(DyeColor.BROWN), Items.WOOL.pick(DyeColor.GREEN), Items.LIGHTNING_ROD.weathering().unaffected());
                plushShapeless(ModBlocks.MIKU_PLUSH_MUMMY, ModBlocks.MIKU_PLUSH, Items.WOOL.pick(DyeColor.BLACK), Items.PAPER, Items.PAPER);

                shaped(RecipeCategory.DECORATIONS, ModBlocks.MIKU_PLUSH_GHOST)
                    .pattern("121")
                    .pattern("131")
                    .define('1', Items.STAINED_GLASS.pick(DyeColor.CYAN))
                    .define('2', Items.STAINED_GLASS.pick(DyeColor.WHITE))
                    .define('3', Items.STAINED_GLASS.pick(DyeColor.GRAY))
                    .unlockedBy(getHasName(Items.STAINED_GLASS.pick(DyeColor.CYAN)), has(Items.STAINED_GLASS.pick(DyeColor.CYAN)))
                    .unlockedBy(getHasName(Items.STAINED_GLASS.pick(DyeColor.WHITE)), has(Items.STAINED_GLASS.pick(DyeColor.WHITE)))
                    .unlockedBy(getHasName(Items.STAINED_GLASS.pick(DyeColor.GRAY)), has(Items.STAINED_GLASS.pick(DyeColor.GRAY)))
                    .save(output);

                plushShapeless(ModBlocks.MIKU_PLUSH_PATATI, ModBlocks.MIKU_PLUSH, Items.WOOL.pick(DyeColor.YELLOW), Items.WOOL.pick(DyeColor.LIGHT_BLUE), Items.WOOL.pick(DyeColor.WHITE));
                plushShapeless(ModBlocks.MIKU_PLUSH_PATATA, ModBlocks.MIKU_PLUSH, Items.WOOL.pick(DyeColor.YELLOW), Items.WOOL.pick(DyeColor.LIME), Items.WOOL.pick(DyeColor.RED));
                plushShapeless(ModBlocks.MIKU_PLUSH_DEVIL, ModBlocks.MIKU_PLUSH, Items.MAGMA_BLOCK, Items.NETHERRACK);
                plushShapeless(ModBlocks.MIKU_PLUSH_WITCH, ModBlocks.MIKU_PLUSH, Items.WOOL.pick(DyeColor.PURPLE), Items.WOOL.pick(DyeColor.GREEN), Items.STICK, Items.WHEAT);
                plushShapeless(ModBlocks.MIKU_PLUSH_SANTA, ModBlocks.MIKU_PLUSH, Items.WOOL.pick(DyeColor.RED), Items.WOOL.pick(DyeColor.WHITE), Items.SNOW_BLOCK);
                plushShapeless(ModBlocks.MIKU_PLUSH_REINDEER, ModBlocks.MIKU_PLUSH, Items.WOOL.pick(DyeColor.BROWN), Items.REDSTONE_TORCH);
                plushShapeless(ModBlocks.MIKU_PLUSH_SANTA_ELF, ModBlocks.MIKU_PLUSH, Items.WOOL.pick(DyeColor.LIME), Items.WOOL.pick(DyeColor.RED));
                plushShapeless(ModBlocks.MIKU_PLUSH_XMAS_TREE, ModBlocks.MIKU_PLUSH, Items.SPRUCE_LEAVES, Items.WOOL.pick(DyeColor.RED));
                plushShapeless(ModBlocks.MIKU_PLUSH_SONIC_CROSSWORLDS, ModBlocks.MIKU_PLUSH, Items.WOOL.pick(DyeColor.MAGENTA), Items.WOOL.pick(DyeColor.BLACK));
                plushShapeless(ModBlocks.MIKU_PLUSH_FORTNITE_NEKO, ModBlocks.MIKU_PLUSH, Items.WOOL.pick(DyeColor.PINK), Items.WOOL.pick(DyeColor.LIGHT_BLUE));
                plushShapeless(ModBlocks.MIKU_PLUSH_V4, ModBlocks.MIKU_PLUSH, Items.IRON_INGOT);
                plushShapeless(ModBlocks.MIKU_PLUSH_MESMERIZER, ModBlocks.MIKU_PLUSH, Items.WOOL.pick(DyeColor.LIGHT_BLUE), Items.WOOL.pick(DyeColor.LIGHT_BLUE));
                plushShapeless(ModBlocks.MIKU_PLUSH_SONIC, ModBlocks.MIKU_PLUSH, Items.WOOL.pick(DyeColor.BLUE), Items.REDSTONE);
                plushShapeless(ModBlocks.MIKU_PLUSH_DIGITAL_STARS_2025, ModBlocks.MIKU_PLUSH, Items.NOTE_BLOCK, Items.GOLD_NUGGET, Items.GOLD_NUGGET, Items.GOLD_NUGGET, Items.GOLD_NUGGET);
                plushShapeless(ModBlocks.MIKU_PLUSH_ROTTEN_GIRL, ModBlocks.MIKU_PLUSH, Items.ROTTEN_FLESH);
                plushShapeless(ModBlocks.MIKU_PLUSH_PSYCHO_MODE, ModBlocks.MIKU_PLUSH, Items.AMETHYST_SHARD);
                plushShapeless(ModBlocks.MIKU_PLUSH_DONT_BELIEVE_IN_T, ModBlocks.MIKU_PLUSH, Items.WOOL.pick(DyeColor.WHITE), Items.WOOL.pick(DyeColor.LIGHT_BLUE));
                plushShapeless(ModBlocks.MIKU_PLUSH_STATIC, ModBlocks.MIKU_PLUSH, Items.DYE.pick(DyeColor.YELLOW), Items.DYE.pick(DyeColor.MAGENTA), Items.DYE.pick(DyeColor.CYAN), Items.WOOL.pick(DyeColor.BLUE));
                plushShapeless(ModBlocks.MIKU_PLUSH_MOCHIMOCHI, ModBlocks.MIKU_PLUSH, Items.WOOL.pick(DyeColor.LIGHT_BLUE), Items.WOOL.pick(DyeColor.PINK), Items.PINK_PETALS);
                plushShapeless(ModBlocks.MIKU_PLUSH_MONITORING, ModBlocks.MIKU_PLUSH, Items.WOOL.pick(DyeColor.BROWN), Items.SPYGLASS);
                plushShapeless(ModBlocks.MIKU_PLUSH_HOLLOW_KNIGHT, ModBlocks.MIKU_PLUSH, Items.WOOL.pick(DyeColor.BLACK), Items.IRON_SWORD, Items.BONE_BLOCK);
                plushShapeless(ModBlocks.MIKU_PLUSH_HORNET, ModBlocks.MIKU_PLUSH, Items.WOOL.pick(DyeColor.RED), Items.IRON_SWORD, Items.BONE_BLOCK);
                plushShapeless(ModBlocks.MIKU_PLUSH_WORLD_IS_MINE, ModBlocks.MIKU_PLUSH, Items.WOOL.pick(DyeColor.WHITE), Items.GOLD_INGOT, Items.CAKE);
                plushShapeless(ModBlocks.MIKU_PLUSH_ROLLING_GIRL, ModBlocks.MIKU_PLUSH, Items.WOOL.pick(DyeColor.WHITE), Items.WOOL.pick(DyeColor.BROWN));
                plushShapeless(ModBlocks.MIKU_PLUSH_DEEP_SEA_GIRL, ModBlocks.MIKU_PLUSH, Items.TUBE_CORAL, Items.BUBBLE_CORAL);
                plushShapeless(ModBlocks.MIKU_PLUSH_LUCARIO_Z, ModBlocks.MIKU_PLUSH, Items.IRON_BARS, Items.WOOL.pick(DyeColor.WHITE), Items.REDSTONE);
                plushShapeless(ModBlocks.MIKU_PLUSH_PPPP, ModBlocks.MIKU_PLUSH, Items.WOOL.pick(DyeColor.CYAN), Items.WOOL.pick(DyeColor.WHITE));
                plushShapeless(ModBlocks.MIKU_PLUSH_LINK, ModBlocks.MIKU_PLUSH, Items.WOOL.pick(DyeColor.GREEN), Items.EMERALD);
                plushShapeless(ModBlocks.MIKU_PLUSH_RENAISSANCE, ModBlocks.MIKU_PLUSH, Items.WOOL.pick(DyeColor.WHITE), Items.WRITABLE_BOOK);
            }

            //TETO
            {
                shaped(RecipeCategory.DECORATIONS, ModBlocks.TETO_PLUSH)
                    .pattern("121")
                    .pattern("131")
                    .define('1', Items.WOOL.pick(DyeColor.RED))
                    .define('2', Items.WOOL.pick(DyeColor.WHITE))
                    .define('3', Items.WOOL.pick(DyeColor.LIGHT_GRAY))
                    .unlockedBy(getHasName(Items.WOOL.pick(DyeColor.RED)), has(Items.WOOL.pick(DyeColor.RED)))
                    .unlockedBy(getHasName(Items.WOOL.pick(DyeColor.WHITE)), has(Items.WOOL.pick(DyeColor.WHITE)))
                    .unlockedBy(getHasName(Items.WOOL.pick(DyeColor.LIGHT_GRAY)), has(Items.WOOL.pick(DyeColor.LIGHT_GRAY)))
                    .save(output);
                pickaxeRecipe(ModItems.TETO_PICKAXE, ModBlocks.TETO_PLUSH);

                plushShapeless(ModBlocks.TETO_PLUSH_MESMERIZER, ModBlocks.TETO_PLUSH, Items.WOOL.pick(DyeColor.RED), Items.WOOL.pick(DyeColor.RED));
                pickaxeRecipe(ModItems.TETO_PICKAXE_MESMERIZER, ModBlocks.TETO_PLUSH_MESMERIZER);

                plushShapeless(ModBlocks.TETO_PLUSH_SHADOW, ModBlocks.TETO_PLUSH, Items.WOOL.pick(DyeColor.BLACK), Items.REDSTONE);

                plushShapeless(ModBlocks.TETO_PLUSH_BIRDBRAIN, ModBlocks.TETO_PLUSH, Items.WHEAT_SEEDS, Items.EGG);
                pickaxeRecipe(ModItems.TETO_PICKAXE_BIRDBRAIN, ModBlocks.TETO_PLUSH_BIRDBRAIN);

                plushShapeless(ModBlocks.TETO_PLUSH_REGRET_ROCK, ModBlocks.TETO_PLUSH, Items.DYE.pick(DyeColor.PURPLE));
                pickaxeRecipe(ModItems.TETO_PICKAXE_REGRET_ROCK, ModBlocks.TETO_PLUSH_REGRET_ROCK);

                plushShapeless(ModBlocks.TETO_PLUSH_DONT_BELIEVE_IN_T, ModBlocks.TETO_PLUSH, Items.WOOL.pick(DyeColor.WHITE), Items.WOOL.pick(DyeColor.RED));
                pickaxeRecipe(ModItems.TETO_PICKAXE_DONT_BELIEVE_IN_T, ModBlocks.TETO_PLUSH_DONT_BELIEVE_IN_T);

                plushShapeless(ModBlocks.TETO_PLUSH_LIAR_DANCER, ModBlocks.TETO_PLUSH, Items.STAINED_GLASS.pick(DyeColor.BLACK), Items.STAINED_GLASS.pick(DyeColor.BLACK), Items.WOOL.pick(DyeColor.WHITE));
                pickaxeRecipe(ModItems.TETO_PICKAXE_LIAR_DANCER, ModBlocks.TETO_PLUSH_LIAR_DANCER);

                plushShapeless(ModBlocks.TETO_PLUSH_WHATCHACALLITSNAME, ModBlocks.TETO_PLUSH, Items.GLASS, Items.GLASS, Items.WOOL.pick(DyeColor.RED), Items.WOOL.pick(DyeColor.ORANGE));
                pickaxeRecipe(ModItems.TETO_PICKAXE_WHATCHACALLITSNAME, ModBlocks.TETO_PLUSH_WHATCHACALLITSNAME);

                plushShapeless(ModBlocks.TETO_PLUSH_SOME_MORE_OF_THAT_SONG, ModBlocks.TETO_PLUSH, Items.WOOL.pick(DyeColor.LIGHT_BLUE));
                pickaxeRecipe(ModItems.TETO_PICKAXE_SOME_MORE_OF_THAT_SONG, ModBlocks.TETO_PLUSH_SOME_MORE_OF_THAT_SONG);

                plushShapeless(ModBlocks.TETO_PLUSH_LOBSTER, ModBlocks.TETO_PLUSH, Items.SEAGRASS, Items.SEAGRASS);

                plushShapeless(ModBlocks.TETO_PLUSH_SYNTHV, ModBlocks.TETO_PLUSH, Items.IRON_INGOT);
                pickaxeRecipe(ModItems.TETO_PICKAXE_SYNTHV, ModBlocks.TETO_PLUSH_SYNTHV);

                plushShapeless(ModBlocks.TETO_PLUSH_SPOKEN_FOR, ModBlocks.TETO_PLUSH, Items.DYE.pick(DyeColor.PINK), Items.GLOWSTONE_DUST);
                pickaxeRecipe(ModItems.TETO_PICKAXE_SPOKEN_FOR, ModBlocks.TETO_PLUSH_SPOKEN_FOR);

                plushShapeless(ModBlocks.TETO_PLUSH_PPPP, ModBlocks.TETO_PLUSH, Items.WOOL.pick(DyeColor.RED), Items.WOOL.pick(DyeColor.WHITE));
                pickaxeRecipe(ModItems.TETO_PICKAXE_PPPP, ModBlocks.TETO_PLUSH_PPPP);

                plushShapeless(ModBlocks.TETO_PLUSH_SHRIMP, ModBlocks.TETO_PLUSH, Items.KELP, Items.KELP);

            }

            //MEIKO
            {
                shaped(RecipeCategory.DECORATIONS, ModBlocks.MEIKO_PLUSH)
                    .pattern("121")
                    .pattern(" 3 ")
                    .define('1', Items.WOOL.pick(DyeColor.BROWN))
                    .define('2', Items.WOOL.pick(DyeColor.WHITE))
                    .define('3', Items.WOOL.pick(DyeColor.RED))
                    .unlockedBy(getHasName(Items.WOOL.pick(DyeColor.BROWN)), has(Items.WOOL.pick(DyeColor.BROWN)))
                    .unlockedBy(getHasName(Items.WOOL.pick(DyeColor.WHITE)), has(Items.WOOL.pick(DyeColor.WHITE)))
                    .unlockedBy(getHasName(Items.WOOL.pick(DyeColor.RED)), has(Items.WOOL.pick(DyeColor.RED)))
                    .save(output);

                plushShapeless(ModBlocks.MEIKO_PLUSH_V3, ModBlocks.MEIKO_PLUSH, Items.IRON_INGOT);
                plushShapeless(ModBlocks.MEIKO_PLUSH_V4, ModBlocks.MEIKO_PLUSH, Items.IRON_INGOT, Items.IRON_INGOT);
            }

            //GUMI
            {
                shaped(RecipeCategory.DECORATIONS, ModBlocks.GUMI_PLUSH)
                    .pattern("121")
                    .pattern(" 3 ")
                    .define('1', Items.WOOL.pick(DyeColor.LIME))
                    .define('2', Items.WOOL.pick(DyeColor.WHITE))
                    .define('3', Items.WOOL.pick(DyeColor.ORANGE))
                    .unlockedBy(getHasName(Items.WOOL.pick(DyeColor.LIME)), has(Items.WOOL.pick(DyeColor.LIME)))
                    .unlockedBy(getHasName(Items.WOOL.pick(DyeColor.WHITE)), has(Items.WOOL.pick(DyeColor.WHITE)))
                    .unlockedBy(getHasName(Items.WOOL.pick(DyeColor.ORANGE)), has(Items.WOOL.pick(DyeColor.ORANGE)))
                    .save(output);

                plushShapeless(ModBlocks.GUMI_PLUSH_V3, ModBlocks.GUMI_PLUSH, Items.IRON_INGOT);
                plushShapeless(ModBlocks.GUMI_PLUSH_V4, ModBlocks.GUMI_PLUSH, Items.IRON_INGOT, Items.IRON_INGOT);
                plushShapeless(ModBlocks.GUMI_PLUSH_V6, ModBlocks.GUMI_PLUSH, Items.IRON_INGOT, Items.IRON_INGOT, Items.REDSTONE);
            }

            //KAITO
            {
                shaped(RecipeCategory.DECORATIONS, ModBlocks.KAITO_PLUSH)
                    .pattern("121")
                    .pattern(" 3 ")
                    .define('1', Items.WOOL.pick(DyeColor.BLUE))
                    .define('2', Items.WOOL.pick(DyeColor.WHITE))
                    .define('3', Items.WOOL.pick(DyeColor.ORANGE))
                    .unlockedBy(getHasName(Items.WOOL.pick(DyeColor.BLUE)), has(Items.WOOL.pick(DyeColor.BLUE)))
                    .unlockedBy(getHasName(Items.WOOL.pick(DyeColor.WHITE)), has(Items.WOOL.pick(DyeColor.WHITE)))
                    .unlockedBy(getHasName(Items.WOOL.pick(DyeColor.ORANGE)), has(Items.WOOL.pick(DyeColor.ORANGE)))
                    .save(output);
                plushShapeless(ModBlocks.KAITO_PLUSH_V3, ModBlocks.KAITO_PLUSH, Items.IRON_INGOT);
                plushShapeless(ModBlocks.KAITO_PLUSH_V4, ModBlocks.KAITO_PLUSH, Items.IRON_INGOT, Items.IRON_INGOT);
            }

    }

        public void pickaxeRecipe(ItemLike result, ItemLike ingredient) {
            shaped(RecipeCategory.TOOLS, result)
                .pattern("121")
                .define('1', Items.DIAMOND)
                .define('2', ingredient)
                .unlockedBy(getHasName(ingredient), has(ingredient))
                .save(output);
        }

        public void plushShapeless(ItemLike result, ItemLike plush, ItemLike... ingredients) {
            //CREATE SHAPELESS RECIPE WITH A PLUSH
            ShapelessRecipeBuilder shapeless =
                shapeless(RecipeCategory.DECORATIONS, result)
                .requires(plush);

            //ADD EXTRA INGREDIENTS AS NEEDED
            for (ItemLike ingredient : ingredients) {
                shapeless.requires(ingredient);
            }

            //ADD RECIPE UNLOCK REQUIREMENT AND EXPORT RECIPE
            shapeless.unlockedBy(getHasName(plush), has(plush))
                .save(output);
        }

        public void simpleShapeless(ItemLike result, ItemLike... ingredients) {
            //CREATE SHAPELESS RECIPE
            ShapelessRecipeBuilder shapeless =
                shapeless(RecipeCategory.DECORATIONS, result);

            //ADD EXTRA INGREDIENTS AS NEEDED
            for (ItemLike ingredient : ingredients) {
                shapeless.requires(ingredient);
                shapeless.unlockedBy(getHasName(ingredient), has(ingredient));
            }

            //EXPORT RECIPE
            shapeless.save(output);
        }
    }
}
