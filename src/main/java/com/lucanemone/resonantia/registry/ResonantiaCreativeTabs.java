package com.lucanemone.resonantia.registry;

import com.lucanemone.resonantia.Resonantia;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ResonantiaCreativeTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Resonantia.MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> RESONANTIA_TAB =
            CREATIVE_TABS.register(
                    "resonantia_tab",
                    () -> CreativeModeTab.builder()
                            .title(Component.translatable("itemGroup.resonantia"))
                            .icon(() -> new ItemStack(ResonantiaItems.RAW_CHRYSOLITHE.get()))
                            .displayItems((parameters, output) -> {
                                output.accept(ResonantiaItems.CHRYSOLITHE_ORE.get());
                                output.accept(ResonantiaItems.RAW_CHRYSOLITHE.get());
                                output.accept(ResonantiaItems.FRAGMENT_CHRYSOLITHE.get());
                                output.accept(ResonantiaItems.CHRYSOLITHE_BLOCK.get());
                                output.accept(ResonantiaItems.FERRITE_ORE.get());
                                output.accept(ResonantiaItems.RAW_FERRITE.get());
                                output.accept(ResonantiaItems.FRAGMENT_FERRITE.get());
                                output.accept(ResonantiaItems.FERRITE_BLOCK.get());
                                output.accept(ResonantiaItems.CONCASSEUR.get());
                                output.accept(ResonantiaItems.SYLVANITE_ORE.get());
                                output.accept(ResonantiaItems.RAW_SYLVANITE.get());
                                output.accept(ResonantiaItems.FRAGMENT_SYLVANITE.get());
                                output.accept(ResonantiaItems.SYLVANITE_BLOCK.get());
                                output.accept(ResonantiaItems.GENERATOR.get());
                                output.accept(ResonantiaItems.BATTERY.get());
                                output.accept(ResonantiaItems.CABLE.get());
                                output.accept(ResonantiaItems.FREQUENCY_KEY.get());
                                output.accept(ResonantiaItems.ASSEMBLER.get());
                                output.accept(ResonantiaItems.RESONANT_PICKAXE.get());
                                output.accept(ResonantiaItems.CINDRITE_ORE.get());
                                output.accept(ResonantiaItems.RAW_CINDRITE.get());
                                output.accept(ResonantiaItems.FRAGMENT_CINDRITE.get());
                                output.accept(ResonantiaItems.CINDRITE_BLOCK.get());
                                output.accept(ResonantiaItems.NEBULITE_ORE.get());
                                output.accept(ResonantiaItems.RAW_NEBULITE.get());
                                output.accept(ResonantiaItems.FRAGMENT_NEBULITE.get());
                                output.accept(ResonantiaItems.NEBULITE_BLOCK.get());
                                output.accept(ResonantiaItems.FAILLE_FRAME.get());
                            })
                            .build()
            );
}
