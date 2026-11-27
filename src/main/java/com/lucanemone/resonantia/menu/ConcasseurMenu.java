package com.lucanemone.resonantia.menu;

import com.lucanemone.resonantia.blockentity.ConcasseurBlockEntity;
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

public class ConcasseurMenu extends AbstractContainerMenu {

    private final ConcasseurBlockEntity blockEntity;
    private final ContainerData data;

    public ConcasseurMenu(int containerId, Inventory playerInventory, ConcasseurBlockEntity blockEntity) {
        super(ResonantiaMenus.CONCASSEUR_MENU.get(), containerId);
        this.blockEntity = blockEntity;

        addSlot(new SlotItemHandler(blockEntity.getItemHandler(), ConcasseurBlockEntity.SLOT_INPUT, 56, 35));
        addSlot(new SlotItemHandler(blockEntity.getItemHandler(), ConcasseurBlockEntity.SLOT_OUTPUT, 116, 35) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }
        });
        addSlot(new SlotItemHandler(blockEntity.getItemHandler(), ConcasseurBlockEntity.SLOT_BATTERY, 8, 17));

        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> blockEntity.getProgress();
                    case 1 -> blockEntity.getMaxProgress();
                    case 2 -> blockEntity.getEnergy();
                    case 3 -> blockEntity.getMaxEnergy();
                    case 4 -> blockEntity.getFrequency();
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {}

            @Override
            public int getCount() {
                return 5;
            }
        };

        addDataSlots(data);

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
    }

    public ConcasseurMenu(int containerId, Inventory playerInventory, RegistryFriendlyByteBuf extraData) {
        this(containerId, playerInventory, getBlockEntity(playerInventory, extraData));
    }

    private static ConcasseurBlockEntity getBlockEntity(Inventory playerInventory, RegistryFriendlyByteBuf extraData) {
        BlockPos pos = extraData.readBlockPos();
        if (playerInventory.player.level().getBlockEntity(pos) instanceof ConcasseurBlockEntity be) {
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

            int machineSlots = 3;
            int invStart = machineSlots;
            int invEnd = invStart + 27;
            int hotbarEnd = invEnd + 9;

            if (index < machineSlots) {
                // De la machine vers l'inventaire joueur
                if (!moveItemStackTo(stackInSlot, invStart, hotbarEnd, true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                // De l'inventaire joueur vers la machine
                boolean isBattery = stackInSlot.getCapability(net.neoforged.neoforge.capabilities.Capabilities.EnergyStorage.ITEM) != null;
                if (isBattery) {
                    if (!moveItemStackTo(stackInSlot, ConcasseurBlockEntity.SLOT_BATTERY, ConcasseurBlockEntity.SLOT_BATTERY + 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    if (!moveItemStackTo(stackInSlot, ConcasseurBlockEntity.SLOT_INPUT, ConcasseurBlockEntity.SLOT_INPUT + 1, false)) {
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

    public int getProgress() { return data.get(0); }
    public int getMaxProgress() { return data.get(1); }
    public int getEnergy() { return data.get(2); }
    public int getMaxEnergy() { return data.get(3); }
    public int getFrequency() { return data.get(4); }   // <-- la ligne à ajouter
}