package com.lucanemone.resonantia.registry;

import com.lucanemone.resonantia.Resonantia;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.ComponentEnergyStorage;

@EventBusSubscriber(modid = Resonantia.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ResonantiaCapabilities {

    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.EnergyStorage.BLOCK,
                ResonantiaBlockEntities.CONCASSEUR.get(),
                (be, side) -> be.getEnergyStorage()
        );

        event.registerBlockEntity(
                Capabilities.EnergyStorage.BLOCK,
                ResonantiaBlockEntities.GENERATOR.get(),
                (be, side) -> be.getEnergyStorage()
        );

        event.registerItem(
                Capabilities.EnergyStorage.ITEM,
                (stack, ctx) -> new ComponentEnergyStorage(
                        stack, ResonantiaDataComponents.ENERGY.get(),
                        com.lucanemone.resonantia.item.BatteryItem.CAPACITY,
                        com.lucanemone.resonantia.item.BatteryItem.MAX_TRANSFER,
                        com.lucanemone.resonantia.item.BatteryItem.MAX_TRANSFER
                ),
                ResonantiaItems.BATTERY.get()
        );

        event.registerBlockEntity(
                Capabilities.EnergyStorage.BLOCK,
                ResonantiaBlockEntities.CABLE.get(),
                (be, side) -> be.getEnergyStorage()
        );

        event.registerBlockEntity(
                Capabilities.EnergyStorage.BLOCK,
                ResonantiaBlockEntities.ASSEMBLER.get(),
                (be, side) -> be.getEnergyStorage()
        );
    }
}