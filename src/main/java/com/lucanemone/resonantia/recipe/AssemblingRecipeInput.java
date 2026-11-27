package com.lucanemone.resonantia.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

public record AssemblingRecipeInput(ItemStack slot1, ItemStack slot2, ItemStack slot3) implements RecipeInput {

    @Override
    public ItemStack getItem(int index) {
        return switch (index) {
            case 0 -> slot1;
            case 1 -> slot2;
            case 2 -> slot3;
            default -> throw new IllegalArgumentException("Index invalide : " + index);
        };
    }

    @Override
    public int size() {
        return 3;
    }
}