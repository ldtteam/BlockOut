package com.ldtteam.blockout.connector.client;

import com.ldtteam.blockout.connector.common.CommonGuiInstantiationController;
import com.ldtteam.blockout.connector.common.builder.CommonGuiKeyBuilder;
import com.ldtteam.blockout.connector.core.IGuiController;
import com.ldtteam.blockout.connector.core.IGuiKey;
import com.ldtteam.blockout.connector.core.builder.IGuiKeyBuilder;
import com.ldtteam.blockout.element.root.IRootGuiElement;
import com.ldtteam.blockout.element.simple.IInventorySlotUIElement;
import com.ldtteam.blockout.gui.BlockOutScreenGui;
import com.ldtteam.blockout.util.Log;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Tuple;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.UUID;
import java.util.function.Consumer;

public class ClientSideOnlyGuiController implements IGuiController
{
    private static final UUID DUMMY_ID = new UUID(0, 0);

    @Nullable
    private Tuple<IGuiKey, IRootGuiElement> openClientSideOnlyGui = null;

    @SuppressWarnings("unchecked")
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

    @SuppressWarnings("unchecked")
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

        IRootGuiElement host;

        try
        {
            host = CommonGuiInstantiationController.getInstance().instantiateNewGui(key);

            if (host.getAllCombinedChildElements().entrySet().stream().anyMatch(e -> e instanceof IInventorySlotUIElement))
            {
                throw new IllegalArgumentException("Can not open UI that holds Slots. Inventories are not supported in ClientSide only gui's.");
            }
        }
        catch (IllegalArgumentException ex)
        {
            Log.getLogger().error("Failed to build client side only gui.", ex);
            return;
        }

        openClientSideOnlyGui = new Tuple<>(key, host);

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
        Minecraft.getInstance().displayGuiScreen(null);
    }

    @Nullable
    @Override
    public IGuiKey getOpenUI(@Nullable final PlayerEntity player)
    {
        if (!(Minecraft.getInstance().player == player))
        {
            throw new IllegalArgumentException("Can not get UI from remote player for ClientSide gui's");
        }

        if (openClientSideOnlyGui == null)
        {
            return null;
        }

        return openClientSideOnlyGui.getA();
    }

    @Nullable
    @Override
    public IGuiKey getOpenUI(@Nullable final UUID player)
    {
        if ((Minecraft.getInstance().player == null && player != null) || (Minecraft.getInstance().player != null && player == null) || (
          Minecraft.getInstance().player.getUniqueID() != player))
        {
            throw new IllegalArgumentException("Can not get UI from remote player for ClientSide gui's");
        }


        if (openClientSideOnlyGui == null)
        {
            return null;
        }

        return openClientSideOnlyGui.getA();
    }

    @Nullable
    @Override
    public IRootGuiElement getRoot(@Nullable final IGuiKey guiKey)
    {
        if (guiKey == null && openClientSideOnlyGui == null)
        {
            return null;
        }

        if (openClientSideOnlyGui != null)
        {
            if (openClientSideOnlyGui.getA() != guiKey)
            {
                throw new IllegalArgumentException("Can not get root from unknown gui key.");
            }

            return openClientSideOnlyGui.getB();
        }

        return null;
    }

    private void openGui(@NotNull final IGuiKey key, @NotNull final IRootGuiElement rootGuiElement)
    {
        Minecraft.getInstance().displayGuiScreen(new BlockOutScreenGui(key, rootGuiElement));
    }
}
