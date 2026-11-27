package com.lucanemone.resonantia.registry;

import com.lucanemone.resonantia.Resonantia;
import com.lucanemone.resonantia.blockentity.ConcasseurBlockEntity;
import com.lucanemone.resonantia.blockentity.GeneratorBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import com.lucanemone.resonantia.blockentity.CableBlockEntity;
import com.lucanemone.resonantia.blockentity.AssemblerBlockEntity;

public class ResonantiaBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Resonantia.MOD_ID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ConcasseurBlockEntity>> CONCASSEUR =
            BLOCK_ENTITIES.register("concasseur",
                    () -> BlockEntityType.Builder.of(ConcasseurBlockEntity::new, ResonantiaBlocks.CONCASSEUR.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<GeneratorBlockEntity>> GENERATOR =
            BLOCK_ENTITIES.register("generator",
                    () -> BlockEntityType.Builder.of(GeneratorBlockEntity::new, ResonantiaBlocks.GENERATOR.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CableBlockEntity>> CABLE =
            BLOCK_ENTITIES.register("cable",
                    () -> BlockEntityType.Builder.of(CableBlockEntity::new, ResonantiaBlocks.CABLE.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<AssemblerBlockEntity>> ASSEMBLER =
            BLOCK_ENTITIES.register("assembler",
                    () -> BlockEntityType.Builder.of(AssemblerBlockEntity::new, ResonantiaBlocks.ASSEMBLER.get()).build(null));
}