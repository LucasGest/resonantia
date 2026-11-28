package com.lucanemone.resonantia.block;

import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

import com.lucanemone.resonantia.blockentity.ConcasseurBlockEntity;
import com.lucanemone.resonantia.item.FrequencyKeyItem;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import net.minecraft.world.ItemInteractionResult;

public class ConcasseurBlock extends BaseEntityBlock {

    public static final MapCodec<ConcasseurBlock> CODEC = simpleCodec(ConcasseurBlock::new);

    // Façade tournée vers le joueur qui pose la machine
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public ConcasseurBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH));
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
        return new ConcasseurBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide) return null;
        return (lvl, pos, st, blockEntity) -> {
            if (blockEntity instanceof ConcasseurBlockEntity concasseur) {
                ConcasseurBlockEntity.tick(lvl, pos, st, concasseur);
            }
        };
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        return openOrTune(level, pos, player, ItemStack.EMPTY);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        openOrTune(level, pos, player, stack);
        return ItemInteractionResult.SUCCESS;
    }

    private InteractionResult openOrTune(Level level, BlockPos pos, Player player, ItemStack stack) {
        if (!level.isClientSide()) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof ConcasseurBlockEntity concasseur && stack.getItem() instanceof FrequencyKeyItem) {
                int delta = player.isShiftKeyDown() ? -10 : 10;
                int newFreq = concasseur.adjustFrequency(delta);
                player.displayClientMessage(Component.literal("Fréquence réglée à " + newFreq), true);
                return InteractionResult.SUCCESS;
            }
            if (be instanceof MenuProvider menuProvider) {
                player.openMenu(menuProvider, buf -> buf.writeBlockPos(pos));
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
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
}
