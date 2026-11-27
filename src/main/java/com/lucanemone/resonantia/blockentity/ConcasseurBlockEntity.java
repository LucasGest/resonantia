package com.lucanemone.resonantia.blockentity;

import com.lucanemone.resonantia.energy.ResonantiaEnergyStorage;
import com.lucanemone.resonantia.menu.ConcasseurMenu;
import com.lucanemone.resonantia.recipe.CrushingRecipe;
import com.lucanemone.resonantia.recipe.ResonantiaRecipes;
import com.lucanemone.resonantia.registry.ResonantiaBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class ConcasseurBlockEntity extends BlockEntity implements MenuProvider {

    public static final int SLOT_INPUT = 0;
    public static final int SLOT_OUTPUT = 1;
    public static final int SLOT_BATTERY = 2;

    private static final int PROCESS_TIME = 100;
    private static final int ENERGY_PER_TICK = 20;
    private static final int BATTERY_DRAIN_PER_TICK = 200;
    private static final int TUNING_TOLERANCE = 10;

    private int progress = 0;
    private int frequency = 50;

    private final ResonantiaEnergyStorage energyStorage = new ResonantiaEnergyStorage(10000, 200, 0);

    private final ItemStackHandler itemHandler = new ItemStackHandler(3) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            if (slot == SLOT_OUTPUT) return false;
            if (slot == SLOT_BATTERY) return stack.getCapability(Capabilities.EnergyStorage.ITEM) != null;
            return true;
        }
    };

    public ConcasseurBlockEntity(BlockPos pos, BlockState state) {
        super(ResonantiaBlockEntities.CONCASSEUR.get(), pos, state);
    }

    public ItemStackHandler getItemHandler() {
        return itemHandler;
    }

    public ResonantiaEnergyStorage getEnergyStorage() {
        return energyStorage;
    }

    public int getProgress() { return progress; }
    public int getMaxProgress() { return PROCESS_TIME; }
    public int getEnergy() { return energyStorage.getEnergyStored(); }
    public int getMaxEnergy() { return energyStorage.getMaxEnergyStored(); }
    public int getFrequency() { return frequency; }

    public int adjustFrequency(int delta) {
        frequency = Math.max(0, Math.min(100, frequency + delta));
        setChanged();
        return frequency;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, ConcasseurBlockEntity be) {
        if (level.isClientSide()) return;

        ItemStack batteryStack = be.itemHandler.getStackInSlot(SLOT_BATTERY);
        if (!batteryStack.isEmpty()) {
            IEnergyStorage batteryEnergy = batteryStack.getCapability(Capabilities.EnergyStorage.ITEM);
            if (batteryEnergy != null && batteryEnergy.canExtract()) {
                int space = be.energyStorage.getMaxEnergyStored() - be.energyStorage.getEnergyStored();
                int toDraw = Math.min(BATTERY_DRAIN_PER_TICK, space);
                int extracted = batteryEnergy.extractEnergy(toDraw, false);
                if (extracted > 0) {
                    be.energyStorage.generateEnergyInternal(extracted);
                }
            }
        }

        ItemStack input = be.itemHandler.getStackInSlot(SLOT_INPUT);
        if (input.isEmpty()) {
            be.progress = 0;
            be.setChanged();
            return;
        }

        SingleRecipeInput recipeInput = new SingleRecipeInput(input);
        Optional<RecipeHolder<CrushingRecipe>> recipeHolder = level.getRecipeManager()
                .getRecipeFor(ResonantiaRecipes.CRUSHING_TYPE.get(), recipeInput, level);

        if (recipeHolder.isEmpty()) {
            be.progress = 0;
            be.setChanged();
            return;
        }

        CrushingRecipe recipe = recipeHolder.get().value();
        ItemStack result = recipe.getResultItem(level.registryAccess());
        ItemStack output = be.itemHandler.getStackInSlot(SLOT_OUTPUT);

        boolean canInsert = output.isEmpty()
                || (ItemStack.isSameItemSameComponents(output, result)
                && output.getCount() + result.getCount() <= output.getMaxStackSize());

        if (!canInsert) {
            be.progress = 0;
            be.setChanged();
            return;
        }

        if (!be.energyStorage.consumeEnergyInternal(ENERGY_PER_TICK)) {
            be.setChanged();
            return;
        }

        boolean tuned = Math.abs(be.frequency - recipe.getFrequency()) <= TUNING_TOLERANCE;
        be.progress += tuned ? 2 : 1;

        if (be.progress >= PROCESS_TIME) {
            be.progress = 0;
            be.itemHandler.extractItem(SLOT_INPUT, 1, false);
            if (output.isEmpty()) {
                be.itemHandler.setStackInSlot(SLOT_OUTPUT, result.copy());
            } else {
                output.grow(result.getCount());
            }
        }
        be.setChanged();
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("inventory", itemHandler.serializeNBT(registries));
        tag.putInt("progress", progress);
        tag.putInt("energy", energyStorage.getEnergyStored());
        tag.putInt("frequency", frequency);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        itemHandler.deserializeNBT(registries, tag.getCompound("inventory"));
        progress = tag.getInt("progress");
        energyStorage.setEnergy(tag.getInt("energy"));
        frequency = tag.contains("frequency") ? tag.getInt("frequency") : 50;
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.resonantia.concasseur");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new ConcasseurMenu(containerId, playerInventory, this);
    }
}