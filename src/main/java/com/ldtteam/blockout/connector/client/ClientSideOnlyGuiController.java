package com.ldtteam.blockout.connector.client;

import com.ldtteam.blockout.connector.common.CommonGuiInstantiationController;
import com.ldtteam.blockout.connector.common.builder.CommonGuiKeyBuilder;
import com.ldtteam.blockout.connector.core.IGuiController;
import com.ldtteam.blockout.connector.core.IGuiKey;
import com.ldtteam.blockout.connector.core.builder.IGuiKeyBuilder;
import com.ldtteam.blockout.element.root.RootGuiElement;
import com.ldtteam.blockout.element.simple.Slot;
import com.ldtteam.blockout.gui.BlockOutGuiClientSideOnly;
import com.ldtteam.blockout.util.Log;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Tuple;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.UUID;
import java.util.function.Consumer;

@SideOnly(Side.CLIENT)
public class ClientSideOnlyGuiController implements IGuiController
{
    private static final UUID DUMMY_ID = new UUID(0, 0);

    @Nullable
    private Tuple<IGuiKey, RootGuiElement> openClientSideOnlyGui = null;

    @Override
    public void openUI(
      @Nullable final EntityPlayer player, @NotNull final Consumer<IGuiKeyBuilder>... guiKeyBuilderConsumer)
    {
        final CommonGuiKeyBuilder builder = new CommonGuiKeyBuilder();
        Arrays.stream(guiKeyBuilderConsumer).forEach(iGuiKeyBuilderConsumer -> iGuiKeyBuilderConsumer.accept(builder));

        openUI(player, builder.build());
    }

    @Override
    public void openUI(@Nullable final EntityPlayer player, @NotNull final IGuiKey key)
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
            throw new IllegalArgumentException("Can not create a ClientSide only gui with inventory support.");
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
            Log.getLogger().error("Failed to create client side only gui.", ex);
            return;
        }

        openClientSideOnlyGui = new Tuple<>(key, host);

        openGui(key, host);
    }

    @Override
    public void closeUI(@Nullable final EntityPlayer player)
    {
        closeUI(DUMMY_ID);
    }

    @Override
    public void closeUI(@Nullable final UUID playerId)
    {
        Minecraft.getMinecraft().displayGuiScreen(null);
    }

    @Nullable
    @Override
    public IGuiKey getOpenUI(@Nullable final EntityPlayer player)
    {
        if (!(Minecraft.getMinecraft().player == player))
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
        if ((Minecraft.getMinecraft().player == null && player != null) || (Minecraft.getMinecraft().player != null && player == null) || (
          Minecraft.getMinecraft().player.getPersistentID() != player))
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
        Minecraft.getMinecraft().displayGuiScreen(new BlockOutGuiClientSideOnly(key, rootGuiElement));
    }
}
