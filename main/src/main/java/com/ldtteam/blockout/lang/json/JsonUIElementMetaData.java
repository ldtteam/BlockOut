package com.ldtteam.blockout.lang.json;

import com.google.gson.JsonObject;
import com.ldtteam.blockout.element.IUIElementHost;
import com.ldtteam.blockout.loader.core.IUIElementMetaData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

import static com.ldtteam.blockout.util.Constants.Controls.General.*;

public class JsonUIElementMetaData implements IUIElementMetaData
{
    @NotNull
    private final JsonObject     object;
    @Nullable
    private final IUIElementHost parent;

    public JsonUIElementMetaData(@NotNull final JsonObject object, @Nullable final IUIElementHost parent)
    {
        this.object = object;
        this.parent = parent;
    }

    @Override
    public String getType()
    {
        return object.get(CONST_TYPE).getAsString();
    }

    @Override
    public String getId()
    {
        return object.get(CONST_ID).getAsString();
    }

    @Override
    public Optional<IUIElementHost> getParent()
    {
        return Optional.ofNullable(parent);
    }

    @Override
    public boolean hasChildren()
    {
        return object.has(CONST_CHILDREN);
    }
}
