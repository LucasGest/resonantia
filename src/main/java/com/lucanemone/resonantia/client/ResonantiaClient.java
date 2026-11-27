package com.lucanemone.resonantia.client;

import com.lucanemone.resonantia.Resonantia;
import com.lucanemone.resonantia.client.screen.AssemblerScreen;
import com.lucanemone.resonantia.client.screen.ConcasseurScreen;
import com.lucanemone.resonantia.client.screen.GeneratorScreen;
import com.lucanemone.resonantia.registry.ResonantiaMenus;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@EventBusSubscriber(modid = Resonantia.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ResonantiaClient {

    @SubscribeEvent
    public static void onRegisterScreens(RegisterMenuScreensEvent event) {
        event.register(ResonantiaMenus.CONCASSEUR_MENU.get(), ConcasseurScreen::new);
        event.register(ResonantiaMenus.GENERATOR_MENU.get(), GeneratorScreen::new);
        event.register(ResonantiaMenus.ASSEMBLER_MENU.get(), AssemblerScreen::new);
    }
}