package com.ldtteam.blockout.loader.core;

import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public interface IUIElementMetaDataBuilder
{

    IUIElementMetaData withId(@NotNull final String string);

    IUIElementMetaData withType(@NotNull final ResourceLocation type);

    IUIElementMetaData build();
}
