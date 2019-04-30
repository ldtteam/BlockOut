package com.ldtteam.blockout.json;

import com.google.gson.JsonObject;
import com.ldtteam.blockout.element.IUIElementHost;
import com.ldtteam.blockout.loader.core.IUIElementData;
import com.ldtteam.blockout.loader.core.IUIElementMetaData;
import com.ldtteam.blockout.loader.core.component.IUIElementDataComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class JsonUIElementData implements IUIElementData<JsonUIElementDataComponent>
{
    @NotNull
    private final JsonObject         object;
    @NotNull
    private final IUIElementMetaData metaData;

    public JsonUIElementData(
      @NotNull final JsonObject object,
      @Nullable IUIElementHost parent)
    {
        this.object = object;
        this.metaData = new JsonUIElementMetaData(object, parent);
    }

    @NotNull
    @Override
    public IUIElementMetaData getMetaData()
    {
        return metaData;
    }

    @Nullable
    @Override
    public Optional<JsonUIElementDataComponent> getComponentWithName(@NotNull final String name, boolean isPrimary)
    {
        if (object.has(name))
        {
            return Optional.of(new JsonUIElementDataComponent(object.get(name)));
        }

        return Optional.empty();
    }

    @Override
    public <D extends IUIElementDataComponent> D toDataComponent(@NotNull final D toWriteInto)
    {
        if (toWriteInto instanceof JsonUIElementDataComponent)
        {
            return (D) toDataComponent();
        }

        return toWriteInto;
    }

    public JsonUIElementDataComponent toDataComponent()
    {
        return new JsonUIElementDataComponent(object);
    }
}
