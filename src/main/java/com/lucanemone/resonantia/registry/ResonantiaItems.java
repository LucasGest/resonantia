package com.lucanemone.resonantia.registry;

import com.lucanemone.resonantia.Resonantia;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import com.lucanemone.resonantia.item.BatteryItem;
import com.lucanemone.resonantia.item.FrequencyKeyItem;
import com.lucanemone.resonantia.item.ResonantiaToolTiers;
import net.minecraft.world.item.PickaxeItem;

public class ResonantiaItems {

    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(Resonantia.MOD_ID);

    public static final DeferredItem<Item> RAW_CHRYSOLITHE = ITEMS.registerSimpleItem(
            "raw_chrysolithe", new Item.Properties()
    );

    public static final DeferredItem<Item> FRAGMENT_CHRYSOLITHE = ITEMS.registerSimpleItem(
            "fragment_chrysolithe", new Item.Properties()
    );

    public static final DeferredItem<BlockItem> CHRYSOLITHE_ORE = ITEMS.registerSimpleBlockItem(
            "chrysolithe_ore", ResonantiaBlocks.CHRYSOLITHE_ORE
    );

    public static final DeferredItem<BlockItem> CHRYSOLITHE_BLOCK = ITEMS.registerSimpleBlockItem(
            "chrysolithe_block", ResonantiaBlocks.CHRYSOLITHE_BLOCK
    );

    // Ferrite Noire
    public static final DeferredItem<Item> RAW_FERRITE = ITEMS.registerSimpleItem(
            "raw_ferrite", new Item.Properties()
    );

    public static final DeferredItem<Item> FRAGMENT_FERRITE = ITEMS.registerSimpleItem(
            "fragment_ferrite", new Item.Properties()
    );

    public static final DeferredItem<BlockItem> FERRITE_ORE = ITEMS.registerSimpleBlockItem(
            "ferrite_ore", ResonantiaBlocks.FERRITE_ORE
    );

    public static final DeferredItem<BlockItem> FERRITE_BLOCK = ITEMS.registerSimpleBlockItem(
            "ferrite_block", ResonantiaBlocks.FERRITE_BLOCK
    );
    public static final DeferredItem<BlockItem> CONCASSEUR = ITEMS.registerSimpleBlockItem(
            "concasseur", ResonantiaBlocks.CONCASSEUR
    );

    public static final DeferredItem<Item> RAW_SYLVANITE = ITEMS.registerSimpleItem(
            "raw_sylvanite", new Item.Properties()
    );

    public static final DeferredItem<Item> FRAGMENT_SYLVANITE = ITEMS.registerSimpleItem(
            "fragment_sylvanite", new Item.Properties()
    );

    public static final DeferredItem<BlockItem> SYLVANITE_ORE = ITEMS.registerSimpleBlockItem(
            "sylvanite_ore", ResonantiaBlocks.SYLVANITE_ORE
    );

    public static final DeferredItem<BlockItem> SYLVANITE_BLOCK = ITEMS.registerSimpleBlockItem(
            "sylvanite_block", ResonantiaBlocks.SYLVANITE_BLOCK
    );

    public static final DeferredItem<BatteryItem> BATTERY = ITEMS.register(
            "battery", () -> new BatteryItem(new Item.Properties().stacksTo(1))
    );

    public static final DeferredItem<BlockItem> GENERATOR = ITEMS.registerSimpleBlockItem(
            "generator", ResonantiaBlocks.GENERATOR
    );

    public static final DeferredItem<BlockItem> CABLE = ITEMS.registerSimpleBlockItem(
            "cable", ResonantiaBlocks.CABLE
    );

    public static final DeferredItem<FrequencyKeyItem> FREQUENCY_KEY = ITEMS.register(
            "frequency_key", () -> new FrequencyKeyItem(new Item.Properties().stacksTo(1))
    );

    public static final DeferredItem<BlockItem> ASSEMBLER = ITEMS.registerSimpleBlockItem(
            "assembler", ResonantiaBlocks.ASSEMBLER
    );

    public static final DeferredItem<PickaxeItem> RESONANT_PICKAXE = ITEMS.register(
            "resonant_pickaxe",
            () -> new PickaxeItem(ResonantiaToolTiers.RESONANT, new Item.Properties()
                    .attributes(PickaxeItem.createAttributes(ResonantiaToolTiers.RESONANT, 1.0F, -2.8F)))
    );

    public static final DeferredItem<Item> RAW_CINDRITE = ITEMS.registerSimpleItem(
            "raw_cindrite", new Item.Properties()
    );

    public static final DeferredItem<Item> FRAGMENT_CINDRITE = ITEMS.registerSimpleItem(
            "fragment_cindrite", new Item.Properties()
    );

    public static final DeferredItem<BlockItem> CINDRITE_ORE = ITEMS.registerSimpleBlockItem(
            "cindrite_ore", ResonantiaBlocks.CINDRITE_ORE
    );

    public static final DeferredItem<BlockItem> CINDRITE_BLOCK = ITEMS.registerSimpleBlockItem(
            "cindrite_block", ResonantiaBlocks.CINDRITE_BLOCK
    );

    public static final DeferredItem<Item> RAW_NEBULITE = ITEMS.registerSimpleItem(
            "raw_nebulite", new Item.Properties()
    );

    public static final DeferredItem<Item> FRAGMENT_NEBULITE = ITEMS.registerSimpleItem(
            "fragment_nebulite", new Item.Properties()
    );

    public static final DeferredItem<BlockItem> NEBULITE_ORE = ITEMS.registerSimpleBlockItem(
            "nebulite_ore", ResonantiaBlocks.NEBULITE_ORE
    );

    public static final DeferredItem<BlockItem> NEBULITE_BLOCK = ITEMS.registerSimpleBlockItem(
            "nebulite_block", ResonantiaBlocks.NEBULITE_BLOCK
    );

    public static final DeferredItem<BlockItem> FAILLE_FRAME = ITEMS.registerSimpleBlockItem(
            "faille_frame", ResonantiaBlocks.FAILLE_FRAME
    );
}
