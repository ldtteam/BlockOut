package com.minecolonies.blockout.connector.common;

import com.minecolonies.blockout.connector.core.IGuiDefinitionLoader;
import org.jetbrains.annotations.NotNull;

public class CommonClassBasedDefinitionLoader implements IGuiDefinitionLoader
{

    private final Class<?> clazz;

    public CommonClassBasedDefinitionLoader(final Class<?> clazz) {this.clazz = clazz;}

    @NotNull
    @Override
    public String getGuiDefinition()
    {
        return clazz.getName() + ".class";
    }
}
