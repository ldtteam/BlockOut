package com.ldtteam.blockout.loader.core;

import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public interface IUIElementMetaDataBuilder<T extends IUIElementMetaData>
{
    IUIElementMetaDataBuilder<T> withId(@NotNull final String string);

    IUIElementMetaDataBuilder<T> withType(@NotNull final ResourceLocation type);

    T build();
}
