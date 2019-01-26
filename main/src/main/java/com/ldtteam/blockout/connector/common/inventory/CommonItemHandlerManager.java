package com.ldtteam.blockout.connector.common.inventory;

import com.google.common.collect.ImmutableList;
import com.ldtteam.blockout.connector.core.inventory.IItemHandlerManager;
import com.ldtteam.blockout.connector.core.inventory.IItemHandlerProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CommonItemHandlerManager implements IItemHandlerManager
{
    @NotNull
    private final Map<String, IItemHandlerProvider> providerMap;

    public CommonItemHandlerManager(@NotNull List<IItemHandlerProvider> providerList)
    {
        providerMap = providerList.stream()
                        .collect(Collectors.toMap(provider -> provider.getId().toString(),
                          Function.identity()));
    }

    @Nullable
    @Override
    public IItemHandler getItemHandlerFromId(@NotNull final ResourceLocation id)
    {
        if (providerMap.containsKey(id.toString()))
        {
            return providerMap.get(id.toString()).get(this);
        }

        return null;
    }

    /**
     * Method used to get a list of ids for {@link IItemHandler}.
     *
     * @return The ids of all registered {@link IItemHandler}.
     */
    @NotNull
    @Override
    public List<ResourceLocation> getAllItemHandlerIds()
    {
        return ImmutableList.copyOf(providerMap.keySet().stream().map(ResourceLocation::new).collect(Collectors.toList()));
    }

    @NotNull
    @Override
    public List<IItemHandlerProvider> getAllProviders()
    {
        return ImmutableList.copyOf(this.providerMap.values());
    }

    @Override
    public int hashCode()
    {
        return providerMap.hashCode();
    }

    @Override
    public boolean equals(final Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }

        final CommonItemHandlerManager that = (CommonItemHandlerManager) o;

        return providerMap.equals(that.providerMap);
    }
}
