package com.lucanemone.resonantia.block;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

import com.lucanemone.resonantia.blockentity.GeneratorBlockEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class GeneratorBlock extends BaseEntityBlock {

    public static final MapCodec<GeneratorBlock> CODEC = simpleCodec(GeneratorBlock::new);

    // Façade tournée vers le joueur qui pose la machine
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    // Vrai tant que le générateur brûle du combustible : la cheminée fume
    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    public GeneratorBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(LIT, false));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new GeneratorBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide) return null;
        return (lvl, pos, st, blockEntity) -> {
            if (blockEntity instanceof GeneratorBlockEntity generator) {
                GeneratorBlockEntity.tick(lvl, pos, st, generator);
            }
        };
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide()) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof MenuProvider menuProvider) {
                player.openMenu(menuProvider, buf -> buf.writeBlockPos(pos));
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, LIT);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }
    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (!state.getValue(LIT)) return;
        // Sortie de la cheminée : (+3, +3) pixels depuis le centre quand la façade est au nord
        double dx = 3 / 16.0, dz = 3 / 16.0, t;
        switch (state.getValue(FACING)) {
            case EAST -> { t = dx; dx = -dz; dz = t; }
            case SOUTH -> { dx = -dx; dz = -dz; }
            case WEST -> { t = dx; dx = dz; dz = -t; }
            default -> { }
        }
        double x = pos.getX() + 0.5 + dx + (random.nextDouble() - 0.5) * 0.15;
        double y = pos.getY() + 1.02;
        double z = pos.getZ() + 0.5 + dz + (random.nextDouble() - 0.5) * 0.15;
        level.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0, 0.04, 0.0);
        if (random.nextInt(3) == 0) {
            level.addParticle(ParticleTypes.LARGE_SMOKE, x, y, z, 0.0, 0.03, 0.0);
        }
    }
}
