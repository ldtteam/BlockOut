package com.ldtteam.blockout.connector.client;

import com.ldtteam.blockout.connector.common.CommonGuiInstantiationController;
import com.ldtteam.blockout.connector.common.builder.CommonGuiKeyBuilder;
import com.ldtteam.blockout.connector.core.IGuiController;
import com.ldtteam.blockout.connector.core.IGuiKey;
import com.ldtteam.blockout.connector.core.builder.IGuiKeyBuilder;
import com.ldtteam.blockout.element.root.RootGuiElement;
import com.ldtteam.blockout.element.simple.Slot;
import com.ldtteam.blockout.gui.BlockOutGuiLogic;
import com.ldtteam.blockout.inventory.BlockOutContainerLogic;
import com.ldtteam.blockout.util.Log;
import com.ldtteam.jvoxelizer.IGameEngine;
import net.minecraft.entity.player.PlayerEntity;
import com.ldtteam.jvoxelizer.util.tuple.ITuple;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.UUID;
import java.util.function.Consumer;

public class ClientSideOnlyGuiController implements IGuiController
{
    private static final UUID DUMMY_ID = new UUID(0, 0);

    @Nullable
    private ITuple<IGuiKey, RootGuiElement> openClientSideOnlyGui = null;

    @Override
    public void openUI(
      @Nullable final PlayerEntity player, @NotNull final Consumer<IGuiKeyBuilder>... guiKeyBuilderConsumer)
    {
        final CommonGuiKeyBuilder builder = new CommonGuiKeyBuilder();
        Arrays.stream(guiKeyBuilderConsumer).forEach(iGuiKeyBuilderConsumer -> iGuiKeyBuilderConsumer.accept(builder));

        openUI(player, builder.build());
    }

    @Override
    public void openUI(@Nullable final PlayerEntity player, @NotNull final IGuiKey key)
    {
        openUI(DUMMY_ID, key);
    }

    @Override
    public void openUI(@Nullable final UUID playerId, @NotNull final Consumer<IGuiKeyBuilder>... guiKeyBuilderConsumer)
    {
        final CommonGuiKeyBuilder builder = new CommonGuiKeyBuilder();
        Arrays.stream(guiKeyBuilderConsumer).forEach(iGuiKeyBuilderConsumer -> iGuiKeyBuilderConsumer.accept(builder));

        openUI(DUMMY_ID, builder.build());
    }

    @Override
    public void openUI(@Nullable final UUID playerId, @NotNull final IGuiKey key)
    {
        closeUI(playerId);

        if (!key.getItemHandlerManager().getAllItemHandlerIds().isEmpty())
        {
            throw new IllegalArgumentException("Can not build a ClientSide only gui with inventory support.");
        }

        RootGuiElement host;

        try
        {
            host = CommonGuiInstantiationController.getInstance().instantiateNewGui(key);

            if (host.getAllCombinedChildElements().entrySet().stream().anyMatch(e -> e instanceof Slot))
            {
                throw new IllegalArgumentException("Can not open UI that holds Slots. Inventories are not supported in ClientSide only gui's.");
            }
        }
        catch (IllegalArgumentException ex)
        {
            Log.getLogger().error("Failed to build client side only gui.", ex);
            return;
        }

        openClientSideOnlyGui = ITuple.create(key, host);

        openGui(key, host);
    }

    @Override
    public void closeUI(@Nullable final PlayerEntity player)
    {
        closeUI(DUMMY_ID);
    }

    @Override
    public void closeUI(@Nullable final UUID playerId)
    {
        IGameEngine.getInstance().displayGuiScreen(null);
    }

    @Nullable
    @Override
    public IGuiKey getOpenUI(@Nullable final PlayerEntity player)
    {
        if (!(IGameEngine.getInstance().getSinglePlayerPlayerEntity() == player))
        {
            throw new IllegalArgumentException("Can not get UI from remote player for ClientSide gui's");
        }

        if (openClientSideOnlyGui == null)
        {
            return null;
        }

        return openClientSideOnlyGui.getFirst();
    }

    @Nullable
    @Override
    public IGuiKey getOpenUI(@Nullable final UUID player)
    {
        if ((IGameEngine.getInstance().getSinglePlayerPlayerEntity() == null && player != null) || (IGameEngine.getInstance().getSinglePlayerPlayerEntity() != null && player == null) || (
          IGameEngine.getInstance().getSinglePlayerPlayerEntity().getId() != player))
        {
            throw new IllegalArgumentException("Can not get UI from remote player for ClientSide gui's");
        }


        if (openClientSideOnlyGui == null)
        {
            return null;
        }

        return openClientSideOnlyGui.getFirst();
    }

    @Nullable
    @Override
    public RootGuiElement getRoot(@Nullable final IGuiKey guiKey)
    {
        if (guiKey == null && openClientSideOnlyGui == null)
        {
            return null;
        }

        if (openClientSideOnlyGui != null)
        {
            if (openClientSideOnlyGui.getFirst() != guiKey)
            {
                throw new IllegalArgumentException("Can not get root from unknown gui key.");
            }

            return openClientSideOnlyGui.getSecond();
        }

        return null;
    }

    private void openGui(@NotNull final IGuiKey key, @NotNull final RootGuiElement rootGuiElement)
    {
        IGameEngine.getInstance().displayGuiScreen(BlockOutGuiLogic.createClientSideOnly(key, rootGuiElement));
    }
}
