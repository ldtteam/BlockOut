package com.ldtteam.blockout.connector.core;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public interface IGuiDefinitionLoader<T>
{
    @NotNull
    String getGuiDefinition(T loadFrom);
}
