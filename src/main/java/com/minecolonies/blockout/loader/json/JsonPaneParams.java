package com.minecolonies.blockout.loader.json;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.minecolonies.blockout.loader.IUIElementData;
import com.minecolonies.blockout.util.Localization;
import com.minecolonies.blockout.views.View;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class JsonPaneParams implements IUIElementData
{
    private final JsonObject object;
    private       View       parent;

    public JsonPaneParams(final JsonObject object) {this.object = object;}

    @Override
    public String getType()
    {
        return object.get("type").getAsString();
    }

    @Nullable
    @Override
    public View getParentView()
    {
        return parent;
    }

    @Override
    public void setParentView(@Nullable final View parent)
    {
        this.parent = parent;
    }

    @Override
    public int getParentWidth()
    {
        return getParentView() != null ? getParentView().getWidth() : 0;
    }

    @Override
    public int getParentHeight()
    {
        return getParentView() != null ? getParentView().getHeight() : 0;
    }

    @Nullable
    @Override
    public List<IUIElementData> getChildren()
    {
        if (!object.has("children"))
        {
            return ImmutableList.of();
        }

        return StreamSupport.stream(object.get("children").getAsJsonArray().spliterator(), false)
                 .filter(JsonElement::isJsonObject)
                 .map(JsonElement::getAsJsonObject)
                 .map(JsonPaneParams::new)
                 .collect(Collectors.toList());
    }

    @Nullable
    @Override
    public String getText()
    {
        return getStringAttribute("text");
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
        return getStringAttribute(name, null);
    }

    @Nullable
    @Override
    public String getStringAttribute(@NotNull final String name, @Nullable final String def)
    {
        if (!object.has(name))
        {
            return def;
        }

        return object.get(name).getAsString();
    }

    @Nullable
    @Override
    public String getLocalizedStringAttribute(@NotNull final String name)
    {
        return getLocalizedStringAttribute(name, null);
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
        if (!object.has(name))
        {
            return def;
        }

        return object.get(name).getAsInt();
    }

    @Override
    public float getFloatAttribute(@NotNull final String name)
    {
        return getFloatAttribute(name, 0f);
    }

    @Override
    public float getFloatAttribute(@NotNull final String name, final float def)
    {
        if (!object.has(name))
        {
            return def;
        }

        return object.get(name).getAsFloat();
    }

    @Override
    public double getDoubleAttribute(@NotNull final String name)
    {
        return getDoubleAttribute(name, 0d);
    }

    @Override
    public double getDoubleAttribute(@NotNull final String name, final double def)
    {
        if (!object.has(name))
        {
            return def;
        }

        return object.get(name).getAsDouble();
    }

    @Override
    public boolean getBooleanAttribute(@NotNull final String name)
    {
        return getBooleanAttribute(name, false);
    }

    @Override
    public boolean getBooleanAttribute(@NotNull final String name, final boolean def)
    {
        if (!object.has(name))
        {
            return def;
        }

        return object.get(name).getAsBoolean();
    }
}
