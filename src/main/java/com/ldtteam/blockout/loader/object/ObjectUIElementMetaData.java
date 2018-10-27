package com.ldtteam.blockout.loader.object;

import com.ldtteam.blockout.element.IUIElementHost;
import com.ldtteam.blockout.loader.core.IUIElementMetaData;
import com.ldtteam.blockout.loader.core.component.IUIElementDataComponent;
import com.ldtteam.blockout.util.Constants;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;

public class ObjectUIElementMetaData implements IUIElementMetaData
{
    @NotNull
    private final Map<String, IUIElementDataComponent> object;

    public ObjectUIElementMetaData(@NotNull final Map<String, IUIElementDataComponent> object) {this.object = object;}

    @Override
    public ResourceLocation getType()
    {
        return object.get(Constants.);
    }

    @Override
    public String getId()
    {
        return null;
    }

    @Override
    public Optional<IUIElementHost> getParent()
    {
        return Optional.empty();
    }
}
