package com.ldtteam.blockout.loader.object;

import com.ldtteam.blockout.loader.core.IUIElementBuilder;
import com.ldtteam.blockout.loader.core.IUIElementData;
import com.ldtteam.blockout.loader.core.IUIElementMetaData;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ObjectUIElementBuilder implements IUIElementBuilder
{
    private final List<ObjectUIElementData> children   = new ArrayList<>();
    private final Map<String, Serializable> attributes = new HashMap<>();
    private ResourceLocation type;

    @Override
    public IUIElementBuilder withMetaData(final IUIElementMetaData metaData)
    {
        return null;
    }

    @Override
    public IUIElementBuilder addChild(@NotNull final IUIElementData elementData)
    {
        return null;
    }

    @Override
    public IUIElementData build()
    {
        return new ObjectUIElementData(type.toString(), children, attributes, null);
    }
}