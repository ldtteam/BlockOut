package com.minecolonies.blockout.loader.object;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.minecolonies.blockout.core.element.IUIElementHost;
import com.minecolonies.blockout.loader.IUIElementData;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class ObjectUIElementData implements IUIElementData, Serializable
{
    @NotNull
    private final           String                    type;
    @NotNull
    private final           List<ObjectUIElementData> children;
    @NotNull
    private final           Map<String, Serializable> attributes;
    @Nullable
    private final transient IUIElementHost            parent;

    public ObjectUIElementData(
      @NotNull final ObjectUIElementData copyFrom,
      @Nullable final IUIElementHost parent
    )
    {
        this(copyFrom.getType().toString(), Lists.newArrayList(copyFrom.children), Maps.newHashMap(copyFrom.attributes), parent);
    }

    public ObjectUIElementData(
      @NotNull final String type,
      @NotNull final List<ObjectUIElementData> children,
      @NotNull final Map<String, Serializable> attributes,
      @Nullable final IUIElementHost parent)
    {
        this.type = type;
        this.children = children;
        this.attributes = attributes;
        this.parent = parent;
    }

    @Override
    public ResourceLocation getType()
    {
        return new ResourceLocation(type);
    }

    @Nullable
    @Override
    public IUIElementHost getParentView()
    {
        return parent;
    }

    @Nullable
    @Override
    public List<IUIElementData> getChildren(@NotNull final IUIElementHost parentOfChildren)
    {
        return children.stream()
                 .map(objectUIElementData -> new ObjectUIElementData(objectUIElementData, parentOfChildren))
                 .collect(Collectors.toList());
    }

    @Override
    public int getIntegerAttribute(@NotNull final String name, final int def)
    {
        return getAttribute(name, Integer.class).orElse(def);
    }

    @Override
    public float getFloatAttribute(@NotNull final String name, final float def)
    {
        return getAttribute(name, Float.class).orElse(def);
    }

    @Override
    public double getDoubleAttribute(@NotNull final String name, final double def)
    {
        return getAttribute(name, Double.class).orElse(def);
    }

    @Override
    public boolean getBooleanAttribute(@NotNull final String name, final boolean def)
    {
        return getAttribute(name, Boolean.class).orElse(def);
    }

    @Nullable
    @Override
    public String getStringAttribute(@NotNull final String name, @Nullable final String def)
    {
        return getAttribute(name, String.class).orElse(def);
    }

    private <T extends Serializable> Optional<T> getAttribute(@NotNull final String name, @NotNull final Class<T> cls)
    {
        if (attributes.get(name) != null)
        {
            Serializable val = attributes.get(name);

            if (cls.isInstance(val))
            {
                return Optional.of(cls.cast(val));
            }
        }

        return Optional.empty();
    }
}
