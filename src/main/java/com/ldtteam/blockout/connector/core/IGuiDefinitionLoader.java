package com.ldtteam.blockout.connector.core;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public interface IGuiDefinitionLoader extends Serializable
{

    @NotNull
    String getGuiDefinition();
}
