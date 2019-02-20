package com.ldtteam.blockout.loader.core;

import com.ldtteam.jvoxelizer.util.identifier.IIdentifier;
import org.jetbrains.annotations.NotNull;

public interface IUIElementMetaDataBuilder<T extends IUIElementMetaData>
{
    IUIElementMetaDataBuilder<T> withId(@NotNull final String string);

    IUIElementMetaDataBuilder<T> withType(@NotNull final String type);

    T build();
}
