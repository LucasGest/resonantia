package com.lucanemone.resonantia.menu;

import com.lucanemone.resonantia.blockentity.GeneratorBlockEntity;
import com.lucanemone.resonantia.registry.ResonantiaMenus;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.SlotItemHandler;

public class GeneratorMenu extends AbstractContainerMenu {

    private final GeneratorBlockEntity blockEntity;
    private final ContainerData data;

    public GeneratorMenu(int containerId, Inventory playerInventory, GeneratorBlockEntity blockEntity) {
        super(ResonantiaMenus.GENERATOR_MENU.get(), containerId);
        this.blockEntity = blockEntity;

        addSlot(new SlotItemHandler(blockEntity.getItemHandler(), GeneratorBlockEntity.SLOT_FUEL, 56, 35));
        addSlot(new SlotItemHandler(blockEntity.getItemHandler(), GeneratorBlockEntity.SLOT_BATTERY, 116, 35));

        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> blockEntity.getBurnTimeRemaining();
                    case 1 -> blockEntity.getBurnTimeTotal();
                    case 2 -> blockEntity.getEnergy();
                    case 3 -> blockEntity.getMaxEnergy();
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {}

            @Override
            public int getCount() {
                return 4;
            }
        };
        addDataSlots(data);

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
    }

    public GeneratorMenu(int containerId, Inventory playerInventory, RegistryFriendlyByteBuf extraData) {
        this(containerId, playerInventory, getBlockEntity(playerInventory, extraData));
    }

    private static GeneratorBlockEntity getBlockEntity(Inventory playerInventory, RegistryFriendlyByteBuf extraData) {
        BlockPos pos = extraData.readBlockPos();
        if (playerInventory.player.level().getBlockEntity(pos) instanceof GeneratorBlockEntity be) {
            return be;
        }
        throw new IllegalStateException("Block entity introuvable à " + pos);
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; i++) {
            for (int l = 0; l < 9; l++) {
                addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; i++) {
            addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack result = ItemStack.EMPTY;
        Slot slot = slots.get(index);

        if (slot != null && slot.hasItem()) {
            ItemStack stackInSlot = slot.getItem();
            result = stackInSlot.copy();

            int machineSlots = 2;
            int invStart = machineSlots;
            int invEnd = invStart + 27;
            int hotbarEnd = invEnd + 9;

            if (index < machineSlots) {
                if (!moveItemStackTo(stackInSlot, invStart, hotbarEnd, true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                boolean isFuel = stackInSlot.getBurnTime(null) > 0;
                if (isFuel) {
                    if (!moveItemStackTo(stackInSlot, GeneratorBlockEntity.SLOT_FUEL, GeneratorBlockEntity.SLOT_FUEL + 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    boolean isBattery = stackInSlot.getCapability(net.neoforged.neoforge.capabilities.Capabilities.EnergyStorage.ITEM) != null;
                    if (isBattery) {
                        if (!moveItemStackTo(stackInSlot, GeneratorBlockEntity.SLOT_BATTERY, GeneratorBlockEntity.SLOT_BATTERY + 1, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else {
                        return ItemStack.EMPTY;
                    }
                }
            }

            if (stackInSlot.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return result;
    }

    @Override
    public boolean stillValid(Player player) {
        return blockEntity.getBlockPos().distSqr(player.blockPosition()) <= 64;
    }

    public int getBurnProgress() { return data.get(0); }
    public int getBurnTotal() { return data.get(1); }
    public int getEnergy() { return data.get(2); }
    public int getMaxEnergy() { return data.get(3); }
}