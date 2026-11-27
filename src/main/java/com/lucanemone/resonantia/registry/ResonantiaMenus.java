package com.lucanemone.resonantia.registry;

import com.lucanemone.resonantia.Resonantia;
import com.lucanemone.resonantia.menu.ConcasseurMenu;
import com.lucanemone.resonantia.menu.GeneratorMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import com.lucanemone.resonantia.menu.AssemblerMenu;

public class ResonantiaMenus {

    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, Resonantia.MOD_ID);

    public static final DeferredHolder<MenuType<?>, MenuType<ConcasseurMenu>> CONCASSEUR_MENU =
            MENUS.register("concasseur", () -> IMenuTypeExtension.create(ConcasseurMenu::new));

    public static final DeferredHolder<MenuType<?>, MenuType<GeneratorMenu>> GENERATOR_MENU =
            MENUS.register("generator", () -> IMenuTypeExtension.create(GeneratorMenu::new));

    public static final DeferredHolder<MenuType<?>, MenuType<AssemblerMenu>> ASSEMBLER_MENU =
            MENUS.register("assembler", () -> IMenuTypeExtension.create(AssemblerMenu::new));

}