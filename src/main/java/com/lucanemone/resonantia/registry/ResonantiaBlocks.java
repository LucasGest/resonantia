package com.lucanemone.resonantia.registry;

import com.lucanemone.resonantia.Resonantia;
import com.lucanemone.resonantia.block.ConcasseurBlock;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import com.lucanemone.resonantia.block.GeneratorBlock;
import com.lucanemone.resonantia.block.CableBlock;
import com.lucanemone.resonantia.block.AssemblerBlock;
import com.lucanemone.resonantia.block.FailleFrameBlock;
import com.lucanemone.resonantia.block.FaillePortalBlock;

public class ResonantiaBlocks {

    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(Resonantia.MOD_ID);

    public static final DeferredBlock<Block> CHRYSOLITHE_ORE = BLOCKS.register(
            "chrysolithe_ore",
            () -> new DropExperienceBlock(
                    UniformInt.of(1, 3),
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.STONE)
                            .requiresCorrectToolForDrops()
                            .strength(3.0F, 3.0F)
                            .sound(SoundType.STONE)
            )
    );

    public static final DeferredBlock<Block> CHRYSOLITHE_BLOCK = BLOCKS.register(
            "chrysolithe_block",
            () -> new Block(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.PLANT)
                            .requiresCorrectToolForDrops()
                            .strength(5.0F, 6.0F)
                            .sound(SoundType.STONE)
            )
    );

    // Ferrite Noire — magnétisme, plus profond que Chrysolithe
    public static final DeferredBlock<Block> FERRITE_ORE = BLOCKS.register(
            "ferrite_ore",
            () -> new DropExperienceBlock(
                    UniformInt.of(2, 5),
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_BLACK)
                            .requiresCorrectToolForDrops()
                            .strength(4.0F, 4.0F)
                            .sound(SoundType.DEEPSLATE)
            )
    );

    public static final DeferredBlock<Block> FERRITE_BLOCK = BLOCKS.register(
            "ferrite_block",
            () -> new Block(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_BLACK)
                            .requiresCorrectToolForDrops()
                            .strength(6.0F, 7.0F)
                            .sound(SoundType.DEEPSLATE)
            )
    );

    // Sylvanite — rare, camouflée, façon débris antique mais en forêt/marais
    public static final DeferredBlock<Block> SYLVANITE_ORE = BLOCKS.register(
            "sylvanite_ore",
            () -> new DropExperienceBlock(
                    UniformInt.of(3, 7),
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.STONE)
                            .requiresCorrectToolForDrops()
                            .strength(6.0F, 12.0F)
                            .sound(SoundType.STONE)
            )
    );

    public static final DeferredBlock<Block> SYLVANITE_BLOCK = BLOCKS.register(
            "sylvanite_block",
            () -> new Block(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.PLANT)
                            .requiresCorrectToolForDrops()
                            .strength(8.0F, 14.0F)
                            .sound(SoundType.STONE)
            )
    );

    public static final DeferredBlock<Block> CONCASSEUR = BLOCKS.register(
            "concasseur",
            () -> new ConcasseurBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.METAL)
                            .strength(4.0F, 6.0F)
                            .sound(SoundType.METAL)
                            .noOcclusion()
            )
    );

    public static final DeferredBlock<Block> GENERATOR = BLOCKS.register(
            "generator",
            () -> new GeneratorBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.METAL)
                            .strength(4.0F, 6.0F)
                            .sound(SoundType.METAL)
                            .noOcclusion()
            )
    );

    public static final DeferredBlock<Block> CABLE = BLOCKS.register(
            "cable",
            () -> new CableBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_BLACK)
                            .strength(1.0F, 3.0F)
                            .sound(SoundType.METAL)
                            .noOcclusion()
            )
    );

    public static final DeferredBlock<Block> ASSEMBLER = BLOCKS.register(
            "assembler",
            () -> new AssemblerBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.METAL)
                            .strength(4.0F, 6.0F)
                            .sound(SoundType.METAL)
                            .noOcclusion()
            )
    );

    public static final DeferredBlock<Block> CINDRITE_ORE = BLOCKS.register(
            "cindrite_ore",
            () -> new DropExperienceBlock(
                    UniformInt.of(3, 6),
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.NETHER)
                            .requiresCorrectToolForDrops()
                            .strength(5.0F, 30.0F)
                            .sound(SoundType.NETHERRACK)
            )
    );

    public static final DeferredBlock<Block> CINDRITE_BLOCK = BLOCKS.register(
            "cindrite_block",
            () -> new Block(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.NETHER)
                            .requiresCorrectToolForDrops()
                            .strength(7.0F, 30.0F)
                            .sound(SoundType.NETHERRACK)
            )
    );

    // Nébulite — End, téléportation/spatial, instable (chance de destruction sans Silk Touch)
    public static final DeferredBlock<Block> NEBULITE_ORE = BLOCKS.register(
            "nebulite_ore",
            () -> new DropExperienceBlock(
                    UniformInt.of(4, 8),
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_PURPLE)
                            .requiresCorrectToolForDrops()
                            .strength(4.0F, 6.0F)
                            .sound(SoundType.GLASS)
            )
    );

    public static final DeferredBlock<Block> NEBULITE_BLOCK = BLOCKS.register(
            "nebulite_block",
            () -> new Block(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_PURPLE)
                            .requiresCorrectToolForDrops()
                            .strength(6.0F, 8.0F)
                            .sound(SoundType.GLASS)
            )
    );

    // La Faille — cadre et portail vers la dimension exclusive au mod
    public static final DeferredBlock<Block> FAILLE_FRAME = BLOCKS.register(
            "faille_frame",
            () -> new FailleFrameBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_PURPLE)
                            .requiresCorrectToolForDrops()
                            .strength(50.0F, 1200.0F)
                            .sound(SoundType.STONE)
            )
    );

    public static final DeferredBlock<Block> FAILLE_PORTAL = BLOCKS.register(
            "faille_portal",
            () -> new FaillePortalBlock(
                    BlockBehaviour.Properties.of()
                            .noCollission()
                            .instabreak()
                            .sound(SoundType.GLASS)
                            .lightLevel(state -> 11)
                            .noLootTable()
            )
    );
}
