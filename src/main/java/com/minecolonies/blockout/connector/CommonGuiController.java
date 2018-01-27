package com.minecolonies.blockout.connector;

import com.minecolonies.blockout.BlockOut;
import com.minecolonies.blockout.connector.core.IGuiController;
import com.minecolonies.blockout.connector.core.ILoaderManager;
import com.minecolonies.blockout.core.element.IUIElement;
import com.minecolonies.blockout.core.element.IUIElementHost;
import com.minecolonies.blockout.loader.IUIElementData;
import com.minecolonies.blockout.util.Log;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CommonGuiController implements IGuiController
{

    private final Map<ResourceLocation, IUIElementHost> openUis = new HashMap<>();
    private final Map<ResourceLocation, List<UUID>> watchers = new HashMap<>();

    @NotNull
    @Override
    public IUIElementHost openUI(@NotNull final ResourceLocation id, @NotNull final UUID playerId)
    {
        if (!openUis.containsKey(id))
        {
            final IUIElementData data = BlockOut.getBlockOut().getProxy().getLoaderManager().loadData(id);
            final IUIElement element = BlockOut.getBlockOut().getProxy().getFactoryController().getElementFromData(data);

            if (element instanceof IUIElementHost)
            {
                IUIElementHost iuiElementHost = (IUIElementHost) element;

                openUis.put(id, )
            }
            else
            {
                Log.getLogger().warn("Tried to open a UI that is not a element host.");
            }
        }
    }

    public void onUiClosed(@NotNull final ResourceLocation id, @NotNull final UUID playerId)
    {
        if (!openUis.containsKey(id))
        {
            Log.getLogger().warn("Tried to closed UI that should not be open.");
            return;
        }

        if (!watchers.get(id).contains(playerId))
        {
            Log.getLogger().warn("Tried to close UI that was not open by given player.");
            return;
        }

        watchers.get(id).remove(playerId);
        if (watchers.get(id).isEmpty())
        {
            openUis.remove(id);
            watchers.remove(id);
        }
    }
}
