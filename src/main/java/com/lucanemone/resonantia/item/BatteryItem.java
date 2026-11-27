package com.lucanemone.resonantia.item;

import com.lucanemone.resonantia.registry.ResonantiaDataComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class BatteryItem extends Item {

    public static final int CAPACITY = 50000;
    public static final int MAX_TRANSFER = 500;
    private static final int BAR_LENGTH = 20;

    public BatteryItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        int energy = stack.getOrDefault(ResonantiaDataComponents.ENERGY.get(), 0);
        return Math.round(13.0F * energy / CAPACITY);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return 0x4FA6E8;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag flag) {
        int energy = stack.getOrDefault(ResonantiaDataComponents.ENERGY.get(), 0);
        float ratio = (float) energy / CAPACITY;

        tooltipComponents.add(Component.literal(String.format("%,d / %,d FE", energy, CAPACITY))
                .withStyle(ChatFormatting.AQUA));

        int filled = Math.round(ratio * BAR_LENGTH);
        StringBuilder bar = new StringBuilder();
        for (int i = 0; i < BAR_LENGTH; i++) {
            bar.append(i < filled ? "█" : "░");
        }

        tooltipComponents.add(Component.literal(bar.toString())
                .withStyle(filled > BAR_LENGTH / 2 ? ChatFormatting.GREEN : (filled > 0 ? ChatFormatting.YELLOW : ChatFormatting.DARK_GRAY)));
    }
}