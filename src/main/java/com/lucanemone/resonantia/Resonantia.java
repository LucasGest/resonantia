package com.lucanemone.resonantia;

import com.lucanemone.resonantia.recipe.ResonantiaRecipes;
import com.lucanemone.resonantia.registry.ResonantiaBlockEntities;
import com.lucanemone.resonantia.registry.ResonantiaBlocks;
import com.lucanemone.resonantia.registry.ResonantiaCreativeTabs;
import com.lucanemone.resonantia.registry.ResonantiaDataComponents;
import com.lucanemone.resonantia.registry.ResonantiaItems;
import com.lucanemone.resonantia.registry.ResonantiaMenus;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(Resonantia.MOD_ID)
public class Resonantia {

    public static final String MOD_ID = "resonantia";
    private static final Logger LOGGER = LoggerFactory.getLogger(Resonantia.class);

    public Resonantia(IEventBus modEventBus, ModContainer modContainer) {
        LOGGER.info("Resonantia se charge — initialisation du mod.");

        ResonantiaBlocks.BLOCKS.register(modEventBus);
        ResonantiaItems.ITEMS.register(modEventBus);
        ResonantiaCreativeTabs.CREATIVE_TABS.register(modEventBus);
        ResonantiaBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        ResonantiaMenus.MENUS.register(modEventBus);
        ResonantiaRecipes.RECIPE_TYPES.register(modEventBus);
        ResonantiaRecipes.RECIPE_SERIALIZERS.register(modEventBus);
        ResonantiaDataComponents.DATA_COMPONENTS.register(modEventBus);
    }
}