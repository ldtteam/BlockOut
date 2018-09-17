package com.ldtteam.blockout.json.loader.json;

import com.google.gson.JsonObject;
import com.ldtteam.blockout.element.IUIElementHost;
import com.ldtteam.blockout.json.loader.core.IUIElementMetaData;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class JsonUIElementMetaData implements IUIElementMetaData
{
    @NotNull
    private final JsonObject object;
    @Nullable
    private final IUIElementHost parent;

    public JsonUIElementMetaData(@NotNull final JsonObject object, @Nullable final IUIElementHost parent) {
        this.object = object;
        this.parent = parent;
    }

    @Override
    public ResourceLocation getType()
    {
        return new ResourceLocation(object.get("type").getAsString());
    }

    @Override
    public String getId()
    {
        return object.get("id").getAsString();
    }

    @Override
    public IUIElementHost getParent()
    {
        return parent;
    }
}
