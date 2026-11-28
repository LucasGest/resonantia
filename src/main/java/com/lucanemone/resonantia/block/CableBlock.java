package com.lucanemone.resonantia.block;

import net.neoforged.neoforge.capabilities.Capabilities;
import com.lucanemone.resonantia.blockentity.CableBlockEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class CableBlock extends BaseEntityBlock {

    public static final MapCodec<CableBlock> CODEC = simpleCodec(CableBlock::new);

    public static final BooleanProperty NORTH = BlockStateProperties.NORTH;
    public static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;
    public static final BooleanProperty EAST = BlockStateProperties.EAST;
    public static final BooleanProperty WEST = BlockStateProperties.WEST;
    public static final BooleanProperty UP = BlockStateProperties.UP;
    public static final BooleanProperty DOWN = BlockStateProperties.DOWN;
    // Vrai tant que le câble contient de l'énergie : le flux apparaît dans le tube
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    private static final VoxelShape CORE = Shapes.box(6/16.0, 6/16.0, 6/16.0, 10/16.0, 10/16.0, 10/16.0);
    private static final VoxelShape ARM_NORTH = Shapes.box(6/16.0, 6/16.0, 0, 10/16.0, 10/16.0, 6/16.0);
    private static final VoxelShape ARM_SOUTH = Shapes.box(6/16.0, 6/16.0, 10/16.0, 10/16.0, 10/16.0, 1);
    private static final VoxelShape ARM_WEST = Shapes.box(0, 6/16.0, 6/16.0, 6/16.0, 10/16.0, 10/16.0);
    private static final VoxelShape ARM_EAST = Shapes.box(10/16.0, 6/16.0, 6/16.0, 1, 10/16.0, 10/16.0);
    private static final VoxelShape ARM_UP = Shapes.box(6/16.0, 10/16.0, 6/16.0, 10/16.0, 1, 10/16.0);
    private static final VoxelShape ARM_DOWN = Shapes.box(6/16.0, 0, 6/16.0, 10/16.0, 6/16.0, 10/16.0);

    public CableBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any()
                .setValue(NORTH, false).setValue(SOUTH, false)
                .setValue(EAST, false).setValue(WEST, false)
                .setValue(UP, false).setValue(DOWN, false)
                .setValue(POWERED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(NORTH, SOUTH, EAST, WEST, UP, DOWN, POWERED);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        VoxelShape shape = CORE;
        if (state.getValue(NORTH)) shape = Shapes.or(shape, ARM_NORTH);
        if (state.getValue(SOUTH)) shape = Shapes.or(shape, ARM_SOUTH);
        if (state.getValue(EAST)) shape = Shapes.or(shape, ARM_EAST);
        if (state.getValue(WEST)) shape = Shapes.or(shape, ARM_WEST);
        if (state.getValue(UP)) shape = Shapes.or(shape, ARM_UP);
        if (state.getValue(DOWN)) shape = Shapes.or(shape, ARM_DOWN);
        return shape;
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CableBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide) return null;
        return (lvl, pos, st, blockEntity) -> {
            if (blockEntity instanceof CableBlockEntity cable) {
                CableBlockEntity.tick(lvl, pos, st, cable);
            }
        };
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return computeState(defaultBlockState(), context.getLevel(), context.getClickedPos());
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        boolean connected = connectsTo(level, neighborPos);
        return applyDirection(state, direction, connected);
    }

    private BlockState computeState(BlockState state, LevelAccessor level, BlockPos pos) {
        for (Direction direction : Direction.values()) {
            boolean connected = connectsTo(level, pos.relative(direction));
            state = applyDirection(state, direction, connected);
        }
        return state;
    }

    private BlockState applyDirection(BlockState state, Direction direction, boolean connected) {
        return switch (direction) {
            case NORTH -> state.setValue(NORTH, connected);
            case SOUTH -> state.setValue(SOUTH, connected);
            case EAST -> state.setValue(EAST, connected);
            case WEST -> state.setValue(WEST, connected);
            case UP -> state.setValue(UP, connected);
            case DOWN -> state.setValue(DOWN, connected);
        };
    }

    private boolean connectsTo(LevelAccessor level, BlockPos pos) {
        if (!(level instanceof Level lvl)) return false;
        // Tout bloc qui stocke de l'énergie (machines du mod, câbles, autres mods)
        return lvl.getCapability(Capabilities.EnergyStorage.BLOCK, pos, null) != null;
    }
}