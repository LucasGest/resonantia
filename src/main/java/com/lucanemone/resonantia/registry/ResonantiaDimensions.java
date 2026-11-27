package com.lucanemone.resonantia.registry;

import com.lucanemone.resonantia.Resonantia;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;

public class ResonantiaDimensions {

    public static final ResourceKey<Level> LA_FAILLE_LEVEL_KEY = ResourceKey.create(
            Registries.DIMENSION,
            ResourceLocation.fromNamespaceAndPath(Resonantia.MOD_ID, "la_faille")
    );

    public static final ResourceKey<DimensionType> LA_FAILLE_TYPE_KEY = ResourceKey.create(
            Registries.DIMENSION_TYPE,
            ResourceLocation.fromNamespaceAndPath(Resonantia.MOD_ID, "la_faille")
    );
}
