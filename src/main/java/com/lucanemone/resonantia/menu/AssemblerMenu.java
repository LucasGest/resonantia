package com.lucanemone.resonantia.menu;

import com.lucanemone.resonantia.blockentity.AssemblerBlockEntity;
import com.lucanemone.resonantia.registry.ResonantiaMenus;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.SlotItemHandler;

public class AssemblerMenu extends AbstractContainerMenu {

    private final AssemblerBlockEntity blockEntity;
    private final ContainerData data;

    public AssemblerMenu(int containerId, Inventory playerInventory, AssemblerBlockEntity blockEntity) {
        super(ResonantiaMenus.ASSEMBLER_MENU.get(), containerId);
        this.blockEntity = blockEntity;

        addSlot(new SlotItemHandler(blockEntity.getItemHandler(), AssemblerBlockEntity.SLOT_BATTERY, 8, 17));
        addSlot(new SlotItemHandler(blockEntity.getItemHandler(), AssemblerBlockEntity.SLOT_INPUT_1, 44, 17));
        addSlot(new SlotItemHandler(blockEntity.getItemHandler(), AssemblerBlockEntity.SLOT_INPUT_2, 44, 35));
        addSlot(new SlotItemHandler(blockEntity.getItemHandler(), AssemblerBlockEntity.SLOT_INPUT_3, 44, 53));
        addSlot(new SlotItemHandler(blockEntity.getItemHandler(), AssemblerBlockEntity.SLOT_OUTPUT, 116, 35) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }
        });

        this.data = new ContainerData() {
            // Côté client : valeurs reçues du serveur (la machine locale n'est pas à jour)
            private final int[] synced = new int[8];

            @Override
            public int get(int index) {
                if (blockEntity.getLevel() != null && blockEntity.getLevel().isClientSide()) return synced[index];
                return switch (index) {
                    case 0 -> blockEntity.getProgress();
                    case 1 -> blockEntity.getMaxProgress();
                    case 2 -> blockEntity.getEnergy();
                    case 3 -> blockEntity.getMaxEnergy();
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                synced[index] = value;
            }

            @Override
            public int getCount() {
                return 4;
            }
        };
        addDataSlots(data);

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
    }

    public AssemblerMenu(int containerId, Inventory playerInventory, RegistryFriendlyByteBuf extraData) {
        this(containerId, playerInventory, getBlockEntity(playerInventory, extraData));
    }

    private static AssemblerBlockEntity getBlockEntity(Inventory playerInventory, RegistryFriendlyByteBuf extraData) {
        BlockPos pos = extraData.readBlockPos();
        if (playerInventory.player.level().getBlockEntity(pos) instanceof AssemblerBlockEntity be) {
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

            int machineSlots = 5;
            int invStart = machineSlots;
            int invEnd = invStart + 27;
            int hotbarEnd = invEnd + 9;

            if (index < machineSlots) {
                if (!moveItemStackTo(stackInSlot, invStart, hotbarEnd, true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                boolean isBattery = stackInSlot.getCapability(Capabilities.EnergyStorage.ITEM) != null;
                if (isBattery) {
                    if (!moveItemStackTo(stackInSlot, AssemblerBlockEntity.SLOT_BATTERY, AssemblerBlockEntity.SLOT_BATTERY + 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    if (!moveItemStackTo(stackInSlot, AssemblerBlockEntity.SLOT_INPUT_1, AssemblerBlockEntity.SLOT_INPUT_3 + 1, false)) {
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
}