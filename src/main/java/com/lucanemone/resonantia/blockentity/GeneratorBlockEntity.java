package com.lucanemone.resonantia.blockentity;

import com.lucanemone.resonantia.block.GeneratorBlock;
import net.minecraft.world.level.block.Block;
import com.lucanemone.resonantia.energy.ResonantiaEnergyStorage;
import com.lucanemone.resonantia.menu.GeneratorMenu;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

public class GeneratorBlockEntity extends BlockEntity implements MenuProvider {

    public static final int SLOT_FUEL = 0;
    public static final int SLOT_BATTERY = 1;

    private static final int CAPACITY = 10000;
    private static final int FE_PER_TICK = 40;
    private static final int BATTERY_TRANSFER_PER_TICK = 200;

    private final ResonantiaEnergyStorage energyStorage = new ResonantiaEnergyStorage(CAPACITY, 0, 400);

    private int burnTimeRemaining = 0;
    private int burnTimeTotal = 0;

    private final ItemStackHandler itemHandler = new ItemStackHandler(2) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            if (slot == SLOT_BATTERY) return stack.getCapability(Capabilities.EnergyStorage.ITEM) != null;
            return slot == SLOT_FUEL;
        }
    };

    public GeneratorBlockEntity(BlockPos pos, BlockState state) {
        super(ResonantiaBlockEntities.GENERATOR.get(), pos, state);
    }

    public ItemStackHandler getItemHandler() {
        return itemHandler;
    }

    public ResonantiaEnergyStorage getEnergyStorage() {
        return energyStorage;
    }

    public int getBurnTimeRemaining() { return burnTimeRemaining; }
    public int getBurnTimeTotal() { return burnTimeTotal; }
    public int getEnergy() { return energyStorage.getEnergyStored(); }
    public int getMaxEnergy() { return energyStorage.getMaxEnergyStored(); }

    public static void tick(Level level, BlockPos pos, BlockState state, GeneratorBlockEntity be) {
        if (level.isClientSide()) return;

        if (be.burnTimeRemaining <= 0) {
            ItemStack fuel = be.itemHandler.getStackInSlot(SLOT_FUEL);
            int burnDuration = fuel.getBurnTime(null);
            if (!fuel.isEmpty() && burnDuration > 0 && be.energyStorage.getEnergyStored() < be.energyStorage.getMaxEnergyStored()) {
                be.burnTimeRemaining = burnDuration;
                be.burnTimeTotal = burnDuration;
                fuel.shrink(1);
            }
        }

        if (be.burnTimeRemaining > 0) {
            be.burnTimeRemaining--;
            be.energyStorage.generateEnergyInternal(FE_PER_TICK);
        }

        boolean lit = be.burnTimeRemaining > 0;
        if (state.hasProperty(GeneratorBlock.LIT) && state.getValue(GeneratorBlock.LIT) != lit) {
            level.setBlock(pos, state.setValue(GeneratorBlock.LIT, lit), Block.UPDATE_CLIENTS);
        }

        ItemStack batteryStack = be.itemHandler.getStackInSlot(SLOT_BATTERY);
        if (!batteryStack.isEmpty()) {
            IEnergyStorage batteryEnergy = batteryStack.getCapability(Capabilities.EnergyStorage.ITEM);
            if (batteryEnergy != null && batteryEnergy.canReceive()) {
                int toSend = Math.min(BATTERY_TRANSFER_PER_TICK, be.energyStorage.getEnergyStored());
                int accepted = batteryEnergy.receiveEnergy(toSend, false);
                if (accepted > 0) {
                    be.energyStorage.consumeEnergyInternal(accepted);
                }
            }
        }

        be.setChanged();
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("inventory", itemHandler.serializeNBT(registries));
        tag.putInt("energy", energyStorage.getEnergyStored());
        tag.putInt("burnTimeRemaining", burnTimeRemaining);
        tag.putInt("burnTimeTotal", burnTimeTotal);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        itemHandler.deserializeNBT(registries, tag.getCompound("inventory"));
        energyStorage.setEnergy(tag.getInt("energy"));
        burnTimeRemaining = tag.getInt("burnTimeRemaining");
        burnTimeTotal = tag.getInt("burnTimeTotal");
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.resonantia.generator");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new GeneratorMenu(containerId, playerInventory, this);
    }
}