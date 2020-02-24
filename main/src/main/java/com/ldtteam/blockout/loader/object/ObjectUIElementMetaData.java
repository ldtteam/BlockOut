package com.ldtteam.blockout.loader.object;

import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.ldtteam.blockout.element.IUIElementHost;
import com.ldtteam.blockout.loader.core.IUIElementMetaData;
import com.ldtteam.blockout.loader.factory.core.IUIElementDataComponentConverter;
import com.ldtteam.blockout.proxy.ProxyHolder;
import com.ldtteam.blockout.util.Constants;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Map;
import java.util.Optional;

public class ObjectUIElementMetaData implements IUIElementMetaData, Serializable
{
    private static final long serialVersionUID = 5842870418670469285L;

    @NotNull
    private final Map<String, ObjectUIElementDataComponent> object;
    @Nullable
    private final IUIElementHost                            parent;

    public ObjectUIElementMetaData(@NotNull final Map<String, ObjectUIElementDataComponent> object, @Nullable final IUIElementHost parent)
    {
        this.object = object;
        this.parent = parent;
    }

    @Override
    public String getType()
    {
        final IUIElementDataComponentConverter<String> factory =
          ProxyHolder.getInstance().getInjector().getInstance(Key.get(new TypeLiteral<IUIElementDataComponentConverter<String>>() {}));
        return factory.readFromElement(object.get(Constants.Controls.General.CONST_TYPE), null);
    }

    @Override
    public String getId()
    {
        return object.get(Constants.Controls.General.CONST_ID).getAsString();
    }

    @Override
    public Optional<IUIElementHost> getParent()
    {
        return Optional.ofNullable(parent);
    }

    @Override
    public boolean hasChildren()
    {
        return object.containsKey(Constants.Controls.General.CONST_CHILDREN);
    }

    final
    /**
     * Merges its required data into the given component map.
     * Overrides if the required data is already contained.
     *
     * @param dataComponentMap The map to merge into.
     * @return The merged map.
     */
    public Map<String, ObjectUIElementDataComponent> mergeData(@NotNull final Map<String, ObjectUIElementDataComponent> dataComponentMap)
    {
        dataComponentMap.put(Constants.Controls.General.CONST_TYPE, object.get(Constants.Controls.General.CONST_TYPE));
        dataComponentMap.put(Constants.Controls.General.CONST_ID, object.get(Constants.Controls.General.CONST_ID));

        return dataComponentMap;
    }
}
