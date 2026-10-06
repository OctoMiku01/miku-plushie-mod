package com.any.mikuplushie.datagen;

import com.any.mikuplushie.MikuPlushie;
import com.any.mikuplushie.registry.ModItems;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Villager trades are data driven since 26.1 (they used to be registered with Fabric's TradeOfferHelper).
 * <p>
 * Every trade is a {@code villager_trade} file, the trades are added to the vanilla trade sets through
 * the (non replacing) {@code villager_trade} tags of the minecraft namespace.
 */
public class ModDataJsonProvider implements DataProvider {
    private final PackOutput.PathProvider trades;
    private final PackOutput.PathProvider tradeTags;

    public ModDataJsonProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        this.trades = output.createPathProvider(PackOutput.Target.DATA_PACK, "villager_trade");
        this.tradeTags = output.createPathProvider(PackOutput.Target.DATA_PACK, "tags/villager_trade");
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cachedOutput) {
        List<CompletableFuture<?>> futures = new ArrayList<>();

        //FARMER LEVEL 1: BUY LEEK SEEDS
        Identifier buyLeekSeeds = MikuPlushie.id("farmer/1/emerald_leek_seeds");
        futures.add(save(cachedOutput, this.trades.json(buyLeekSeeds),
            trade(stack(Items.EMERALD, 1), stack(ModItems.LEEK_SEEDS, 3))));

        //FARMER LEVEL 1: SELL LEEK
        Identifier sellLeek = MikuPlushie.id("farmer/1/leek_emerald");
        futures.add(save(cachedOutput, this.trades.json(sellLeek),
            trade(stack(ModItems.LEEK, 16), stack(Items.EMERALD, 1))));

        futures.add(save(cachedOutput, this.tradeTags.json(Identifier.withDefaultNamespace("farmer/level_1")),
            tag(buyLeekSeeds, sellLeek)));

        //WANDERING TRADER: ONE RANDOM PLUSH
        //EVERY PLUSH REPLACES THE PREVIOUS ONE WITH A CHANCE OF 1/N, SO EVERY PLUSH IS EQUALLY LIKELY
        Identifier randomPlush = MikuPlushie.id("wandering_trader/emerald_random_plush");
        JsonObject plushTrade = trade(stack(Items.EMERALD, 1), stack(ModItems.PLUSH_ITEMS.getFirst(), 1));
        JsonArray modifiers = new JsonArray();
        for (int plush = 1; plush < ModItems.PLUSH_ITEMS.size(); plush++) {
            JsonObject chance = new JsonObject();
            chance.addProperty("condition", "minecraft:random_chance");
            chance.addProperty("chance", 1.0F / (plush + 1));
            JsonArray conditions = new JsonArray();
            conditions.add(chance);

            JsonObject setItem = new JsonObject();
            setItem.addProperty("function", "minecraft:set_item");
            setItem.addProperty("item", id(ModItems.PLUSH_ITEMS.get(plush)));
            setItem.add("conditions", conditions);
            modifiers.add(setItem);
        }
        plushTrade.add("given_item_modifiers", modifiers);
        futures.add(save(cachedOutput, this.trades.json(randomPlush), plushTrade));

        futures.add(save(cachedOutput, this.tradeTags.json(Identifier.withDefaultNamespace("wandering_trader/common")),
            tag(randomPlush)));

        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
    }

    private static CompletableFuture<?> save(CachedOutput cachedOutput, Path path, JsonObject json) {
        return DataProvider.saveStable(cachedOutput, json, path);
    }

    private static String id(Item item) {
        return BuiltInRegistries.ITEM.getKey(item).toString();
    }

    private static JsonObject stack(Item item, int count) {
        JsonObject stack = new JsonObject();
        stack.addProperty("id", id(item));
        if (count != 1) {
            stack.addProperty("count", count);
        }
        return stack;
    }

    //SAME VALUES AS THE OLD TRADE OFFERS: 6 MAX USES, 2 XP AND A 0.02 PRICE MULTIPLIER
    private static JsonObject trade(JsonObject wants, JsonObject gives) {
        JsonObject trade = new JsonObject();
        trade.add("wants", wants);
        trade.add("gives", gives);
        trade.addProperty("max_uses", 6);
        trade.addProperty("xp", 2);
        trade.addProperty("reputation_discount", 0.02F);
        return trade;
    }

    private static JsonObject tag(Identifier... values) {
        JsonArray entries = new JsonArray();
        for (Identifier value : values) {
            entries.add(value.toString());
        }
        JsonObject tag = new JsonObject();
        tag.addProperty("replace", false);
        tag.add("values", entries);
        return tag;
    }

    @Override
    public String getName() {
        return MikuPlushie.MOD_ID + " Villager Trades";
    }
}
