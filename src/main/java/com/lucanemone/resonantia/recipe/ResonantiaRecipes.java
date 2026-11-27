package com.lucanemone.resonantia.recipe;

import com.lucanemone.resonantia.Resonantia;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ResonantiaRecipes {

    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
            DeferredRegister.create(Registries.RECIPE_TYPE, Resonantia.MOD_ID);

    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, Resonantia.MOD_ID);

    public static final DeferredHolder<RecipeType<?>, RecipeType<CrushingRecipe>> CRUSHING_TYPE =
            RECIPE_TYPES.register("crushing", () -> new RecipeType<CrushingRecipe>() {
                @Override
                public String toString() { return "resonantia:crushing"; }
            });

    public static final DeferredHolder<RecipeSerializer<?>, CrushingRecipe.Serializer> CRUSHING_SERIALIZER =
            RECIPE_SERIALIZERS.register("crushing", CrushingRecipe.Serializer::new);

    public static final DeferredHolder<RecipeType<?>, RecipeType<AssemblingRecipe>> ASSEMBLING_TYPE =
            RECIPE_TYPES.register("assembling", () -> new RecipeType<AssemblingRecipe>() {
                @Override
                public String toString() { return "resonantia:assembling"; }
            });

    public static final DeferredHolder<RecipeSerializer<?>, AssemblingRecipe.Serializer> ASSEMBLING_SERIALIZER =
            RECIPE_SERIALIZERS.register("assembling", AssemblingRecipe.Serializer::new);
}