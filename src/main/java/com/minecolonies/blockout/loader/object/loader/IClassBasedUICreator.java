package com.minecolonies.blockout.loader.object.loader;

import com.minecolonies.blockout.loader.IUIElementDataBuilder;
import org.jetbrains.annotations.NotNull;

public interface IClassBasedUICreator
{
    void build(@NotNull final IUIElementDataBuilder builder);
}
