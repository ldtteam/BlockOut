package com.ldtteam.blockout.loader.object;

import com.ldtteam.blockout.loader.core.IUIElementData;
import com.ldtteam.blockout.loader.core.IUIElementMetaData;
import com.ldtteam.blockout.loader.core.component.IUIElementDataComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ObjectUIElementData implements IUIElementData<ObjectUIElementDataComponent>, Serializable
{

    private static final long serialVersionUID = 7495696922826437117L;

    @NotNull
    private final Map<String, ObjectUIElementDataComponent> object;
    @NotNull
    private transient final ObjectUIElementMetaData                   metaData;

    public ObjectUIElementData(@NotNull final Map<String, ObjectUIElementDataComponent> object, @NotNull final ObjectUIElementMetaData metaData)
    {
        this.object = object;
        this.metaData = metaData;
    }

    @NotNull
    @Override
    public IUIElementMetaData getMetaData()
    {
        return metaData;
    }

    @Nullable
    @Override
    public Optional<ObjectUIElementDataComponent> getComponentWithName(@NotNull final String name, final boolean isPrimary)
    {
        if (object.containsKey(name))
        {
            return Optional.of(object.get(name));
        }

        return Optional.empty();
    }

    @Override
    public <D extends IUIElementDataComponent> D toDataComponent(@NotNull final D toWriteInto)
    {
        final Map<String, ObjectUIElementDataComponent> dataMap = new HashMap<>(this.object);
        this.metaData.mergeData(dataMap);

        toWriteInto.setMap(dataMap);

        return toWriteInto;
    }
}
