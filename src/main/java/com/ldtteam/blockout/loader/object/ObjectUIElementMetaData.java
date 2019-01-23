package com.ldtteam.blockout.loader.object;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.ldtteam.blockout.BlockOut;
import com.ldtteam.blockout.element.IUIElementHost;
import com.ldtteam.blockout.loader.core.IUIElementMetaData;
import com.ldtteam.blockout.loader.factory.core.IUIElementDataComponentConverter;
import com.ldtteam.blockout.util.Constants;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ObjectUIElementMetaData implements IUIElementMetaData, Serializable
{
    @NotNull
    private final Map<String, ObjectUIElementDataComponent> object;
    @Nullable
    private final IUIElementHost parent;
    @NotNull
    private transient Injector injector;

    public ObjectUIElementMetaData(@NotNull final Map<String, ObjectUIElementDataComponent> object, @Nullable final IUIElementHost parent)
    {
        this.object = object;
        this.parent = parent;
        this.injector = Guice.createInjector(BlockOut.getBlockOut().getProxy().getFactoryInjectionModules());
    }

    public ObjectUIElementMetaData(@NotNull final Map<String, ObjectUIElementDataComponent> object, @Nullable final IUIElementHost parent, @NotNull final Injector injector)
    {
        this.object = object;
        this.parent = parent;
        this.injector = injector;
    }

    @Override
    public ResourceLocation getType()
    {
        final IUIElementDataComponentConverter<ResourceLocation> factory = getInjector().getInstance(Key.get(new TypeLiteral<IUIElementDataComponentConverter<ResourceLocation>>() {}));
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

    @NotNull
    public Injector getInjector()
    {
        if (injector == null)
        {
            injector = Guice.createInjector(BlockOut.getBlockOut().getProxy().getFactoryInjectionModules());
        }

        return injector;
    }
}
