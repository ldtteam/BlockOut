package com.minecolonies.blockout.loader.object;

import com.google.common.collect.ImmutableList;
import com.minecolonies.blockout.core.element.IUIElementHost;
import com.minecolonies.blockout.loader.IUIElementData;
import com.minecolonies.blockout.util.Localization;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class ObjectUIElementData implements IUIElementData, Serializable
{
    @NotNull
    private final String                    type;
    @NotNull
    private final List<ObjectUIElementData> children;
    @NotNull
    private final Map<String, Serializable> attributes;
    @Nullable
    private transient IUIElementHost parent = null;

    public ObjectUIElementData(
      @NotNull final String type,
      @NotNull final List<ObjectUIElementData> children,
      @NotNull final Map<String, Serializable> attributes)
    {
        this.type = type;
        this.children = children;
        this.attributes = attributes;
    }

    @Override
    public String getType()
    {
        return type;
    }

    @Nullable
    @Override
    public IUIElementHost getParentView()
    {
        return parent;
    }

    @Override
    public void setParentView(@Nullable final IUIElementHost parent)
    {
        this.parent = parent;
    }

    @Override
    public double getParentWidth()
    {
        return Objects.isNull(getParentView()) ? 0 : getParentView().getAbsoluteInternalBoundingBox().getSize().getX();
    }

    @Override
    public double getParentHeight()
    {
        return Objects.isNull(getParentView()) ? 0 : getParentView().getAbsoluteInternalBoundingBox().getSize().getY();
    }

    @Nullable
    @Override
    public List<IUIElementData> getChildren()
    {
        return ImmutableList.copyOf(children);
    }

    @Nullable
    @Override
    public String getText()
    {
        return getStringAttribute("text", "");
    }

    @Nullable
    @Override
    public String getLocalizedText()
    {
        return Localization.localize(getText());
    }

    @Nullable
    @Override
    public String getStringAttribute(@NotNull final String name)
    {
        return getStringAttribute(name, "");
    }

    @Nullable
    @Override
    public String getLocalizedStringAttribute(@NotNull final String name)
    {
        return getLocalizedStringAttribute(name, "");
    }

    @Nullable
    @Override
    public String getLocalizedStringAttribute(@NotNull final String name, @Nullable final String def)
    {
        return Localization.localize(getStringAttribute(name, def));
    }

    @Override
    public int getIntegerAttribute(@NotNull final String name)
    {
        return getIntegerAttribute(name, 0);
    }

    @Override
    public int getIntegerAttribute(@NotNull final String name, final int def)
    {
        return getAttribute(name, Integer.class).orElse(def);
    }

    @Override
    public float getFloatAttribute(@NotNull final String name)
    {
        return getFloatAttribute(name, 0f);
    }

    @Override
    public float getFloatAttribute(@NotNull final String name, final float def)
    {
        return getAttribute(name, Float.class).orElse(def);
    }

    @Override
    public double getDoubleAttribute(@NotNull final String name)
    {
        return getDoubleAttribute(name, 0d);
    }

    @Override
    public double getDoubleAttribute(@NotNull final String name, final double def)
    {
        return getAttribute(name, Double.class).orElse(def);
    }

    @Override
    public boolean getBooleanAttribute(@NotNull final String name)
    {
        return getBooleanAttribute(name, false);
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
