package com.lucanemone.resonantia.blockentity;

import com.lucanemone.resonantia.energy.ResonantiaEnergyStorage;
import com.lucanemone.resonantia.registry.ResonantiaBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import com.lucanemone.resonantia.block.CableBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;

public class CableBlockEntity extends BlockEntity {

    private static final int CAPACITY = 4000;
    private static final int TRANSFER_RATE = 400;

    // Nombre de ticks sans énergie : le flux disparaît après un court délai, sans clignoter
    private static final int EMPTY_DELAY = 10;
    private int emptyTicks = 0;

    private final ResonantiaEnergyStorage energyStorage = new ResonantiaEnergyStorage(CAPACITY, TRANSFER_RATE, TRANSFER_RATE);

    public CableBlockEntity(BlockPos pos, BlockState state) {
        super(ResonantiaBlockEntities.CABLE.get(), pos, state);
    }

    public ResonantiaEnergyStorage getEnergyStorage() {
        return energyStorage;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, CableBlockEntity be) {
        if (level.isClientSide()) return;

        for (Direction direction : Direction.values()) {
            BlockPos neighborPos = pos.relative(direction);
            IEnergyStorage neighborEnergy = level.getCapability(Capabilities.EnergyStorage.BLOCK, neighborPos, direction.getOpposite());
            if (neighborEnergy == null) continue;

            if (neighborEnergy.canExtract()) {
                int space = be.energyStorage.getMaxEnergyStored() - be.energyStorage.getEnergyStored();
                if (space > 0) {
                    int extracted = neighborEnergy.extractEnergy(Math.min(TRANSFER_RATE, space), false);
                    if (extracted > 0) {
                        be.energyStorage.generateEnergyInternal(extracted);
                    }
                }
            }

            if (neighborEnergy.canReceive()) {
                int available = be.energyStorage.getEnergyStored();
                if (available > 0) {
                    int accepted = neighborEnergy.receiveEnergy(Math.min(TRANSFER_RATE, available), false);
                    if (accepted > 0) {
                        be.energyStorage.consumeEnergyInternal(accepted);
                    }
                }
            }
        }

        be.emptyTicks = be.energyStorage.getEnergyStored() > 0 ? 0 : be.emptyTicks + 1;
        boolean powered = be.emptyTicks < EMPTY_DELAY;
        if (state.getValue(CableBlock.POWERED) != powered) {
            level.setBlock(pos, state.setValue(CableBlock.POWERED, powered), Block.UPDATE_CLIENTS);
        }

        be.setChanged();
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("energy", energyStorage.getEnergyStored());
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        energyStorage.setEnergy(tag.getInt("energy"));
    }
}