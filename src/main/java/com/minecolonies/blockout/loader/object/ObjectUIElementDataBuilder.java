package com.minecolonies.blockout.loader.object;

import com.minecolonies.blockout.loader.IUIElementData;
import com.minecolonies.blockout.loader.IUIElementDataBuilder;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ObjectUIElementDataBuilder implements IUIElementDataBuilder
{

    private final List<ObjectUIElementData> children   = new ArrayList<>();
    private final Map<String, Serializable> attributes = new HashMap<>();
    private String type;

    @Override
    public IUIElementDataBuilder setType(final String type)
    {
        this.type = type;
        return this;
    }

    @Override
    public IUIElementDataBuilder addChild(@NotNull final IUIElementDataBuilder builder)
    {
        return addChild(builder.build());
    }

    @Override
    public IUIElementDataBuilder addChild(@NotNull final IUIElementData elementData)
    {
        if (!(elementData instanceof ObjectUIElementData))
        {
            throw new IllegalArgumentException("Not serializable element data");
        }

        children.add((ObjectUIElementData) elementData);
        return this;
    }

    @Override
    public IUIElementDataBuilder addAttribute(@NotNull final String key, @NotNull final Serializable attribute)
    {
        attributes.put(key, attribute);
        return this;
    }

    @Override
    public IUIElementData build()
    {
        return new ObjectUIElementData(type, children, attributes);
    }
}