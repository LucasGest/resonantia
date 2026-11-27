package com.lucanemone.resonantia.block;

import com.lucanemone.resonantia.registry.ResonantiaDimensions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class FaillePortalBlock extends Block {

    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.HORIZONTAL_AXIS;

    private static final VoxelShape X_SHAPE = Shapes.box(0, 0, 0.375, 1, 1, 0.625);
    private static final VoxelShape Z_SHAPE = Shapes.box(0.375, 0, 0, 0.625, 1, 1);

    private static final int COOLDOWN_TICKS = 40;

    public FaillePortalBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(AXIS, Direction.Axis.X));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AXIS);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.getValue(AXIS) == Direction.Axis.Z ? Z_SHAPE : X_SHAPE;
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (level.isClientSide()) return;
        if (!(level instanceof ServerLevel serverLevel)) return;
        if (!(entity instanceof ServerPlayer player)) return;

        CompoundTag data = entity.getPersistentData();
        long now = level.getGameTime();
        long last = data.getLong("resonantia_last_teleport");
        if (now - last < COOLDOWN_TICKS) return;
        data.putLong("resonantia_last_teleport", now);

        MinecraftServer server = serverLevel.getServer();
        boolean inFaille = serverLevel.dimension() == ResonantiaDimensions.LA_FAILLE_LEVEL_KEY;
        ResourceKey<Level> targetKey = inFaille ? Level.OVERWORLD : ResonantiaDimensions.LA_FAILLE_LEVEL_KEY;
        ServerLevel destLevel = server.getLevel(targetKey);
        if (destLevel == null) return;

        BlockPos destPos = findSafeSpot(destLevel, pos);
        player.teleportTo(destLevel, destPos.getX() + 0.5, destPos.getY(), destPos.getZ() + 0.5, player.getYRot(), player.getXRot());
    }

    private BlockPos findSafeSpot(ServerLevel level, BlockPos origin) {
        BlockPos.MutableBlockPos mutable = origin.mutable();
        for (int y = level.getMaxBuildHeight() - 2; y > level.getMinBuildHeight(); y--) {
            mutable.setY(y);
            if (!level.getBlockState(mutable).isAir()
                    && level.getBlockState(mutable.above()).isAir()
                    && level.getBlockState(mutable.above(2)).isAir()) {
                return mutable.above().immutable();
            }
        }
        return origin;
    }
}
