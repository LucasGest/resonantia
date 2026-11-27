package com.lucanemone.resonantia.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.List;

public class AssemblingRecipe implements Recipe<AssemblingRecipeInput> {

    private final List<Ingredient> ingredients;
    private final ItemStack result;

    public AssemblingRecipe(List<Ingredient> ingredients, ItemStack result) {
        this.ingredients = ingredients;
        this.result = result;
    }

    public Ingredient getIngredient(int index) {
        return ingredients.get(index);
    }

    @Override
    public boolean matches(AssemblingRecipeInput input, Level level) {
        for (int i = 0; i < 3; i++) {
            if (!ingredients.get(i).test(input.getItem(i))) return false;
        }
        return true;
    }

    @Override
    public ItemStack assemble(AssemblingRecipeInput input, HolderLookup.Provider registries) {
        return result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return result;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ResonantiaRecipes.ASSEMBLING_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ResonantiaRecipes.ASSEMBLING_TYPE.get();
    }

    public static class Serializer implements RecipeSerializer<AssemblingRecipe> {

        public static final MapCodec<AssemblingRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                Ingredient.CODEC.optionalFieldOf("ingredient1", Ingredient.of()).forGetter(r -> r.ingredients.get(0)),
                Ingredient.CODEC.optionalFieldOf("ingredient2", Ingredient.of()).forGetter(r -> r.ingredients.get(1)),
                Ingredient.CODEC.optionalFieldOf("ingredient3", Ingredient.of()).forGetter(r -> r.ingredients.get(2)),
                ItemStack.CODEC.fieldOf("result").forGetter(r -> r.result)
        ).apply(inst, (i1, i2, i3, result) -> new AssemblingRecipe(List.of(i1, i2, i3), result)));

        public static final StreamCodec<RegistryFriendlyByteBuf, AssemblingRecipe> STREAM_CODEC = StreamCodec.composite(
                Ingredient.CONTENTS_STREAM_CODEC, r -> r.ingredients.get(0),
                Ingredient.CONTENTS_STREAM_CODEC, r -> r.ingredients.get(1),
                Ingredient.CONTENTS_STREAM_CODEC, r -> r.ingredients.get(2),
                ItemStack.STREAM_CODEC, r -> r.result,
                (i1, i2, i3, result) -> new AssemblingRecipe(List.of(i1, i2, i3), result)
        );

        @Override
        public MapCodec<AssemblingRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, AssemblingRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}