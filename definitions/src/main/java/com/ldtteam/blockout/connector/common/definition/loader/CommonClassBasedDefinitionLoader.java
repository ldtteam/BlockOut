package com.ldtteam.blockout.connector.common.definition.loader;

import com.ldtteam.blockout.connector.core.IGuiDefinitionLoader;
import org.jetbrains.annotations.NotNull;

public class CommonClassBasedDefinitionLoader implements IGuiDefinitionLoader<Class>
{

    public CommonClassBasedDefinitionLoader() {
    }

    @NotNull
    @Override
    public String getGuiDefinition(final Class loadFrom) {
        return String.format("%s.class", loadFrom.getName());
    }
}
