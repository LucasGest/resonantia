package com.lucanemone.resonantia.block;

import com.lucanemone.resonantia.registry.ResonantiaBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class FaillePortalHelper {

    public static boolean tryCreatePortal(Level level, BlockPos anchor) {
        return tryOrientation(level, anchor, Direction.Axis.X) || tryOrientation(level, anchor, Direction.Axis.Z);
    }

    private static boolean tryOrientation(Level level, BlockPos anchor, Direction.Axis axis) {
        for (int offset = 0; offset <= 3; offset++) {
            BlockPos corner = axis == Direction.Axis.X
                    ? anchor.offset(-offset, 0, 0)
                    : anchor.offset(0, 0, -offset);
            if (checkAndBuild(level, corner, axis)) return true;
        }
        return false;
    }

    private static boolean checkAndBuild(Level level, BlockPos corner, Direction.Axis axis) {
        for (int i = 0; i <= 3; i++) {
            if (!isFrame(level, offset(corner, axis, i, 0))) return false;
            if (!isFrame(level, offset(corner, axis, i, 4))) return false;
        }
        for (int j = 1; j <= 3; j++) {
            if (!isFrame(level, offset(corner, axis, 0, j))) return false;
            if (!isFrame(level, offset(corner, axis, 3, j))) return false;
        }
        for (int i = 1; i <= 2; i++) {
            for (int j = 1; j <= 3; j++) {
                if (!level.getBlockState(offset(corner, axis, i, j)).isAir()) return false;
            }
        }

        BlockState portalState = ResonantiaBlocks.FAILLE_PORTAL.get().defaultBlockState()
                .setValue(FaillePortalBlock.AXIS, axis);
        for (int i = 1; i <= 2; i++) {
            for (int j = 1; j <= 3; j++) {
                level.setBlock(offset(corner, axis, i, j), portalState, 3);
            }
        }
        return true;
    }

    private static boolean isFrame(Level level, BlockPos pos) {
        return level.getBlockState(pos).is(ResonantiaBlocks.FAILLE_FRAME.get());
    }

    private static BlockPos offset(BlockPos corner, Direction.Axis axis, int i, int j) {
        return axis == Direction.Axis.X ? corner.offset(i, j, 0) : corner.offset(0, j, i);
    }
}
