package com.ldtteam.blockout.connector.core;

import org.jetbrains.annotations.NotNull;

public interface IGuiDefinitionLoader<T>
{
    @NotNull
    String getGuiDefinition(T loadFrom);
}
